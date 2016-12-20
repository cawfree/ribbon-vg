package uk.ac.manchester.sisp.ribbon.event.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uk.ac.manchester.sisp.ribbon.common.IEnabled;
import uk.ac.manchester.sisp.ribbon.event.IEvent;
import uk.ac.manchester.sisp.ribbon.event.IEventListener;

public abstract class EventDispatcher <T extends IEvent, U extends EventDispatcher<T, U>> implements IEventListener<T>, IEnabled.W {
	
	/* Member Variables. */
	private final List<IEventsFilter<T,U>> mEventFilters;
	private       boolean                  mEnabled;
	
	public EventDispatcher(final boolean pIsEnabled) {
		/* Initialize Member Variables. */
		this.mEventFilters = Collections.synchronizedList(new ArrayList<IEventsFilter<T, U>>());
		this.mEnabled      = pIsEnabled;
	}

	/* Synchronously iterates through registered EventFilters and fires the Event. */
	@SuppressWarnings("unchecked")
	@Override public synchronized void onRibbonEvent(final T pRibbonEvent) { /** TODO: final **/
		/* Determine if the EventDispatcher is enabled, and whether we should fire the event at all. */
		if(this.isEnabled() && this.onProcessEvent(pRibbonEvent)) {
			/* Synchronize upon the EventFilters. */
			synchronized(this.getEventFilters()) {
				/* Reverse iterate the EventFilters. This supports in-place modification and places priority towards the more recent events. */
				for(int i = this.getEventFilters().size() - 1; i >= 0; i--) {
					/* Fetch the next EventFilter. */
					final IEventsFilter<T, U> lEventsFilter = this.getEventFilters().get(i);
					/* Synchronize along the EventsFilter. */
					synchronized(lEventsFilter) {
						/* Fire the event. Pass a reference to ourselves. */
						if(!lEventsFilter.onHandleEvent(this.onGenerateImmutableEvent(pRibbonEvent), (U)this)) {
							/* Remove the EventFilter. */
							this.getEventFilters().remove(i);
							/* Implement event-specific disposal. */
							lEventsFilter.dispose();
							/* Wake up any tasks that were waiting upon this event. */
							lEventsFilter.notifyAll();
						}
					}
				}
			}
		}
		/* Dispose of the event. */
		pRibbonEvent.dispose();
	}
	
	/* Allows a RibbonEvent to be manipulated before exporting. Returning false will prevent the event from being distributed to listeners at all. */
	protected boolean onProcessEvent(final T pRibbonEvent) {
		/* By default, distribute all events. */
		return true;
	}
	
//	/* Defines whether we wish to transmit an event */
//	protected boolean isEventExportable(final T pRibbonEvent) {
//		return true;
//	}
	
	protected abstract T onGenerateImmutableEvent(final T pT);
	
	public final List<IEventsFilter<T, U>> getEventFilters() {
		return this.mEventFilters;
	}

	@Override public final boolean isEnabled() {
		return this.mEnabled;
	}

	@Override public final void setEnabled(final boolean pIsEnabled) {
		this.mEnabled = pIsEnabled;
	}

}