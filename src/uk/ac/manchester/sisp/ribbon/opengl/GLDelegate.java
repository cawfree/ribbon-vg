package uk.ac.manchester.sisp.ribbon.opengl;

import uk.ac.manchester.sisp.ribbon.common.IDisposable;
import uk.ac.manchester.sisp.ribbon.utils.GLUtils;

public abstract class GLDelegate implements IDisposable {
	
	/* Member Variables. */
	private int         mGLObjectId;
	private final int[] mDelegationBuffer; /** TODO: This is an antipattern. Avoid implementation, or reserve within some active context. **/
	
	public GLDelegate() {
		/* Initialize the GLObjectId. */
		this.mGLObjectId       = IGL.GL_NONE;
		/* Initialize the DelegationBuffer. */
		this.mDelegationBuffer = new int[1];
	}
	
	/* This is called by the GLDelegateManager within the GLES20 context, to allow proper context destruction. */
	protected void onDispose() {}
	
	/* Instantiate the GLDelegate within the OpenGL context. */
	protected final void load(final IGLES20 pGLES20) {
		/* Attempt to load the object. */
		this.mGLObjectId = this.onLoad(pGLES20);
		/* Determine whether the load operation was successful. */
		if(!GLUtils.isUnsuccessful(this.getGLObjectId())) {
			/* Allow the GLDelegate to perform concrete operations once initialized. */
			this.onLoaded(pGLES20);
		}
		else {
			throw new RuntimeException(this.getClass().getSimpleName()+ " failed to load! Aborting...");
		}
	}
	
	@Override public void dispose() {}

	protected abstract int onLoad(final IGLES20 pGLES20);
	protected void         onLoaded(final IGLES20 pGLES20) {}
	
	/* Deconstruct the GLDelegate within the OpenGL context. */
	protected final void unload(final IGLES20 pGLES20) {
		/* Force the GLDelegate to perform concrete unload operations. */
		this.onUnload(pGLES20);
		/* Reset the GLDelegate ID. */
		this.mGLObjectId = IGL.GL_NONE;
	}
	
	protected abstract void onUnload(final IGLES20 pGLES20);
	
	public final int getGLObjectId() {
		return this.mGLObjectId;
	}
	
	public final boolean isLoaded() {
		return !GLUtils.isUnsuccessful(this.getGLObjectId());
	}
	
	protected final int[] getDelegationBuffer() {
		return this.mDelegationBuffer;
	}
	
	protected final boolean isDelegationBufferBadStatus() {
		return GLUtils.isUnsuccessful(this.getDelegationBuffer()[0]);
	}
	
}
