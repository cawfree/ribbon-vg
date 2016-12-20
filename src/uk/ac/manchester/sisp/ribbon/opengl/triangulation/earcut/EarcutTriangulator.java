package uk.ac.manchester.sisp.ribbon.opengl.triangulation.earcut;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import uk.ac.manchester.sisp.ribbon.common.IVec2;
import uk.ac.manchester.sisp.ribbon.global.EReleaseMode;
import uk.ac.manchester.sisp.ribbon.global.RibbonGlobal;
import uk.ac.manchester.sisp.ribbon.io.ArrayStore;
import uk.ac.manchester.sisp.ribbon.opengl.triangulation.global.TriangulationGlobal;
import uk.ac.manchester.sisp.ribbon.opengl.vector.global.EWindingOrder;
import uk.ac.manchester.sisp.ribbon.opengl.vector.global.VectorGlobal;
import uk.ac.manchester.sisp.ribbon.utils.MathUtils;

/** TODO: Update to the latest version. **/
public final class EarcutTriangulator {

	private static final boolean NATIVE_FORCE_OPTIMIZATION = true;
	
	static {
		if(EarcutTriangulator.NATIVE_FORCE_OPTIMIZATION) { /** TODO: Force JIT runtime compilation to native code. **/ }
	}
	
	private static final Comparator<EarcutNode> COMPARATOR_SORT_BY_X = new Comparator<EarcutNode>() { @Override public int compare(final EarcutNode pNodeA, final EarcutNode pNodeB) { return pNodeA.getX() < pNodeB.getX() ? -1 : pNodeA.getX() == pNodeB.getX() ? 0 : 1; } };
	
	private static enum EEarcutState {
		INIT, CURE, SPLIT;
	}
	
	/** Produces an array of vertices representing the triangulated result set of the Points array. **/
	public static final void earcut(final float[][] pPolygon, final List<float[][]> pHoles, final ArrayStore.Float pArrayStore) {
		/** TODO: If this is a common case, refactor the hole generation code. **/
		/* Determine whether the input vertices satisfy the minimum requirement for triangulation. */
		if(pPolygon.length < TriangulationGlobal.MINIMUM_TRIANGULATION_VERTICES) {
			if(RibbonGlobal.isReleaseModeSupported(EReleaseMode.DEVELOPMENT)) {
				RibbonGlobal.e(EarcutTriangulator.class, "Skipping triangulation set; minimum number of vertices not satisfied!");
			}
			/* Return the untriangulated set. */
			return;
		}
		/* Attempt to establish a doubly-linked list of the provided Points set, and then filter instances of intersections. */
		EarcutNode lOuterNode = EarcutTriangulator.onFilterPoints(EarcutTriangulator.onCreateDoublyLinkedList(pPolygon), null);
		/* If an outer node hasn't been detected, the input array is malformed. */
		if(lOuterNode == null) {
			if(RibbonGlobal.isReleaseModeSupported(EReleaseMode.DEVELOPMENT)) {
				RibbonGlobal.e(EarcutTriangulator.class, "Could not process shape!");
			}
			/* Return the untriangulated set. */
			return;
		}
		/* Determine if the specified list of points contains holes. */
		if (pHoles != null && !pHoles.isEmpty()) {
			/* Eliminate the hole triangulation. */
			lOuterNode = EarcutTriangulator.onEliminateHoles(pHoles, lOuterNode);
		}
		/* Calculate an EarcutTriangulator operation on the generated LinkedList. */
		EarcutTriangulator.onEarcutLinkedList(lOuterNode, pArrayStore, EEarcutState.INIT);
	}
	
