package uk.ac.manchester.sisp.ribbon.common;

public interface IDim2 {
	
	public static class Impl implements IDim2.W {
		
		/* Member Variables. */
		private float mWidth;
		private float mHeight;
		
		public Impl(final float pWidth, final float pHeight) {
			/* Initialize Member Variables. */
			this.mWidth  = pWidth;
			this.mHeight = pHeight;
		}
		
		/* Setters. */
		@Override public final void  setWidth(final float pWidth)   { this.mWidth  = pWidth;  }
		@Override public final void  setHeight(final float pHeight) { this.mHeight = pHeight; }
		/* Getters. */
		@Override public final float getWidth()                     { return this.mWidth;     }
		@Override public final float getHeight()                    { return this.mHeight;    }
		
	}
	
	public static interface W extends IDim2 {
		public abstract void  setWidth(final float pWidth);
		public abstract void setHeight(final float pHeight);
	}
	
	public abstract float getWidth();
	public abstract float getHeight();
	
}
