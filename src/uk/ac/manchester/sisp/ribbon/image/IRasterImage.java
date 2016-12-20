package uk.ac.manchester.sisp.ribbon.image;

import uk.ac.manchester.sisp.ribbon.common.IDim2;

public interface IRasterImage extends IImage, IDim2.I {
	
	public abstract byte[] getImageData();
	public abstract int    getBitDepth();
	public abstract int    getBytesPerPixel();

}
