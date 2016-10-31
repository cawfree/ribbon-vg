package uk.ac.manchester.sisp.ribbon.image.png;

import java.io.IOException;
import java.nio.MappedByteBuffer;

import uk.ac.manchester.sisp.ribbon.common.IDisposable;

interface IPNGHandler extends IDisposable {
	
	public abstract void onInitializeHandler(final PNGImage pReturnImage) throws IOException;
	public abstract void onHandleChunk(final PNGImage pReturnImage, final MappedByteBuffer pMappedByteBuffer, final byte[] pInputBuffer, int pChunkLength) throws IOException;
	public abstract void onFinalizeChunkHandler(final PNGImage pReturnImage) throws IOException;
	
}