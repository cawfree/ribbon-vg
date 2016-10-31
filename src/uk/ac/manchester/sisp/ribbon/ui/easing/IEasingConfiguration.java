package uk.ac.manchester.sisp.ribbon.ui.easing;

/** TODO: Make a class, make UIReactiveEasing extend. **/
public interface IEasingConfiguration {
	
	/* Static Easing Definitions. */
	public static final float EASING_AVOID_NEXT_TOUCH = 0.35f; 
	
	public static final IEasingConfiguration CONFGIRUATION_DEFAULT = new IEasingConfiguration() {
		@Override public final EEasingAlgorithm getDistributionAlgorithm() { return EEasingAlgorithm.EXPONENTIAL_EASE_OUT;        }
		@Override public final float            getDistributionDuration()  { return IEasingConfiguration.EASING_AVOID_NEXT_TOUCH; } 
	};
	
	public abstract EEasingAlgorithm getDistributionAlgorithm();
	public abstract float            getDistributionDuration();
	
}