	/** Links every hole into the outer loop, producing a single-ring polygon without holes. **/
	private static final EarcutNode onEliminateHoles(final List<float[][]> pHoles, EarcutNode lOuterNode) {
		/* Define a list to hole a reference to each filtered hole list. */
		final List<EarcutNode> lHoleQueue = new ArrayList<EarcutNode>();
		/* Iterate through each array of hole vertices. */
		for(int i = 0; i < pHoles.size(); i++) {
			/* Filter the doubly-linked hole list. */
			final EarcutNode lListNode = EarcutTriangulator.onFilterPoints(EarcutTriangulator.onCreateDoublyLinkedList(pHoles.get(i)), null);
			/* Determine if the resulting hole polygon was successful. */
			if(lListNode != null) {
				/* Add the leftmost vertex of the hole. */
				lHoleQueue.add(EarcutTriangulator.onFetchLeftmost(lListNode));
			}
		}
		/* Sort the hole vertices by increasing X. */
		Collections.sort(lHoleQueue, EarcutTriangulator.COMPARATOR_SORT_BY_X);
		/* Process holes from left to right. */
		for(int i = 0; i < lHoleQueue.size(); i++) {
			/* Eliminate hole triangles from the result set. */
			EarcutTriangulator.onEliminateHole(lHoleQueue.get(i), lOuterNode);
			/* Filter the new polygon. */
			lOuterNode = EarcutTriangulator.onFilterPoints(lOuterNode, lOuterNode.getNext());
		}
		/* Return a pointer to the list. */
		return lOuterNode;
	}
	
	/** Finds a bridge between vertices that connects a hole with an outer ring, and links it. **/
	private static final void onEliminateHole(final EarcutNode pHoleNode, EarcutNode pOuterNode) {
		/* Attempt to find a logical bridge between the HoleNode and OuterNode. */
		pOuterNode = EarcutTriangulator.onEberlyFetchHoleBridge(pHoleNode, pOuterNode);
		/* Determine whether a hole bridge could be fetched. */
		if(pOuterNode != null) {
			/* Split the resulting polygon. */
			EarcutNode lNode = EarcutTriangulator.onSplitPolygon(pOuterNode, pHoleNode);
			/* Filter the split nodes. */
			EarcutTriangulator.onFilterPoints(lNode, lNode.getNext());
		}
	}
	
	/** David Eberly's algorithm for finding a bridge between a hole and outer polygon. **/
	private static final EarcutNode onEberlyFetchHoleBridge(final EarcutNode pHoleNode, final EarcutNode pOuterNode) { /** TODO: Update earcut accordingly. **/
		EarcutNode node = pOuterNode;
		final IVec2.F p = pHoleNode;
		float px = p.getX();
		float py = p.getY();
		float qMax = Float.NEGATIVE_INFINITY;
		EarcutNode mNode = null;
		/* Find a segment intersection. Segments with lesser x become a potential point of connection. */
		do {
			final EarcutNode a = node;
			final EarcutNode b = node.getNext();
			if(py <= a.getY() && py >= b.getY()) {
				float qx = a.getX() + (py - a.getY()) * (b.getX() - a.getX()) / (b.getY() - a.getY());
				if (qx <= px && qx > qMax) {
					qMax = qx;
					mNode = a.getX() < b.getX() ? node : node.getNext();
				}
			}
			node = node.getNext();
		} while (node != pOuterNode);
		
		if (mNode == null) return null;
		/* Looks for points strictly inside the triangle of the hole point, segment intersection and endpoint. No points; vlaid connection, otherwise choose the point with minimum angle as the ray connection. */
		final float bx = mNode.getX(),
		by = mNode.getY(),
		pbd = px * by - py * bx,
		pcd = px * py - py * qMax,
		cpy = py - py,
		pcx = px - qMax,
		pby = py - by,
		bpx = bx - px,
		A = pbd - pcd - (qMax * by - py * bx),
		sign = A <= 0 ? -1 : 1;
		final EarcutNode stop = mNode;
		float tanMin = Float.POSITIVE_INFINITY;
		node = mNode.getNext();
		while (node != stop) {
			final float mx = node.getX();
			final float my = node.getY();
			final float amx = px - mx;
			if(amx >= 0 && mx >= bx) {
				final float s = (cpy * mx + pcx * my - pcd) * sign;
				if(s >= 0) {
					final float t = (pby * mx + bpx * my + pbd) * sign;
					if(t >= 0 && A * sign - s - t >= 0) {
						final float tan = Math.abs(py - my) / amx; // tangential
						if(tan < tanMin && isLocallyInside(node, pHoleNode)) {
							mNode = node;
							tanMin = tan;
						}
					}
				}
			}
			node = node.getNext();
		}
		return mNode;
	}
	
