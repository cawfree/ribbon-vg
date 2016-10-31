package uk.ac.manchester.sisp.ribbon.font.truetype.global;

public final class TrueTypeGlobal {

	/** TODO: Refactor these back into TrueTypeDecoder. It's totally unnecessary for these to be globally accessible. **/
	/* Decoder table configuration. */
	public static final int NUMBER_SUPPORTED_CORE_TABLES        = 6;
	public static final int TABLE_PARAMETERS_ELEMENTS_PER_ENTRY = 2;
	
	/* Internal table representation.  */
	public static final int TABLE_PARAMETERS_OFFSET_HEADER            = 0;
	public static final int TABLE_PARAMETERS_OFFSET_MEMORY            = 2;
	public static final int TABLE_PARAMETERS_OFFSET_LOCATIONS         = 4;
	public static final int TABLE_PARAMETERS_OFFSET_MAPPING           = 6;
	public static final int TABLE_PARAMETERS_OFFSET_HORIZONTAL_HEADER = 8;
	public static final int TABLE_PARAMETERS_OFFSET_GLYPHS            = 10;
	
	public static final int TAG_OPENTYPE_VERSION     = 0x4F54544F;
	public static final int TAG_TRUETYPE_VERSION     = 0x00010000;

	/* Core Tables. */
	public static final int TAG_TRUETYPE_CORE_CMAP 	 = 0x636D6170;
	public static final int TAG_TRUETYPE_CORE_GLYF 	 = 0x676C7966;
	public static final int TAG_TRUETYPE_CORE_HEAD 	 = 0x68656164;
	public static final int TAG_TRUETYPE_CORE_HHEA 	 = 0x68686561;
	public static final int TAG_TRUETYPE_CORE_HMTX 	 = 0x686D7478;
	public static final int TAG_TRUETYPE_CORE_LOCA 	 = 0x6C6F6361;
	public static final int TAG_TRUETYPE_CORE_MAXP 	 = 0x6D617870;
	public static final int TAG_TRUETYPE_CORE_NAME 	 = 0x6E616D65;
	public static final int TAG_TRUETYPE_CORE_POST 	 = 0x706F7374;
	
	/* Optional Tables. */
	public static final int TAG_TRUETYPE_DSIG  	     = 0x44534947;
	public static final int TAG_TRUETYPE_GDEF 	     = 0x47444546;
	public static final int TAG_TRUETYPE_GPOS 	     = 0x47504F53;
	public static final int TAG_TRUETYPE_GSUB 	     = 0x47535542;
	public static final int TAG_TRUETYPE_OS_2 	     = 0x4F532F32;
	public static final int TAG_TRUETYPE_CVT  	     = 0x63767420;
	public static final int TAG_TRUETYPE_FPGM 	     = 0x6670676D;
	public static final int TAG_TRUETYPE_GASP 	     = 0x67617370;
	public static final int TAG_TRUETYPE_KERN 	     = 0x6B65726E;
	public static final int TAG_TRUETYPE_PREP 	     = 0x70726570;

	/* Platform IDs. */
	public static enum EPlatformID {
		PLATFORM_ID_UNICODE, 
		PLATFORM_ID_MACINTOSH_QUICKDRAW,
		PLATFORM_ID_ISO,
		PLATFORM_ID_WINDOWS
	}
	
	public static enum EWindowsEncoding {
		UNDEFINED,
		UGL_CHARACTER_SET
	}

