package uk.ac.manchester.sisp.ribbon.utils;

import java.io.File;

public final class ResourceUtils {
	
	/* JVM Properties. */
	public static final String JVM_PROPERTY_OS_NAME = "os.name";
	
	/** TODO: Unsure regarding whether this will function when compiled to jar... **/
	public static final File getResource(final ClassLoader pClassLoader, final String pPath) {
		return new File(pClassLoader.getResource(pPath).getPath());
	}
	
	public static final float getSystemTimeSeconds() {
		return (((float)System.nanoTime()) * MathUtils.SCALAR_1_E_MINUS_9);
	}
	
	/* Prevent direct instantiation of this class. */
	private ResourceUtils() {}

}