	/** Finds the left-most hole of a polygon ring. **/
	private static final EarcutNode onFetchLeftmost(final EarcutNode pStart) {
		EarcutNode lNode     = pStart;
		EarcutNode lLeftMost = pStart;
		do {
			/* Determine if the current node possesses a lesser X position. */
			if (lNode.getX() < lLeftMost.getX()) {
				/* Maintain a reference to this EarcutNode. */
				lLeftMost = lNode;
			}
			/* Progress the search to the next node in the doubly-linked list. */
			lNode = lNode.getNext();
		} while (lNode != pStart);
		
		/* Return the node with the smallest X value. */
		return lLeftMost;
	}
	
	/** Main ear slicing loop which triangulates the vertices of a polygon, provided as a doubly-linked list. **/
	private static final void onEarcutLinkedList(EarcutNode lCurrentEar, final ArrayStore.Float pArrayStore, final EEarcutState pEarcutState) {
		if (lCurrentEar == null) {
			return;
		}
		EarcutNode lStop         = lCurrentEar;
		EarcutNode lPreviousNode = null;
		EarcutNode lNextNode     = null;
		/* Iteratively slice ears. */
		while (lCurrentEar.getPrevious() != lCurrentEar.getNext()) {
			lPreviousNode = lCurrentEar.getPrevious();
			lNextNode = lCurrentEar.getNext();
			/* Determine whether the current triangle must be cut off. */
			if(EarcutTriangulator.isEar(lCurrentEar)) {
				/* Return the triangulated data back to the Callback. */
				VectorGlobal.onStoreTriangle(pArrayStore, lPreviousNode.getX(), lPreviousNode.getY(), lCurrentEar.getX(), lCurrentEar.getY(), lNextNode.getX(), lNextNode.getY());
				/* Remove the ear node. */
				lNextNode.setPrevious(lPreviousNode);
				lPreviousNode.setNext(lNextNode);
				/* Skipping to the next node leaves less slither triangles. */
				lCurrentEar = lNextNode.getNext();
				lStop = lNextNode.getNext();
				/* Continue iterating. */
				continue;
			}
			lCurrentEar = lNextNode;
			/* If the whole polygon has been iterated over and no more ears can be found. */
			if(lCurrentEar == lStop) {
				/* Process the EarcutState. */
				switch(pEarcutState) {
					case INIT :
						/* Filter points and slice again. */
						EarcutTriangulator.onEarcutLinkedList(EarcutTriangulator.onFilterPoints(lCurrentEar, null), pArrayStore, EEarcutState.CURE);
					break;
					case CURE :
						/* Attempt to cure local self-intersections. */
						lCurrentEar = EarcutTriangulator.onCureLocalIntersections(lCurrentEar, pArrayStore);
						/* Split the LinkdeList. */
						EarcutTriangulator.onEarcutLinkedList(lCurrentEar, pArrayStore, EEarcutState.SPLIT);
					break;
					case SPLIT :
						/* As a last resort, split the the remaining polygon into two. */
						EarcutTriangulator.onSplitEarcut(lCurrentEar, pArrayStore);
					break;
				}
				break;
			}
		}
	}
	
