package uk.ac.manchester.sisp.ribbon.utils;

import java.util.concurrent.ThreadLocalRandom;

public final class ColorUtils {
	
	/* Generates a random RGBA color. */
	public static final float[] onGenerateRandomColor(final float pAlpha) {
		return new float[]{ ThreadLocalRandom.current().nextFloat(), ThreadLocalRandom.current().nextFloat(), ThreadLocalRandom.current().nextFloat(), pAlpha};
	}
	
	/* Prevent instantiation of this class. */
	private ColorUtils() {}
	
}