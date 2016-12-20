package uk.ac.manchester.sisp.ribbon.opengl.vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uk.ac.manchester.sisp.ribbon.common.IVec2;
import uk.ac.manchester.sisp.ribbon.io.ArrayStore;
import uk.ac.manchester.sisp.ribbon.opengl.triangulation.earcut.EarcutTriangulator;
import uk.ac.manchester.sisp.ribbon.opengl.triangulation.global.TriangulationGlobal;
import uk.ac.manchester.sisp.ribbon.opengl.vector.exception.VectorException;
import uk.ac.manchester.sisp.ribbon.opengl.vector.global.EFillRule;
import uk.ac.manchester.sisp.ribbon.opengl.vector.global.EHull;
import uk.ac.manchester.sisp.ribbon.opengl.vector.global.ELineCap;
import uk.ac.manchester.sisp.ribbon.opengl.vector.global.ELineJoin;
import uk.ac.manchester.sisp.ribbon.opengl.vector.global.EWindingOrder;
import uk.ac.manchester.sisp.ribbon.opengl.vector.global.VectorGlobal;
import uk.ac.manchester.sisp.ribbon.utils.DataUtils;
import uk.ac.manchester.sisp.ribbon.utils.MathUtils;

/** TODO: Use call to onStoreRectangle where appropriate. **/
/** TODO: Many thanks to Vlad's excellent Earcut, and the stunning and intelligent NanoVG. **/
/** TODO: implements IDisposable? **/
public final class VectorPathContext { 
	
	/* High-level shape generation constants. */
	private static final float KAPPA_90                      = 0.5522847493f;
	
	/* Fill generation constants. */
	private static final int   FILL_BEZIER_SUBDIVISIONS      = 1;
	
	/* Stroke generation constants. */
	private static final int   STROKE_BUFFER_SEGMENT_SIZE    = 4;

	private static final void onStoreFilledVertices(final VectorPath pVectorPath, final ArrayStore.Float pFloatStore, final List<float[][]>pPolygonVertices, final EFillRule pFillRule, final int pOuterPolygonIndex) {
		/* Fill the vertices using the specified FillRule. */
		switch(pFillRule) {
			case NON_ZERO : 
				/* Define a List to track current fill and hole regions of the Polygon. */
				final List<float[][]> lFillsList = new ArrayList<float[][]>(1);
				final List<float[][]> lHolesList = new ArrayList<float[][]>(1);
				/* Calculate the WindingOrder of the outermost polygon. */
				final EWindingOrder lOuterWindingOrder = MathUtils.onCalculateWindingOrder(MathUtils.onCalculatePolygonWindingSum(pPolygonVertices.get(pOuterPolygonIndex)));
				/* If the OuterWindingOrder is CW, reverse all polygon points. */
				if(lOuterWindingOrder == EWindingOrder.CW) {
					/* Iterate through each Polygon. */
					for(int i = 0; i < pPolygonVertices.size(); i++) {
						/* Reverse the Polygon. */
						DataUtils.onReverseElements(pPolygonVertices.get(i));
					}
				}
				/* Iterate through each remaining Polygon. */
				for(int i = 0; i < pPolygonVertices.size(); i++) {
					/* Fetch the CurrentPolygon. */
					final float[][] lCurrentPolygon   = pPolygonVertices.get(i);
					/* Calculate the WindingSum. */
					final float     lWindingSum       = MathUtils.onCalculatePolygonWindingSum(lCurrentPolygon);
					/* Calculate the WindingOrder of the CurrentPolygon. */
					final EWindingOrder lWindingOrder = MathUtils.onCalculateWindingOrder(lWindingSum);
					/* Determine whether the CurrentPolygon is a fill region or a hole. */
					if(lWindingOrder == EWindingOrder.CCW) { 
						/* Track the fill region. */
						lFillsList.add(lCurrentPolygon); 
					} 
					else {
						/* Track the hole region. */
						lHolesList.add(lCurrentPolygon); 
					}
				}
				/* Finally, triangulate the accumulated polygons. */
				for(int i = 0; i < lFillsList.size(); i++) {
					/* Triangulate the currently filled region. */
					EarcutTriangulator.earcut(lFillsList.get(i), lHolesList, pFloatStore);
				}
			break;
			case EVEN_ODD : 
			default : 
				throw new VectorException("Fill mode not supported!");
		}
	}
	
	/* Member Variables. */
	private float            mCurrentX;
	private float            mCurrentY;
	private final int   []   mSegmentBuffer;
	private final float [][] mPositionBuffer;
	private final double[][] mAngleBuffer;
	private final IVec2.F.W  mPICI;
	private final IVec2.F.W  mPOCI;
	private final IVec2.F.W  mPOCO;
	private final IVec2.F.W  mPICO;
	private final IVec2.F.W  mCIFI;
	private final IVec2.F.W  mCOFI;
	private final IVec2.F.W  mCOFO;
	private final IVec2.F.W  mCIFO;
	
	public VectorPathContext() {
		/* Initialize Member Variables. */
		this.mCurrentX       = 0.0f;
		this.mCurrentY       = 0.0f;
		this.mSegmentBuffer  = new    int[VectorPathContext.STROKE_BUFFER_SEGMENT_SIZE];
		this.mPositionBuffer = new  float[VectorPathContext.STROKE_BUFFER_SEGMENT_SIZE][];
		this.mAngleBuffer    = new double[VectorPathContext.STROKE_BUFFER_SEGMENT_SIZE][];
		this.mPICI           = new IVec2.F.Impl(); 
		this.mPOCI           = new IVec2.F.Impl(); 
		this.mPOCO           = new IVec2.F.Impl(); 
		this.mPICO           = new IVec2.F.Impl(); 
		this.mCIFI           = new IVec2.F.Impl(); 
		this.mCOFI           = new IVec2.F.Impl(); 
		this.mCOFO           = new IVec2.F.Impl(); 
		this.mCIFO           = new IVec2.F.Impl(); 
		/* Initialize buffers. */
		Arrays.fill(this.getSegmentBuffer(),  0);
		Arrays.fill(this.getPositionBuffer(), new float[]  { Float.MAX_VALUE,  Float.MIN_VALUE  });
		Arrays.fill(this.getAngleBuffer(),    new double[] { Double.MAX_VALUE, Double.MIN_VALUE });
	}
	
	public final VectorPathContext onMoveTo(final ArrayStore.Float pFloatStore, final float pX, final float pY) {
		/* Write the path data. */
		pFloatStore.store(new float[]{ EVectorSegment.MOVE_TO.ordinal(), pX, pY });
		/* Update the Current Position. */
		this.onUpdatePosition(pX, pY);
		/* Return the VectorPathContext. */
		return this;
	}
	
	public final VectorPathContext onLineTo(final ArrayStore.Float pFloatStore, final float pX, final float pY) {
		/* Write the path data. */
		pFloatStore.store(new float[]{ EVectorSegment.LINE_TO.ordinal(), pX, pY });
		/* Update the Current Position. */
		this.onUpdatePosition(pX, pY);
		/* Return the VectorPathContext. */
		return this;
	}
	
	public final VectorPathContext onBezierTo(final ArrayStore.Float pFloatStore, final float pControlX, final float pControlY, final float pX, final float pY) {
		/* Write the path data. */
		pFloatStore.store(new float[]{ EVectorSegment.BEZIER_TO.ordinal(), pControlX, pControlY, pX, pY });
		/* Update the Current Position. */
		this.onUpdatePosition(pX, pY);
		/* Return the VectorPathContext. */
		return this;
	}
	
	public final VectorPathContext onClosePath(final ArrayStore.Float pFloatStore) {
		/* Write the path data. */
		pFloatStore.store(new float[]{ EVectorSegment.CLOSE.ordinal() });
		/* Return the VectorPathContext. */
		return this;
	}
	