	/** Determines whether a polygon node forms a valid ear with adjacent nodes. **/
	private static final boolean isEar(final EarcutNode pEar) {
		final float ax = pEar.getPrevious().getX(), bx = pEar.getX(), cx = pEar.getNext().getX(), ay = pEar.getPrevious().getY(), by = pEar.getY(), cy = pEar.getNext().getY(),
		abd = ax * by - ay * bx,
		acd = ax * cy - ay * cx,
		cbd = cx * by - cy * bx,
		A = abd - acd - cbd;
		/* Reflex, can't be an ear! */
		if(A <= 0) return false;
		/* Next, ensure there are no points within the potential ear. */
		final float cay = cy - ay, acx = ax - cx, aby = ay - by, bax = bx - ax;
		EarcutNode lEarcutNode = pEar.getNext().getNext();
		/* Iterate through adjacent nodes. */
		while (lEarcutNode != pEar.getPrevious()) {
			final float px = lEarcutNode.getX();
			final float py = lEarcutNode.getY();
			lEarcutNode = lEarcutNode.getNext();
			final float s = cay * px + acx * py - acd;
			if(s >= 0) {
				final float t = aby * px + bax * py + abd;
				if(t >= 0) {
					final float k = A - s - t;
					final float term1 = (s == 0 ? s : t);
					final float term2 = (s == 0 ? s : k);
					final float calculation = (term1 != 0 ? term1 : term2 != 0 ? term2 : (t == 0 ? t : k));
					if((k >= 0) && (calculation != 0)) return false;
				}
			}
		}
		return true;
	}
	
	/** Iterates through all polygon nodes and cures small local self-intersections. **/
	private static final EarcutNode onCureLocalIntersections(EarcutNode pStartNode, final ArrayStore.Float pArrayStore) {
		EarcutNode lNode = pStartNode;
		do {
			EarcutNode a = lNode.getPrevious(), b = lNode.getNext().getNext();
			/*  a self-intersection where edge (v[i-1],v[i]) intersects (v[i+1],v[i+2]) */
			if (MathUtils.isIntersecting(a.getX(), a.getY(), lNode.getX(), lNode.getY(), lNode.getNext().getX(), lNode.getNext().getY(), b.getX(), b.getY()) && EarcutTriangulator.isLocallyInside(a, b) && EarcutTriangulator.isLocallyInside(b, a)) {
				/* Return the triangulated vertices . */
				VectorGlobal.onStoreTriangle(pArrayStore, a.getX(), a.getY(), lNode.getX(), lNode.getY(), b.getX(), b.getY());
				/* Remove the two nodes involved. */
				a.setNext(b);
				b.setPrevious(a);
				/* Progress the Node. */
				lNode = pStartNode = b;
			}
			lNode = lNode.getNext();
		}
		while (lNode != pStartNode);
		/* Return the Node. */
		return lNode;
	}
	
	/** Tries to split a polygon and triangulate each side independently. **/
	private static final void onSplitEarcut(final EarcutNode pStart, final ArrayStore.Float pArrayStore) {
		/* Search for a valid diagonal that divides the polygon into two. */
		EarcutNode lSearchNode = pStart;
		do {
			EarcutNode lDiagonal = lSearchNode.getNext().getNext();
			while (lDiagonal != lSearchNode.getPrevious()) {
				if(EarcutTriangulator.isValidDiagonal(lSearchNode, lDiagonal)) {
					/* Split the polygon into two at the point of the diagonal. */
					EarcutNode lSplitNode = EarcutTriangulator.onSplitPolygon(lSearchNode, lDiagonal);
					/* Filter the resulting polygon. */
					lSearchNode = EarcutTriangulator.onFilterPoints(lSearchNode, lSearchNode.getNext());
					lSplitNode  = EarcutTriangulator.onFilterPoints(lSplitNode,  lSplitNode.getNext());
					/* Attempt to earcut both of the resulting polygons. */
					EarcutTriangulator.onEarcutLinkedList(lSearchNode, pArrayStore, EEarcutState.INIT);
					EarcutTriangulator.onEarcutLinkedList(lSplitNode,  pArrayStore, EEarcutState.INIT);
					/* Finish the iterative search. */
					return;
				}
				lDiagonal = lDiagonal.getNext();
			}
			lSearchNode = lSearchNode.getNext();
		} while (lSearchNode != pStart);
	}
	
