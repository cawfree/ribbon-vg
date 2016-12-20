package uk.ac.manchester.sisp.ribbon.common;

public interface IColorRGBA {
	
	/* Static Declarations. */
	public static final int INDEX_COLOR_RED   = 0;
	public static final int INDEX_COLOR_GREEN = 1;
	public static final int INDEX_COLOR_BLUE  = 2;
	public static final int INDEX_COLOR_ALPHA = 3;
	
	public static class Impl implements IColorRGBA.W {
		
		/* Member Variables. */
		private float[] mColor;
		
		public Impl(final float[] pColor) {
			/* Initialize Member Variables. */
			this.mColor = pColor;
		}
		
		@Override public void setColor(final float[] pColor) {
			this.mColor = pColor;
		}
		
		@Override public float[] getColor() {
			return this.mColor;
		}
		
	}
	
	public static interface W extends IColorRGBA {
		public abstract void setColor(final float[] pColorRGBA);
	}
	
	/* Returns a floating point array in the form [R, G, B, A], (0.0f -> 1.0f). */
	public abstract float[] getColor();
}
