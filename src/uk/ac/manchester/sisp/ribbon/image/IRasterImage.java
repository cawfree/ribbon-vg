package uk.ac.manchester.sisp.ribbon.image;

public interface IRasterImage extends IImage {
	
	public abstract byte[] getImageData();
	public abstract int    getBitDepth();
	public abstract int    getBytesPerPixel();

}