	/** Links two polygon vertices using a bridge. **/
	private static final EarcutNode onSplitPolygon(final EarcutNode pNodeA, final EarcutNode pNodeB) {
		final EarcutNode a2 = new EarcutNode(pNodeA.getX(), pNodeA.getY());
		final EarcutNode b2 = new EarcutNode(pNodeB.getX(), pNodeB.getY());
		final EarcutNode an = pNodeA.getNext();
		final EarcutNode bp = pNodeB.getPrevious();
		
		pNodeA.setNext(pNodeB);
		pNodeB.setPrevious(pNodeA);
		a2.setNext(an);
		an.setPrevious(a2);
		b2.setNext(a2);
		a2.setPrevious(b2);
		bp.setNext(b2);
		b2.setPrevious(bp);
		/* Return the split node. */
		return b2;
	}
	
	/** Determines whether a diagonal between two polygon nodes lies within a polygon interior. (This determines the validity of the ray.) **/
	private static final boolean isValidDiagonal(final EarcutNode pNodeA, final EarcutNode pNodeB) {
	    return !EarcutTriangulator.isIntersectingPolygon(pNodeA, pNodeA.getX(), pNodeA.getY(), pNodeB.getX(), pNodeB.getY()) && EarcutTriangulator.isLocallyInside(pNodeA, pNodeB) && EarcutTriangulator.isLocallyInside(pNodeB, pNodeA) && EarcutTriangulator.onMiddleInsert(pNodeA, pNodeA.getX(), pNodeA.getY(), pNodeB.getX(), pNodeB.getY());
	}
	
	/** Determines whether a polygon diagonal rests locally within a polygon. **/
	private static final boolean isLocallyInside(final EarcutNode pNodeA, final EarcutNode pNodeB) {
		return MathUtils.onCalculateWindingOrder(pNodeA.getPrevious().getX(), pNodeA.getPrevious().getY(), pNodeA.getX(), pNodeA.getY(), pNodeA.getNext().getX(), pNodeA.getNext().getY()) == EWindingOrder.CCW ? MathUtils.onCalculateWindingOrder(pNodeA.getX(), pNodeA.getY(), pNodeB.getX(), pNodeB.getY(), pNodeA.getNext().getX(), pNodeA.getNext().getY()) != EWindingOrder.CCW && MathUtils.onCalculateWindingOrder(pNodeA.getX(), pNodeA.getY(), pNodeA.getPrevious().getX(), pNodeA.getPrevious().getY(), pNodeB.getX(), pNodeB.getY()) != EWindingOrder.CCW : MathUtils.onCalculateWindingOrder(pNodeA.getX(), pNodeA.getY(), pNodeB.getX(), pNodeB.getY(), pNodeA.getPrevious().getX(), pNodeA.getPrevious().getY()) == EWindingOrder.CCW || MathUtils.onCalculateWindingOrder(pNodeA.getX(), pNodeA.getY(), pNodeA.getNext().getX(), pNodeA.getNext().getY(), pNodeB.getX(), pNodeB.getY()) == EWindingOrder.CCW;
	}
	
	/** Determines whether the middle point of a polygon diagonal is contained within the polygon. **/
	private static final boolean onMiddleInsert(final EarcutNode pPolygonStart, final float pX0, final float pY0, final float pX1, final float pY1) {
		EarcutNode    lNode     = pPolygonStart;
		boolean lIsInside = false;
		float lDx = (pX0 + pX1) * 0.5f;
		float lDy = (pY0 + pY1) * 0.5f;
		do {
			if(((lNode.getY() > lDy) != (lNode.getNext().getY() > lDy)) && (lDx < (lNode.getNext().getX() - lNode.getX()) * (lDy - lNode.getY()) / (lNode.getNext().getY() - lNode.getY()) + lNode.getX())) {
				lIsInside = !lIsInside;
			}
			lNode = lNode.getNext();
		} while (lNode != pPolygonStart);
		return lIsInside;
	}
	
