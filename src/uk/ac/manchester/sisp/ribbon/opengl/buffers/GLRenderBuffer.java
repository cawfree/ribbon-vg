package uk.ac.manchester.sisp.ribbon.opengl.buffers;

import uk.ac.manchester.sisp.ribbon.opengl.GLDelegate;
import uk.ac.manchester.sisp.ribbon.opengl.IGL;
import uk.ac.manchester.sisp.ribbon.opengl.IGLBindable;
import uk.ac.manchester.sisp.ribbon.opengl.IGLES20;
import uk.ac.manchester.sisp.ribbon.utils.GLUtils;

public final class GLRenderBuffer extends GLDelegate implements IGLBindable {
	
	/* Member Variables. */
	
	public GLRenderBuffer() {
		/* Initialize Member Variables. */
	}

	@Override
	protected final int onLoad(final IGLES20 pGLES20) {
		/* Create the DelegationBuffer. */
		final int[] lDelegationBuffer = new int[1];
		/* Request generation of a new RenderBuffer target. */
		pGLES20.glGenRenderbuffers(lDelegationBuffer.length, lDelegationBuffer, 0);
		/* Determine whether instantiation was successful. */
		if(GLUtils.isUnsuccessful(lDelegationBuffer[0])) {
			throw new RuntimeException("Could not create new RenderBuffer object!");
		}
		/* Return the RenderBuffer's pointer. */
		return lDelegationBuffer[0];
	}

	@Override
	public final void bind(final IGLES20 pGLES20) {
		/* Bind to the RenderBufferObject. */
		pGLES20.glBindRenderbuffer(IGL.GL_RENDERBUFFER, this.getGLObjectId());
	}

	@Override
	public final void unbind(final IGLES20 pGLES20) {
		/* Unbind from the RenderBufferObject. */
		pGLES20.glBindRenderbuffer(IGL.GL_RENDERBUFFER, IGL.GL_NONE);
	}

	@Override
	protected final void onUnload(final IGLES20 pGLES20) {
		/* Delegate the RenderBuffer object. */
		pGLES20.glDeleteRenderbuffers(1, new int[]{ this.getGLObjectId() }, 0);
	}

}
