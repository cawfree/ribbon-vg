package uk.ac.manchester.sisp.ribbon.image.png;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import uk.ac.manchester.sisp.ribbon.image.png.exception.PNGException;
import uk.ac.manchester.sisp.ribbon.image.png.global.PNGGlobal;

final class PNGDataHandler implements IPNGHandler {
	
	private static final void onReconstructAverage(final byte[] pFilteredLine, final byte[] pReconstructedPreviousLine, final int pBytesPerPixel) {
		int i, j;
		for (j = 1 - pBytesPerPixel, i = 1; i < pFilteredLine.length; i++, j++) {
	       pFilteredLine[i] = (byte) (pFilteredLine[i] + ((j > 0 ? (pFilteredLine[j] & 0xFF) : 0) + (pReconstructedPreviousLine[i] & 0xFF)) / 2);
	    }
	}
	
	private static final void onReconstructPaethe(final byte[] pFilteredLine, final byte[] pReconstructedPreviousLine, final int pBytesPerPixel) {
		int i, j;
		for(i = 1, j = 1-pBytesPerPixel; i < pFilteredLine.length; i++, j++) {
			pFilteredLine[i] = (byte)(pFilteredLine[i] + PNGDataHandler.doPaethPrediction(j > 0 ? pFilteredLine[j] & 0xFF : 0, pReconstructedPreviousLine[i] & 0xFF, j > 0 ? pReconstructedPreviousLine[j] & 0xFF : 0));
		}
	}
	
	private static final void onReconstructSub(final byte[] pFilteredLine, final byte[] pReconstructedPreviousLine, final int pBytesPerPixel) {
		int i, j;
		for(i = pBytesPerPixel + 1, j = 1; i < pFilteredLine.length; i++, j++) {
			pFilteredLine[i] = (byte)(pFilteredLine[i] + pFilteredLine[j]);
		}
	}
	
	private static final void onReconstructUp(final byte[] pFilteredLine, final byte[] pReconstructedPreviousLine, final int pBytesPerPixel) {
		int i;
		for(i = 1; i < pFilteredLine.length; i++) {
			pFilteredLine[i] = (byte)(pFilteredLine[i]  + pReconstructedPreviousLine[i]);
		}
	}
	
	private static final int doPaethPrediction(final int a, final int b, final int c) {
		final int p = a + b - c;
		final int pa = p >= a ? p - a : a - p;
		final int pb = p >= b ? p - b : b - p;
		final int pc = p >= c ? p - c : c - p;
		return (pa <= pb && pa <= pc) ? a : (pb <= pc) ? b : c;
	}
	
	/* Member Variables. */
	private byte[]                mCurrentLineBuffer     = null;
	private byte[]                mLastLineBuffer        = null;
	private int                   mLineInflationTotal    = 0;
	private ByteArrayOutputStream mByteArrayOutputStream;
	private Inflater              mInflater;
	
	PNGDataHandler() {
		/* Initialize the Inflater. */
		this.mInflater = new Inflater();
		/* Create a new ByteArrayOutputStream. */
		this.mByteArrayOutputStream = new ByteArrayOutputStream();
	}

	@Override
	public final void onInitializeHandler(final PNGImage pReturnImage) throws IOException {
		/* Reset the Inflater. */
		this.getInflater().reset();
		/* Reset counters. */
		this.mLineInflationTotal = 0;
		/* Calculate the LineBuffer width depending on the color type. */
		int lLineBufferSize = (((pReturnImage.getBitDepth() - 1) / 8) + 1) * pReturnImage.getWidth();
		/* Expand the LineBufferSize for multi-element pixels. */
		switch(pReturnImage.getColorType()) {
			case PNGGlobal.PNG_IMAGE_TYPE_TRUECOLOR            : 
				/* R, G, B. */
				lLineBufferSize *= 3;
			break;
			case PNGGlobal.PNG_IMAGE_TYPE_GREYSCALE_WITH_ALPHA :
				/* G, A. */
				lLineBufferSize *= 2;
			break;
			case PNGGlobal.PNG_IMAGE_TYPE_TRUECOLOR_WITH_ALPHA : 
				/* R, G, B, A. */
				lLineBufferSize *= 4;
			break;
			default :
				throw new PNGException("ColorType not supported!");
		}
		/* Accommodate for the PNGFilterType byte. */
		lLineBufferSize++;
		/* Allocate the LineBuffers. */
		this.mCurrentLineBuffer = new byte[lLineBufferSize];
		this.mLastLineBuffer    = new byte[lLineBufferSize];
		/* Calculate the number of bytes per pixel. */
		pReturnImage.setBytesPerPixel((lLineBufferSize - 1) / pReturnImage.getWidth()); /** TODO: Come up with a better pattern than this. **/
	}
	
