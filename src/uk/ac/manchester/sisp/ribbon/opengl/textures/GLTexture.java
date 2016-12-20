package uk.ac.manchester.sisp.ribbon.opengl.textures;

import java.nio.Buffer;

import uk.ac.manchester.sisp.ribbon.common.IDim2;
import uk.ac.manchester.sisp.ribbon.image.IImage;
import uk.ac.manchester.sisp.ribbon.image.IRasterImage;
import uk.ac.manchester.sisp.ribbon.opengl.GLDelegate;
import uk.ac.manchester.sisp.ribbon.opengl.IGL;
import uk.ac.manchester.sisp.ribbon.opengl.IGLBindable;
import uk.ac.manchester.sisp.ribbon.opengl.IGLES20;
import uk.ac.manchester.sisp.ribbon.opengl.IGLInternalFormat;
import uk.ac.manchester.sisp.ribbon.utils.DataUtils;

public class GLTexture extends GLDelegate implements IGLBindable, IImage, IDim2.I, IGLInternalFormat { /** TODO: Abstract **/

	/* Member Variables. */
	private final int    mTextureTarget;
	private final int    mInternalFormat;
	private final int    mFormat;
	private final int    mDataType;
	private final int    mWidth;
	private final int    mHeight;
	private final Buffer mImageData;
	
	public GLTexture(final int pInternalFormat, final int pFormat, final int pDataType, final int pWidth, final int pHeight) {
		this(IGL.GL_TEXTURE_2D, pInternalFormat, pFormat, pDataType, pWidth, pHeight, null);
	}
	
	public GLTexture(final int pInternalFormat, final int pFormat, final IRasterImage pRasterImage) {
		this(IGL.GL_TEXTURE_2D, pInternalFormat, pFormat, IGL.GL_UNSIGNED_BYTE, pRasterImage.getWidth(), pRasterImage.getHeight(), DataUtils.delegateNative(pRasterImage.getImageData()));
	}
	
	public GLTexture(final int pTextureTarget, final int pInternalFormat, final int pFormat, final int pDataType, final int pWidth, final int pHeight, final Buffer pImageData) {
		super();
		/* Initialize Member Variables. */
		this.mTextureTarget  = pTextureTarget;
		this.mInternalFormat = pInternalFormat;
		this.mFormat         = pFormat;
		this.mDataType       = pDataType;
		this.mWidth          = pWidth;
		this.mHeight         = pHeight;
		this.mImageData      = pImageData;
	}

	@Override
	protected final int onLoad(final IGLES20 pGLES20) {
		/* Request generation of a Texture object. */
		pGLES20.glGenTextures(this.getDelegationBuffer().length, this.getDelegationBuffer(), 0);
		/* Determine whether creation succeeded. */
		if(this.getDelegationBuffer()[0] == IGLES20.GL_NONE) {
			throw new RuntimeException("FAILED to generate a new GLTexture2D!");
		}
		/* Bind to the Texture. */
		pGLES20.glBindTexture(this.getTextureTarget(), this.getDelegationBuffer()[0]);
		/* Load the Texture onto the GPU. (Will throw an GL_INVALID_OPERATION if the wrong data type has been specified. (i.e. the format and data widths should match for the provided data.)) */
		pGLES20.glTexImage2D(this.getTextureTarget(), 0, this.getInternalFormat(), this.getWidth(), this.getHeight(), 0, this.getFormat(), this.getDataType(), this.getImageData());
		/* Configure Texture wrapping. */
		pGLES20.glTexParameteri(this.getTextureTarget(), IGLES20.GL_TEXTURE_WRAP_S, IGLES20.GL_CLAMP_TO_EDGE);
		pGLES20.glTexParameteri(this.getTextureTarget(), IGLES20.GL_TEXTURE_WRAP_T, IGLES20.GL_CLAMP_TO_EDGE);
		/* Configure Texture filtering. */
		pGLES20.glTexParameteri(this.getTextureTarget(), IGLES20.GL_TEXTURE_MIN_FILTER, IGLES20.GL_LINEAR);
		pGLES20.glTexParameteri(this.getTextureTarget(), IGLES20.GL_TEXTURE_MAG_FILTER, IGLES20.GL_LINEAR);
		/* Generate the corresponding mipmapping for the specified filter parameters. */
		pGLES20.glGenerateMipmap(this.getTextureTarget());
		/* Unbind from the Texture. */
		pGLES20.glBindTexture(this.getTextureTarget(), IGLES20.GL_NONE);
		/* Return the Texture ID. */
		return this.getDelegationBuffer()[0];
	}
	
	@Override
	public final void bind(final IGLES20 pGLES20) {
		/* Bind to the Texture. */
		pGLES20.glBindTexture(this.getTextureTarget(), this.getDelegationBuffer()[0]);
	}

	@Override
	public final void unbind(final IGLES20 pGLES20) {
		/* Unbind from the texture. */
		pGLES20.glBindTexture(this.getTextureTarget(), IGLES20.GL_NONE);
	}

	@Override
	protected final void onUnload(final IGLES20 pGLES20) {
		/* Delete the texture. */
		pGLES20.glDeleteTextures(this.getDelegationBuffer().length, this.getDelegationBuffer(), 0);
	}
	
	public final int getTextureTarget() {
		return this.mTextureTarget;
	}
	
	@Override
	public final int getInternalFormat() {
		return this.mInternalFormat;
	}
	
	public final int getFormat() {
		return this.mFormat;
	}
	
	public final int getDataType() {
		return this.mDataType;
	}

	@Override
	public final int getWidth() {
		return this.mWidth;
	}

	@Override
	public final int getHeight() {
		return this.mHeight;
	}
	
	private final Buffer getImageData() {
		return this.mImageData;
	}
	
}
