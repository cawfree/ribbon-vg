package uk.ac.manchester.sisp.ribbon.ui.easing;

import uk.ac.manchester.sisp.ribbon.ui.easing.global.EEasingAlgorithm;

/** TODO: Make a class, make EasingGroup extend. **/
/** TODO: this interface is stupid. **/
public interface IEasingConfiguration {
	
	/* Static Easing Definitions. */
	public static final IEasingConfiguration CONFGIRUATION_RESPONSE = new IEasingConfiguration.Impl(EEasingAlgorithm.CUBIC_EASE_OUT,       0.20f);
	public static final IEasingConfiguration CONFIGURATION_DRAG     = new IEasingConfiguration.Impl(EEasingAlgorithm.EXPONENTIAL_EASE_OUT, 0.30f);
	public static final IEasingConfiguration CONFIGURATION_NONE     = new IEasingConfiguration.Impl(EEasingAlgorithm.NONE,                 0.00f);
	public static final IEasingConfiguration CONFIGURATION_DROP     = new IEasingConfiguration.Impl(EEasingAlgorithm.BOUNCE_EASE_OUT,      0.50f);
	public static final IEasingConfiguration CONFIGURATION_SMOOTH   = new IEasingConfiguration.Impl(EEasingAlgorithm.SINE_EASE_OUT,        0.18f);
	public static final IEasingConfiguration CONFIGURATION_APP      = new IEasingConfiguration.Impl(EEasingAlgorithm.SINE_EASE_OUT,        0.10f);
	
	/* Static Class Definitions. */
	public static class Impl implements IEasingConfiguration {
		
		/* Member Variables. */
		private EEasingAlgorithm mEasingAlgorithm;
		private float            mDurationSeconds;
		
		public Impl(final EEasingAlgorithm pEasingAlgorithm, final float pDurationSeconds) {
			/* Initialize Member Variables. */
			this.mEasingAlgorithm      = pEasingAlgorithm;
			this.mDurationSeconds = pDurationSeconds;
		}
		
		@Override public EEasingAlgorithm getEasingAlgorithm() { return this.mEasingAlgorithm; }
		@Override public float            getDurationSeconds() { return this.mDurationSeconds; }
		
	}
	
	public abstract EEasingAlgorithm getEasingAlgorithm();
	public abstract float            getDurationSeconds();
	
}