	public static final int ID_LOCALE_WINDOWS_ALBANIA 		  	= 0x041C;
	public static final int ID_LOCALE_WINDOWS_BASQUE 		 	= 0x042D;
	public static final int ID_LOCALE_WINDOWS_BYELORUSSIA 	 	= 0x0423;
	public static final int ID_LOCALE_WINDOWS_BULGARIA		  	= 0x0402;
	public static final int ID_LOCALE_WINDOWS_CATALAN 		  	= 0x0403;
	public static final int ID_LOCALE_WINDOWS_CROATIAN 		  	= 0x041A;
	public static final int ID_LOCALE_WINDOWS_CZECH 		  	= 0x0405;
	public static final int ID_LOCALE_WINDOWS_DANISH 		  	= 0x0406;
	public static final int ID_LOCALE_WINDOWS_DUTCH 		  	= 0x0413;
	public static final int ID_LOCALE_WINDOWS_FLEMISH		  	= 0x0813;
	public static final int ID_LOCALE_WINDOWS_AMERICAN 		  	= 0x0409;
	public static final int ID_LOCALE_WINDOWS_BRITISH 		  	= 0x0809;
	public static final int ID_LOCALE_WINDOWS_AUSTRAILIAN 	  	= 0x0C09;
	public static final int ID_LOCALE_WINDOWS_CANADIAN		  	= 0x1009;
	public static final int ID_LOCALE_WINDOWS_NEW_ZEALAND 	  	= 0x1409;
	public static final int ID_LOCALE_WINDOWS_IRELAND 		  	= 0x1809;
	public static final int ID_LOCALE_WINDOWS_ESTONIA 		  	= 0x0425;
	public static final int ID_LOCALE_WINDOWS_FINNISH 		  	= 0x040B;
	public static final int ID_LOCALE_WINDOWS_FRENCH 		  	= 0x040C;
	public static final int ID_LOCALE_WINDOWS_BELGIAN         	= 0x080C;
	public static final int ID_LOCALE_WINDOWS_FRENCH_CANADIAN 	= 0x0C0C;
	public static final int ID_LOCALE_WINDOWS_SWISS 		  	= 0x100C;
	public static final int ID_LOCALE_WINDOWS_LUXEMBOURG	  	= 0x140C;
	public static final int ID_LOCALE_WINDOWS_GERMAN 		  	= 0x0407;
	public static final int ID_LOCALE_WINDOWS_GERMAN_SWISS 	  	= 0x0807;
	public static final int ID_LOCALE_WINDOWS_AUSTRIAN 		  	= 0x0C07;
	public static final int ID_LOCALE_WINDOWS_GERMAN_LUXEMBOURG = 0x1007;
	public static final int ID_LOCALE_WINDOWS_LIECHTENSTEIN 	= 0x1407;
	public static final int ID_LOCALE_WINDOWS_GREEK 		    = 0x0408;
	public static final int ID_LOCALE_WINDOWS_HUNGARIAN 		= 0x040E;
	public static final int ID_LOCALE_WINDOWS_ICELANDIC 		= 0x040F;
	public static final int ID_LOCALE_WINDOWS_ITALIAN 			= 0x0410;
	public static final int ID_LOCALE_WINDOWS_ITALIAN_SWISS 	= 0x0810;
	public static final int ID_LOCALE_WINDOWS__LATVIA 			= 0x0426;
	public static final int ID_LOCALE_WINDOWS__LITHUANIA 		= 0x0427;
	public static final int ID_LOCALE_WINDOWS_BOKMAL 			= 0x0414;
	public static final int ID_LOCALE_WINDOWS_NYNORSK 			= 0x0814;
	public static final int ID_LOCALE_WINDOWS_POLISH 			= 0x0415;
	public static final int ID_LOCALE_WINDOWS_BRAZILIAN			= 0x0416;
	public static final int ID_LOCALE_WINDOWS_PORTUGUESE		= 0x0816;
	public static final int ID_LOCALE_WINDOWS_ROMANIAN 			= 0x0418;
	public static final int ID_LOCALE_WINDOWS_RUSSIAN 			= 0x0419;
	public static final int ID_LOCALE_WINDOWS_SLOVAK 			= 0x041B;
	public static final int ID_LOCALE_WINDOWS_SLOVENIA 			= 0x0424;
	public static final int ID_LOCALE_WINDOWS_SPANISH 			= 0x040A;
	public static final int ID_LOCALE_WINDOWS_MEXICAN 			= 0x080A;
	public static final int ID_LOCALE_WINDOWS_SPANISH_MODERN 	= 0x0C0A;
	public static final int ID_LOCALE_WINDOWS_SWEDISH 			= 0x041D;
	public static final int ID_LOCALE_WINDOWS_TURKISH 			= 0x041F;
	public static final int ID_LOCALE_WINDOWS_UKRAINE 			= 0x0422;
	
	public static final int   TAG_WINDOWS_TABLE_FORMAT_UNICODE = 1;
	
	public static final int   CMAP_INDEX_GLYPH_MISSING         = 0;
	public static final int   CMAP_INDEX_GLYPH_NULL            = 1;
	public static final int   CMAP_INDEX_GLYPH_CARRIAGE_RETURN = 2;
	public static final int   CMAP_INDEX_GLYPH_SPACE           = 3;
	
	public static final short TAG_LOCA_INDEX_FORMAT_SHORT      = 0;
	public static final short TAG_LOCA_INDEX_FORMAT_LONG       = 1;

	public static final int MASK_ARG_1_AND_2_ARE_WORDS         = 0x001;
	public static final int MASK_ARGS_ARE_XY_VALUES            = 0x002;
	public static final int MASK_ROUND_XY_TO_GRID              = 0x004;
	public static final int MASK_WE_HAVE_A_SCALE               = 0x008;
	public static final int MASK_MORE_COMPONENTS               = 0x010;
	public static final int MASK_WE_HAVE_AN_X_AND_Y_SCALE      = 0x020;
	public static final int MASK_WE_HAVE_A_TWO_BY_TWO          = 0x040;
	public static final int MASK_WE_HAVE_INSTRUCTIONS          = 0x080;
	public static final int MASK_USE_MY_METRICS                = 0x100;
	public static final int MASK_ON_CURVE                      = 0x001;
	public static final int MASK_X_IS_BYTE				       = 0x002;
	public static final int MASK_Y_IS_BYTE                     = 0x004;
	public static final int MASK_REPEAT					       = 0x008;
	public static final int MASK_X_IS_SAME				       = 0x010;
	public static final int MASK_Y_IS_SAME				       = 0x020;
	
	/* Support Windows fonts implementations only. */
	public static final boolean isWindowsPlatform(final int pPlatformId) {
		return pPlatformId ==  EPlatformID.PLATFORM_ID_WINDOWS.ordinal();
	}
	
	/* Converts a 32-bit integer to a +/-, 15, 15 32-bit FXP number. */
	public static final float toFixed(final int pValue) {
		return ((float)((pValue >> 16) & 0xFF)) + (((float)(pValue & 0xFF)) / 16384.0f);
	}
	
	/* Converts a 32-bit integer to a +/-, 2, 14 32-bit FXP number. */
	public static final float toFixedDot(final short pValue) {
		return (float)((pValue >> 14) & 0x03) + (((float)(pValue & 0x3FFF)) / 16384.0f);
	}
	
	/* Determines whether a glyph is a composite glyph or simple glyph. */
	public static final boolean isCompoundGlyph(final short pNumberOfContours) {
		return pNumberOfContours < 0;
	}
	
	/* Prevent instantiation of this class. */
	private TrueTypeGlobal() {}
	
}
