package uk.ac.manchester.sisp.ribbon.font.truetype;

import java.nio.MappedByteBuffer;

import uk.ac.manchester.sisp.ribbon.font.FontGlyph;
import uk.ac.manchester.sisp.ribbon.font.truetype.global.TrueTypeGlobal;
import uk.ac.manchester.sisp.ribbon.io.ArrayStore;
import uk.ac.manchester.sisp.ribbon.opengl.vector.VectorPath;
import uk.ac.manchester.sisp.ribbon.opengl.vector.VectorPathContext;
import uk.ac.manchester.sisp.ribbon.utils.DataUtils;

public abstract class TrueTypeGlyph extends FontGlyph {
	
	/** TODO: FreeType's TTFGlyfTable. **/
	
	protected static final class Simple extends TrueTypeGlyph {
		
		private static final VectorPath onGenerateGlyphPath(final ArrayStore.Float pArrayStore, final VectorPathContext pVectorPathContext, final MappedByteBuffer pMappedByteBuffer, final short pNumberOfContours, final int[] pContoursBuffer, final int[] pXCoordinatesBuffer, final int[] pYCoordinatesBuffer, final byte[] pFlagsBuffer, final byte[] pInstructionsBuffer) {
			/* Declare iteration variable. */
			int i = 0;
			/* Fill the EndPointsOfContours array. (Last org.poly2tri.triangulation.point of each contour) */
			for(i = 0; i < pNumberOfContours; i++) {
				pContoursBuffer[i] = DataUtils.asUnsigned(pMappedByteBuffer.getShort());
			}
			/* The number of mPoints in the glyph is equal to an increment of the final org.poly2tri.triangulation.point in the ContoursBuffer. */
			final int lNumberOfPoints = pContoursBuffer[pNumberOfContours - 1] + 1;
			/* Fill the InstructionsBuffer. */
			pMappedByteBuffer.get(pInstructionsBuffer, 0, DataUtils.asUnsigned(pMappedByteBuffer.getShort()));
			/* Fill the FlagsBuffer. */
			TrueTypeGlyph.Simple.onFillFlagsBuffer(pMappedByteBuffer, pFlagsBuffer, lNumberOfPoints);
			/* Fill the XCoordinatesBuffer. */
			TrueTypeGlyph.Simple.onFillCoordinatesBuffer(pMappedByteBuffer, pXCoordinatesBuffer, TrueTypeGlobal.MASK_X_IS_BYTE, TrueTypeGlobal.MASK_X_IS_SAME, pFlagsBuffer, lNumberOfPoints);
			/* Fill the YCoordinatesBuffer. */
			TrueTypeGlyph.Simple.onFillCoordinatesBuffer(pMappedByteBuffer, pYCoordinatesBuffer, TrueTypeGlobal.MASK_Y_IS_BYTE, TrueTypeGlobal.MASK_Y_IS_SAME, pFlagsBuffer, lNumberOfPoints);
			
			/* Define a variable to track the position in array iteration. */
			int lArrayOffset = 0;
			/* Iterate through each contour. */
			for(i = 0; i < pNumberOfContours; i++) {
				/* Offset to the beginning of the coordinates. */
				final int lStartIndex = lArrayOffset++;
				/* Move to the beginning of the glyph's contour. */
				pVectorPathContext.onMoveTo(pArrayStore, pXCoordinatesBuffer[lStartIndex], pYCoordinatesBuffer[lStartIndex]);
				/* Define a variable to track whether the glyph is up to the final org.poly2tri.triangulation.point on the curve. */
				boolean lLastOnCurve = true;
				/* Iterate through the current contour's co-ordinates. */
				while(lArrayOffset <= pContoursBuffer[i]) {
					/* Track whether the current point resides upon the curve. */
					boolean lIsOnCurve = DataUtils.isFlagSet(pFlagsBuffer[lArrayOffset] & 0xFFFF, TrueTypeGlobal.MASK_ON_CURVE);
					/* Update the path. */
					if(lIsOnCurve) {
						/* Determine whether the last point was on the curve. */
						if(lLastOnCurve) {
							/* Draw a line. */
							pVectorPathContext.onLineTo(pArrayStore, pXCoordinatesBuffer[lArrayOffset], pYCoordinatesBuffer[lArrayOffset]);
						}
						else {
							/* Draw a Bezier. */
							pVectorPathContext.onBezierTo(pArrayStore, pXCoordinatesBuffer[lArrayOffset - 1], pYCoordinatesBuffer[lArrayOffset - 1], pXCoordinatesBuffer[lArrayOffset], pYCoordinatesBuffer[lArrayOffset]);
						}
					}
					else if(!lLastOnCurve) {
						final int lLastX         = pXCoordinatesBuffer[lArrayOffset - 1];
						final int lLastY         = pYCoordinatesBuffer[lArrayOffset - 1];
						final int lIntermediateX = ((lLastX + pXCoordinatesBuffer[lArrayOffset]) / 2);
						final int lIntermediateY = ((lLastY + pYCoordinatesBuffer[lArrayOffset]) / 2);
						/* Draw an assumed intermediary Bezier. */
						pVectorPathContext.onBezierTo(pArrayStore, lLastX, lLastY, lIntermediateX, lIntermediateY);
					}
					/* Update the LastOnCurve metric to match the current point for the following iteration. */
					lLastOnCurve = lIsOnCurve;
					/* Increment the ArrayOffset. */
					lArrayOffset++;
				}
				/* Finish the current contour path. */
				if(!DataUtils.isFlagSet(pFlagsBuffer[lArrayOffset - 1] & 0xFFFF, TrueTypeGlobal.MASK_ON_CURVE)) {
					/* Bezier to the original co-ordinate. */
					pVectorPathContext.onBezierTo(pArrayStore, pXCoordinatesBuffer[lArrayOffset - 1], pYCoordinatesBuffer[lArrayOffset - 1], pXCoordinatesBuffer[lStartIndex], pYCoordinatesBuffer[lStartIndex]);
				}
				/* Close the path. */
				pVectorPathContext.onClosePath(pArrayStore);
			}
			/* Return the generated path. */
			return pVectorPathContext.onCreatePath(pArrayStore); /** TODO: Remove this constant. **/
		}
		