	/** TODO: Optimum fitting? **/
	/** TODO: Allow a 'core' cubic representation? **/
	public final VectorPathContext onCubicTo(final ArrayStore.Float pFloatStore, final float pControlX0, final float pControlY0, final float pControlX1, final float pControlY1, final float pX, final float pY) {
		/** TODO: Must be dynamic. **/
		final int   lDivisions      = 2;
		/* Calculate the size of each Bezier step. */
		final float lBezierStepSize = (1.0f / (float)lDivisions);
		/* Track the Start Position. */
		final float lStartX       = this.getCurrentX();
		final float lStartY       = this.getCurrentY();
		/* Calculate the start point of the Bezier. */
		      float lBezierStartX = lStartX;
		      float lBezierStartY = lStartY;
		/* Re-calculate the cubic curve as a set of segments. */
		for(int i = 0; i < lDivisions; i++) {
			/* Calculate the end point of the Bezier. */
			final float lBezierEndX     = MathUtils.onCalculateCubicBezierPoint(lStartX, pControlX0, pControlX1, pX, ((i + 1) * lBezierStepSize));
			final float lBezierEndY     = MathUtils.onCalculateCubicBezierPoint(lStartY, pControlY0, pControlY1, pY, ((i + 1) * lBezierStepSize));
			/* Calculate the start point of the Bezier. */
			final float lBezierMaximaX  = MathUtils.onCalculateCubicBezierPoint(lStartX, pControlX0, pControlX1, pX, ((i + 0.5f) * lBezierStepSize));
			final float lBezierMaximaY  = MathUtils.onCalculateCubicBezierPoint(lStartY, pControlY0, pControlY1, pY, ((i + 0.5f) * lBezierStepSize));
			/* Re-arrange for control point solution. */
			final float lBezierControlX = ((lBezierMaximaX - (0.25f * lBezierStartX)) - (0.25f * lBezierEndX)) * 2.0f;
			final float lBezierControlY = ((lBezierMaximaY - (0.25f * lBezierStartY)) - (0.25f * lBezierEndY)) * 2.0f;
			/* Deploy a quadratic Bezier. */
			this.onBezierTo(pFloatStore, lBezierControlX, lBezierControlY, lBezierEndX, lBezierEndY);
			/* Update the BezierStart co-ordinates. */
			lBezierStartX = lBezierEndX;
			lBezierStartY = lBezierEndY;
		}
		/* Return the VectorPathContext. */
		return this;
	}
	
	public final VectorPathContext onEllipse(final ArrayStore.Float pFloatStore, final float pCX, final float pCY, final float pRX, final float pRY) {
		this.onMoveTo(pFloatStore, pCX - pRX, pCY);
		this.onCubicTo(pFloatStore, pCX - pRX, pCY + pRY * VectorPathContext.KAPPA_90, pCX - pRX * VectorPathContext.KAPPA_90, pCY + pRY, pCX, pCY + pRY);
		this.onCubicTo(pFloatStore, pCX + pRX * VectorPathContext.KAPPA_90, pCY + pRY, pCX + pRX, pCY + pRY * VectorPathContext.KAPPA_90, pCX + pRX, pCY);
		this.onCubicTo(pFloatStore, pCX + pRX, pCY - pRY * VectorPathContext.KAPPA_90, pCX + pRX * VectorPathContext.KAPPA_90, pCY - pRY, pCX, pCY - pRY);
		this.onCubicTo(pFloatStore, pCX - pRX * VectorPathContext.KAPPA_90, pCY - pRY, pCX - pRX, pCY - pRY * VectorPathContext.KAPPA_90, pCX - pRX, pCY);
		this.onClosePath(pFloatStore);
		/* Return the VectorPathContext. */
		return this;
	}
	
	public final VectorPathContext onCircle(final ArrayStore.Float pFloatStore, final float pCX, final float pCY, final float pR) {
		/* Draw an ellipse with uniform radius. */
		this.onEllipse(pFloatStore, pCX, pCY, pR, pR);
		/* Return the VectorPathContext. */
		return this;
	}
	
	public final VectorPathContext onRectangle(final ArrayStore.Float pFloatStore, final float pX, final float pY, final float pWidth, final float pHeight) {
		this.onMoveTo(pFloatStore, pX, pY);
		this.onLineTo(pFloatStore, pX, pY + pHeight);
		this.onLineTo(pFloatStore, pX + pWidth, pY + pHeight);
		this.onLineTo(pFloatStore, pX + pWidth, pY);
		this.onClosePath(pFloatStore);
		/* Return the VectorPathContext. */
		return this;
	}
	
//	public final VectorPathContext onArc(final ArrayStore.Float pFloatStore, final float pCX, final float pCY, final float pR, final double pA0, final double pA1) {
//		/* Calculate the angular difference. */
//		double lDelta = pA1 - pA0;
//		/* Split the arc into max 90 degree segments. */
//		final int lDivisions = 20;//VectorPathContext.BEZIER_CUBIC_POLYNOMIAL_ORDER;
//		/* Calculate the stepped angular difference. */
//		lDelta /= lDivisions;
//		/* Deploy iterative curves. */
//		for(int i = 0; i < lDivisions; i++) {
//			/* Calculate start and end points. */ /** TODO: Clear inter-iteration-interaction. **/
//			final float lX0 = pCX + pR * (float)Math.cos(pA0 + lDelta * (i + 0.0));
//			final float lY0 = pCY + pR * (float)Math.sin(pA0 + lDelta * (i + 0.0));
//			final float lX1 = pCX + pR * (float)Math.cos(pA0 + lDelta * (i + 0.5));
//			final float lY1 = pCY + pR * (float)Math.sin(pA0 + lDelta * (i + 0.5));
//			final float lX2 = pCX + pR * (float)Math.cos(pA0 + lDelta * (i + 1.0));
//			final float lY2 = pCY + pR * (float)Math.sin(pA0 + lDelta * (i + 1.0));
//			/* Re-arrange for control point solution. */
//			final float lBezierControlX = ((lX1 - (0.25f * lX0)) - (0.25f * lX2)) * 2.0f;
//			final float lBezierControlY = ((lY1 - (0.25f * lY0)) - (0.25f * lY2)) * 2.0f;
//			/* Deploy the quadratic Bezier curve. */
//			this.onBezierTo(pFloatStore, lBezierControlX, lBezierControlY, lX2, lY2);
//		}
//		/* Return the VectorPathContext. */
//		return this;
//	}
	
	/** TODO: This should be comprised of quads. **/
	public final VectorPathContext onRoundedRectangle(final ArrayStore.Float pFloatStore, final float pX, final float pY, final float pWidth, final float pHeight, final float pR) { /** TODO: Quadratic implementation. **/
		/* Calculate corner radius and direction. */
		final float lRX = Math.min(pR, Math.abs(pWidth)  * 0.5f) * Math.signum(pWidth);
		final float lRY = Math.min(pR, Math.abs(pHeight) * 0.5f) * Math.signum(pHeight);
		/* Deploy a rounded rectangle. */
		this.onMoveTo(pFloatStore,  pX, pY + lRY);
		this.onLineTo(pFloatStore,  pX, pY + pHeight - lRY);
		this.onCubicTo(pFloatStore, pX, pY + pHeight - lRY * (1 - VectorPathContext.KAPPA_90), pX + lRX * (1 - VectorPathContext.KAPPA_90), pY + pHeight, pX + lRX, pY + pHeight);
		this.onLineTo(pFloatStore,  pX + pWidth - lRX, pY + pHeight);
		this.onCubicTo(pFloatStore, pX + pWidth - lRX * (1 - VectorPathContext.KAPPA_90), pY + pHeight, pX + pWidth, pY + pHeight - lRY * (1 - VectorPathContext.KAPPA_90), pX + pWidth, pY + pHeight - lRY);
		this.onLineTo(pFloatStore,  pX + pWidth, pY + lRY);
		this.onCubicTo(pFloatStore, pX + pWidth, pY + lRY * (1 - VectorPathContext.KAPPA_90), pX + pWidth - lRX * (1 - VectorPathContext.KAPPA_90), pY, pX + pWidth - lRX, pY);
		this.onLineTo(pFloatStore,  pX + lRX, pY);
		this.onCubicTo(pFloatStore, pX + lRX * (1 - VectorPathContext.KAPPA_90), pY, pX, pY + lRY * (1 - VectorPathContext.KAPPA_90), pX, pY + lRY);
		this.onClosePath(pFloatStore);
		/* Return the VectorPathContext. */
		return this;
	}
	
	public final VectorPathContext onAppendPath(final ArrayStore.Float pFloatStore, final VectorPath pVectorPath) {
		/* Write the additional path data. */
		pFloatStore.store(pVectorPath.getPathData());
		/* Return the VectorPathContext. */
		return this;
	}
	
	public final VectorPath onCreatePath(final ArrayStore.Float pFloatStore) {
		/* Create the VectorPath. */
		final VectorPath lVectorPath = new VectorPath(pFloatStore.onProduceArray());
		/* Reset the position. */
		this.onUpdatePosition(0.0f, 0.0f);
		/* Return the VectorPath. */
		return lVectorPath;
	}

