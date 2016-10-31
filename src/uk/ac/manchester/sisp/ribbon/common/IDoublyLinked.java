package uk.ac.manchester.sisp.ribbon.common;

public interface IDoublyLinked <T> {
	
	public abstract void setNext(final T pT);
	public abstract T    getNext();
	public abstract void setPrevious(final T pT);
	public abstract T    getPrevious();

}
