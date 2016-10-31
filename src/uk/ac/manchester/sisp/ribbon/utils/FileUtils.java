package uk.ac.manchester.sisp.ribbon.utils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public final class FileUtils {

	public static final String RANDOM_ACCESS_FILE_MODE_READ                 = "r";
	public static final String RANDOM_ACCESS_FILE_MODE_READ_WRITE           = "rw";
	public static final String RANDOM_ACCESS_FILE_MODE_READ_WRITE_SYNC      = "rwd";
	public static final String RANDOM_ACCESS_FILE_MODE_READ_WRITE_SYNC_META = "rws";
	
	public static final String getExtension(final File pFile) {
		return pFile.getAbsolutePath().substring(pFile.getAbsolutePath().lastIndexOf(".") + 1);
	}
	
	public static final boolean onStringCompare(final String pString, final RandomAccessFile pRandomAccessFile) throws IOException {
		/* Declare the return boolean. */
		boolean lIsMatching      = true;
		/* Acquire a reference to the String's backing character array. */
		final char[] lCharacters = pString.toCharArray();
		/* Iterate through the String's characters. */
		for(int i = 0; i < lCharacters.length && lIsMatching; i++) {
			/* Update the variable. */
			lIsMatching &= lCharacters[i] == pRandomAccessFile.readByte();
		}
		/* Return the matching boolean. */
		return lIsMatching;
	}
	
	private FileUtils() {}
	
}
