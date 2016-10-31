package uk.ac.manchester.sisp.ribbon.opengl;

public interface IGLBindable {
	
	public abstract void   bind(final IGLES20 pGLES20);
	public abstract void unbind(final IGLES20 pGLES20);

}
