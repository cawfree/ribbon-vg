package uk.ac.manchester.sisp.ribbon.common;

public interface IDim2 {
	
	/* Static Constants. */
	public static final int NUM_COMPONENTS = 2;
	
	public static interface I extends IDim2 {
		
		public static class Impl implements W {
			
			/* Member Variables. */
			private int mWidth;
			private int mHeight;
			
			public Impl() {
				this(0, 0);
			}
			
			public Impl(final IDim2.I pDim2) {
				this(pDim2.getWidth(), pDim2.getHeight());
			}
			
			public Impl(final int pWidth, final int pHeight) {
				/* Initialize Member Variables. */
				this.mWidth  = pWidth;
				this.mHeight = pHeight;
			}
			
			public void setWidth(final int pWidth)   { this.mWidth = pWidth; }
			public int  getWidth()                   { return this.mWidth;   }
			public void setHeight(final int pHeight) { this.mHeight = pHeight; }
			public int  getHeight()                  { return this.mHeight;    }
			
		}
		
		public static interface W extends I {
			public abstract void setWidth(final int pWidth);
			public abstract void setHeight(final int pHeight);
		}
		
		public abstract int getWidth();
		public abstract int getHeight();
		
	}
	
	public static interface F extends IDim2 {
		
		public static class Impl implements W {
			
			/* Member Variables. */
			private float mWidth;
			private float mHeight;
			
			public Impl(final float pWidth, final float pHeight) {
				/* Initialize Member Variables. */
				this.mWidth  = pWidth;
				this.mHeight = pHeight;
			}
			
			public void  setWidth(final float pWidth)   { this.mWidth = pWidth;   }
			public float getWidth()                     { return this.mWidth;     }
			public void  setHeight(final float pHeight) { this.mHeight = pHeight; }
			public float getHeight()                    { return this.mHeight;    }
			
		}
		
		public static interface W extends F {
			public abstract void setWidth(final float pWidth);
			public abstract void setHeight(final float pHeight);
		}
		
		public abstract float getWidth();
		public abstract float getHeight();
		
	}
	
}
