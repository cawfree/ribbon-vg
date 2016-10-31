package uk.ac.manchester.sisp.ribbon.common;

public interface IBounds2 {
	
	public static class Impl implements IBounds2.W {
		
		/* Member Variables. */
		private float mMinimumX;
		private float mMinimumY;
		private float mMaximumX;
		private float mMaximumY;
		
		public <T extends IVec2 & IDim2> Impl(final T pT) {
			/* Initialize the Member Variables to the bounds of T. */
			this(pT.getX(), pT.getY(), pT.getX() + pT.getWidth(), pT.getY() + pT.getHeight());
		}
		
		public Impl(final IBounds2 pBounds2) {
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

		@Override public float getMinimumX() { return this.mMinimumX; }
		@Override public float getMinimumY() { return this.mMinimumY; }
		@Override public float getMaximumX() { return this.mMaximumX; }
		@Override public float getMaximumY() { return this.mMaximumY; }

		@Override public void setMinimumX(float pMinimumX) { this.mMinimumX = pMinimumX; }
		@Override public void setMinimumY(float pMinimumY) { this.mMinimumY = pMinimumY; }
		@Override public void setMaximumX(float pMaximumX) { this.mMaximumX = pMaximumX; }
		@Override public void setMaximumY(float pMaximumY) { this.mMaximumY = pMaximumY; }
		
	}
	
	public static interface W extends IBounds2 {
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