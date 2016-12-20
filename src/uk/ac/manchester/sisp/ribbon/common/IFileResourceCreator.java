package uk.ac.manchester.sisp.ribbon.common;

import java.io.File;
import java.io.IOException;

public interface IFileResourceCreator <T> extends IDisposable {
	
	public abstract T createFromFile(final File pFile) throws IOException;

}
