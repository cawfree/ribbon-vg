package uk.ac.manchester.sisp.ribbon.event.filter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import uk.ac.manchester.sisp.ribbon.common.IEnabled;
import uk.ac.manchester.sisp.ribbon.event.IEvent;
import uk.ac.manchester.sisp.ribbon.event.IEventListener;

public abstract class EventDispatcher <T extends IEvent, U extends EventDispatcher<T, U>> implements IEventListener<T>, IEnabled.W {
	
	/* Member Variables. */
	private final Set<IEventsFilter<T, U>> mEventFilters;
	private       boolean                  mEnabled;
	
	public EventDispatcher(final boolean pIsEnabled) {
		/* Initialize Member Variables. */
		this.mEventFilters = Collections.synchronizedSet(new HashSet<IEventsFilter<T, U>>());
		this.mEnabled      = pIsEnabled;
	}

	/* Synchronously iterates through registered EventFilters and fires the Event. */
	@SuppressWarnings("unchecked")
	@Override public void onRibbonEvent(final T pRibbonEvent) {
		/* Determine if the EventDispatcher is enabled. */
		if(this.isEnabled()) {
			/* Attain exclusive access to the EventListeners. */
			synchronized(this.getEventFilters()) {
				/* Fetch the EventFilters' Iterator. */
				final Iterator<IEventsFilter<T, U>> lIterator = this.getEventFilters().iterator();
				/* Iterate the EventFilters. */
				while(lIterator.hasNext()) {
					/* Fetch the next EventFilter. */
					final IEventsFilter<T, U> lEventsFilter = lIterator.next();
					/* Fire the event. Pass a reference to ourselves. */
					if(!lEventsFilter.onHandleEvent(this.onGenerateImmutableEvent(pRibbonEvent), (U)this)) {
						/* Remove the EventFilter. */
						lIterator.remove();
						/* Implement event-specific disposal. */
						lEventsFilter.dispose();
					}
				}
			}
		}
	}
	
	protected abstract T onGenerateImmutableEvent(final T pT);
	
	public final Set<IEventsFilter<T, U>> getEventFilters() {
		return this.mEventFilters;
	}

	@Override
	public final boolean isEnabled() {
		return this.mEnabled;
	}

	@Override
	public final void setEnabled(final boolean pIsEnabled) {
		this.mEnabled = pIsEnabled;
	}

}