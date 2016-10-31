package uk.ac.manchester.sisp.ribbon.opengl.buffers;

import uk.ac.manchester.sisp.ribbon.opengl.IGLES20;
import uk.ac.manchester.sisp.ribbon.utils.DataUtils;

public abstract class GLAttribute<T extends IGLAttributeProvider> {
	
	/* Attribute Constants. */
	public static final GLAttribute<IGLAttributeProvider.Position>   ATTRIBUTE_XY   = new GLAttribute<IGLAttributeProvider.Position>  (2, IGLES20.GL_FLOAT, (DataUtils.BYTES_PER_FLOAT)) { @Override protected final int onFetchAttributeLocation(final IGLAttributeProvider.Position pPositionProvider)     { return pPositionProvider.getAttributePosition();     } };
	public static final GLAttribute<IGLAttributeProvider.Procedural> ATTRIBUTE_UV   = new GLAttribute<IGLAttributeProvider.Procedural>(2, IGLES20.GL_FLOAT, (DataUtils.BYTES_PER_FLOAT)) { @Override protected final int onFetchAttributeLocation(final IGLAttributeProvider.Procedural pProceduralProvider) { return pProceduralProvider.getAttributeProcedural(); } };
	public static final GLAttribute<IGLAttributeProvider.Color>      ATTRIBUTE_RGBA = new GLAttribute<IGLAttributeProvider.Color>     (4, IGLES20.GL_FLOAT, (DataUtils.BYTES_PER_FLOAT)) { @Override protected final int onFetchAttributeLocation(final IGLAttributeProvider.Color pColorProvider)           { return pColorProvider.getAttributeColor();           } };
	
	/* Member Variables. */
	private final int mNumberOfComponents;
	private final int mDataType;
	private final int mNumberOfBytes;
	
	/* Prevent external instantiation of this controlled type. */
	private GLAttribute(final int pNumberOfComponents, final int pDataType, final int pNumberOfBytes) { /** TODO: Abstract number of bytes to a method. **/
		/* Initialize Member Variables. */
		this.mNumberOfComponents = pNumberOfComponents;
		this.mDataType           = pDataType;
		this.mNumberOfBytes      = pNumberOfBytes;
	}
	
	public final int getNumberOfComponents() {
		return this.mNumberOfComponents;
	}
	
	public final int getDataType() {
		return this.mDataType;
	}
	
	public final int getNumberOfBytesPerComponent() {
		return this.mNumberOfBytes;
	}
	
	protected final void onEnableAttribute(final IGLES20 pGLES20, final T pT) {
		pGLES20.glEnableVertexAttribArray(this.onFetchAttributeLocation(pT));
	}
	
	protected final void onConfigureStride(final IGLES20 pGLES20, final T pT, final int pTotalCapacity, final int pByteOffset) {
		pGLES20.glVertexAttribPointer(this.onFetchAttributeLocation(pT), this.getNumberOfComponents(), this.getDataType(), false, pTotalCapacity, pByteOffset);
	}
	
	protected final void onDisableAttribute(final IGLES20 pGLES20, final T pT) {
		pGLES20.glDisableVertexAttribArray(this.onFetchAttributeLocation(pT));
	}
	
	protected abstract int onFetchAttributeLocation(final T pT);

}