	@Override
	public final void onHandleChunk(final PNGImage pReturnImage, final MappedByteBuffer pMappedByteBuffer, final byte[] pInputBuffer, int pChunkLength) throws IOException {
		try {
			/* Iteratively read all bytes from the InputStream. */
			while(pChunkLength > 0) {
				/* Calculate the total number of BytesToRead. */
				final int lBytesToRead = pChunkLength  > pInputBuffer.length ? pInputBuffer.length: pChunkLength;
				/* Read the next chunk from the MappedByteBuffer into the InputBuffer. */
				pMappedByteBuffer.get(pInputBuffer, 0, lBytesToRead);
				/* Point the inflater towards the InputBuffer. */
				this.getInflater().setInput(pInputBuffer, 0, pChunkLength - (pChunkLength -= lBytesToRead));
				/* Iteratively inflate bytes. */
				while(this.getLineInflationTotal() != (this.mLineInflationTotal += this.getInflater().inflate(this.getCurrentLineBuffer(), this.getLineInflationTotal(), this.getCurrentLineBuffer().length - this.getLineInflationTotal()))) {
					/* Determine whether a complete line has been processed. */
					if(this.getLineInflationTotal() == this.getCurrentLineBuffer().length) {
						/* Reset the count. */
						this.mLineInflationTotal = 0;
						/* Reconstruct the CurrentLineBuffer using the supported PNGFilters. */
						switch(this.getCurrentLineBuffer()[0]) {
							case PNGGlobal.PNG_FILTER_TYPE_NONE    : break;
							case PNGGlobal.PNG_FILTER_TYPE_SUB     : PNGDataHandler.onReconstructSub(this.getCurrentLineBuffer(),     this.getLastLineBuffer(), pReturnImage.getBytesPerPixel()); break;
							case PNGGlobal.PNG_FILTER_TYPE_UP      : PNGDataHandler.onReconstructUp(this.getCurrentLineBuffer(),      this.getLastLineBuffer(), pReturnImage.getBytesPerPixel()); break;
							case PNGGlobal.PNG_FILTER_TYPE_AVERAGE : PNGDataHandler.onReconstructAverage(this.getCurrentLineBuffer(), this.getLastLineBuffer(), pReturnImage.getBytesPerPixel()); break;
							case PNGGlobal.PNG_FILTER_TYPE_PAETHE  : PNGDataHandler.onReconstructPaethe(this.getCurrentLineBuffer(),  this.getLastLineBuffer(), pReturnImage.getBytesPerPixel()); break;
						}
						/* Push the processed line through the ByteArrayOutputStream. */
						this.getByteArrayOutputStream().write(this.getCurrentLineBuffer(), 1, this.getCurrentLineBuffer().length - 1);
						/* Copy the processed CurrentLineBuffer to the LastLineBuffer. */
						System.arraycopy(this.getCurrentLineBuffer(), 0, this.getLastLineBuffer(), 0, this.getCurrentLineBuffer().length);
					}
				}
			}
		}
		catch(final DataFormatException pDataFormatException) {
			/* Re-interpret the Inflater exception as a hierarchy-supported PNGException. */
			throw new PNGException(pDataFormatException.getMessage());
		}
	}

	@Override
	public final void onFinalizeChunkHandler(final PNGImage pReturnImage) throws IOException {
		/* Use the contents of the ByteArrayOutputStream as the completed PNGImage data. */
		pReturnImage.setImageData(this.getByteArrayOutputStream().toByteArray());
		/* Flush the ByteArrayOutputStream. */
		this.getByteArrayOutputStream().reset();
		/* Nullify the temporarily assigned LineBuffers. */
		this.mCurrentLineBuffer = null;
		this.mLastLineBuffer    = null;
	}

	@Override
	public final void dispose() {
		try {
			this.getByteArrayOutputStream().close();
		}
		catch(final IOException pIOException) {
			pIOException.printStackTrace();
		}
		finally {
			this.getInflater().end();
			this.mInflater          = null;
			this.mCurrentLineBuffer = null;
			this.mLastLineBuffer    = null;
		}
	}
	
	private final Inflater getInflater() {
		return this.mInflater;
	}
	
	private final ByteArrayOutputStream getByteArrayOutputStream() {
		return this.mByteArrayOutputStream;
	}
	
	private final byte[] getCurrentLineBuffer() {
		return this.mCurrentLineBuffer;
	}
	
	private final byte[] getLastLineBuffer() {
		return this.mLastLineBuffer;
	}
	
	private final int getLineInflationTotal() {
		return this.mLineInflationTotal;
	}

}