	/** Determines if the diagonal of a polygon is intersecting with any polygon elements. **/
	private static final boolean isIntersectingPolygon(final EarcutNode pStartNode, final float pX0, final float pY0, final float pX1, final float pY1) {
		EarcutNode lNode = pStartNode;
		do {
			if(lNode.getX() != pX0 && lNode.getY() != pY0 && lNode.getNext().getX() != pX0 && lNode.getNext().getY() != pY0 && lNode.getX() != pX1 && lNode.getY() != pY1 && lNode.getNext().getX() != pX1 && lNode.getNext().getY() != pY1 && MathUtils.isIntersecting(lNode.getX(), lNode.getY(), lNode.getNext().getX(), lNode.getNext().getY(), pX0, pY0, pX1, pY1)) {
				return true;
			}
			lNode = lNode.getNext();
		} while (lNode != pStartNode);
		return false;
	}
	
	/** Creates a circular doubly linked list using polygon points. The order is governed by the specified winding order. **/
	private static final EarcutNode onCreateDoublyLinkedList(final float[][] pPoints) {
		EarcutNode lLastNode = null;
		for(int i = pPoints.length - 1; i >= 0; i--) {
			lLastNode = EarcutTriangulator.onInsertNode(pPoints[i][0], pPoints[i][1], lLastNode);
		}
		/* Return the last node in the Doubly-Linked List. */
	    return lLastNode;
	}
	
	/** Eliminates colinear/duplicate points. **/
	private static final EarcutNode onFilterPoints(final EarcutNode pStartNode, EarcutNode pEndNode) {
		if(pEndNode == null) {
			pEndNode = pStartNode;
		}
		pEndNode = ((pEndNode == null) ? pStartNode : pEndNode);
		EarcutNode    lNode        = pStartNode;
		boolean lContinueIteration = false;
		do {
			lContinueIteration = false;
			if (EarcutTriangulator.isVertexEquals(lNode.getX(), lNode.getY(), lNode.getNext().getX(), lNode.getNext().getY()) || MathUtils.onCalculateWindingOrder(lNode.getPrevious().getX(), lNode.getPrevious().getY(), lNode.getX(), lNode.getY(), lNode.getNext().getX(), lNode.getNext().getY()) == EWindingOrder.COLINEAR) {
				/* Remove the node. */
				lNode.getPrevious().setNext(lNode.getNext());
				lNode.getNext().setPrevious(lNode.getPrevious());
				lNode = pEndNode = lNode.getPrevious();
				if(lNode == lNode.getNext()) return null;
				lContinueIteration = true;
			
			} else {
				lNode = lNode.getNext();
			}
		}
		while (lContinueIteration || lNode != pEndNode);
		return pEndNode;
	}
	
	/** Creates a node and optionally links it with a previous node in a circular doubly-linked list. **/
	private static final EarcutNode onInsertNode(final float pX, final float pY, final EarcutNode pLastNode) {
		final EarcutNode lNode = new EarcutNode(pX, pY);
		if(pLastNode == null) {
			lNode.setPrevious(lNode);
			lNode.setNext(lNode);
		}
		else {
			lNode.setNext(pLastNode.getNext());
			lNode.setPrevious(pLastNode);
			pLastNode.getNext().setPrevious(lNode);
			pLastNode.setNext(lNode);
		}
		return lNode;
	}
	
	/** Determines if two point vertices are equal. **/
	private static final boolean isVertexEquals(final float pX0, final float pY0, final float pX1, final float pY1) {
		return pX0 == pX1 && pY0 == pY1;
	}
	
	/* Prevent instantiation of this class. */
	private EarcutTriangulator() {}

}