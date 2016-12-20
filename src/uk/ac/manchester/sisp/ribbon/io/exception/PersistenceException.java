package uk.ac.manchester.sisp.ribbon.io.exception;

import java.io.IOException;

@SuppressWarnings("serial")
public final class PersistenceException extends IOException {
	
	public PersistenceException(final String pMessage) {
		super(pMessage);
	}
	
}
