package uk.ac.manchester.sisp.ribbon.ui.easing;

import uk.ac.manchester.sisp.ribbon.common.ILifeTime;
import uk.ac.manchester.sisp.ribbon.common.ITimedObject;
import uk.ac.manchester.sisp.ribbon.event.IEvent;
import uk.ac.manchester.sisp.ribbon.event.filter.IEventsFilter;
import uk.ac.manchester.sisp.ribbon.ui.easing.global.EEasingAlgorithm;
import uk.ac.manchester.sisp.ribbon.ui.easing.global.EasingGlobal;
import uk.ac.manchester.sisp.ribbon.ui.time.UITimeDispatcher;

public interface IReactiveEasing extends IEventsFilter<IEvent, UITimeDispatcher>, ILifeTime, ITimedObject.W {
	
	/* Static Declarations. */
	public static class Impl implements IReactiveEasing {
		
		/* Member Variables. */
		private final IEasingConfiguration mEasingConfiguration;
		private       float                mObjectTimeSeconds;
		
		/* Interpolation Buffers. */
		private float[] mInitials;
		private float[] mTerminals;
		private float[] mResultsBuffer;
		
		public Impl(final IEasingConfiguration pEasingConfiguration, final float pObjectTimeSeconds, final float[] pInitials, final float[] pTerminals) {
			/* Initialize Member Variables. */
			this.mEasingConfiguration = pEasingConfiguration;
			this.mObjectTimeSeconds   = pObjectTimeSeconds;
			/* Initialize Interpolation Buffers. */
			this.mInitials            = pInitials;
			this.mTerminals           = pTerminals;
			this.mResultsBuffer       = this.getInitials().clone();
		}

		@Override
		public boolean onHandleEvent(final IEvent pEvent, final UITimeDispatcher pEventsDispatcher) {
			/* Iterate through each Initial. */
			for(int i = 0; i < this.getInitials().length; i++) {
				/* Calculate the EasingResult for this index. */
				final float lEasingResult  = EEasingAlgorithm.onCalculateBoundedEasing(this.getEasingConfiguration(), this.getObjectTimeSeconds(), pEvent.getObjectTimeSeconds(), this.getInitials()[i], this.getTerminals()[i]);
				/* Assign the ResultsBuffer at this position. */
				this.getResultsBuffer()[i] = lEasingResult;
			}
			/* Define whether the event was successful if we're still mid-easing. */
			return this.isAlive(pEvent.getObjectTimeSeconds());
		}

		@Override public void dispose() {
			/* Dispose of the InterpolationBuffers. */
			this.mInitials      = null;
			this.mTerminals     = null;
			this.mResultsBuffer = null;
		}

		@Override public boolean isAlive(final float pCurrentTimeSeconds) {
			/* Define whether we're still mid easing. */
			return EasingGlobal.isMidEasing(pCurrentTimeSeconds, this);
		}

		@Override
		public void setObjectTimeSeconds(final float pObjectTimeSeconds) {
			this.mObjectTimeSeconds = pObjectTimeSeconds;
		}

		@Override
		public float getObjectTimeSeconds() {
			return this.mObjectTimeSeconds;
		}

		@Override public final IEasingConfiguration getEasingConfiguration() {
			return this.mEasingConfiguration;
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
	
	/* Returns the EasingConfiguration associated with this ReactiveEasing process. */
	public abstract IEasingConfiguration getEasingConfiguration();
	
}
