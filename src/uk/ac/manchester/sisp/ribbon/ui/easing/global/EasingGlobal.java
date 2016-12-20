package uk.ac.manchester.sisp.ribbon.ui.easing.global;

import uk.ac.manchester.sisp.ribbon.ui.easing.IReactiveEasing;

public final class EasingGlobal {
	
	/* Determines whether an EasingAlgorithm has not finished it's interpolation. */
	public static final boolean isMidEasing(final float pCurrentTimeSeconds, final IReactiveEasing pReactiveEasing) {
		/* Defines whether the IReactiveEasing's distribution duration has been exceeded.  */
		return (pCurrentTimeSeconds < (pReactiveEasing.getObjectTimeSeconds() + pReactiveEasing.getEasingConfiguration().getDurationSeconds()));
	}
	
	/* Prevent instantiation of this class. */
	private EasingGlobal() { };
	
}