	/** TODO: Just needs miter limit calculation. **/
	/* The Deferred-Stroke-Pipeline (DSP) algorithm. (Stroke a path with minimal overheads.) */
	public final ArrayStore<float[]> onTriangulateStroke(final VectorPath pVectorPath, final ArrayStore.Float pFloatStore, final float pStrokeWidth, final ELineCap pLineCap, final ELineJoin pLineJoin) {
		/* Stroke the path by buffering the state. */
		      boolean        lIsLast	   = false;
		      int            lNextSubpath = 0;
		/* Initialize buffers. */
		Arrays.fill(this.getSegmentBuffer(),  0);
		/* Calculate the stroke application width. */
		final float lStrokeWidth    = pStrokeWidth * 0.5f;
		/* Iterate through each Subpath. */
		while(lNextSubpath != pVectorPath.getPathData().length) {
			/* Declare the CurrentSubPath index. */
			final int        lCurrentSubpath         = lNextSubpath;
			/* Declare iteration variables. */
			int i = lCurrentSubpath, lSegmentIndex = 0;
			/* Maintain a reference to the final index at which wrapping took place. */
			int lWrapIndex    = DataUtils.JAVA_NULL_INDEX;
			/* Initialize the path as open. */
			boolean lIsClosed = false;
			/* Track previous branch WindingOrder. */
			EWindingOrder lPreviousWinding = null;
			/* Stroke the Subpath. */
			do {
				/* Grab the VectorSegmentIndex. */
				final int lVectorSegmentIndex          = i;
				/* Attain a reference to the VectorSegment. */
				final EVectorSegment lVectorSegment    = EVectorSegment.onFetchSegment(pVectorPath, lVectorSegmentIndex);
				/* Calculate the index of the next potential VectorSegment. */
				final int            lNextSegmentIndex = i + lVectorSegment.getNumberOfComponents() + 1;
				/* Determine whether the VectorSegment is the last in the path. */
				lIsLast = lNextSegmentIndex >= pVectorPath.getPathData().length || (EVectorSegment.onFetchSegment(pVectorPath, lNextSegmentIndex) == EVectorSegment.MOVE_TO);
				/* Define whether the stroke pipeline should be stalled. */
				boolean lIsStallPipeline = false;
				/* Track the WrapIndex. */
				if(lSegmentIndex == this.getSegmentBuffer().length) {
					/* Assign the WrapIndex. */
					lWrapIndex = i;
				}
				/* Process the wrapping mechanic. */
				if(lIsLast) {
					/* Wrap the path. */
					i = lCurrentSubpath;
					/* Determine whether a new Subpath must be stroked. */
					if(lSegmentIndex >= this.getSegmentBuffer().length) {
						/* Create a reference to the following path. */
						lNextSubpath = lNextSegmentIndex;
					}
				}
				else {
					/* Continue iterating. */
					i = lNextSegmentIndex;
				}
				/* Process the VectorSegment. */
				switch(lVectorSegment) {
					case MOVE_TO : 
						/* Determine if the closed path is manually closed; i.e. the user ends the path at the start location. */
						if(lIsClosed && MathUtils.calculateMagnitudeOf(pVectorPath.getPathData()[lVectorSegmentIndex + 1], pVectorPath.getPathData()[lVectorSegmentIndex + 2], this.getPositionBuffer()[0][0], this.getPositionBuffer()[0][1]) < pStrokeWidth) {	
							/* Stall the pipeline. */
							lIsStallPipeline = true;
							/* Do not process the current vertex. */
							break;
						}
					case LINE_TO :
						/* Grab the current position. */
						final float lX = pVectorPath.getPathData()[lVectorSegmentIndex + 1];
						final float lY = pVectorPath.getPathData()[lVectorSegmentIndex + 2];
						/* Calculate the Entry and Exit angle. */
						final double lLineAngle = Math.atan2((this.getPositionBuffer()[0][1] - lY), (this.getPositionBuffer()[0][0] - lX));
						/* Buffer matching Entry/Exit angles. */
						DataUtils.onPushArrayElement(this.getAngleBuffer(),    new double[]{ lLineAngle, lLineAngle });
						/* Buffer the CurrentSegment. */
						DataUtils.onPushArrayElement(this.getSegmentBuffer(), lVectorSegmentIndex);
						/* Buffer the Current X,Y. */
						DataUtils.onPushArrayElement(this.getPositionBuffer(), new float[]{ lX, lY });
					break;
					case BEZIER_TO : 
						/* Calculate the Entry and Exit angle. */
						final double lBezierEntry = Math.atan2((pVectorPath.getPathData()[lVectorSegmentIndex + 2]  - pVectorPath.getPathData()[lVectorSegmentIndex + 4]), (pVectorPath.getPathData()[lVectorSegmentIndex + 1]  - pVectorPath.getPathData()[lVectorSegmentIndex + 3]));
						final double lBezierExit  = Math.atan2((this.getPositionBuffer()[0][1] -  pVectorPath.getPathData()[lVectorSegmentIndex + 2]), (this.getPositionBuffer()[0][0] -  pVectorPath.getPathData()[lVectorSegmentIndex + 1]));
						/* Buffer the Current X,Y. */
						DataUtils.onPushArrayElement(this.getPositionBuffer(), new float[]{ pVectorPath.getPathData()[lVectorSegmentIndex + 3], pVectorPath.getPathData()[lVectorSegmentIndex + 4] });
						/* Buffer matching Entry/Exit angles. */
						DataUtils.onPushArrayElement(this.getAngleBuffer(), new double[]{ lBezierEntry, lBezierExit });
						/* Buffer the CurrentSegment. */
						DataUtils.onPushArrayElement(this.getSegmentBuffer(), lVectorSegmentIndex);
					break;
					case CLOSE : 
						/* Mark the path as closed. */
						lIsClosed        = true;
						/* Stall the pipeline. */
						lIsStallPipeline = true;
					break;
					default    :
						throw new VectorException("Segment not supported!");
				}
				/* Increase the SegmentIndex. */
				lSegmentIndex++;
				
				/* Fetch position metrics. */
				final float lX1 = this.getPositionBuffer()[1][0];
				final float lY1 = this.getPositionBuffer()[1][1];
				final float lX2 = this.getPositionBuffer()[2][0];
				final float lY2 = this.getPositionBuffer()[2][1];
				final float lX3 = this.getPositionBuffer()[3][0];
				final float lY3 = this.getPositionBuffer()[3][1];
				
				/* Fetch stroke entry/exit angle data. */
				final double  lBTheta0   = this.getAngleBuffer()[1][1];
				final double  lATheta1   = this.getAngleBuffer()[2][0];
				final double  lBTheta1   = this.getAngleBuffer()[2][1];
				final double  lATheta2   = this.getAngleBuffer()[3][0];

				/* Calculate CurrentWinding. */
				final EWindingOrder lWindingOrder  = MathUtils.onCalculateWindingOrder(lX1, lY1, lX2, lY2, lX3, lY3);
				
				/* Future Intersection. */
				if(MathUtils.isWithinTolerance(MathUtils.calculateIntersectionOf(this.getCIFI(), lX2 + lStrokeWidth * (float)Math.cos(lATheta1 - MathUtils.PI_OVER_2), lY2 + lStrokeWidth * (float)Math.sin(lATheta1 - MathUtils.PI_OVER_2), lX3 + lStrokeWidth * (float)Math.cos(lBTheta1 - MathUtils.PI_OVER_2), lY3 + lStrokeWidth * (float)Math.sin(lBTheta1 - MathUtils.PI_OVER_2), lX1 + lStrokeWidth * (float)Math.cos(lBTheta0 - MathUtils.PI_OVER_2), lY1 + lStrokeWidth * (float)Math.sin(lBTheta0 - MathUtils.PI_OVER_2), lX2 + lStrokeWidth * (float)Math.cos(lBTheta0 - MathUtils.PI_OVER_2), lY2 + lStrokeWidth * (float)Math.sin(lBTheta0 - MathUtils.PI_OVER_2)), VectorGlobal.COLINEARITY_TOLERANCE) || MathUtils.isWithinTolerance(MathUtils.calculateIntersectionOf(this.getCIFO(), lX2 + lStrokeWidth * (float)Math.cos(lATheta1 - MathUtils.PI_OVER_2), lY2 + lStrokeWidth * (float)Math.sin(lATheta1 - MathUtils.PI_OVER_2), lX3 + lStrokeWidth * (float)Math.cos(lBTheta1 - MathUtils.PI_OVER_2), lY3 + lStrokeWidth * (float)Math.sin(lBTheta1 - MathUtils.PI_OVER_2), lX1 + lStrokeWidth * (float)Math.cos(lBTheta0 + MathUtils.PI_OVER_2), lY1 + lStrokeWidth * (float)Math.sin(lBTheta0 + MathUtils.PI_OVER_2), lX2 + lStrokeWidth * (float)Math.cos(lBTheta0 + MathUtils.PI_OVER_2), lY2 + lStrokeWidth * (float)Math.sin(lBTheta0 + MathUtils.PI_OVER_2)), VectorGlobal.COLINEARITY_TOLERANCE)) { 
					/* Resolve the poor intersection by making an approximate connection between vertices. */
					this.getCIFI().setX(((lX2 + lStrokeWidth * (float)Math.cos(lATheta1 - MathUtils.PI_OVER_2)) + (lX3 + lStrokeWidth * (float)Math.cos(lBTheta1 - MathUtils.PI_OVER_2)) + (lX1 + lStrokeWidth * (float)Math.cos(lBTheta0 - MathUtils.PI_OVER_2)) + (lX2 + lStrokeWidth * (float)Math.cos(lBTheta0 - MathUtils.PI_OVER_2))) * 0.25f); 
					this.getCIFI().setY(((lY2 + lStrokeWidth * (float)Math.sin(lATheta1 - MathUtils.PI_OVER_2)) + (lY3 + lStrokeWidth * (float)Math.sin(lBTheta1 - MathUtils.PI_OVER_2)) + (lY1 + lStrokeWidth * (float)Math.sin(lBTheta0 - MathUtils.PI_OVER_2)) + (lY2 + lStrokeWidth * (float)Math.sin(lBTheta0 - MathUtils.PI_OVER_2))) * 0.25f); 
					this.getCIFO().setX(((lX2 + lStrokeWidth * (float)Math.cos(lATheta1 - MathUtils.PI_OVER_2)) + (lX3 + lStrokeWidth * (float)Math.cos(lBTheta1 - MathUtils.PI_OVER_2)) + (lX1 + lStrokeWidth * (float)Math.cos(lBTheta0 + MathUtils.PI_OVER_2)) + (lX2 + lStrokeWidth * (float)Math.cos(lBTheta0 + MathUtils.PI_OVER_2))) * 0.25f); 
					this.getCIFO().setY(((lY2 + lStrokeWidth * (float)Math.sin(lATheta1 - MathUtils.PI_OVER_2)) + (lY3 + lStrokeWidth * (float)Math.sin(lBTheta1 - MathUtils.PI_OVER_2)) + (lY1 + lStrokeWidth * (float)Math.sin(lBTheta0 + MathUtils.PI_OVER_2)) + (lY2 + lStrokeWidth * (float)Math.sin(lBTheta0 + MathUtils.PI_OVER_2))) * 0.25f);
				};
				if(MathUtils.isWithinTolerance(MathUtils.calculateIntersectionOf(this.getCOFI(), lX2 + lStrokeWidth * (float)Math.cos(lATheta1 + MathUtils.PI_OVER_2), lY2 + lStrokeWidth * (float)Math.sin(lATheta1 + MathUtils.PI_OVER_2), lX3 + lStrokeWidth * (float)Math.cos(lBTheta1 + MathUtils.PI_OVER_2), lY3 + lStrokeWidth * (float)Math.sin(lBTheta1 + MathUtils.PI_OVER_2), lX1 + lStrokeWidth * (float)Math.cos(lBTheta0 - MathUtils.PI_OVER_2), lY1 + lStrokeWidth * (float)Math.sin(lBTheta0 - MathUtils.PI_OVER_2), lX2 + lStrokeWidth * (float)Math.cos(lBTheta0 - MathUtils.PI_OVER_2), lY2 + lStrokeWidth * (float)Math.sin(lBTheta0 - MathUtils.PI_OVER_2)), VectorGlobal.COLINEARITY_TOLERANCE) || MathUtils.isWithinTolerance(MathUtils.calculateIntersectionOf(this.getCOFO(), lX2 + lStrokeWidth * (float)Math.cos(lATheta1 + MathUtils.PI_OVER_2), lY2 + lStrokeWidth * (float)Math.sin(lATheta1 + MathUtils.PI_OVER_2), lX3 + lStrokeWidth * (float)Math.cos(lBTheta1 + MathUtils.PI_OVER_2), lY3 + lStrokeWidth * (float)Math.sin(lBTheta1 + MathUtils.PI_OVER_2), lX1 + lStrokeWidth * (float)Math.cos(lBTheta0 + MathUtils.PI_OVER_2), lY1 + lStrokeWidth * (float)Math.sin(lBTheta0 + MathUtils.PI_OVER_2), lX2 + lStrokeWidth * (float)Math.cos(lBTheta0 + MathUtils.PI_OVER_2), lY2 + lStrokeWidth * (float)Math.sin(lBTheta0 + MathUtils.PI_OVER_2)), VectorGlobal.COLINEARITY_TOLERANCE)) { 
					/* Resolve the poor intersection by making an approximate connection between vertices. */
					this.getCOFI().setX(((lX2 + lStrokeWidth * (float)Math.cos(lATheta1 + MathUtils.PI_OVER_2)) + (lX3 + lStrokeWidth * (float)Math.cos(lBTheta1 + MathUtils.PI_OVER_2)) + (lX1 + lStrokeWidth * (float)Math.cos(lBTheta0 - MathUtils.PI_OVER_2)) + (lX2 + lStrokeWidth * (float)Math.cos(lBTheta0 - MathUtils.PI_OVER_2))) * 0.25f); 
					this.getCOFI().setY(((lY2 + lStrokeWidth * (float)Math.sin(lATheta1 + MathUtils.PI_OVER_2)) + (lY3 + lStrokeWidth * (float)Math.sin(lBTheta1 + MathUtils.PI_OVER_2)) + (lY1 + lStrokeWidth * (float)Math.sin(lBTheta0 - MathUtils.PI_OVER_2)) + (lY2 + lStrokeWidth * (float)Math.sin(lBTheta0 - MathUtils.PI_OVER_2))) * 0.25f);
					this.getCOFO().setX(((lX2 + lStrokeWidth * (float)Math.cos(lATheta1 + MathUtils.PI_OVER_2)) + (lX3 + lStrokeWidth * (float)Math.cos(lBTheta1 + MathUtils.PI_OVER_2)) + (lX1 + lStrokeWidth * (float)Math.cos(lBTheta0 + MathUtils.PI_OVER_2)) + (lX2 + lStrokeWidth * (float)Math.cos(lBTheta0 + MathUtils.PI_OVER_2))) * 0.25f);
					this.getCOFO().setY(((lY2 + lStrokeWidth * (float)Math.sin(lATheta1 + MathUtils.PI_OVER_2)) + (lY3 + lStrokeWidth * (float)Math.sin(lBTheta1 + MathUtils.PI_OVER_2)) + (lY1 + lStrokeWidth * (float)Math.sin(lBTheta0 + MathUtils.PI_OVER_2)) + (lY2 + lStrokeWidth * (float)Math.sin(lBTheta0 + MathUtils.PI_OVER_2))) * 0.25f);
				};
				
				/* Process the segment once buffers have been filled. */
				if((lSegmentIndex - 1  >= this.getSegmentBuffer().length) && !lIsStallPipeline) {
					/* Define LineCap configuration. */
					final boolean lIsCapped   = (int)pVectorPath.getPathData()[this.getSegmentBuffer()[3]] == EVectorSegment.MOVE_TO.ordinal() && !lIsClosed;
					final boolean lIsEndCap   = (int)pVectorPath.getPathData()[this.getSegmentBuffer()[1]] == EVectorSegment.MOVE_TO.ordinal() && !lIsClosed;
					/* Render the CurrentSegment. */
					switch(EVectorSegment.values()[(int)pVectorPath.getPathData()[this.getSegmentBuffer()[2]]]) {
						case MOVE_TO   : 
							if(!lIsClosed) {
								/* Render the LineCap. */
								switch(pLineCap) {
									case BUTT   :
										/* Render nothing. */
									break;
									case SQUARE : 
										/* Render the core. */
										VectorGlobal.onStoreTriangle(pFloatStore, lX2 + lStrokeWidth * (float)Math.cos(lBTheta0),           lY2 + lStrokeWidth * (float)Math.sin(lBTheta0),           lX2 + lStrokeWidth * (float)Math.cos(lBTheta0 + MathUtils.PI_OVER_2), lY2 + lStrokeWidth * (float)Math.sin(lBTheta0 + MathUtils.PI_OVER_2), lX2 + lStrokeWidth * (float)Math.cos(lBTheta0 - MathUtils.PI_OVER_2), lY2 + lStrokeWidth * (float)Math.sin(lBTheta0 - MathUtils.PI_OVER_2));
										VectorGlobal.onStoreTriangle(pFloatStore, lX3 + lStrokeWidth * (float)Math.cos(lATheta2 - Math.PI), lY3 + lStrokeWidth * (float)Math.sin(lATheta2 - Math.PI), lX3 + lStrokeWidth * (float)Math.cos(lATheta2 - MathUtils.PI_OVER_2), lY3 + lStrokeWidth * (float)Math.sin(lATheta2 - MathUtils.PI_OVER_2), lX3 + lStrokeWidth * (float)Math.cos(lATheta2 + MathUtils.PI_OVER_2), lY3 + lStrokeWidth * (float)Math.sin(lATheta2 + MathUtils.PI_OVER_2));
										/* Render corners. */
										VectorGlobal.onStoreTriangle(pFloatStore, lX2 + lStrokeWidth * (float)Math.cos(lBTheta0),           lY2 + lStrokeWidth * (float)Math.sin(lBTheta0),           lX2 + lStrokeWidth * (float)Math.cos(lBTheta0) + lStrokeWidth * (float)Math.cos(lBTheta0 + MathUtils.PI_OVER_2),                     lY2 + lStrokeWidth * (float)Math.sin(lBTheta0) + lStrokeWidth * (float)Math.sin(lBTheta0 + MathUtils.PI_OVER_2),                     lX2 + lStrokeWidth * (float)Math.cos(lBTheta0 + MathUtils.PI_OVER_2),           lY2 + lStrokeWidth * (float)Math.sin(lBTheta0 + MathUtils.PI_OVER_2)          );
										VectorGlobal.onStoreTriangle(pFloatStore, lX2 + lStrokeWidth * (float)Math.cos(lBTheta0),           lY2 + lStrokeWidth * (float)Math.sin(lBTheta0),           lX2 + lStrokeWidth * (float)Math.cos(lBTheta0) + lStrokeWidth * (float)Math.cos(lBTheta0 - MathUtils.PI_OVER_2),                     lY2 + lStrokeWidth * (float)Math.sin(lBTheta0) + lStrokeWidth * (float)Math.sin(lBTheta0 - MathUtils.PI_OVER_2),                     lX2 + lStrokeWidth * (float)Math.cos(lBTheta0 - MathUtils.PI_OVER_2),           lY2 + lStrokeWidth * (float)Math.sin(lBTheta0 - MathUtils.PI_OVER_2)          );
										VectorGlobal.onStoreTriangle(pFloatStore, lX3 + lStrokeWidth * (float)Math.cos(lATheta2 - Math.PI), lY3 + lStrokeWidth * (float)Math.sin(lATheta2 - Math.PI), lX3 + lStrokeWidth * (float)Math.cos(lATheta2 - Math.PI) + lStrokeWidth * (float)Math.cos(lATheta2 - Math.PI + MathUtils.PI_OVER_2), lY3 + lStrokeWidth * (float)Math.sin(lATheta2 - Math.PI) + lStrokeWidth * (float)Math.sin(lATheta2 - Math.PI + MathUtils.PI_OVER_2), lX3 + lStrokeWidth * (float)Math.cos(lATheta2 - Math.PI + MathUtils.PI_OVER_2), lY3 + lStrokeWidth * (float)Math.sin(lATheta2 - Math.PI + MathUtils.PI_OVER_2));
										VectorGlobal.onStoreTriangle(pFloatStore, lX3 + lStrokeWidth * (float)Math.cos(lATheta2 - Math.PI), lY3 + lStrokeWidth * (float)Math.sin(lATheta2 - Math.PI), lX3 + lStrokeWidth * (float)Math.cos(lATheta2 - Math.PI) + lStrokeWidth * (float)Math.cos(lATheta2 - Math.PI - MathUtils.PI_OVER_2), lY3 + lStrokeWidth * (float)Math.sin(lATheta2 - Math.PI) + lStrokeWidth * (float)Math.sin(lATheta2 - Math.PI - MathUtils.PI_OVER_2), lX3 + lStrokeWidth * (float)Math.cos(lATheta2 - Math.PI - MathUtils.PI_OVER_2), lY3 + lStrokeWidth * (float)Math.sin(lATheta2 - Math.PI - MathUtils.PI_OVER_2));
									break;
									case ROUND : 
										/* Render the core. */
										VectorGlobal.onStoreTriangle(pFloatStore, lX2 + lStrokeWidth * (float)Math.cos(lBTheta0),           lY2 + lStrokeWidth * (float)Math.sin(lBTheta0),           lX2 + lStrokeWidth * (float)Math.cos(lBTheta0 + MathUtils.PI_OVER_2), lY2 + lStrokeWidth * (float)Math.sin(lBTheta0 + MathUtils.PI_OVER_2), lX2 + lStrokeWidth * (float)Math.cos(lBTheta0 - MathUtils.PI_OVER_2), lY2 + lStrokeWidth * (float)Math.sin(lBTheta0 - MathUtils.PI_OVER_2));
										VectorGlobal.onStoreTriangle(pFloatStore, lX3 + lStrokeWidth * (float)Math.cos(lATheta2 - Math.PI), lY3 + lStrokeWidth * (float)Math.sin(lATheta2 - Math.PI), lX3 + lStrokeWidth * (float)Math.cos(lATheta2 - MathUtils.PI_OVER_2), lY3 + lStrokeWidth * (float)Math.sin(lATheta2 - MathUtils.PI_OVER_2), lX3 + lStrokeWidth * (float)Math.cos(lATheta2 + MathUtils.PI_OVER_2), lY3 + lStrokeWidth * (float)Math.sin(lATheta2 + MathUtils.PI_OVER_2));
										/* Render round ends. */
										VectorGlobal.onStorePolarizedBezier(pFloatStore, lX2 + lStrokeWidth * (float)Math.cos(lBTheta0),           lY2 + lStrokeWidth * (float)Math.sin(lBTheta0),           lX2 + lStrokeWidth * (float)Math.cos(lBTheta0) + lStrokeWidth * (float)Math.cos(lBTheta0 + MathUtils.PI_OVER_2),                     lY2 + lStrokeWidth * (float)Math.sin(lBTheta0) + lStrokeWidth * (float)Math.sin(lBTheta0 + MathUtils.PI_OVER_2),                     lX2 + lStrokeWidth * (float)Math.cos(lBTheta0 + MathUtils.PI_OVER_2),           lY2 + lStrokeWidth * (float)Math.sin(lBTheta0 + MathUtils.PI_OVER_2),           EHull.CONVEX);
										VectorGlobal.onStorePolarizedBezier(pFloatStore, lX2 + lStrokeWidth * (float)Math.cos(lBTheta0),           lY2 + lStrokeWidth * (float)Math.sin(lBTheta0),           lX2 + lStrokeWidth * (float)Math.cos(lBTheta0) + lStrokeWidth * (float)Math.cos(lBTheta0 - MathUtils.PI_OVER_2),                     lY2 + lStrokeWidth * (float)Math.sin(lBTheta0) + lStrokeWidth * (float)Math.sin(lBTheta0 - MathUtils.PI_OVER_2),                     lX2 + lStrokeWidth * (float)Math.cos(lBTheta0 - MathUtils.PI_OVER_2),           lY2 + lStrokeWidth * (float)Math.sin(lBTheta0 - MathUtils.PI_OVER_2),           EHull.CONVEX);
										VectorGlobal.onStorePolarizedBezier(pFloatStore, lX3 + lStrokeWidth * (float)Math.cos(lATheta2 - Math.PI), lY3 + lStrokeWidth * (float)Math.sin(lATheta2 - Math.PI), lX3 + lStrokeWidth * (float)Math.cos(lATheta2 - Math.PI) + lStrokeWidth * (float)Math.cos(lATheta2 - Math.PI + MathUtils.PI_OVER_2), lY3 + lStrokeWidth * (float)Math.sin(lATheta2 - Math.PI) + lStrokeWidth * (float)Math.sin(lATheta2 - Math.PI + MathUtils.PI_OVER_2), lX3 + lStrokeWidth * (float)Math.cos(lATheta2 - Math.PI + MathUtils.PI_OVER_2), lY3 + lStrokeWidth * (float)Math.sin(lATheta2 - Math.PI + MathUtils.PI_OVER_2), EHull.CONVEX);
										VectorGlobal.onStorePolarizedBezier(pFloatStore, lX3 + lStrokeWidth * (float)Math.cos(lATheta2 - Math.PI), lY3 + lStrokeWidth * (float)Math.sin(lATheta2 - Math.PI), lX3 + lStrokeWidth * (float)Math.cos(lATheta2 - Math.PI) + lStrokeWidth * (float)Math.cos(lATheta2 - Math.PI - MathUtils.PI_OVER_2), lY3 + lStrokeWidth * (float)Math.sin(lATheta2 - Math.PI) + lStrokeWidth * (float)Math.sin(lATheta2 - Math.PI - MathUtils.PI_OVER_2), lX3 + lStrokeWidth * (float)Math.cos(lATheta2 - Math.PI - MathUtils.PI_OVER_2), lY3 + lStrokeWidth * (float)Math.sin(lATheta2 - Math.PI - MathUtils.PI_OVER_2), EHull.CONVEX);
									break;
								}
								/* Do not render a previous branch. */
								break;
							}
						case LINE_TO   : 
							/* Define connection configuration. */
							final boolean lIsJoined   = (int)pVectorPath.getPathData()[this.getSegmentBuffer()[3]] == EVectorSegment.LINE_TO.ordinal() || (int)pVectorPath.getPathData()[this.getSegmentBuffer()[3]] == EVectorSegment.MOVE_TO.ordinal();
							final boolean lIsEndJoin  = (int)pVectorPath.getPathData()[this.getSegmentBuffer()[1]] == EVectorSegment.LINE_TO.ordinal() || (int)pVectorPath.getPathData()[this.getSegmentBuffer()[1]] == EVectorSegment.MOVE_TO.ordinal();
							/* Determine whether to stroke a LineJoin. */
							if(lIsJoined && !lIsCapped) {
								/* Render the previous LineJoin. */
								switch(pLineJoin) {
									case MITER : 
										if(lPreviousWinding == EWindingOrder.CCW) {
											/* Render the core. */
											VectorGlobal.onStoreTriangle(pFloatStore, this.getPICO().getX(), this.getPICO().getY(), this.getPOCI().getX(), this.getPOCI().getY(), this.getPOCO().getX(), this.getPOCO().getY());
											/* Render the extended intersection. */
											VectorGlobal.onStoreTriangle(pFloatStore, this.getPICO().getX(), this.getPICO().getY(), this.getPOCI().getX(), this.getPOCI().getY(), this.getPICI().getX(), this.getPICI().getY());
										}
										else {
											/* Render the core. */
											VectorGlobal.onStoreTriangle(pFloatStore, this.getPICO().getX(), this.getPICO().getY(), this.getPOCI().getX(), this.getPOCI().getY(), this.getPICI().getX(), this.getPICI().getY());
											/* Render the extended intersection. */
											VectorGlobal.onStoreTriangle(pFloatStore, this.getPOCI().getX(), this.getPOCI().getY(), this.getPOCO().getX(), this.getPOCO().getY(), this.getPICO().getX(), this.getPICO().getY());
										}
									break;
									case BEVEL : 
										if(lPreviousWinding == EWindingOrder.CCW) {
											/* Render the core. */
											VectorGlobal.onStoreTriangle(pFloatStore, this.getPICO().getX(), this.getPICO().getY(), this.getPOCI().getX(), this.getPOCI().getY(), this.getPOCO().getX(), this.getPOCO().getY());
										}
										else {
											/* Render the core. */
											VectorGlobal.onStoreTriangle(pFloatStore, this.getPICO().getX(), this.getPICO().getY(), this.getPOCI().getX(), this.getPOCI().getY(), this.getPICI().getX(), this.getPICI().getY());
										}
									break;
									case ROUND : 
										if(lPreviousWinding == EWindingOrder.CCW) {
											/* Render the core. */
											VectorGlobal.onStoreTriangle(pFloatStore, this.getPICO().getX(), this.getPICO().getY(), this.getPOCI().getX(), this.getPOCI().getY(), this.getPOCO().getX(), this.getPOCO().getY());
											/* Render the extended intersection. */
											VectorGlobal.onStorePolarizedBezier(pFloatStore, this.getPICO().getX(), this.getPICO().getY(), this.getPICI().getX(), this.getPICI().getY(), this.getPOCI().getX(), this.getPOCI().getY(), EHull.CONVEX);
											/* Render inner curvature. */
											VectorGlobal.onStorePolarizedBezier(pFloatStore, this.getPOCO().getX() + lStrokeWidth * (float)Math.cos(lATheta2), this.getPOCO().getY() + lStrokeWidth * (float)Math.sin(lATheta2), this.getPOCO().getX(), this.getPOCO().getY(), this.getPOCO().getX() + lStrokeWidth * (float)Math.cos(lATheta1 - Math.PI), this.getPOCO().getY() + lStrokeWidth * (float)Math.sin(lATheta1 - Math.PI), EHull.CONCAVE);
										}
										else {
											/* Render the core. */
											VectorGlobal.onStoreTriangle(pFloatStore, this.getPICO().getX(), this.getPICO().getY(), this.getPOCI().getX(), this.getPOCI().getY(), this.getPICI().getX(), this.getPICI().getY());
											/* Render the extended intersection. */
											VectorGlobal.onStorePolarizedBezier(pFloatStore, this.getPOCI().getX(), this.getPOCI().getY(), this.getPOCO().getX(), this.getPOCO().getY(), this.getPICO().getX(), this.getPICO().getY(), EHull.CONVEX);
											/* Render inner curvature. */
											VectorGlobal.onStorePolarizedBezier(pFloatStore, this.getPICI().getX() + lStrokeWidth * (float)Math.cos(lATheta2), this.getPICI().getY() + lStrokeWidth * (float)Math.sin(lATheta2), this.getPICI().getX(), this.getPICI().getY(), this.getPICI().getX() + lStrokeWidth * (float)Math.cos(lATheta1 - Math.PI), this.getPICI().getY() + lStrokeWidth * (float)Math.sin(lATheta1 - Math.PI), EHull.CONCAVE);
										}
									break;
								}
							}
							/* Define outer vertices. */
							final float lAX = (lIsCapped ? lX3 + lStrokeWidth * (float)Math.cos(lBTheta1 - MathUtils.PI_OVER_2) : (lIsJoined  ? (lPreviousWinding == EWindingOrder.CCW) ? this.getPOCI().getX() : this.getPICI().getX() : this.getPICI().getX()));
							final float lAY = (lIsCapped ? lY3 + lStrokeWidth * (float)Math.sin(lBTheta1 - MathUtils.PI_OVER_2) : (lIsJoined  ? (lPreviousWinding == EWindingOrder.CCW) ? this.getPOCI().getY() : this.getPICI().getY() : this.getPICI().getY()));
							final float lBX = (lIsCapped ? lX3 + lStrokeWidth * (float)Math.cos(lBTheta1 + MathUtils.PI_OVER_2) : (lIsJoined  ? (lPreviousWinding == EWindingOrder.CCW) ? this.getPOCO().getX() : this.getPICO().getX() : this.getPOCO().getX()));
							final float lBY = (lIsCapped ? lY3 + lStrokeWidth * (float)Math.sin(lBTheta1 + MathUtils.PI_OVER_2) : (lIsJoined  ? (lPreviousWinding == EWindingOrder.CCW) ? this.getPOCO().getY() : this.getPICO().getY() : this.getPOCO().getY()));
							/* Define inner vertices. */                                                                                                                                                                                                        
							final float lCX = (lIsEndCap ? lX2 + lStrokeWidth * (float)Math.cos(lATheta1 - MathUtils.PI_OVER_2) : (lIsEndJoin ? (lWindingOrder  == EWindingOrder.CCW) ? this.getCIFO().getX() : this.getCIFI().getX() : this.getCIFI().getX()));
							final float lCY = (lIsEndCap ? lY2 + lStrokeWidth * (float)Math.sin(lATheta1 - MathUtils.PI_OVER_2) : (lIsEndJoin ? (lWindingOrder  == EWindingOrder.CCW) ? this.getCIFO().getY() : this.getCIFI().getY() : this.getCIFI().getY()));
							final float lDX = (lIsEndCap ? lX2 + lStrokeWidth * (float)Math.cos(lATheta1 + MathUtils.PI_OVER_2) : (lIsEndJoin ? (lWindingOrder  == EWindingOrder.CCW) ? this.getCOFO().getX() : this.getCOFI().getX() : this.getCOFO().getX()));
							final float lDY = (lIsEndCap ? lY2 + lStrokeWidth * (float)Math.sin(lATheta1 + MathUtils.PI_OVER_2) : (lIsEndJoin ? (lWindingOrder  == EWindingOrder.CCW) ? this.getCOFO().getY() : this.getCOFI().getY() : this.getCOFO().getY()));
							/* Render the line segment. */
							VectorGlobal.onStoreTriangle(pFloatStore, lAX, lAY, lBX, lBY, lCX, lCY);
							VectorGlobal.onStoreTriangle(pFloatStore, lBX, lBY, lDX, lDY, lCX, lCY);
						break;
						case BEZIER_TO : 
							/* Calculate the bezier's WindingOrder. */
							final EWindingOrder lBezierWindingOrder = MathUtils.onCalculateWindingOrder(lX3, lY3, pVectorPath.getPathData()[this.getSegmentBuffer()[2] + 1], pVectorPath.getPathData()[this.getSegmentBuffer()[2] + 2], lX2, lY2);
							/* Define start vertices. */
							final float lBAX = lIsCapped ? lX3 + lStrokeWidth * (float)Math.cos(lBTheta1 - MathUtils.PI_OVER_2) : this.getPICI().getX();
							final float lBAY = lIsCapped ? lY3 + lStrokeWidth * (float)Math.sin(lBTheta1 - MathUtils.PI_OVER_2) : this.getPICI().getY();
							final float lBBX = lIsCapped ? lX3 + lStrokeWidth * (float)Math.cos(lBTheta1 + MathUtils.PI_OVER_2) : this.getPOCO().getX();
							final float lBBY = lIsCapped ? lY3 + lStrokeWidth * (float)Math.sin(lBTheta1 + MathUtils.PI_OVER_2) : this.getPOCO().getY();
							/* Define end vertices. */
							final float lBCX = lIsEndCap ? lX2 + lStrokeWidth * (float)Math.cos(lATheta1 - MathUtils.PI_OVER_2) : this.getCIFI().getX();
							final float lBCY = lIsEndCap ? lY2 + lStrokeWidth * (float)Math.sin(lATheta1 - MathUtils.PI_OVER_2) : this.getCIFI().getY();
							final float lBDX = lIsEndCap ? lX2 + lStrokeWidth * (float)Math.cos(lATheta1 + MathUtils.PI_OVER_2) : this.getCOFO().getX();
							final float lBDY = lIsEndCap ? lY2 + lStrokeWidth * (float)Math.sin(lATheta1 + MathUtils.PI_OVER_2) : this.getCOFO().getY();
							/* Fetch the corresponding hull for the calculated WindingOrder. */
							final EHull         lHull     = lBezierWindingOrder.getCorrespondingHull();
							/* Calculate the Bezier's angle. */
							final double lBezierAngle     = Math.atan2((lY2 - lY3), (lX2 - lX3));
							/* Calculate offset control points. */
							final float  lOffsetControlX0 = pVectorPath.getPathData()[this.getSegmentBuffer()[2] + 1] + lStrokeWidth * (float)Math.cos(lBezierAngle + MathUtils.PI_OVER_2);
							final float  lOffsetControlY0 = pVectorPath.getPathData()[this.getSegmentBuffer()[2] + 2] + lStrokeWidth * (float)Math.sin(lBezierAngle + MathUtils.PI_OVER_2);
							final float  lOffsetControlX1 = pVectorPath.getPathData()[this.getSegmentBuffer()[2] + 1] + lStrokeWidth * (float)Math.cos(lBezierAngle - MathUtils.PI_OVER_2);
							final float  lOffsetControlY1 = pVectorPath.getPathData()[this.getSegmentBuffer()[2] + 2] + lStrokeWidth * (float)Math.sin(lBezierAngle - MathUtils.PI_OVER_2);
							/* Calculate the midpoint of the Bezier. */
							final float  lBezierMidpointX = MathUtils.onCalculateQuadraticBezierPoint(lX3, pVectorPath.getPathData()[this.getSegmentBuffer()[2] + 1], lX2, 0.5f);
							final float  lBezierMidpointY = MathUtils.onCalculateQuadraticBezierPoint(lY3, pVectorPath.getPathData()[this.getSegmentBuffer()[2] + 2], lY2, 0.5f);
							/* Calculate the magnitude between the midpoint and the control point. */
							final float  lBezierThickness = MathUtils.calculateMagnitudeOf(lBezierMidpointX, lBezierMidpointY, pVectorPath.getPathData()[this.getSegmentBuffer()[2] + 1], pVectorPath.getPathData()[this.getSegmentBuffer()[2] + 2]);
							/* Determine the required level of subdivision for the specified stroke width. */
							final int    lSubdivision     = (int)Math.ceil(lBezierThickness / pStrokeWidth);
							/* Declare the subdivision metric. */
							final float  lStepSize        = 1.0f / (float)lSubdivision;
							/* Declare Bezier initialization points. */
							      float  lBezierStartX0   = lBAX;
							      float  lBezierStartY0   = lBAY;
							      float  lBezierStartX1   = lBBX;
							      float  lBezierStartY1   = lBBY;
							/* Store subdivided curves. */ /** TODO: Encapsulate the method above, too. **/
							for(int j = 0; j < lSubdivision; j++) { /** TODO: Encpasulate to a non-intersection method. **/
								/* Calculate the section endpoint. */
								final float lBezierEndX0     = MathUtils.onCalculateQuadraticBezierPoint(lBAX, lOffsetControlX0, lBCX, ((j + 1) * lStepSize));
								final float lBezierEndY0     = MathUtils.onCalculateQuadraticBezierPoint(lBAY, lOffsetControlY0, lBCY, ((j + 1) * lStepSize));
								final float lBezierEndX1     = MathUtils.onCalculateQuadraticBezierPoint(lBBX, lOffsetControlX1, lBDX, ((j + 1) * lStepSize));
								final float lBezierEndY1     = MathUtils.onCalculateQuadraticBezierPoint(lBBY, lOffsetControlY1, lBDY, ((j + 1) * lStepSize));
								/* Calculate maxima regions. */
								final float lBezierMaximaX0  = MathUtils.onCalculateQuadraticBezierPoint(lBAX, lOffsetControlX0, lBCX, ((j) * lStepSize) + lStepSize * 0.5f);
								final float lBezierMaximaY0  = MathUtils.onCalculateQuadraticBezierPoint(lBAY, lOffsetControlY0, lBCY, ((j) * lStepSize) + lStepSize * 0.5f);
								final float lBezierMaximaX1  = MathUtils.onCalculateQuadraticBezierPoint(lBBX, lOffsetControlX1, lBDX, ((j) * lStepSize) + lStepSize * 0.5f);
								final float lBezierMaximaY1  = MathUtils.onCalculateQuadraticBezierPoint(lBBY, lOffsetControlY1, lBDY, ((j) * lStepSize) + lStepSize * 0.5f);
								/* Re-arrange for the control point solution. */
								final float lBezierControlX0 = ((lBezierMaximaX0 - (0.25f * lBezierStartX0)) - (0.25f * lBezierEndX0)) * 2.0f;
								final float lBezierControlY0 = ((lBezierMaximaY0 - (0.25f * lBezierStartY0)) - (0.25f * lBezierEndY0)) * 2.0f;
								final float lBezierControlX1 = ((lBezierMaximaX1 - (0.25f * lBezierStartX1)) - (0.25f * lBezierEndX1)) * 2.0f;
								final float lBezierControlY1 = ((lBezierMaximaY1 - (0.25f * lBezierStartY1)) - (0.25f * lBezierEndY1)) * 2.0f;
								/* Deploy the polarized bezier curves. */
								VectorGlobal.onStorePolarizedBezier(pFloatStore, lBezierStartX0, lBezierStartY0, lBezierControlX0, lBezierControlY0, lBezierEndX0, lBezierEndY0, lHull);
								VectorGlobal.onStorePolarizedBezier(pFloatStore, lBezierStartX1, lBezierStartY1, lBezierControlX1, lBezierControlY1, lBezierEndX1, lBezierEndY1, lHull.getInverse());
								/* Deploy the corresponding fill. */
								switch(lHull) {
									case CONCAVE : 
										VectorGlobal.onStoreTriangle(pFloatStore, lBezierStartX0, lBezierStartY0, lBezierStartX1, lBezierStartY1, lBezierControlX0, lBezierControlY0);
										VectorGlobal.onStoreTriangle(pFloatStore,   lBezierEndX0,   lBezierEndY0,   lBezierEndX1,   lBezierEndY1, lBezierControlX0, lBezierControlY0);
										VectorGlobal.onStoreTriangle(pFloatStore,   lBezierEndX1,   lBezierEndY1, lBezierStartX1, lBezierStartY1, lBezierControlX0, lBezierControlY0);
									break;
									case CONVEX  : 
										VectorGlobal.onStoreTriangle(pFloatStore, lBezierStartX1, lBezierStartY1, lBezierStartX0, lBezierStartY0, lBezierControlX1, lBezierControlY1);
										VectorGlobal.onStoreTriangle(pFloatStore,   lBezierEndX1,   lBezierEndY1,   lBezierEndX0,   lBezierEndY0, lBezierControlX1, lBezierControlY1);
										VectorGlobal.onStoreTriangle(pFloatStore,   lBezierEndX0,   lBezierEndY0, lBezierStartX0, lBezierStartY0, lBezierControlX1, lBezierControlY1);
									break;
									default      : 
										throw new VectorException("Critcal path error!");
								}
								/* Update the entry variables for the next iteration. */
								lBezierStartX0 = lBezierEndX0;
								lBezierStartY0 = lBezierEndY0;
								lBezierStartX1 = lBezierEndX1;
								lBezierStartY1 = lBezierEndY1;
							}
						break;
						case CLOSE     : 
						default        :
							throw new VectorException("Critcal path error!");
					}
				}
				
				/* Assign previous intersection vertices. */
				this.getPICI().setX(this.getCIFI().getX());
				this.getPICI().setY(this.getCIFI().getY());
				this.getPOCI().setX(this.getCOFI().getX());
				this.getPOCI().setY(this.getCOFI().getY());
				this.getPOCO().setX(this.getCOFO().getX());
				this.getPOCO().setY(this.getCOFO().getY());
				this.getPICO().setX(this.getCIFO().getX());
				this.getPICO().setY(this.getCIFO().getY());
				
				/* Assign the previous WindingOrder. */
				lPreviousWinding = lWindingOrder;
			}
			while(i != lWrapIndex || lSegmentIndex < this.getSegmentBuffer().length);
		}
		/* Return the ArrayStore. */
		return pFloatStore;
	}
	
