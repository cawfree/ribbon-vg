package uk.ac.manchester.sisp.ribbon.image;

import uk.ac.manchester.sisp.ribbon.common.IDisposable;

public interface IImage extends IDisposable {
	
	public abstract int getWidth();
	public abstract int getHeight();
	
}
