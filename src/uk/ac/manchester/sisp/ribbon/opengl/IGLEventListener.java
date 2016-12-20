package uk.ac.manchester.sisp.ribbon.opengl;

public interface IGLEventListener {
	
	public abstract void onInitialize(final IGLES20 pGL);
	public abstract void onResized(final IGLES20 pGL, final int pX, final int pY, final int pWidth, final int pHeight);
	public abstract void onDisplay(final IGLES20 pGL);
	public abstract void onDispose(final IGLES20 pGL);

}