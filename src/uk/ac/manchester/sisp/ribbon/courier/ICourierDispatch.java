package uk.ac.manchester.sisp.ribbon.courier;

public interface ICourierDispatch <U extends ICourier<?>> {
	
	public abstract <T> void onDispatchCourier(final U pCourier, final T pCourierPackage);

}