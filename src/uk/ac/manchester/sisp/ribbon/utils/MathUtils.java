package uk.ac.manchester.sisp.ribbon.utils;

import java.util.List;

import uk.ac.manchester.sisp.ribbon.common.IBounds2;
import uk.ac.manchester.sisp.ribbon.common.IDim2;
import uk.ac.manchester.sisp.ribbon.common.IVec2;
import uk.ac.manchester.sisp.ribbon.opengl.vector.global.EWindingOrder;

public class MathUtils {

	public static final double PI_OVER_2          = Math.PI * 0.5;
	public static final double PI_TIMES_2         = Math.PI * 2.0;
	public static final float  SCALAR_1_E_MINUS_3 = 1.0f /       1000.0f;
	public static final float  SCALAR_1_E_MINUS_6 = 1.0f /    1000000.0f;
	public static final float  SCALAR_1_E_MINUS_9 = 1.0f / 1000000000.0f;
	public static final float  TOLERANCE_DIV_ZERO = 0.000001f;
	
	public static final int negate(final int pValue) {
		/* Unary minus implementation. */
		return (-pValue);
	}
	
	public static final <T extends IVec2.F & IDim2.F> IVec2.F.W onCalculateCentreOf(final T pT) {
		return new IVec2.F.Impl(pT.getX() + (pT.getWidth() * 0.5f), pT.getY() + (pT.getHeight() * 0.5f));
	}
	
	public static final IVec2.F.W onCalculateCentreOf(final IBounds2.F pBounds2) {
		return new IVec2.F.Impl(((pBounds2.getMaximumX() - pBounds2.getMinimumX()) * 0.5f) + pBounds2.getMinimumX(), ((pBounds2.getMaximumY() - pBounds2.getMinimumY()) * 0.5f) + pBounds2.getMinimumY());
	}
	
	public static final IVec2.I.W onCalculateCentreOf(final IBounds2.I pBounds2) {
		return new IVec2.I.Impl(((pBounds2.getMaximumX() - pBounds2.getMinimumX()) / 2) + pBounds2.getMinimumX(), ((pBounds2.getMaximumY() - pBounds2.getMinimumY()) / 2) + pBounds2.getMinimumY());
	}
	
	public static final boolean isWithinXBounds(final float pX, final IBounds2.F pBounds2) {
		return pX > pBounds2.getMinimumX() && pX < pBounds2.getMaximumX(); 
	}
	
	public static final boolean isWithinYBounds(final float pY, final IBounds2.F pBounds2) {
		 return pY > pBounds2.getMinimumY() && pY < pBounds2.getMaximumY();
	}
	
	public static final boolean isWithinBounds(final float pX, final float pY, final IBounds2.F pBounds2) {
		return MathUtils.isWithinXBounds(pX, pBounds2) && MathUtils.isWithinYBounds(pY, pBounds2);
	}
	
	public static final float onCalculateWidth(final IBounds2.F pBounds2) {
		return pBounds2.getMaximumX() - pBounds2.getMinimumX();
	}
	
	public static final int onCalculateWidth(final IBounds2.I pBounds2) {
		return pBounds2.getMaximumX() - pBounds2.getMinimumX();
	}
	
	public static final float onCalculateHeight(final IBounds2.F pBounds2) {
		return pBounds2.getMaximumY() - pBounds2.getMinimumY();
	}
	
	public static final int onCalculateHeight(final IBounds2.I pBounds2) {
		return pBounds2.getMaximumY() - pBounds2.getMinimumY();
	}
	
	public static final float clamp(float a, float mn, float mx) { 
		return a < mn ? mn : (a > mx ? mx : a); 
	}

	public static final int clamp(int a, int mn, int mx) {
		return a < mn ? mn : (a > mx ? mx : a); 
	}
	
	/** TODO: Remove these functions. Should leave squaring up to the handler. **/
	public static final int calculateSquareOf(final int pX) {
		return (pX * pX);
	}
	
	public static final float calculateSquareOf(final float pX) {
		return (pX * pX);
	}
	
	/** TODO: Obscures useful computation. Remove. **/
	public static final float calculateCubeOf(final float pX) {
		return pX * MathUtils.calculateSquareOf(pX);
	}
	
	/** Returns the absolute distance between points (X0, Y0) and (X1, Y1). **/
	public static final float calculateMagnitudeOf(final float pX0, final float pY0, final float pX1, final float pY1) {
		return MathUtils.calculateMagnitudeOf(pX1 - pX0, pY1 - pY0);
	}
	
