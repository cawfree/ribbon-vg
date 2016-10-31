package uk.ac.manchester.sisp.ribbon.image.png.global;

public final class PNGGlobal {
	
	public static final long   PNG_FILE_SIGNATURE         = 0x89504E470D0A1A0AL;
	public static final int    PNG_CHUNK_IDAT             = 0x49444154;
	public static final int    PNG_CHUNK_SIGNATURE_LENGTH = 4;

	public static final int PNG_IMAGE_TYPE_GREYSCALE            = 0;
	public static final int PNG_IMAGE_TYPE_TRUECOLOR            = 2;
	public static final int PNG_IMAGE_TYPE_INDEXED_COLOR        = 3;
	public static final int PNG_IMAGE_TYPE_GREYSCALE_WITH_ALPHA = 4;
	public static final int PNG_IMAGE_TYPE_TRUECOLOR_WITH_ALPHA = 6;
	
	public static final int PNG_FILTER_TYPE_NONE     = 0;
	public static final int PNG_FILTER_TYPE_SUB      = 1;
	public static final int PNG_FILTER_TYPE_UP       = 2;
	public static final int PNG_FILTER_TYPE_AVERAGE  = 3;
	public static final int PNG_FILTER_TYPE_PAETHE   = 4;
	
}
