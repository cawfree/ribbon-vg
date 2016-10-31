package uk.ac.manchester.sisp.ribbon.ui.easing;

import uk.ac.manchester.sisp.ribbon.common.IDim2;
import uk.ac.manchester.sisp.ribbon.common.ILifeTime;
import uk.ac.manchester.sisp.ribbon.common.IVec2;
import uk.ac.manchester.sisp.ribbon.event.IEvent;
import uk.ac.manchester.sisp.ribbon.event.filter.IEventsFilter;
import uk.ac.manchester.sisp.ribbon.ui.time.UITimeDispatcher;

/** TODO: Get around to modifying the constructors to work using just EasingConfigurations. **/
public class UIReactiveEasing implements IEventsFilter<IEvent, UITimeDispatcher>, ILifeTime {
	
	/* Static Declarations. */
	public static final int INDEX_PARAMETER_X      = 0;
	public static final int INDEX_PARAMETER_Y      = 1;
	public static final int INDEX_PARAMETER_WIDTH  = 2;
	public static final int INDEX_PARAMETER_HEIGHT = 3;
	
	/* Member Variables. */
	private final IEasingConfiguration mEasingConfiguration;
	private       float                mStartTimeSeconds;
	
	/* Interpolation Buffers. */
	private final float[] mInitials;
	private final float[] mTerminals;
	private final float[] mResultsBuffer;
	
	/* Position Interpolation. */
	public <T extends IVec2> UIReactiveEasing(final EEasingAlgorithm pEasingAlgorithm, final float pStartTimeSeconds, final float pDurationSeconds, final T pT) {
		this(pEasingAlgorithm, pStartTimeSeconds, pDurationSeconds, new float[]{ pT.getX(), pT.getY() }, new float[]{ pT.getX(), pT.getY() });
	}
	
	/* Position and Dimension Interpolation. */
	public <T extends IVec2 & IDim2> UIReactiveEasing(final EEasingAlgorithm pEasingAlgorithm, final float pStartTimeSeconds, final float pDurationSeconds, final T pT, final float pX, final float pY, final float pWidth, final float pHeight) {
		this(pEasingAlgorithm, pStartTimeSeconds, pDurationSeconds, new float[]{ pT.getX(), pT.getY(), pT.getWidth(), pT.getHeight() }, new float[]{ pX, pY, pWidth, pHeight });
	}
	
	/* Arbitrary Interpolation. */
	public UIReactiveEasing(final EEasingAlgorithm pEasingAlgorithm, final float pStartTimeSeconds, final float pDurationSeconds, final float[] pInitials, final float[] pTerminals) {
		/* Initialize Member Variables. */
		this.mEasingConfiguration = new IEasingConfiguration(){
			@Override public final EEasingAlgorithm getDistributionAlgorithm() { return pEasingAlgorithm; }
			@Override public final float getDistributionDuration()             { return pDurationSeconds; }
		};
		this.mStartTimeSeconds = pStartTimeSeconds;
		this.mInitials         = pInitials;
		this.mTerminals        = pTerminals;
		this.mResultsBuffer    = this.getInitials().clone();
	}
	
	/* Arbitrary Interpolation. */
	public UIReactiveEasing(final IEasingConfiguration pEasingConfiguration, final float pStartTimeSeconds, final float[] pInitials, final float[] pTerminals) {
		/* Initialize Member Variables. */
		this.mEasingConfiguration = pEasingConfiguration;
		this.mStartTimeSeconds    = pStartTimeSeconds;
		this.mInitials            = pInitials;
		this.mTerminals           = pTerminals;
		this.mResultsBuffer       = this.getInitials().clone();
	}

	@Override
	public boolean onHandleEvent(final IEvent pEvent, final UITimeDispatcher pEventsDispatcher) {
		/* Iteratively populate the ResultsBuffer. */
		for(int i = 0; i < this.getResultsBuffer().length; i++) {
			/* Calculate the EasingAlgorithm progression for this index. */
			this.getResultsBuffer()[i] = EEasingAlgorithm.onCalculateBoundedEasing(this.getEasingConfiguration(), this.getStartTimeSeconds(), pEvent.getEventTimeSeconds(), this.getInitials()[i], this.getTerminals()[i]);
		}
		/* Define whether the event was successful if we're still mid-easing. */
		return this.isAlive(pEvent.getEventTimeSeconds());
	}

	@Override
	public final boolean isAlive(final float pCurrentTimeSeconds) {
		return this.isMidEasing(pCurrentTimeSeconds);
	}
	
	/* Ignore disposals. */
	@Override public void dispose() {}
	
	public final void onUpdateBounds(final float pCurrentTimeSeconds, final float[] pTerminals) {
		/* Update the Initials to reflect upon the current progression of the EasingAlgorithm. */
		for(int i = 0; i < pTerminals.length; i++) {
			/* Update the progression for this index. */
			this.getInitials()[i]  = EEasingAlgorithm.onCalculateBoundedEasing(this.getEasingConfiguration(), this.getStartTimeSeconds(), pCurrentTimeSeconds, this.getInitials()[i], this.getTerminals()[i]);
			/* Update the corresponding Terminal. */
			this.getTerminals()[i] = pTerminals[i];
		}
		/* Update the StartTimeSeconds. */
		this.mStartTimeSeconds     = pCurrentTimeSeconds;
	}
	
	public final boolean isMidEasing(final float pCurrentTimeSeconds) {
		return (pCurrentTimeSeconds < (this.getStartTimeSeconds() + this.getEasingConfiguration().getDistributionDuration()));
	}
	
	public final IEasingConfiguration getEasingConfiguration() {
		return this.mEasingConfiguration;
	}
	
	public final float getStartTimeSeconds() {
		return this.mStartTimeSeconds;
	}
	
	public final float[] getInitials() {
		return this.mInitials;
	}
	
	public final float[] getTerminals() {
		return this.mTerminals;
	}
	
	public final float[] getResultsBuffer() {
		return this.mResultsBuffer;
	}
	
}