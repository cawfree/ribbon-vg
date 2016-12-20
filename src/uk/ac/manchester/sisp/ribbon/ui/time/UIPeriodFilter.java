package uk.ac.manchester.sisp.ribbon.ui.time;

import uk.ac.manchester.sisp.ribbon.common.ILifeTime;
import uk.ac.manchester.sisp.ribbon.event.IEvent;
import uk.ac.manchester.sisp.ribbon.event.filter.IEventsFilter;

public abstract class UIPeriodFilter implements IEventsFilter<IEvent, UITimeDispatcher>, ILifeTime {
	
	/* Member Variables. */
	private       float mLastUpdateSeconds; /** TODO: These are used a ton. Should be an interface. **/
	private final float mPeriodSeconds;
	
	public UIPeriodFilter(final float pLastUpdateSeconds, final float pPeriodSeconds) {
		/* Initialize Member Variables. */
		this.mLastUpdateSeconds = pLastUpdateSeconds;
		this.mPeriodSeconds     = pPeriodSeconds;
	}

	@Override
	public final boolean onHandleEvent(final IEvent pEvent, final UITimeDispatcher pEventsDispatcher) {
		/* Calculate the DeltaT. */
		final float lDeltaT = (pEvent.getObjectTimeSeconds() - this.getLastUpdateSeconds()) - this.getPeriodSeconds();
		/* Determine if the period has elapsed. */
		if(lDeltaT >= 0) {
			/* Allow the concrete implementation to handle the period. */
			this.onPeriodEvent(pEvent.getObjectTimeSeconds());
			/* Update the LastUpdateSeconds, compensating for any losses. */ /** TODO: Running average? **/
			this.mLastUpdateSeconds = pEvent.getObjectTimeSeconds() + lDeltaT;
		}
		/* Allow the concrete caller to determine whether to continue receiving future events. */
		return this.isAlive(pEvent.getObjectTimeSeconds());
	}
	
	/* Empty override methods. */
	          public abstract void onPeriodEvent(final float pCurrentTimeSeconds);
	@Override public          void dispose() { }
	
	public final float getLastUpdateSeconds() {
		return this.mLastUpdateSeconds;
	}
	
	public final float getPeriodSeconds() {
		return this.mPeriodSeconds;
	}

}
