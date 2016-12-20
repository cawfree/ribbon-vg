package uk.ac.manchester.sisp.ribbon.opengl.vector.global;

import uk.ac.manchester.sisp.ribbon.common.IColorRGBA;
import uk.ac.manchester.sisp.ribbon.io.ArrayStore;
import uk.ac.manchester.sisp.ribbon.opengl.vector.VectorPath;
import uk.ac.manchester.sisp.ribbon.opengl.vector.VectorPathContext;

public interface IPathDefinition extends IColorRGBA.W, ITriangulatable {
	
	public static interface Group {
		/* Returns an array of PathDefinitions which apply to a VectorPath. */
		public abstract IPathDefinition[] getPathDefinitions();
	}
	
	public static final class Fill implements IPathDefinition {
		
		/* Member Variables. */
		private float[] mColor;

		public Fill(float[] pColor) {
			/* Initialize Member Variables. */
			this.mColor = pColor;
		}

		@Override public final void onTriangulate(final ArrayStore.Float pFloatStore, final VectorPathContext pVectorPathContext, final VectorPath pVectorPath) {
			/* Triangulate the VectorPath using the default FillRule. */
			pVectorPathContext.onTriangulateFill(pVectorPath, pFloatStore, EFillRule.NON_ZERO);
		}
		
		public final void setColor(final float[] pColor) {
			this.mColor = pColor;
		}
		
		public final float[] getColor() {
			return this.mColor;
		}
		
		@Override public boolean isTriangulatable() { return this.getColor()[IColorRGBA.INDEX_COLOR_ALPHA] > 0.0f; }
		
	}
	
	public static final class Stroke implements IPathDefinition, IStroke.W {
		
		/* Member Variables. */
		private float[]   mColor;
		private float     mStrokeWidth;
		private ELineCap  mLineCap;
		private ELineJoin mLineJoin;
		
		public Stroke(final float[] pColor, final IStroke pStroke) {
			this(pColor, pStroke.getStrokeWidth(), pStroke.getLineCap(), pStroke.getLineJoin());
		}
		
		public Stroke(final float[] pColor, final float pStrokeWidth, final ELineCap pLineCap, final ELineJoin pLineJoin) {
			/* Initialize Member Variables. */
			this.mColor       = pColor;
			this.mStrokeWidth = pStrokeWidth;
			this.mLineCap     = pLineCap;
			this.mLineJoin    = pLineJoin;
		}

		@Override
		public final void onTriangulate(final ArrayStore.Float pFloatStore, final VectorPathContext pVectorPathContext, final VectorPath pVectorPath) {
			/* Triangulates the stroke of the VectorPath. */
			pVectorPathContext.onTriangulateStroke(pVectorPath, pFloatStore, this.getStrokeWidth(), this.getLineCap(), this.getLineJoin());
		}

		@Override public final void setStrokeWidth(final float pStrokeWidth) { this.mStrokeWidth = pStrokeWidth; }
		@Override public final void setLineJoin(final ELineJoin pLineJoin)   { this.mLineJoin = pLineJoin;       }
		@Override public final void setLineCap(final ELineCap pLineCap)      { this.mLineCap = pLineCap;         }
		
		@Override public final float     getStrokeWidth() { return this.mStrokeWidth; }
		@Override public final ELineCap  getLineCap()     { return this.mLineCap;     }
		@Override public final ELineJoin getLineJoin()    { return this.mLineJoin;    }
		
		@Override public final void      setColor(final float[] pColor) { this.mColor = pColor; }
		@Override public final float[]   getColor()                     { return this.mColor;   }

		@Override public boolean isTriangulatable() { return (this.getColor()[IColorRGBA.INDEX_COLOR_ALPHA] > 0.0f && this.getStrokeWidth() > 0.0f); }
		
	};
	
	/** TODO: Abstract? **/
	public abstract boolean isTriangulatable();
	
}