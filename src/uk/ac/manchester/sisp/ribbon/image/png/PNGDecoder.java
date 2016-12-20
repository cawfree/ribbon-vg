package uk.ac.manchester.sisp.ribbon.image.png;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;

import uk.ac.manchester.sisp.ribbon.common.IDisposable;
import uk.ac.manchester.sisp.ribbon.image.png.global.PNGGlobal;
import uk.ac.manchester.sisp.ribbon.utils.DataUtils;
import uk.ac.manchester.sisp.ribbon.utils.FileUtils;

public final class PNGDecoder implements IDisposable {
	
	private static final int BUFFER_INPUT_SIZE = 512;
	
	/* Member Variables. */
	private IPNGHandler mDataChunkHandler;
	private byte[]      mInputBuffer; /** TODO: Abstract this to the caller. **/

	public PNGDecoder() {
		/* Instantiate Persistent Member Variables. */
		this.mDataChunkHandler     = new PNGDataHandler();
		/* Initialize the InputBuffer. */
		this.mInputBuffer = new byte[PNGDecoder.BUFFER_INPUT_SIZE];
	}
	
	public final PNGImage createFromFile(final File pFile) throws IOException {
		/* Create a RandomAccessFile to parse the PNG image data. */
		final RandomAccessFile lRandomAccessFile = new RandomAccessFile(pFile, FileUtils.RANDOM_ACCESS_FILE_MODE_READ);
		/* Fetch a reference to the RandomAccessFile's total length. */
		final long lFileLength = lRandomAccessFile.length();
		/* Use a MappedByteBuffer to cache the file. */
		final MappedByteBuffer lMappedByteBuffer = lRandomAccessFile.getChannel().map(MapMode.READ_ONLY, 0, lFileLength);
		/* Close the RandomAccessFile. (The Mapping will remain valid until garbage collected.) */
		lRandomAccessFile.close();
		/* Create a reference to the PNGImage to return. */
		PNGImage lReturnImage = null;
		/* Determine whether the File is a PNG. Load the bytes into the allocated buffer. */
		if(lMappedByteBuffer.getLong() == PNGGlobal.PNG_FILE_SIGNATURE) {
			/* Read the chunk length. */
			lMappedByteBuffer.getInt();
			/* Read the chunk type. */
			lMappedByteBuffer.getInt();
			/* Initialize the PNGImage. */
			lReturnImage = new PNGImage(
				lMappedByteBuffer.getInt(),
				lMappedByteBuffer.getInt(),
				(lMappedByteBuffer.get() & 0xFF),
				lMappedByteBuffer.get(),
				lMappedByteBuffer.get(),
				lMappedByteBuffer.get(), 
				lMappedByteBuffer.get()
			);
			/* Discard the checksum. */
			lMappedByteBuffer.getInt();
			/* With the ReturnImage initialized, configure the ChunkHandlers. */
			this.getPNGChunkDataHandler().onInitializeHandler(lReturnImage);
			/* While data is available to sample... */
			while(lMappedByteBuffer.position() < lFileLength) {
				/* Read the chunk length. */
				final int lChunkLength = lMappedByteBuffer.getInt();
				/* Read the chunk type */
				final int lChunkType   = lMappedByteBuffer.getInt();
				/* Perform processing on supported chunk types. */
				switch(lChunkType) {
					case PNGGlobal.PNG_CHUNK_IDAT : 
						/* Process the chunk. */
						this.getPNGChunkDataHandler().onHandleChunk(lReturnImage, lMappedByteBuffer, this.getInputBuffer(), lChunkLength);
						/* Skip the checksum. */
						lMappedByteBuffer.getInt();
					break;
					default :
						/* Skip the unsupported chunk and corresponding checksum. */
						lMappedByteBuffer.position(lMappedByteBuffer.position() + lChunkLength + DataUtils.BYTES_PER_INT);
					break;
				}
			}
		}
		/* Finalise the supported chunk handlers. */
		this.getPNGChunkDataHandler().onFinalizeChunkHandler(lReturnImage);
		/* Return the created image. */
		return lReturnImage;			
	}

	@Override
	public final void dispose() {
		this.getPNGChunkDataHandler().dispose();
		this.mDataChunkHandler  = null;
		this.mInputBuffer       = null;
	}
	
	private final IPNGHandler getPNGChunkDataHandler() {
		return this.mDataChunkHandler;
	}
	
	private final byte[] getInputBuffer() {
		return this.mInputBuffer;
	}
	
}