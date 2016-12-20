package uk.ac.manchester.sisp.ribbon.common;

public interface IBounds2 {
	
//	public <T extends IVec2 & IDim2> Impl(final T pT) {
//		/* Initialize the Member Variables to the bounds of T. */
//		this(pT.getX(), pT.getY(), pT.getX() + pT.getWidth(), pT.getY() + pT.getHeight());
//	}
//	
//	public Impl(final IBounds2 pBounds2) {
//		/* Initialize the Member Variables to the bounds of T. */
//		this(pBounds2.getMinimumX(), pBounds2.getMinimumY(), pBounds2.getMaximumX(), pBounds2.getMaximumY());
//	}
//	
//	public Impl() {
//		/* Initialize the Member Variables to a bounding box across infinity. (Prepares for range analysis.) */
//		this(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);
//	}
//	
//	public Impl(final float pMinimumX, final float pMinimumY, final float pMaximumX, final float pMaximumY) {
//		/* Initialize Member Variables. */
//		this.mMinimumX = pMinimumX;
//		this.mMinimumY = pMinimumY;
//		this.mMaximumX = pMaximumX;
//		this.mMaximumY = pMaximumY;
//	}
	
	
	/* Static Constants. */
	public static final int NUM_COMPONENTS = 4;
	
	public static interface I extends IBounds2 {
		
		public static class Impl implements IBounds2.I.W {
			
			/* Member Variables. */
			private int mMinimumX;
			private int mMinimumY;
			private int mMaximumX;
			private int mMaximumY;
//			This is good but we're disabling it for testing.
//			public <T extends IVec2.I & IDim2.I>Impl(final T pT) {
//				this(pT.getX(), pT.getY(), (pT.getX() + pT.getWidth()), (pT.getY() + pT.getHeight()));
//			}
			
			public Impl(final IBounds2.I pBounds2) {
				/* Initialize the Member Variables to the bounds of T. */
				this(pBounds2.getMinimumX(), pBounds2.getMinimumY(), pBounds2.getMaximumX(), pBounds2.getMaximumY());
			}
			
			public Impl() {
				/* Initialize the Member Variables to a bounding box across infinity. (Prepares for range analysis.) */
				this(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
			}
			
			public Impl(final int pMinimumX, final int pMinimumY, final int pMaximumX, final int pMaximumY) {
				/* Initialize Member Variables. */
				this.mMinimumX = pMinimumX;
				this.mMinimumY = pMinimumY;
				this.mMaximumX = pMaximumX;
				this.mMaximumY = pMaximumY;
			}

			@Override public int  getMinimumX()                    { return this.mMinimumX;      }
			@Override public int  getMinimumY()                    { return this.mMinimumY;      }
			@Override public int  getMaximumX()                    { return this.mMaximumX;      }
			@Override public int  getMaximumY()                    { return this.mMaximumY;      }
			@Override public void setMinimumX(final int pMinimumX) { this.mMinimumX = pMinimumX; }
			@Override public void setMinimumY(final int pMinimumY) { this.mMinimumY = pMinimumY; }
			@Override public void setMaximumX(final int pMaximumX) { this.mMaximumX = pMaximumX; }
			@Override public void setMaximumY(final int pMaximumY) { this.mMaximumY = pMaximumY; }
			
		}
		
		public static interface W extends IBounds2.I {
			public abstract void setMinimumX(final int pMinimumX);
			public abstract void setMinimumY(final int pMinimumY);
			public abstract void setMaximumX(final int pMaximumX);
			public abstract void setMaximumY(final int pMaximumY);
		}
		
		public abstract int getMinimumX();
		public abstract int getMinimumY();
		public abstract int getMaximumX();
		public abstract int getMaximumY();
	}
	
	public static interface F extends IBounds2 {
		
		public static class Impl implements IBounds2.F.W {
			
			/* Member Variables. */
			private float mMinimumX;
			private float mMinimumY;
			private float mMaximumX;
			private float mMaximumY;
			
			public Impl(final IBounds2.F pBounds2) {
				/* Initialize the Member Variables to the bounds of T. */
				this(pBounds2.getMinimumX(), pBounds2.getMinimumY(), pBounds2.getMaximumX(), pBounds2.getMaximumY());
			}
			
			public Impl() {
				/* Initialize the Member Variables to a bounding box across infinity. (Prepares for range analysis.) */
				this(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);
			}
			
			public Impl(final float pMinimumX, final float pMinimumY, final float pMaximumX, final float pMaximumY) {
				/* Initialize Member Variables. */
				this.mMinimumX = pMinimumX;
				this.mMinimumY = pMinimumY;
				this.mMaximumX = pMaximumX;
				this.mMaximumY = pMaximumY;
			}

			@Override public float  getMinimumX()                      { return this.mMinimumX;      }
			@Override public float  getMinimumY()                      { return this.mMinimumY;      }
			@Override public float  getMaximumX()                      { return this.mMaximumX;      }
			@Override public float  getMaximumY()                      { return this.mMaximumY;      }
			@Override public void   setMinimumX(final float pMinimumX) { this.mMinimumX = pMinimumX; }
			@Override public void   setMinimumY(final float pMinimumY) { this.mMinimumY = pMinimumY; }
			@Override public void   setMaximumX(final float pMaximumX) { this.mMaximumX = pMaximumX; }
			@Override public void   setMaximumY(final float pMaximumY) { this.mMaximumY = pMaximumY; }
			
		}
		
		public static interface W extends IBounds2.F {
			public abstract void setMinimumX(final float pMinimumX);
			public abstract void setMinimumY(final float pMinimumY);
			public abstract void setMaximumX(final float pMaximumX);
			public abstract void setMaximumY(final float pMaximumY);
		}
		
		public abstract float getMinimumX();
		public abstract float getMinimumY();
		public abstract float getMaximumX();
		public abstract float getMaximumY();
	}
	
}