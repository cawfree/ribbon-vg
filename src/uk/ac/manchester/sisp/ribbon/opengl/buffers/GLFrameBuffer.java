package uk.ac.manchester.sisp.ribbon.opengl.buffers;

import uk.ac.manchester.sisp.ribbon.opengl.GLDelegate;
import uk.ac.manchester.sisp.ribbon.opengl.IGL;
import uk.ac.manchester.sisp.ribbon.opengl.IGLBindable;
import uk.ac.manchester.sisp.ribbon.opengl.IGLES20;
import uk.ac.manchester.sisp.ribbon.utils.GLUtils;

public final class GLFrameBuffer extends GLDelegate implements IGLBindable {
	
	/* Member Variables. */
	
	public GLFrameBuffer() {
		/* Initialize Member Variables. */
		
	}

	@Override
	protected final int onLoad(final IGLES20 pGLES20) {
		/* Allocate the DelegationBuffer. */
		final int[] lDelegationBuffer = new int[1];
		/* Request generation of a new FrameBuffer object. */
		pGLES20.glGenFramebuffers(lDelegationBuffer.length, lDelegationBuffer, 0);
		/* Ensure creation was successful. */
		if(GLUtils.isUnsuccessful(lDelegationBuffer[0])) {
			throw new RuntimeException("Could not create FrameBuffer object!");
		}
		/* Return a pointer to the created FrameBuffer. */
		return lDelegationBuffer[0];
	}

	@Override
	public final void bind(final IGLES20 pGLES20) {
		/* Bind to the FrameBuffer. */
		pGLES20.glBindFramebuffer(IGL.GL_FRAMEBUFFER, this.getGLObjectId());
	}

	@Override
	public final void unbind(final IGLES20 pGLES20) {
		/* Unbind from the FrameBuffer. */
		pGLES20.glBindFramebuffer(IGL.GL_FRAMEBUFFER, IGL.GL_NONE);
	}

	@Override
	protected final void onUnload(final IGLES20 pGLES20) {
		/* Delete the FrameBuffer. */
		pGLES20.glDeleteFramebuffers(1, new int[]{ this.getGLObjectId() }, 0);
	}

}
