package uk.ac.manchester.sisp.ribbon.utils;

import java.util.concurrent.ThreadLocalRandom;

import uk.ac.manchester.sisp.ribbon.ui.global.UIGlobal;

public final class ColorUtils {
	
	/* Generates a random RGBA color. */
	public static final float[] onGenerateRandomColor() {
		/* Generate an RGBA array. */
		return ColorUtils.onGenerateRandomColor(UIGlobal.UI_UNITY);
	}
	
	/* Generates a random RGBA color of the specified alpha. */
	public static final float[] onGenerateRandomColor(final float pAlpha) {
		/* Generate an RGBA array. */
		return ColorUtils.onGenerateRandomColor(pAlpha, 0.0f, UIGlobal.UI_UNITY);
	}
	
	/* Generates a random RGBA color, whose R, G and B are bounded between the Minimum and Maximum. */
	public static final float[] onGenerateRandomColor(final float pAlpha, final float pMinimum, final float pMaximum) {
		/* Calculate the Delta. */
		final float lDelta = pMaximum - pMinimum;
		/* Generate an RGBA array. */
		return new float[]{ (lDelta * ThreadLocalRandom.current().nextFloat()) + pMinimum, (lDelta * ThreadLocalRandom.current().nextFloat()) + pMinimum,  (lDelta * ThreadLocalRandom.current().nextFloat()) + pMinimum, pAlpha};
	}
	
	/* Prevent instantiation of this class. */
	private ColorUtils() {}
	
}