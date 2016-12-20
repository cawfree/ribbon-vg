package uk.ac.manchester.sisp.ribbon.ui.time;

import uk.ac.manchester.sisp.ribbon.event.IEvent;
import uk.ac.manchester.sisp.ribbon.event.filter.EventDispatcher;

public final class UITimeDispatcher extends EventDispatcher<IEvent, UITimeDispatcher> {
	
	/* Member Variables. */
	
	public UITimeDispatcher(final boolean pIsEnabled) {
		super(pIsEnabled);
		/* Initialize Member Variables. */
	}

	@Override
	protected final IEvent onGenerateImmutableEvent(final IEvent pEvent) {
		/* Return a new implementation of the core IEvent. */
		return new IEvent.Impl(pEvent.getObjectTimeSeconds());
	}

}