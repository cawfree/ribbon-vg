package uk.ac.manchester.sisp.ribbon.global;

public final class RibbonGlobal {
	
	public  static final String       LIBRARY_NAME             = "RibbonGL";
	public  static final long         SERIALIZATION_USE_CUSTOM = 1L;
	public  static final String       FILE_RIBBON_HEADER       = "Ribbon<3";
	private static final EReleaseMode RELEASE_MODE             = EReleaseMode.DEVELOPMENT;
	
	public static final boolean isReleaseModeSupported(final EReleaseMode pReleaseMode) {
		return pReleaseMode.compareTo(RibbonGlobal.RELEASE_MODE) >= 0;
	}
	
	public static final void d(final Class<?> pClass, final String pMessage) {
		System.out.println(pClass.getSimpleName() + "\t" + pMessage);
	}
	
	public static final void d(final Object pObject, final String pMessage) {
		RibbonGlobal.d(pObject.getClass(), pMessage);
	}
	
	public static final void e(final Class<?> pClass, final String pMessage) {
		System.err.println(pClass.getSimpleName() + "\t" + pMessage);
	}
	
	public static final void e(final Object pObject, final String pMessage) {
		RibbonGlobal.e(pObject.getClass(), pMessage);
	}
	
	public static final void e(final Object pObject, final Exception pException) {
		RibbonGlobal.e(pObject.getClass(), pException.getMessage());
	}
	
	/* Prevent instantiation of this class. */
	private RibbonGlobal() {}
	
}
