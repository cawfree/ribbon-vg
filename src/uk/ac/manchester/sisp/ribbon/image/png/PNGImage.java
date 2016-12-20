package uk.ac.manchester.sisp.ribbon.image.png;

import uk.ac.manchester.sisp.ribbon.image.IRasterImage;

public final class PNGImage implements IRasterImage {
	
	private final int    mWidth;
	private final int    mHeight;
	private final int    mBitDepth;
	private final byte   mColorType;
	private final byte   mCompressionMethod;
	private final byte   mFilterMethod;
	private final byte   mInterlaceMethod;
	private       byte[] mImageData;
	private       int    mBytesPerPixel;
	
	
	protected PNGImage(final int pWidth, final int pHeight, final int pBitDepth, final byte pColorType, final byte pCompressionMethod, final byte pFilterMethod, final byte pInterlaceMethod) {
		this.mWidth             = pWidth;
		this.mHeight            = pHeight;
		this.mBitDepth          = pBitDepth;
		this.mColorType         = pColorType;
		this.mCompressionMethod = pCompressionMethod;
		this.mFilterMethod      = pFilterMethod;
		this.mInterlaceMethod   = pInterlaceMethod;
	}

	@Override
	public final int getWidth() {
		return this.mWidth;
	}

	@Override
	public final int getHeight() {
		return this.mHeight;
	}
	
	@Override
	public final int getBitDepth() {
		return this.mBitDepth;
	}
	
	public final byte getColorType() {
		return this.mColorType;
	}
	
	public final byte getCompressionMethod() {
		return this.mCompressionMethod;
	}
	
	public final byte getFilterMethod() {
		return this.mFilterMethod;
	}
	
	public final byte getInterlaceMethod() {
		return this.mInterlaceMethod;
	}
	
	protected final void setImageData(final byte[] pImageData) {
		this.mImageData = pImageData;
	}

	@Override
	public final byte[] getImageData() {
		return this.mImageData;
	}
	
	protected final void setBytesPerPixel(final int pBytesPerPixel) {
		this.mBytesPerPixel = pBytesPerPixel;
	}
	
	public final int getBytesPerPixel() {
		return this.mBytesPerPixel;
	}

	@Override
	public final void dispose() {
		this.mImageData = null;
	}

}
