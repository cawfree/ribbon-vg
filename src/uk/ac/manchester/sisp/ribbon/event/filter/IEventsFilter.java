package uk.ac.manchester.sisp.ribbon.event.filter;

import uk.ac.manchester.sisp.ribbon.common.IDisposable;
import uk.ac.manchester.sisp.ribbon.event.IEvent;

public interface IEventsFilter <T extends IEvent, U extends EventDispatcher<T, U>> extends IDisposable {
	
	/* True indicates that the event has been handled. */
	public abstract boolean onHandleEvent(final T pEvent, final U pEventsDispatcher);
	
}