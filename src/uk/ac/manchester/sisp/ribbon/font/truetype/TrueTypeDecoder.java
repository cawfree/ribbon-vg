package uk.ac.manchester.sisp.ribbon.font.truetype;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;

import uk.ac.manchester.sisp.ribbon.common.IDisposable;
import uk.ac.manchester.sisp.ribbon.font.truetype.global.TrueTypeGlobal;
import uk.ac.manchester.sisp.ribbon.global.RibbonGlobal;
import uk.ac.manchester.sisp.ribbon.utils.DataUtils;
import uk.ac.manchester.sisp.ribbon.utils.FileUtils;

public final class TrueTypeDecoder implements IDisposable {
	
	/* Member Variables. */
	
	public TrueTypeDecoder() {
		
	}
	
	public final TrueTypeFont onCreateFromFile(final File pFile) throws IOException {
		/* Create a RandomAccessFile to parse the TrueTypeFont using cached performance. */
		final RandomAccessFile lRandomAccessFile = new RandomAccessFile(pFile, FileUtils.RANDOM_ACCESS_FILE_MODE_READ);
		/* Use a MappedByteBuffer to improve I/O performance with the file using cached page access. */
		final MappedByteBuffer lMappedByteBuffer = lRandomAccessFile.getChannel().map(MapMode.READ_ONLY, 0, lRandomAccessFile.length());
		/* Close the RandomAccessFile after attaining the MappedByteBuffer's reference. (The mapping remains valid even after the file channel has been closed.) */
		lRandomAccessFile.close();
		/* Format the ByteBuffer to use Big Endian endianness. */
		lMappedByteBuffer.order(ByteOrder.BIG_ENDIAN);
		/* Parse the first four bytes. */
		final int lFileVersion    = lMappedByteBuffer.getInt();
		/* Instantiate the according type... (?) */
		switch(lFileVersion) {
			case TrueTypeGlobal.TAG_TRUETYPE_VERSION 		   :
				/* Continue processing... */
			break;
			case TrueTypeGlobal.TAG_OPENTYPE_VERSION:
				throw new IOException(".otf files not yet supported!");
		}
		/* Process the offset subtable. */
		final int lNumberOfTables = DataUtils.asUnsigned(lMappedByteBuffer.getShort());
		/* (Discarded) SearchRange: Largest power of two that can be used for fast binary searching. */
		DataUtils.asUnsigned(lMappedByteBuffer.getShort());
		/* (Discarded) EntrySelector: How many iterations of the mSearch loop are required. (How many times to cut the range in half.) */
		DataUtils.asUnsigned(lMappedByteBuffer.getShort());
		/* (Discarded) RangeShift: Number of items that will not get looked at if utilising SearchRange items. */
		DataUtils.asUnsigned(lMappedByteBuffer.getShort());
		/* Initialize Member Variables. */
		final int[] lTableParametersArray  = new int[TrueTypeGlobal.NUMBER_SUPPORTED_CORE_TABLES * TrueTypeGlobal.TABLE_PARAMETERS_ELEMENTS_PER_ENTRY];
		/* Next, implement caching for these locations for fast paged access. */
		for(int i = 0; i < lNumberOfTables; i++) {
			/* Grab the tag for the current table. */
			final int lTrueTypeTableTag = lMappedByteBuffer.getInt();
			/* Discard the checksum. */
			lMappedByteBuffer.getInt();
			/* Calculate the table offset and length. */
			final int lTableOffset      = lMappedByteBuffer.getInt();
			final int lTableLength      = lMappedByteBuffer.getInt();
			/* Iteratively instantiate each core table. */
			switch(lTrueTypeTableTag) {
				case TrueTypeGlobal.TAG_TRUETYPE_CORE_HEAD : 
					lTableParametersArray[TrueTypeGlobal.TABLE_PARAMETERS_OFFSET_HEADER]                = lTableOffset;
					lTableParametersArray[TrueTypeGlobal.TABLE_PARAMETERS_OFFSET_HEADER + 1]            = lTableLength;
				break;
				case TrueTypeGlobal.TAG_TRUETYPE_CORE_MAXP :
					lTableParametersArray[TrueTypeGlobal.TABLE_PARAMETERS_OFFSET_MEMORY]                = lTableOffset;
					lTableParametersArray[TrueTypeGlobal.TABLE_PARAMETERS_OFFSET_MEMORY + 1]            = lTableLength;
				break;
				case TrueTypeGlobal.TAG_TRUETYPE_CORE_CMAP :
					lTableParametersArray[TrueTypeGlobal.TABLE_PARAMETERS_OFFSET_MAPPING]               = lTableOffset;
					lTableParametersArray[TrueTypeGlobal.TABLE_PARAMETERS_OFFSET_MAPPING + 1]           = lTableLength;
				break;
				case TrueTypeGlobal.TAG_TRUETYPE_CORE_LOCA :
					lTableParametersArray[TrueTypeGlobal.TABLE_PARAMETERS_OFFSET_LOCATIONS]             = lTableOffset;
					lTableParametersArray[TrueTypeGlobal.TABLE_PARAMETERS_OFFSET_LOCATIONS + 1]         = lTableLength;
				break;
				case TrueTypeGlobal.TAG_TRUETYPE_CORE_GLYF :
					lTableParametersArray[TrueTypeGlobal.TABLE_PARAMETERS_OFFSET_GLYPHS]                = lTableOffset;
					lTableParametersArray[TrueTypeGlobal.TABLE_PARAMETERS_OFFSET_GLYPHS + 1]            = lTableLength;
				break;
				case TrueTypeGlobal.TAG_TRUETYPE_CORE_HHEA : 
					lTableParametersArray[TrueTypeGlobal.TABLE_PARAMETERS_OFFSET_HORIZONTAL_HEADER]     = lTableOffset;
					lTableParametersArray[TrueTypeGlobal.TABLE_PARAMETERS_OFFSET_HORIZONTAL_HEADER + 1] = lTableLength;
				break;
			}
			
		}
		/* Define the initial style flags. */
		short lStyleFlags                = 0;
		int   lIndexToLocationFormat     = 0; /** TODO: Abstract to a proper enumeration! **/
		int   lMaximumSequenceLength     = 0;
		int   lMaximumContoursLength     = 0;
		int   lMaximumInstructionsLength = 0;
		int[] lGlyphLocations            = null;
		int[] lEndCharacterCodes         = null;
		int[] lStartCharacterCodes       = null;
		int[] lCharacterIdDelta          = null;
		int[] lIDRangeOffset             = null;
		int   lIDRangeOffsetLocation     = 0;
		int lAscent                      = 0;
		int lDescent                     = 0;
		int lLineGap                     = 0;
		int lUnitsPerEM                  = 0;
		int lAdvanceWidth                = 0;
		
		/* Next, process each table in order. (Safely synchronize inter-table data dependencies.) */
		for(int lCurrentTable = 0; lCurrentTable < lTableParametersArray.length; lCurrentTable += TrueTypeGlobal.TABLE_PARAMETERS_ELEMENTS_PER_ENTRY) {
			/* Jump to the table offset. */
			lMappedByteBuffer.position(lTableParametersArray[lCurrentTable]);
			/* Process the table based on the iteration index. */
			switch(lCurrentTable) {
				case TrueTypeGlobal.TABLE_PARAMETERS_OFFSET_HEADER    :
					/* Table Version. */
					TrueTypeGlobal.toFixed(lMappedByteBuffer.getInt());
					/* Font Revision. */
					TrueTypeGlobal.toFixed(lMappedByteBuffer.getInt());
					/* Checksum adjustment. */
					DataUtils.asUnsigned(lMappedByteBuffer.getInt());
					/* Magic number. */
					DataUtils.asUnsigned(lMappedByteBuffer.getInt());
					/* Flags. */
					final short lFlags = lMappedByteBuffer.getShort();
					/* Font metrics. */
					DataUtils.isFlagSet(lFlags, 0x01); // BaseLineZeroY
					DataUtils.isFlagSet(lFlags, 0x02); // LeftSideBearingZeroX
					DataUtils.isFlagSet(lFlags, 0x04); // InstructionsDeltaWithPointSize
					DataUtils.isFlagSet(lFlags, 0x08); // lForceIntegerMath
					DataUtils.isFlagSet(lFlags, 0x10); // lAdvanceWidthsNonLinear
					/* Units per EM. */
					lUnitsPerEM = DataUtils.asUnsigned(lMappedByteBuffer.getShort());
					/* Date Created. */
					lMappedByteBuffer.getLong();
					/* Date Modified. */
					lMappedByteBuffer.getLong();
					/* Quantities in FUnits. */
					DataUtils.asUnsigned(lMappedByteBuffer.getShort()); // XMinFUnits
					DataUtils.asUnsigned(lMappedByteBuffer.getShort()); // YMinFUnits
					DataUtils.asUnsigned(lMappedByteBuffer.getShort()); // XMaxFUnits
					DataUtils.asUnsigned(lMappedByteBuffer.getShort()); // YMaxFUnits
					/* Define StyleFlags. */
					lStyleFlags          = lMappedByteBuffer.getShort();
					/* Smallest readable size in pixels. */
					DataUtils.asUnsigned(lMappedByteBuffer.getShort());
					/* Font direction hints. */
					lMappedByteBuffer.getShort();
					/* Location conversion. */
					lIndexToLocationFormat = lMappedByteBuffer.getShort();
					
					lMappedByteBuffer.getShort();
				break;
				case TrueTypeGlobal.TABLE_PARAMETERS_OFFSET_MEMORY    :
					/* Table Version Number. */
					TrueTypeGlobal.toFixed(lMappedByteBuffer.getInt());
					/* Total Number of Glyphs. */
					DataUtils.asUnsigned(lMappedByteBuffer.getShort());
					/* Initialize the XCoordinatesBuffer and YCoordinatesBuffer and FlagsBuffer to store the maximum possible number of entries. */
					lMaximumSequenceLength = DataUtils.asUnsigned(lMappedByteBuffer.getShort());
					/* Initialize the maximum contours length. */
					lMaximumContoursLength = DataUtils.asUnsigned(lMappedByteBuffer.getShort());
					/* Max Composite Points. */
					DataUtils.asUnsigned(lMappedByteBuffer.getShort());
					/* Max Composite Contours. */
					DataUtils.asUnsigned(lMappedByteBuffer.getShort());
					/* Max Zones. */
					DataUtils.asUnsigned(lMappedByteBuffer.getShort());
					/* Max Twilight Points. */
					DataUtils.asUnsigned(lMappedByteBuffer.getShort());
					/* Max Storage. */
					DataUtils.asUnsigned(lMappedByteBuffer.getShort());
					/* Max Function Defintions. */
					DataUtils.asUnsigned(lMappedByteBuffer.getShort());
					/* Max Instruction Definitions. */
					DataUtils.asUnsigned(lMappedByteBuffer.getShort());
					/* Max Stack Elements. */
					DataUtils.asUnsigned(lMappedByteBuffer.getShort());
					/* Initialize the GlyphInstructionsBuffer to the maximum number of instuctions. */
					lMaximumInstructionsLength = DataUtils.asUnsigned(lMappedByteBuffer.getShort());
					/* Max Component Elements. */
					DataUtils.asUnsigned(lMappedByteBuffer.getShort());
					/* Max Component Depth. */
					DataUtils.asUnsigned(lMappedByteBuffer.getShort());
				break;
				case TrueTypeGlobal.TABLE_PARAMETERS_OFFSET_LOCATIONS :
					/* Initialize the GlyphLocations array. */
					lGlyphLocations = new int[lTableParametersArray[lCurrentTable + 1] / (lIndexToLocationFormat == TrueTypeGlobal.TAG_LOCA_INDEX_FORMAT_SHORT  ? DataUtils.BYTES_PER_SHORT : DataUtils.BYTES_PER_INT)];
					/* Populate the GlyphLocations array. */
					for(int i = 0; i < lGlyphLocations.length; i++) {
						/* Assign the GlyphLocation. */
						lGlyphLocations[i] = ((lIndexToLocationFormat == TrueTypeGlobal.TAG_LOCA_INDEX_FORMAT_SHORT) ? DataUtils.asUnsigned(lMappedByteBuffer.getShort()) * 2 : lMappedByteBuffer.getInt());
					}
				break;
				case TrueTypeGlobal.TABLE_PARAMETERS_OFFSET_HORIZONTAL_HEADER :
					/* Table Version. (1.0) */
					TrueTypeGlobal.toFixed(lMappedByteBuffer.getInt());
					/* Fetch font configuration. */
					lAscent  = lMappedByteBuffer.getShort();
					lDescent = lMappedByteBuffer.getShort();
					lLineGap = lMappedByteBuffer.getShort();
					lAdvanceWidth = DataUtils.asUnsigned(lMappedByteBuffer.getShort());
				break;
				case TrueTypeGlobal.TABLE_PARAMETERS_OFFSET_MAPPING   :
					/* Read the Table Version. */
					if(lMappedByteBuffer.getShort() != 0) {
						RibbonGlobal.e(this, "cmap table contained non-zero version!");
					}
					/* Read the NumberOfSubTables. */
					final int lNumberOfSubTables    = DataUtils.asUnsigned(lMappedByteBuffer.getShort());
					/* Iterate SubTables. */
					for(int i = 0; i < lNumberOfSubTables; i++) {
						/* Read SubTable data. */
						final int  lPlatformId            = DataUtils.asUnsigned(lMappedByteBuffer.getShort());
						final int  lPlatformSpecificId    = DataUtils.asUnsigned(lMappedByteBuffer.getShort());
						final int  lTableOffset           = lMappedByteBuffer.getInt();
						/* Process Windows Format Tables. */
						if(TrueTypeGlobal.isWindowsPlatform(lPlatformId)) {
							switch(lPlatformSpecificId) {
								case TrueTypeGlobal.TAG_WINDOWS_TABLE_FORMAT_UNICODE:
									/* Skip to the table location. */
									lMappedByteBuffer.position(lTableParametersArray[lCurrentTable] + lTableOffset);
									/* Read the format (0x04). */
									if(lMappedByteBuffer.getShort() != 0x04) {
										RibbonGlobal.e(this, "Unicode Table possesses an incorrect format mX!");
									}
									/* Length of the table in bytes. */
									DataUtils.asUnsigned(lMappedByteBuffer.getShort());
									/* Table Version. */
									DataUtils.asUnsigned(lMappedByteBuffer.getShort());
									/* Segment Count. */
									lEndCharacterCodes   = new int[DataUtils.asUnsigned(lMappedByteBuffer.getShort()) / 2];
									lStartCharacterCodes = new int[lEndCharacterCodes.length];
									lCharacterIdDelta    = new int[lEndCharacterCodes.length];
									lIDRangeOffset       = new int[lEndCharacterCodes.length];
									/* Search Range. */
									DataUtils.asUnsigned(lMappedByteBuffer.getShort());
									/* Entry Selector. */
									DataUtils.asUnsigned(lMappedByteBuffer.getShort());
									/* Range Shift. */
									DataUtils.asUnsigned(lMappedByteBuffer.getShort());
									/* Initialize the EndCharacterCodes array. (Last = 0xFFFF). */
									for(int j = 0; j < lEndCharacterCodes.length; j++) { lEndCharacterCodes[j] = DataUtils.asUnsigned(lMappedByteBuffer.getShort()); }
									/* Read the reserved index. */
									if(lMappedByteBuffer.getShort() != 0) { RibbonGlobal.e(this, "Reserved field is nonzero!"); }
									/* Initialize the StartCharacterCodes array. */
									for(int j = 0; j < lStartCharacterCodes.length; j++) { lStartCharacterCodes[j] = DataUtils.asUnsigned(lMappedByteBuffer.getShort()); }
									/* Initialize the CharacterIdDelta array. */
									for(int j = 0; j < lCharacterIdDelta.length; j++) { lCharacterIdDelta[j] = DataUtils.asUnsigned(lMappedByteBuffer.getShort()); }
									/* Track the current location of the IDRangeOffset array. */
									lIDRangeOffsetLocation = lMappedByteBuffer.position();
									/* Initialize the CharacterIdDelta array. */
									for(int j = 0; j < lIDRangeOffset.length; j++) { lIDRangeOffset[j] = DataUtils.asUnsigned(lMappedByteBuffer.getShort()); }
									/* Decrement the last charactercode index to not include the final erroneous character. */
									lEndCharacterCodes[lEndCharacterCodes.length - 1]--;
								}
							break;
						}
					}
				break;
			}
		}
		/* Return the created font. */
		return new TrueTypeFont(lMappedByteBuffer, lTableParametersArray, lGlyphLocations, lAscent, lDescent, lLineGap, lAdvanceWidth, lUnitsPerEM, lStyleFlags, lStartCharacterCodes, lEndCharacterCodes, lCharacterIdDelta, lIDRangeOffset, lIDRangeOffsetLocation, lMaximumSequenceLength, lMaximumContoursLength, lMaximumInstructionsLength);
	}

	@Override
	public final void dispose() {
		
	}
	
}
