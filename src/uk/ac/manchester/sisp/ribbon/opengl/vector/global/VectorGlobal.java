package uk.ac.manchester.sisp.ribbon.opengl.vector.global;

import uk.ac.manchester.sisp.ribbon.common.IBounds2;
import uk.ac.manchester.sisp.ribbon.common.IDim2;
import uk.ac.manchester.sisp.ribbon.io.ArrayStore;
import uk.ac.manchester.sisp.ribbon.opengl.vector.EVectorSegment;
import uk.ac.manchester.sisp.ribbon.opengl.vector.VectorPath;

public final class VectorGlobal {
	
	public static final float COLINEARITY_TOLERANCE = 0.35f;
	
	public static final ArrayStore.Float onStorePolarizedBezier(final ArrayStore.Float pFloatStore, final float pX0, final float pY0, final float pX1, final float pY1, final float pX2, final float pY2, final EHull pHull) {
		/* Deploy curve vertices. */
		pFloatStore.store(new float[]{ pX0, pY0, pHull.getBezierStart(), 0.0f, pX1, pY1, pHull.getBezierControl(), 0.0f, pX2, pY2, pHull.getBezierEnd(), 1.0f });
		/* Return the ArrayStore. */
		return pFloatStore;
	}
	
	public static final ArrayStore.Float onStoreTriangle(final ArrayStore.Float pFloatStore, final float pX0, final float pY0, final float pX1, final float pY1, final float pX2, final float pY2) {
		/* Deploy triangle vertices. */
		pFloatStore.store(new float[]{ pX0, pY0, 0.0f, 0.0f, pX1, pY1, 0.0f, 0.0f, pX2, pY2, 0.0f, 0.0f });
		/* Return the ArrayStore. */
		return pFloatStore;
	}
	
	/** TODO: Check that this works, then roll out into a single FloatStore store() call. **/
	public static final ArrayStore.Float onStoreRectangle(final ArrayStore.Float pFloatStore, final float pX0, final float pY0, final float pX1, final float pY1, final float pX2, final float pY2, final float pX3, final float pY3) {
		/* Deploy a rectangle composed of two triangles. */
		VectorGlobal.onStoreTriangle(pFloatStore, pX0, pY0, pX1, pY1, pX2, pY2);
		VectorGlobal.onStoreTriangle(pFloatStore, pX1, pY1, pX2, pY2, pX3, pY3);
		/* Return the ArrayStore. */
		return pFloatStore;
	}
	
	public static final ArrayStore.Float onStoreBoundingBox(final ArrayStore.Float pFloatStore, final IDim2.F pDim2) {
		/* Store the Dim2's dimensions. */
		VectorGlobal.onStoreRectangle(pFloatStore, 0.0f, 0.0f, 0.0f, pDim2.getHeight(), pDim2.getWidth(), 0, pDim2.getWidth(), pDim2.getHeight());
		/* Return the ArrayStore. */
		return pFloatStore;
	}
	
	public static final IBounds2.F onCalculateBounds(final IBounds2.F.W pReturnBounds, final VectorPath pVectorPath) {
		/* Iterate the VectorPath and produce the corresponding triangles. */
		for(int i = 0; i < pVectorPath.getPathData().length; i++) {
			/* Acquire a reference to the current VectorPathComponent. */
			final EVectorSegment lVectorPathComponent = EVectorSegment.onFetchSegment(pVectorPath, i);
			/* Process the component. */
			switch(lVectorPathComponent) {
				case MOVE_TO   :
				case LINE_TO   : 
					/* Update the ReturnBounds. */
					pReturnBounds.setMinimumX(Math.min(pReturnBounds.getMinimumX(), pVectorPath.getPathData()[i + 1]));
					pReturnBounds.setMaximumX(Math.max(pReturnBounds.getMaximumX(), pVectorPath.getPathData()[i + 1]));
					pReturnBounds.setMinimumY(Math.min(pReturnBounds.getMinimumY(), pVectorPath.getPathData()[i + 2]));
					pReturnBounds.setMaximumY(Math.max(pReturnBounds.getMaximumY(), pVectorPath.getPathData()[i + 2]));
				break;
				case BEZIER_TO :
					/* Update the ReturnBounds for the Control Point. */
					pReturnBounds.setMinimumX(Math.min(pReturnBounds.getMinimumX(), pVectorPath.getPathData()[i + 1]));
					pReturnBounds.setMaximumX(Math.max(pReturnBounds.getMaximumX(), pVectorPath.getPathData()[i + 1]));
					pReturnBounds.setMinimumY(Math.min(pReturnBounds.getMinimumY(), pVectorPath.getPathData()[i + 2]));
					pReturnBounds.setMaximumY(Math.max(pReturnBounds.getMaximumY(), pVectorPath.getPathData()[i + 2]));
					/* Update the ReturnBounds for the Return Point. */
					pReturnBounds.setMinimumX(Math.min(pReturnBounds.getMinimumX(), pVectorPath.getPathData()[i + 3]));
					pReturnBounds.setMaximumX(Math.max(pReturnBounds.getMaximumX(), pVectorPath.getPathData()[i + 3]));
					pReturnBounds.setMinimumY(Math.min(pReturnBounds.getMinimumY(), pVectorPath.getPathData()[i + 4]));
					pReturnBounds.setMaximumY(Math.max(pReturnBounds.getMaximumY(), pVectorPath.getPathData()[i + 4]));
				break;
				case CLOSE     : 
					/* Do nothing. This simply indicates a closed path. */
				break;
			}
			/* Jump to the next VectorPathComponent. */
			i += lVectorPathComponent.getNumberOfComponents();
		}
		/* Return the ReturnBounds. */
		return pReturnBounds;
	}
	
	/* Prevent instantiation of this class. */
	private VectorGlobal() {}
	
}