	public final ArrayStore.Float onTriangulateFill(final VectorPath pVectorPath, final ArrayStore.Float pFloatStore, final EFillRule pFillRule) {
		/* Track the initial position of the path. */
		float lStartX = 0.0f;
		float lStartY = 0.0f;
		/* Declare Lists to temporarily store vertices. */
		final List<float[]>   lCurrentContour  = new ArrayList<float[]>(1);
		final List<float[][]> lPolygonVertices = new ArrayList<float[][]>(1);
		/* Declare a placeholder variable to track the per-polygon and global MinimumX. */
		float lGlobalMinimumX  = Float.MAX_VALUE;
		float lCurrentMinimumX = Float.MAX_VALUE;
		/* Declare a variable to track the EnclosingPolygonIndex. */
		int   lEnclosingPolygonIndex = DataUtils.JAVA_NULL_INDEX;
		/* Iterate the VectorPath and produce the corresponding triangles. */
		for(int i = 0; i < pVectorPath.getPathData().length; i++) {
			/* Acquire a reference to the current VectorPathComponent. */
			final EVectorSegment lVectorPathComponent = EVectorSegment.onFetchSegment(pVectorPath, i);
			/* Process the component. */
			switch(lVectorPathComponent) {
				case MOVE_TO   :
					/* Determine if there are contour vertices to store. */
					if(lCurrentContour.size() >= TriangulationGlobal.MINIMUM_TRIANGULATION_VERTICES) {
						/* Check whether the CurrentMinimumX is smaller than the GlobalMinimumX. */
						if(lCurrentMinimumX < lGlobalMinimumX) {
							/* Update the EnclosingPolygonIndex. */
							lEnclosingPolygonIndex = lPolygonVertices.size();
							/* Update the GlobalMinumumX. */
							lGlobalMinimumX = lCurrentMinimumX;
						}
						/* Add the PrimitiveContour to the Polygon vertices. */
						lPolygonVertices.add(lCurrentContour.toArray(new float[lCurrentContour.size()][]));
					}
					/* Clear the CurrentContour vertices. */
					lCurrentContour.clear();
					/* Initialize the start location of the path. */
					lStartX = pVectorPath.getPathData()[i + 1];
					lStartY = pVectorPath.getPathData()[i + 2];
					/* Update the current MinimumX. */
					lCurrentMinimumX = lStartX;
					/* Add the vertex to the CurrentContour. */
					lCurrentContour.add(new float[]{lStartX, lStartY});
				break;
				case LINE_TO   : 
					/* Calculate the scaled vertices. */
					float lCurrentX  = pVectorPath.getPathData()[i + 1] ;
					float lCurrentY  = pVectorPath.getPathData()[i + 2] ;
					/* Update the CurrentMinimumX. */
					lCurrentMinimumX = (lCurrentX < lCurrentMinimumX) ? lCurrentX : lCurrentMinimumX;
					/* Add the vertex to the CurrentContour. */
					lCurrentContour.add(new float[]{ lCurrentX, lCurrentY });
					break;
				case BEZIER_TO :
					/* Calculate the current Bezier WindingOrder. */
					final EWindingOrder lBezierWindingOrder = MathUtils.onCalculateWindingOrder(pVectorPath.getPathData()[i - 2], pVectorPath.getPathData()[i - 1], pVectorPath.getPathData()[i + 1], pVectorPath.getPathData()[i + 2], pVectorPath.getPathData()[i + 3], pVectorPath.getPathData()[i + 4]);
					/** TODO: Infer the subdivision metric. **/
					/* Calculate the size of each Bezier step. */
					final float lBezierStepSize = 1.0f / VectorPathContext.FILL_BEZIER_SUBDIVISIONS;
					/* Fetch the Hull of the Bezier. */
					final EHull lHull = lBezierWindingOrder.getCorrespondingHull(); /** TODO: Can throw null. Fix. **/
					/* Iterate through each subdivision point. */
					for(int j = 0; j < VectorPathContext.FILL_BEZIER_SUBDIVISIONS; j++) {
						/* Calculate the start point of the Bezier. */
						final float lBezierStartX   = MathUtils.onCalculateQuadraticBezierPoint(pVectorPath.getPathData()[i - 2], pVectorPath.getPathData()[i + 1], pVectorPath.getPathData()[i + 3], j * lBezierStepSize);
						final float lBezierStartY   = MathUtils.onCalculateQuadraticBezierPoint(pVectorPath.getPathData()[i - 1], pVectorPath.getPathData()[i + 2], pVectorPath.getPathData()[i + 4], j * lBezierStepSize);
						/* Calculate the end point of the Bezier. */
						final float lBezierEndX     = MathUtils.onCalculateQuadraticBezierPoint(pVectorPath.getPathData()[i - 2], pVectorPath.getPathData()[i + 1], pVectorPath.getPathData()[i + 3], (j + 1) * lBezierStepSize);
						final float lBezierEndY     = MathUtils.onCalculateQuadraticBezierPoint(pVectorPath.getPathData()[i - 1], pVectorPath.getPathData()[i + 2], pVectorPath.getPathData()[i + 4], (j + 1) * lBezierStepSize);
						/* Calculate the midpoint of the split Bezier. */
						final float lBezierMaximaX  = MathUtils.onCalculateQuadraticBezierPoint(pVectorPath.getPathData()[i - 2], pVectorPath.getPathData()[i + 1], pVectorPath.getPathData()[i + 3], (j * lBezierStepSize) + (lBezierStepSize * 0.5f));
						final float lBezierMaximaY  = MathUtils.onCalculateQuadraticBezierPoint(pVectorPath.getPathData()[i - 1], pVectorPath.getPathData()[i + 2], pVectorPath.getPathData()[i + 4], (j * lBezierStepSize) + (lBezierStepSize * 0.5f));
						/* Re-arrange for control point solution. */
						final float lBezierControlX = ((lBezierMaximaX - (0.25f * lBezierStartX)) - (0.25f * lBezierEndX)) * 2.0f;
						final float lBezierControlY = ((lBezierMaximaY - (0.25f * lBezierStartY)) - (0.25f * lBezierEndY)) * 2.0f;
						/* Draw the Bezier. */
						VectorGlobal.onStorePolarizedBezier(pFloatStore, lBezierStartX, lBezierStartY, lBezierControlX, lBezierControlY, lBezierEndX, lBezierEndY, lHull);
						/* Add the start contour. */
						lCurrentContour.add(new float[]{lBezierStartX, lBezierStartY});
						/* If the Hull is concave... */
						if(lHull == EHull.CONCAVE) { 
							/* Bind to the control point upon the contour. */
							lCurrentContour.add(new float[]{lBezierControlX, lBezierControlY}); 
						}
						/* Append the end contour. */
						lCurrentContour.add(new float[]{lBezierEndX, lBezierEndY});
					}
					/* Update the CurrentMinimumX. */
					lCurrentMinimumX = (pVectorPath.getPathData()[i + 3] < lCurrentMinimumX) ? pVectorPath.getPathData()[i + 3] : lCurrentMinimumX;
				break;
				case CLOSE     : 
					/* Do nothing. This simply indicates a closed path. */
				break;
			}
			/* Jump to the next VectorPathComponent. */
			i += lVectorPathComponent.getNumberOfComponents();
		}
		/* Determine if there are contour vertices to store. */
		if(lCurrentContour.size() >= TriangulationGlobal.MINIMUM_TRIANGULATION_VERTICES) {
			/* Update the EnclosingPolygonIndex. */
			lEnclosingPolygonIndex = (lCurrentMinimumX < lGlobalMinimumX) ? lPolygonVertices.size() : lEnclosingPolygonIndex;
			/* Add the PrimitiveContour to the Polygon vertices. */
			lPolygonVertices.add(lCurrentContour.toArray(new float[lCurrentContour.size()][]));
		}
		/* Ensure the PolygonVertices are non-null before attempting triangulation. */
		if(!lPolygonVertices.isEmpty()) {
			/* Bank the filled triangles. */
			VectorPathContext.onStoreFilledVertices(pVectorPath, pFloatStore, lPolygonVertices, pFillRule, lEnclosingPolygonIndex);
		}
		/* Return the ArrayStore. */
		return pFloatStore;
	}
	
