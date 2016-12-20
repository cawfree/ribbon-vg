package uk.ac.manchester.sisp.ribbon.opengl.buffers;

import java.nio.ByteBuffer;

import uk.ac.manchester.sisp.ribbon.opengl.GLDelegate;
import uk.ac.manchester.sisp.ribbon.opengl.IGLES20;

public class GLBuffer <T extends IGLAttributeProvider> extends GLDelegate {
	
	public static interface IXY_UV_RGBA extends IGLAttributeProvider, IGLAttributeProvider.Position, IGLAttributeProvider.Procedural, IGLAttributeProvider.Color {}
	public static interface IXY_RGBA    extends IGLAttributeProvider, IGLAttributeProvider.Position, IGLAttributeProvider.Color {}
	public static interface IXY_UV      extends IGLAttributeProvider, IGLAttributeProvider.Position, IGLAttributeProvider.Procedural {}
	public static interface IXY         extends IGLAttributeProvider, IGLAttributeProvider.Position {}
	
	@SuppressWarnings("unchecked")public static class XY_UV_RGBA extends GLBuffer<GLBuffer.IXY_UV_RGBA> { public XY_UV_RGBA(final ByteBuffer pByteBuffer, final int pBufferType, final int pDrawMode) { super(pByteBuffer, pBufferType, pDrawMode, new GLAttribute[]{ GLAttribute.ATTRIBUTE_XY, GLAttribute.ATTRIBUTE_UV, GLAttribute.ATTRIBUTE_RGBA }); } }
	@SuppressWarnings("unchecked")public static class XY_RGBA    extends GLBuffer<GLBuffer.IXY_RGBA>    { public    XY_RGBA(final ByteBuffer pByteBuffer, final int pBufferType, final int pDrawMode) { super(pByteBuffer, pBufferType, pDrawMode, new GLAttribute[]{ GLAttribute.ATTRIBUTE_XY, GLAttribute.ATTRIBUTE_RGBA });                           } }
	@SuppressWarnings("unchecked")public static class XY_UV      extends GLBuffer<GLBuffer.IXY_UV>      { public      XY_UV(final ByteBuffer pByteBuffer, final int pBufferType, final int pDrawMode) { super(pByteBuffer, pBufferType, pDrawMode, new GLAttribute[]{ GLAttribute.ATTRIBUTE_XY, GLAttribute.ATTRIBUTE_UV });                             } }
	@SuppressWarnings("unchecked")public static class XY         extends GLBuffer<GLBuffer.IXY>         { public         XY(final ByteBuffer pByteBuffer, final int pBufferType, final int pDrawMode) { super(pByteBuffer, pBufferType, pDrawMode, new GLAttribute[]{ GLAttribute.ATTRIBUTE_XY });                                                       } }
	
	/* Member Variables. */
	private final ByteBuffer       mByteBuffer;
	private final int              mBufferType;
	private final int              mDrawMode;
	private final GLAttribute<T>[] mAttributes;
	private final int              mTotalCapacityBytes;
	
	protected GLBuffer(final ByteBuffer pByteBuffer, final int pBufferType, final int pDrawMode, final GLAttribute<T>[] pGLAttributes) {
		/* Initialize Member Variables. */
		this.mByteBuffer         = pByteBuffer;
		this.mBufferType         = pBufferType;
		this.mDrawMode           = pDrawMode;
		this.mAttributes         = pGLAttributes;
		this.mTotalCapacityBytes = GLBuffer.onCalculateTotalCapacityBytes(pGLAttributes);
	}
	
	public static final int onCalculateTotalCapacityBytes(final GLAttribute<?>[] pGLAttributes) {
		/* Define a counter to accumulate the total number of bytes in the GLAttributes array. */
		int lTotalCapacityBytes = 0;
		/* Iterate through each attribute. */
		for(final GLAttribute<?> lGLAttribute : pGLAttributes) {
			/* Increase the total capacity by the current attribute's capacity. */
			lTotalCapacityBytes += lGLAttribute.getNumberOfComponents() * lGLAttribute.getNumberOfBytesPerComponent();
		}
		/* Return the TotalCapacityBytes. */
		return lTotalCapacityBytes;
	}
	