		private static final void onFillFlagsBuffer(final MappedByteBuffer pMappedByteBuffer, final byte[] pFlagsBuffer, final int pNumberOfPoints) {
			/* Process the repeat flags. (Continue parsing until the correct number of mPoints hve been found.) */
			byte lRepeatCount = 0;
			byte lRepeatFlag  = 0;
			for(int i = 0; i < pNumberOfPoints; i++) {
				if (lRepeatCount > 0) {
					pFlagsBuffer[i] = lRepeatFlag;
					lRepeatCount--;
				}
				else {
					pFlagsBuffer[i] = pMappedByteBuffer.get();
					if(DataUtils.isFlagSet(pFlagsBuffer[i], TrueTypeGlobal.MASK_REPEAT)) {
						lRepeatCount = pMappedByteBuffer.get();
						lRepeatFlag  = pFlagsBuffer[i];
					}
				}
			}
		}
		
		private static final void onFillCoordinatesBuffer(final MappedByteBuffer pMappedByteBuffer, final int[] pCoordinatesBuffer, final int pByteMask, final int pSameness, final byte[] pFlagsBuffer, final int pNumberOfPoints) {
			int lLast = 0;
			/* Calculate X Co-ordinates. */
			for(int i = 0; i < pNumberOfPoints; i++) {
				/* Reference the last co-ordinate. */
				pCoordinatesBuffer[i]  = lLast;
				/* Determine the width of the current co-ordinate. */
				if(DataUtils.isFlagSet(pFlagsBuffer[i] & 0xFFFF, pByteMask)) {
					/* Determine polarity of the flag. */
					if(DataUtils.isFlagSet(pFlagsBuffer[i] & 0xFFFF, pSameness)) {
						/* Positive. */
						pCoordinatesBuffer[i] += (pMappedByteBuffer.get() & 0xFF);
					}
					else {
						/* Negative. */
						pCoordinatesBuffer[i] -= (pMappedByteBuffer.get() & 0xFF);
					}
				}
				else if(!DataUtils.isFlagSet(pFlagsBuffer[i], pSameness)) {
					/* Calculate the new offset from the last co-ordinate. */
					pCoordinatesBuffer[i] += (pMappedByteBuffer.getShort());
				}
				/* Make the Last variable equal to the updated co-ordinate. */
				lLast = pCoordinatesBuffer[i];
			}
		}

		protected Simple(final ArrayStore.Float pArrayStore, final VectorPathContext pVectorPathContext, final MappedByteBuffer pMappedByteBuffer, final short pNumberOfContours, final int[] pContoursBuffer, final int[] pXCoordinatesBuffer, final int[] pYCoordinatesBuffer, final byte[] pFlagsBuffer, final byte[] pInstructionsBuffer) {
			super(pMappedByteBuffer, pMappedByteBuffer.getShort(), pMappedByteBuffer.getShort(), pMappedByteBuffer.getShort(), pMappedByteBuffer.getShort(), Simple.onGenerateGlyphPath(pArrayStore, pVectorPathContext, pMappedByteBuffer, pNumberOfContours, pContoursBuffer, pXCoordinatesBuffer, pYCoordinatesBuffer, pFlagsBuffer, pInstructionsBuffer));
		}
		
	}
	
	/** Instantiate a IGlyph from it's location within the Glyf table of a TrueTypeFont. **/
	protected TrueTypeGlyph(final MappedByteBuffer pMappedByteBuffer, final int pMinimumX, final int pMinimumY, final int pMaximumX, final int pMaximumY, final VectorPath pVectorPath) {
		/* Initialize Member Variables. */
		super(pVectorPath, pMinimumX, pMinimumY, pMaximumX, pMaximumY);
	}
	
}