	private final void onUpdatePosition(final float pX, final float pY) {
		/* Update the CurrentX and CurrentY. */
		this.mCurrentX = pX;
		this.mCurrentY = pY;
	}
	
	public final float getCurrentX() {
		return this.mCurrentX;
	}
	
	public final float getCurrentY() {
		return this.mCurrentY;
	}
	
	private final int[] getSegmentBuffer() {
		return this.mSegmentBuffer;
	}
	
	private final float[][] getPositionBuffer() {
		return this.mPositionBuffer;
	}
	
	private final double[][] getAngleBuffer() {
		return this.mAngleBuffer;
	}
	
	private final IVec2.F.W getPICI() {
		return this.mPICI;
	}
	
	private final IVec2.F.W getPOCI() {
		return this.mPOCI;
	}
	
	private final IVec2.F.W getPOCO() {
		return this.mPOCO;
	}
	
	private final IVec2.F.W getPICO() {
		return this.mPICO;
	}
	
	private final IVec2.F.W getCIFI() {
		return this.mCIFI;
	}
	
	private final IVec2.F.W getCOFI() {
		return this.mCOFI;
	}
	
	private final IVec2.F.W getCOFO() {
		return this.mCOFO;
	}
	
	private final IVec2.F.W getCIFO() {
		return this.mCIFO;
	}

}