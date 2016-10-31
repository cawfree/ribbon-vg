package uk.ac.manchester.sisp.ribbon.common;

public interface IVec2 {
	
	public static class Impl implements IVec2.W {
		
		/* Member Variables. */
		private float mX;
		private float mY;
		
		public Impl() {
			/* Initialize Member Variables. */
			this(0.0f, 0.0f);
		}
		
		public Impl(final IVec2 pVec2) {
			/* Initialize Member Variables. */
			this(pVec2.getX(), pVec2.getY());
		}
		
		public Impl(final float pX, final float pY) {
			/* Initialize Member Variables. */
			this.mX = pX;
			this.mY = pY;
		}

		@Override public void setX(final float pX) {
			this.mX = pX;
		}

		@Override public float getX() {
			return this.mX;
		}

		@Override public void setY(final float pY) {
			this.mY = pY;
		}

		@Override public float getY() {
			return this.mY;
		}
		
	}
	
	public static interface W extends IVec2 {
		public abstract void  setX(final float pX);
		public abstract void  setY(final float pY);
	}
	
	public abstract float getX();
	public abstract float getY();
	
}