	public static final float calculateMagnitudeOf(final IVec2.F pA, final IVec2.F pB) {
		return MathUtils.calculateMagnitudeOf(pA.getX(), pA.getY(), pB.getX(), pB.getY());
	}
	
	public static final float calculateMagnitudeOf(final IVec2.I pA, final IVec2.I pB) {
		return MathUtils.calculateMagnitudeOf(pA.getX(), pA.getY(), pB.getX(), pB.getY());
	}
	
	public static final float calculateMagnitudeOf(final float pX, final float pY) {
		return (float)(Math.sqrt(MathUtils.calculateSquareOf(pX) + MathUtils.calculateSquareOf(pY)));
	}
	
	public static final float calculateMagnitudeOf(final float pX, final float pY, final float pZ) {
		return (float)(Math.sqrt(MathUtils.calculateSquareOf(pX) + MathUtils.calculateSquareOf(pY) + MathUtils.calculateSquareOf(pZ)));
	}
	
	public static final float calculateMagnitudeOf(final IVec2.F pVec2) {
		return MathUtils.calculateMagnitudeOf(pVec2.getX(), pVec2.getY());
	}
	
	public static final float calculateMagnitudeOf(final IVec2.I pVec2) {
		return MathUtils.calculateMagnitudeOf(pVec2.getX(), pVec2.getY());
	}
	
	public static final int toNearestPowerOfTwo(final int pValue) {
		return 1 << (Integer.SIZE - (Integer.numberOfLeadingZeros(pValue - 1)));
	}
	
	public static final float calculateCrossOf(final float pDX0, final float pDY0, final float pDX1, final float pDY1) {
		return  (pDX0 * pDY1) - (pDX1 * pDY0);
	}
	
	public static final float calculateCrossOf(final IVec2.F pA, final IVec2.F pB) {
		return  (pA.getX() * pB.getY()) - (pB.getX() * pA.getY());
	}
	
	public static final boolean isWithinTolerance(final float pValue, final float pTolerance) {
		return (pValue > (-1 * pTolerance) && pValue < pTolerance);
	}
	
	public static final boolean isWithinTolerance(final double pValue, final double pTolerance) {
		return (pValue > (-1 * pTolerance) && pValue < pTolerance);
	}
	
	public static final double onSplitRadians(final double pA, final double pB) {
		return (Math.PI) - Math.abs(Math.abs(pA - pB) - (Math.PI)); 
	}
	
	public static final float onCalculateQuadraticBezierPoint(final float pB0, final float pB1, final float pB2, final float pT) {
		return (pT * pT) * (pB2 - (2 * pB1) + pB0) + (pT * (2 * pB1 - 2 * pB0)) + pB0;
	}
	
	public static final float onCalculateCubicBezierPoint(final float pB0, final float pB1, final float pB2, final float pB3, final float pT) {
		/* Calculate shared results. */
		float lX0 =   1 - pT;
		float lX1 = lX0 * lX0;
		float lX2 =  pT * pT;
		/* Return the parameterized cubic Bezier point. */
		return pB0*(lX1 * lX0) + pB1*(3 * pT * lX1) + pB2*(3 * lX2 * lX0) + pB3 * (lX2 * pT);
	}
	
	public static final float calculateCrossOf(final float pX0, final float pY0, final float pX1, final float pY1, final float pX2, final float pY2, final float pX3, final float pY3) {
		return (pX0 - pX1) * (pY2 - pY3) - (pY0 - pY1) * (pX2 - pX3);
	}
	
	/** TODO: Abstract the cross calculation. **/
	public static final float calculateIntersectionOf(final IVec2.F.W pVec2, final float pX0, final float pY0, final float pX1, final float pY1, final float pX2, final float pY2, final float pX3, final float pY3) {
		final float   lCross = MathUtils.calculateCrossOf(pX0, pY0, pX1, pY1, pX2, pY2, pX3, pY3);
		/* Define the intersection. */
		pVec2.setX(lCross != 0 ? (((pX2 - pX3) * (pX0 * pY1 - pY0 * pX1) - (pX0 - pX1) * (pX2 * pY3 - pY2 * pX3)) / lCross) : Float.NaN);
		pVec2.setY(lCross != 0 ? (((pY2 - pY3) * (pX0 * pY1 - pY0 * pX1) - (pY0 - pY1) * (pX2 * pY3 - pY2 * pX3)) / lCross) : Float.NaN);
		/* Return the cross product. */
		return lCross;
	}
	
