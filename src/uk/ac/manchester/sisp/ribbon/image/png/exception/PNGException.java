package uk.ac.manchester.sisp.ribbon.image.png.exception;

import java.io.IOException;

@SuppressWarnings("serial")
public class PNGException extends IOException {
	
	public PNGException(final String pMessage) {
		super(pMessage);
	}

}
