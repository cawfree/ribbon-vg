package uk.ac.manchester.sisp.ribbon.opengl.textures;

import uk.ac.manchester.sisp.ribbon.common.IDim2;
import uk.ac.manchester.sisp.ribbon.image.IImage;
import uk.ac.manchester.sisp.ribbon.io.EEntryMode;
import uk.ac.manchester.sisp.ribbon.opengl.GLContext;
import uk.ac.manchester.sisp.ribbon.opengl.IGL;
import uk.ac.manchester.sisp.ribbon.opengl.IGLES20;
import uk.ac.manchester.sisp.ribbon.opengl.IGLRunnable;
import uk.ac.manchester.sisp.ribbon.opengl.buffers.GLFrameBuffer;
import uk.ac.manchester.sisp.ribbon.opengl.buffers.GLRenderBuffer;

public abstract class GLRenderTexture implements IGLRunnable, IImage, IDim2.I {
	
	/* Member Variables. */
	private final int       mWidth;
	private final int       mHeight;
	private final int       mInternalFormat;
	private final int       mFormat;
	private       GLTexture mGLTexture;
	
	public GLRenderTexture(final int pWidth, final int pHeight, final int pInternalFormat, final int pFormat) {
		/* Initialize Member Variables. */
		this.mWidth          = pWidth;
		this.mHeight         = pHeight;
		this.mInternalFormat = pInternalFormat;
		this.mFormat         = pFormat;
		this.mGLTexture      = null;
	}
	
	@Override
	public final void run(final IGLES20 pGLES20, final GLContext pGLContext) {
		/* Instantiate the GLDynamicTexture. */
		this.mGLTexture = new GLTexture(this.getInternalFormat(), this.getFormat(), IGL.GL_UNSIGNED_BYTE, this.getWidth(), this.getHeight());
		/* Create a new GLFrameBuffer. */
		final GLFrameBuffer lGLFrameBuffer   = new GLFrameBuffer();
		/* Create a new GLRenderBuffer. */
		final GLRenderBuffer lGLRenderBuffer = new GLRenderBuffer();
		/* Load all the dependencies. */
		pGLContext.onHandleDelegates(EEntryMode.SUPPLY, pGLES20, this.getGLTexture(), lGLFrameBuffer, lGLRenderBuffer);
		/* Bind to the DynamicTexure. */
		this.getGLTexture().bind(pGLES20);
		/* Bind to the FrameBuffer. */
		lGLFrameBuffer.bind(pGLES20);
		/* Attach the RenderedTexture. */
		pGLES20.glFramebufferTexture2D(IGL.GL_FRAMEBUFFER, IGL.GL_COLOR_ATTACHMENT0, this.getGLTexture().getTextureTarget(), this.getGLTexture().getGLObjectId(), 0);
		/* Bind to the RenderBuffer. */
		lGLRenderBuffer.bind(pGLES20);
		/* Configure the RenderBuffer's storage. */
		pGLES20.glRenderbufferStorage(IGL.GL_RENDERBUFFER, IGL.GL_DEPTH_COMPONENT32, this.getWidth(), this.getHeight());
		/* Attach the RenderBuffer to the FrameBuffer. */
		pGLES20.glFramebufferRenderbuffer(IGL.GL_FRAMEBUFFER, IGL.GL_DEPTH_ATTACHMENT, IGL.GL_RENDERBUFFER, lGLRenderBuffer.getGLObjectId());
		/* Perform concrete rendering. */
		this.onRenderTexture(pGLES20, pGLContext);
		/* Unbind from the RenderBuffer. */
		lGLRenderBuffer.unbind(pGLES20);
		/* Unbind from the FrameBuffer. */
		lGLFrameBuffer.unbind(pGLES20);
		/* Delete the RenderBuffer. */
		pGLContext.onHandleDelegates(EEntryMode.WITHDRAW, pGLES20, lGLRenderBuffer, lGLFrameBuffer);
	}
	
	protected abstract void onRenderTexture(final IGLES20 pGLES20, final GLContext pGLContext);

	@Override
	public final int getWidth() {
		return this.mWidth;
	}

	@Override
	public final int getHeight() {
		return this.mHeight;
	}
	
	public final int getInternalFormat() {
		return this.mInternalFormat;
	}
	
	public final int getFormat() {
		return this.mFormat;
	}
	
	public final GLTexture getGLTexture() {
		return this.mGLTexture;
	}

}