	public static final EWindingOrder onCalculateWindingOrder(final float pX0, final float pY0, final float pX1, final float pY1, final float pX2, final float pY2) {
		final float lCross = ((pY1 - pY0) * (pX2 - pX1)) - ((pX1 - pX0) * (pY2 - pY1));
		return		lCross > 0 ? EWindingOrder.CW : lCross < 0 ? EWindingOrder.CCW : EWindingOrder.COLINEAR;
	}
	
	public static final EWindingOrder onCalculateWindingOrder(final float pWindingSum) {
		return pWindingSum < 0 ? EWindingOrder.CCW : EWindingOrder.CW;
	}
	
	public static final float onCalculatePolygonWindingSum(final float[][] pPolygon) {
		/* Define a variable for accumulating the WindingSum. */
		float lWindingSum = 0;
		/* Calculate the original order of the Polygon ring. */
		for(int i = 0, j = pPolygon.length - 1; i < pPolygon.length; j = i++) {
			final float[] p1 = pPolygon[i];
			final float[] p2 = pPolygon[j];
			lWindingSum += (p2[0] - p1[0]) * (p1[1] + p2[1]);
		}
		return lWindingSum;
	}
	
	public static final boolean isIntersecting(final float pX0, final float pY0, final float pX1, final float pY1, final float pX2, final float pY2, final float pX3, final float pY3) {
		return MathUtils.onCalculateWindingOrder(pX0, pY0, pX1, pY1, pX2, pY2) != MathUtils.onCalculateWindingOrder(pX0, pY0, pX1, pY1, pX3, pY3) && MathUtils.onCalculateWindingOrder(pX2, pY2, pX3, pY3, pX0, pY0) != MathUtils.onCalculateWindingOrder(pX2, pY2, pX3, pY3, pX1, pY1);
	}
	
	public static final double onCalculateAngleBetween(final IVec2.F pA, final IVec2.F pB) {
		return Math.atan2(pB.getY() - pA.getY(), pB.getX() - pB.getY());
	}
	
	/* A simple bounding box collision detection algorithm. This checks if the point (pX, pY) lies within the bounds (pX0, pY0) and (pX1, pY1). */
	public static final boolean isIntersecting(final float pX, final float pY, final float pX0, final float pY0, final float pX1, final float pY1) {
		return (pX > pX0) && (pY > pY0) && (pX < pX1) && (pY < pY1);
	}
	
	public static final <T extends IVec2.F & IDim2.F> boolean isIntersecting(final T pT, final float pX, final float pY) {
		return (pX > pT.getX()) && (pY > pT.getY()) && (pX < (pT.getX() + pT.getWidth())) && (pY < (pT.getY() + pT.getHeight()));
	}
	
	public static final <T extends IVec2.I & IDim2.I> boolean isIntersecting(final T pT, final int pX, final int pY) {
		return (pX > pT.getX()) && (pY > pT.getY()) && (pX < (pT.getX() + pT.getWidth())) && (pY < (pT.getY() + pT.getHeight()));
	}
	
	public static final <T extends IVec2 & IDim2.F> float onCalculateArea(final T pT) {
		/* Calculate the Area of T. */
		return (pT.getWidth() * pT.getHeight());
	}
	
	public static final <T extends IBounds2.F> float onCalculateArea(final T pT) {
		/* Calculate the Area of T. */
		return (pT.getMaximumX() - pT.getMinimumX()) * (pT.getMaximumY() - pT.getMinimumY());
	}
	
//	public static final <T extends IVec2.F & IDim2.F> void onCalculateGeometricCentre(final List<T> pT, final IVec2 pResult) {
//		/* Declare the initial Bounds. */
//		final IBounds2.F.W lBounds = new IBounds2.F.Impl();
//		/* Iterate the List. */
//		for(final T lT : pT) {
//			/* Update the horizontal bounds. */
//			lBounds.setMaximumX(Math.max((lT.getX() + lT.getWidth()),  lBounds.getMaximumX()));
//			lBounds.setMinimumX(Math.min((lT.getX()),                  lBounds.getMinimumX()));
//			/* Update the vertical bounds. */
//			lBounds.setMaximumY(Math.max((lT.getY() + lT.getHeight()), lBounds.getMaximumY()));
//			lBounds.setMinimumY(Math.min((lT.getY()),                  lBounds.getMinimumY()));
//		}
//	}
	