	public final void onEnableAttributes(final IGLES20 pGLES20, final T pGLAttributeProvider) {
		/* Iterate through each attribute. */
		for(final GLAttribute<T> lGLAttribute : this.getAttributes()) {
			/* Enable the attribute. */
			pGLES20.glEnableVertexAttribArray(lGLAttribute.onFetchAttributeLocation(pGLAttributeProvider));
		}
	}
	
	public void onDefineAttributeStride(final IGLES20 pGLES20, final T pGLAttributeProvider) {
		/* Define a counter for the number of bytes iterated. */
		int lBytesIterated = 0;
		/* Iterate through each attribute. */
		for(final GLAttribute<T> lGLAttribute : this.getAttributes()) {
			/* Configure the current attribute stride. */
			lGLAttribute.onConfigureStride(pGLES20, pGLAttributeProvider, this.getTotalCapacityBytes(), lBytesIterated);
			/* Increase the offset. */
			lBytesIterated += lGLAttribute.getNumberOfComponents() * lGLAttribute.getNumberOfBytesPerComponent();
		}
	}
	
	public final void onDisableAttributes(final IGLES20 pGLES20, final T pGLAttributeProvider) {
		/* Iterate through each attribute. */
		for(final GLAttribute<T> lGLAttribute : this.getAttributes()) {
			/* Disable the attribute. */
			lGLAttribute.onDisableAttribute(pGLES20, pGLAttributeProvider);
		}
	}
	
	public final void bind(final IGLES20 pGLES20, final T pGLAttributeProvider) {
		/* Bind to the buffer. */
		pGLES20.glBindBuffer(this.getBufferType(), this.getGLObjectId());
		/* Enable the attributes. */
		this.onEnableAttributes(pGLES20, pGLAttributeProvider);
		/* Configure the attribute stride. */
		this.onDefineAttributeStride(pGLES20, pGLAttributeProvider);
	}
	
	public final void unbind(final IGLES20 pGLES20, final T pGLAttributeProvider) {
		/* Disable the attribute stride. */
		this.onDisableAttributes(pGLES20, pGLAttributeProvider);
		/* Unbind from the buffer. */
		pGLES20.glBindBuffer(this.getBufferType(), IGLES20.GL_NONE);
	}

	@Override
	protected final int onLoad(final IGLES20 pGLES20) {
		/* Attempt to allocate a new buffer. */
		pGLES20.glGenBuffers(1, this.getDelegationBuffer(), 0);
		/* Determine whether generation succeeded. */
		if(this.isDelegationBufferBadStatus()) {
			throw new RuntimeException("Could not allocate a new buffer!");
		}
		/* Bind to the buffer. */
		pGLES20.glBindBuffer(this.getBufferType(), this.getDelegationBuffer()[0]);
		/* Transfer data from native memory to the GPU. */
		pGLES20.glBufferData(this.getBufferType(), this.getByteBuffer().capacity(), this.getByteBuffer(), this.getDrawMode());
		/* Return the buffer's ID. */
		return this.getDelegationBuffer()[0];
	}

	@Override
	protected final void onUnload(final IGLES20 pGLES20) {
		/* Delete the buffer. */
		pGLES20.glDeleteBuffers(1, this.getDelegationBuffer(), 0);
	}

	public final ByteBuffer getByteBuffer() {
		return this.mByteBuffer;
	}
	
	public final int getBufferType() {
		return this.mBufferType;
	}
	
	public final int getDrawMode() {
		return this.mDrawMode;
	}
	
	private final GLAttribute<T>[] getAttributes() {
		return this.mAttributes;
	}
	
	public final int getTotalCapacityBytes() {
		return this.mTotalCapacityBytes;
	}
	
}