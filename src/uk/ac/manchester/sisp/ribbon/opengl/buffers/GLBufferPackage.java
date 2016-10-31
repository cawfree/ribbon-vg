package uk.ac.manchester.sisp.ribbon.opengl.buffers;

import uk.ac.manchester.sisp.ribbon.common.IDisposable;

public class GLBufferPackage <T extends GLBuffer<?>> implements IDisposable {
	
	/* Member Variables. */
	private final T     mGLBuffer;
	private       int[] mIndices;
	
	public GLBufferPackage(final T pGLBuffer, final int[] pIndices) {
		/* Initialize Member Variables. */
		this.mGLBuffer = pGLBuffer;
		this.mIndices  = pIndices;
	}
	
	public final T getGLBuffer() {
		return this.mGLBuffer;
	}
	
	public final int[] getIndices() {
		return this.mIndices;
	}

	@Override
	public final void dispose() {
		/* Dispose of the GLBuffer. */
		this.getGLBuffer().dispose(); /** TODO: Is this safe? Do we really own the GLBuffer? **/
		/* Nullify dependent variables. */
		this.mIndices = null;
	}

}