	public static final float normalize(final IVec2.F.W pVec2, final float pDivideByZeroLimit) {
		/* Calculate the magnitude of the vector to normalize. */
		final float lDistance = MathUtils.calculateMagnitudeOf(pVec2);
		/* Scale the by the magnitude. */
		if(lDistance > pDivideByZeroLimit) {
			/* Calculate the reciprocal distance. */
			float lInverseDistance = 1.0f / lDistance;
			pVec2.setX(pVec2.getX() * lInverseDistance);
			pVec2.setY(pVec2.getY() * lInverseDistance);
		}
		return lDistance;
	}
	
	public static final float onCalculateMidpoint(final float pMaximum, final float pMinimum) {
		return ((pMaximum - pMinimum) * 0.5f) + pMinimum;
	}
	
	public static final void onSupplyOffset(final IVec2.F.W pVec2, final float pX, final float pY) {
		pVec2.setX(pVec2.getX() + pX);
		pVec2.setY(pVec2.getY() + pY);
	}
	
	public static final void onSupplyOffset(final IVec2.F.W pVec2, final IVec2.F pOffset) {
		MathUtils.onSupplyOffset(pVec2, pOffset.getX(), pOffset.getY());
	}
	
	public static final void onWithdrawOffset(final IVec2.F.W pVec2, final float pX, final float pY) {
		pVec2.setX(pVec2.getX() - pX);
		pVec2.setY(pVec2.getY() - pY);
	}
	
	public static final void onWithdrawOffset(final IVec2.F.W pVec2, final IVec2.F pOffset) {
		MathUtils.onWithdrawOffset(pVec2, pOffset.getX(), pOffset.getY());
	}
	
	public static final void onSupplyOffset(final IVec2.I.W pVec2, final int pX, final int pY) {
		pVec2.setX(pVec2.getX() + pX);
		pVec2.setY(pVec2.getY() + pY);
	}
	
	public static final void onSupplyOffset(final IVec2.I.W pVec2, final IVec2.I pOffset) {
		MathUtils.onSupplyOffset(pVec2, pOffset.getX(), pOffset.getY());
	}
	
	public static final void onWithdrawOffset(final IVec2.I.W pVec2, final int pX, final int pY) {
		pVec2.setX(pVec2.getX() - pX);
		pVec2.setY(pVec2.getY() - pY);
	}
	
	public static final void onWithdrawOffset(final IVec2.I.W pVec2, final IVec2.I pOffset) {
		MathUtils.onWithdrawOffset(pVec2, pOffset.getX(), pOffset.getY());
	}
	
	/* Updates the members of EnclosingBounds to ensure the dimensions of T can be completely encapsulated. */
	public static final void onEncapsulateBounds(final IBounds2.F.W pEnclosingBounds, final float pMinimumX, final float pMinimumY, final float pMaximumX, final float pMaximumY) {
		/* Update the MinimumX and MinimumY. */
		pEnclosingBounds.setMinimumX(Math.min(pEnclosingBounds.getMinimumX(), pMinimumX));
		pEnclosingBounds.setMinimumY(Math.min(pEnclosingBounds.getMinimumY(), pMinimumY));
		/* Update the MaximumX and MaximumY. */
		pEnclosingBounds.setMaximumX(Math.max(pEnclosingBounds.getMaximumX(), pMaximumX));
		pEnclosingBounds.setMaximumY(Math.max(pEnclosingBounds.getMaximumY(), pMaximumY));
	}
	
	/* Updates the members of EnclosingBounds to ensure the dimensions of T can be completely encapsulated. */
	public static final void onEncapsulateBounds(final IBounds2.I.W pEnclosingBounds, final int pMinimumX, final int pMinimumY, final int pMaximumX, final int pMaximumY) {
		/* Update the MinimumX and MinimumY. */
		pEnclosingBounds.setMinimumX(Math.min(pEnclosingBounds.getMinimumX(), pMinimumX));
		pEnclosingBounds.setMinimumY(Math.min(pEnclosingBounds.getMinimumY(), pMinimumY));
		/* Update the MaximumX and MaximumY. */
		pEnclosingBounds.setMaximumX(Math.max(pEnclosingBounds.getMaximumX(), pMaximumX));
		pEnclosingBounds.setMaximumY(Math.max(pEnclosingBounds.getMaximumY(), pMaximumY));
	}
	
	public static final void onEncapsulateBounds(final IBounds2.F.W pEnclosingBounds, final IBounds2.F pBounds) {
		/* Encapsulate using the existing metrics within the Bounds. */
		MathUtils.onEncapsulateBounds(pEnclosingBounds, pBounds.getMinimumX(), pBounds.getMinimumY(), pBounds.getMaximumX(), pBounds.getMaximumY());
	}
	
