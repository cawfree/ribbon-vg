package uk.ac.manchester.sisp.ribbon.image.svg.exception;

import java.io.IOException;

@SuppressWarnings("serial")
public final class SVGException extends IOException {
	
	public SVGException(final String pMessage) {
		super(pMessage);
	}

}
