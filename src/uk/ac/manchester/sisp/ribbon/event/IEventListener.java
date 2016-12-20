package uk.ac.manchester.sisp.ribbon.event;

public interface IEventListener <T extends IEvent> {
	
	public abstract void onRibbonEvent(final T pT);
	
}