	public static final <T extends IVec2.F & IDim2.F> void onEncapsulateBounds(final IBounds2.F.W pEnclosingBounds, final T pT) {
		/* Encapsulate using the existing metrics within the Bounds. */
		MathUtils.onEncapsulateBounds(pEnclosingBounds, pT.getX(), pT.getY(), pT.getX() + pT.getWidth(), pT.getY() + pT.getHeight());
	}
	
	public static final <T extends IVec2.I & IDim2.I> void onEncapsulateBounds(final IBounds2.I.W pEnclosingBounds, final T pT) {
		/* Encapsulate using the existing metrics within the Bounds. */
		MathUtils.onEncapsulateBounds(pEnclosingBounds, pT.getX(), pT.getY(), pT.getX() + pT.getWidth(), pT.getY() + pT.getHeight());
	}
	
	public static final void setBounds(final IBounds2.F.W pBounds2, final float pMinimumX, final float pMinimumY, final float pMaximumX, final float pMaximumY) {
		/* Define the MinimumX and MinimumY. */
		pBounds2.setMinimumX(pMinimumX);
		pBounds2.setMinimumY(pMinimumY);
		/* Define the MaximumX and MaximumY. */
		pBounds2.setMaximumX(pMaximumX);
		pBounds2.setMaximumY(pMaximumY);
	}
	
	public static final void setBounds(final IBounds2.I.W pBounds2, final int pMinimumX, final int pMinimumY, final int pMaximumX, final int pMaximumY) {
		/* Define the MinimumX and MinimumY. */
		pBounds2.setMinimumX(pMinimumX);
		pBounds2.setMinimumY(pMinimumY);
		/* Define the MaximumX and MaximumY. */
		pBounds2.setMaximumX(pMaximumX);
		pBounds2.setMaximumY(pMaximumY);
	}
	
	public static final void setBounds(final IBounds2.F.W pReturnBounds, final IBounds2.F pBounds2) {
		/* Set the bounds of the ReturnBounds to match the Bounds2. */
		MathUtils.setBounds(pReturnBounds, pBounds2.getMinimumX(), pBounds2.getMinimumY(), pBounds2.getMaximumX(), pBounds2.getMaximumY());
	}
	
	public static final void setBounds(final IBounds2.I.W pReturnBounds, final IBounds2.I pBounds2) {
		/* Set the bounds of the ReturnBounds to match the Bounds2. */
		MathUtils.setBounds(pReturnBounds, pBounds2.getMinimumX(), pBounds2.getMinimumY(), pBounds2.getMaximumX(), pBounds2.getMaximumY());
	}
	
	public static final <T extends IVec2.I & IDim2.I> int onCalculateWidth(final List<T> pT) {
		/* Declare variables to track the MinimumX and MaximumX. */
		int lMinimumX = Integer.MAX_VALUE;
		int lMaximumX = Integer.MIN_VALUE;
		/* Iterate across the UIElements. */
		for(final T lT : pT) {
			/* Update the MinimumX and MaximumX. */
			lMinimumX = Math.min(lMinimumX, lT.getX());
			lMaximumX = Math.max(lMaximumX, lT.getX() + lT.getWidth());
		}
		/* Return the difference between the MinimumX and MaximumX. */
		return lMaximumX - lMinimumX;
	}
	
	public static final void setPosition(final IVec2.I.W pVec2, final IVec2.I pVec) {
		/* Set the point's location. */
		MathUtils.setPosition(pVec2, pVec.getX(), pVec2.getY());
	}
	
	public static final void setPosition(final IVec2.I.W pVec2, final int pX, final int pY) {
		/* Set the point's location. */
		pVec2.setX(pX);
		pVec2.setY(pY);
	}
	
	public static final void setPosition(final IVec2.F.W pVec2, final float pX, final float pY) {
		/* Set the point's location. */
		pVec2.setX(pX);
		pVec2.setY(pY);
	}
	
	public static final void setDimension(final IDim2.I.W pDim2, final int pWidth, final int pHeight) {
		/* Set the new dimension. */
		pDim2.setWidth(pWidth);
		pDim2.setHeight(pHeight);
	}
	
	public static final void setDimension(final IDim2.I.W pDim2, final IDim2.I pDimension) {
		/* Set the new dimension. */
		MathUtils.setDimension(pDim2, pDimension.getWidth(), pDimension.getHeight());
	}
	
}
