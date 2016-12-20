package uk.ac.manchester.sisp.ribbon.opengl;

public interface IScreenParameters {
	
	public static class Impl implements IScreenParameters {
		
		/* Member Variables. */
		private final int   mX;
		private final int   mY;
		private final int   mWidth;
		private final int   mHeight;
		private final float mDotsPerInch;
		
		public Impl(final int pX, final int pY, final int pWidth, final int pHeight, final float pDotsPerInch) {
			/* Initialize Member Variables. */
			this.mX           = pX;
			this.mY           = pY;
			this.mWidth       = pWidth;
			this.mHeight      = pHeight;
			this.mDotsPerInch = pDotsPerInch;
		}

		@Override public int   getScreenX() { return this.mX; }
		@Override public int   getScreenY() { return this.mY; }

		@Override public int   getScreenWidth()  { return this.mWidth;  }
		@Override public int   getScreenHeight() { return this.mHeight; }

		@Override public float getDotsPerInch()  { return this.mDotsPerInch; }
		
	};

	public abstract int   getScreenX();
	public abstract int   getScreenY();
	public abstract int   getScreenWidth();
	public abstract int   getScreenHeight();
	public abstract float getDotsPerInch();
	
}
