package uk.ac.manchester.sisp.ribbon.font.truetype;

import java.nio.MappedByteBuffer;
import java.util.HashMap;
import java.util.Map;

import uk.ac.manchester.sisp.ribbon.font.FontGlyph;
import uk.ac.manchester.sisp.ribbon.font.GlyphData;
import uk.ac.manchester.sisp.ribbon.font.IFont;
import uk.ac.manchester.sisp.ribbon.font.truetype.global.TrueTypeGlobal;
import uk.ac.manchester.sisp.ribbon.io.ArrayStore;
import uk.ac.manchester.sisp.ribbon.io.ArrayStore.Float;
import uk.ac.manchester.sisp.ribbon.opengl.vector.VectorPathContext;
import uk.ac.manchester.sisp.ribbon.opengl.vector.global.EVectorUnits;
import uk.ac.manchester.sisp.ribbon.utils.DataUtils;

public final class TrueTypeFont implements IFont {
	
	private static final int STYLE_FLAG_BOLD   = (1 << 0);
	private static final int STYLE_FLAG_ITALIC = (1 << 1);
	
	/** TODO: Which of these parameters can be abstracted to IFont? **/
	/* Member Variables. */
	private       MappedByteBuffer          mMappedByteBuffer;
	private       int[]                     mTableParameters;
	private       int[]                     mGlyphLocations;
	private final int                       mUnitsPerEM;
	private final boolean                   mBold;
	private final boolean                   mItalic;
	private final int                       mAscent;
	private final int                       mDescent;
	private final int                       mLineGap;
	private final int                       mAdvanceWidth;
	private       Map<Character, FontGlyph> mFontGlyphMap;
	
	 /** TODO: This all needs to be encapsulated within a table. **/
	private       int[]                     mStartCharacterCodes;
	private       int[]                     mEndCharacterCodes;
	private       int[]                     mCharacterIdDelta;
	private       int[]                     mIDRangeOffset;
	private final int                       mIDRangeOffsetLocation;
	private final int                       mMaximumSequence;
	private final int                       mMaximumContours;
	private final int                       mMaximumInstructions;
	
	/** TODO: How to handle buffers efficiently? **/
	// sequence is x/y/flags array length (int, int, byte)
	protected TrueTypeFont(final MappedByteBuffer pMappedByteBuffer, final int[] pTableParameters, final int[] pGlyphLocations, final int pAscent, final int pDescent, final int pLineGap, final int pAdvanceWidth, final int pUnitsPerEM, final short pStyleFlags, final int[] pStartCharacterCodes, final int[] pEndCharacterCodes, final int[] pCharacterIdDelta, final int[] pIDRangeOffset, final int pIDRangeOffsetLocation, final int pMaximumSequence, final int pMaximumContours, final int pMaximumInstructions) {
		/* Initialize Member Variables. */
		this.mMappedByteBuffer      = pMappedByteBuffer;
		this.mTableParameters       = pTableParameters;
		this.mGlyphLocations        = pGlyphLocations;
		this.mAscent                = pAscent;
		this.mDescent               = pDescent;
		this.mLineGap               = pLineGap;
		this.mAdvanceWidth          = pAdvanceWidth;
		this.mUnitsPerEM            = pUnitsPerEM;
		this.mBold                  = DataUtils.isFlagSet(pStyleFlags, TrueTypeFont.STYLE_FLAG_BOLD);
		this.mItalic                = DataUtils.isFlagSet(pStyleFlags, TrueTypeFont.STYLE_FLAG_ITALIC);
		this.mStartCharacterCodes   = pStartCharacterCodes;
		this.mEndCharacterCodes     = pEndCharacterCodes;
		this.mCharacterIdDelta      = pCharacterIdDelta;
		this.mIDRangeOffset         = pIDRangeOffset;
		this.mIDRangeOffsetLocation = pIDRangeOffsetLocation;
		this.mMaximumSequence       = pMaximumSequence;
		this.mMaximumContours       = pMaximumContours;
		this.mMaximumInstructions   = pMaximumInstructions;
		this.mFontGlyphMap          = new HashMap<Character, FontGlyph>(); /** TODO: End up destroying this. **/
		
		/** TODO: Iterate through all glyphs and read base information. **/
	}
	
	/** TODO: Buffer these. **/
	@Override
	public final GlyphData onFetchDetails(final Character pCharacter) {
		/* Fetch the GlyphIndex. */
		final int lGlyphIndex = this.onFetchGlyphIndex(pCharacter);
		/* Jump to the GlyphIndex. */
		this.onJumpToGlyph(lGlyphIndex);
		/* Allocate the GlyphData for the current character. */
		return new GlyphData(lGlyphIndex, this.getMappedByteBuffer().getShort(), this.getMappedByteBuffer().getShort(), this.getMappedByteBuffer().getShort(), this.getMappedByteBuffer().getShort(), this.getMappedByteBuffer().getShort());
	}
	
	public final float onCalculateLineHeight(final float pFontScale) {
		return (this.getAscent() - this.getDescent() + this.getLineGap()) * pFontScale;
	}
	
	@Override
	public final FontGlyph onFetchFontGlyph(final Float pFloatStore, final VectorPathContext pVectorPathContext, final char pCharacter) {
		if(pCharacter == '\r') { /** TODO: Gross architecture, poor, inflexible, just get rid of it. **/
			return this.onFetchFontGlyph(pFloatStore, pVectorPathContext, ' '); 
		}
		/* Determine if the FontGlyph has already been loaded. */
		if(this.getFontGlyphMap().containsKey(pCharacter)) {
			/* The FontGlyph already exists. */
			return this.getFontGlyphMap().get(pCharacter);
		}
		else {
			/* Fetch the GlyphIndex. */
			final int lGlyphIndex = this.onFetchGlyphIndex(pCharacter);
			/* Enable special character support. */ /** TODO: Support other special characters. **/
			switch(lGlyphIndex) {
				case TrueTypeGlobal.CMAP_INDEX_GLYPH_MISSING : 
					/* Return a null character if the glyph is missing. */ /** TODO: Use a question mark or something! **/
					return null;
				case TrueTypeGlobal.CMAP_INDEX_GLYPH_SPACE : 
					/* Fetch the missing glyph. */
					final FontGlyph lMissingGlyph = this.onFetchGlyph(pFloatStore, pVectorPathContext, 0); /** TODO: Abstract. **/
					/* Create a new FontGlyph using the bounds specified by the MissingGlyph, whilst supplying a null VectorPath. */
					final FontGlyph lSpaceGlyph   = new FontGlyph(pVectorPathContext.onCreatePath(pFloatStore), lMissingGlyph.getMinimumX(), lMissingGlyph.getMinimumY(), lMissingGlyph.getMaximumX(), lMissingGlyph.getMaximumY());
					/* Add the SpaceGlyph to the FontGlyphMap. */
					this.getFontGlyphMap().put(pCharacter, lSpaceGlyph);
					/* Return the SpaceGlyph. */
					return lSpaceGlyph;
				default: 
					final FontGlyph lCurrentGlyph = this.onFetchGlyph(pFloatStore, pVectorPathContext, lGlyphIndex);
					/* Add the CurrentGlyph into the FontGlyphMap. */
					this.getFontGlyphMap().put(pCharacter, lCurrentGlyph);
					/* Return the CurrentGlyph. */
					return lCurrentGlyph;
			}
		}
	}
	
	private final int onFetchGlyphIndex(final char pCharacter) {
		/* Determine if the character exists. */
		boolean lCharacterExists = false;
		/* Define a variable for tracking the character's RangeIndex. */
		int lRangeIndex = 0;
		/* Iterate through each character code table. */
		for(lRangeIndex = 0; lRangeIndex < this.getEndCharacterCodes().length & !lCharacterExists; lRangeIndex++) {
			/* Determine whether the character lies in this range. */
			lCharacterExists |= (pCharacter >= this.getStartCharacterCodes()[lRangeIndex] && pCharacter <= this.getEndCharacterCodes()[lRangeIndex]);
		}
		/* After searching for the character, find it's location within the GlyphID array. */
		if(lCharacterExists) {
			/* Decrement the CharacterCodeIndex. */
			lRangeIndex--;
			/* Calculate the IDPointerOffset. */
			final int lIDPointerOffset = this.getIDRangeOffsetLocation() + (lRangeIndex * DataUtils.BYTES_PER_SHORT);
			/* Define a variable to track the glyph index. */
			int lGlyphIndex;
			/* Determine if the character is rangeable. */
			if(this.getIDRangeOffsets()[lRangeIndex] != 0) {
				/* Calculate the GlyphIndex. */
				lGlyphIndex = (this.getIDRangeOffsets()[lRangeIndex] + (2 * ((int)pCharacter - this.getStartCharacterCodes()[lRangeIndex])) + lIDPointerOffset) % 65535;
				/* Jump to the GlyphIDArray. */
				this.getMappedByteBuffer().position(lGlyphIndex);
				/* Sample the index mX. */
				lGlyphIndex = DataUtils.asUnsigned(this.getMappedByteBuffer().getShort());
				/* Check to see that the GlyphIndex does not point towards a null character. */
				if(lGlyphIndex != 0) {
					/* Add the corresponding IDDelta. */
					lGlyphIndex += this.getCharacterIdDelta()[lRangeIndex];
				}
			}
			else {
				/* Assign the direct glyph index. */
				lGlyphIndex = (pCharacter + this.getCharacterIdDelta()[lRangeIndex]) % 65536;
			}
			/* Return the GlyphIndex. */
			return lGlyphIndex;
		}
		/* Define that the glyph is missing. */
		return TrueTypeGlobal.CMAP_INDEX_GLYPH_MISSING;
	}
	
	private final FontGlyph onFetchGlyph(final ArrayStore.Float pFloatStore, final VectorPathContext pVectorPathContext, final int pGlyphIndex) {
		/* Jump to the GlyphIndex. */
		this.onJumpToGlyph(pGlyphIndex);
		/* Define a reference for the current glyph. */
		TrueTypeGlyph lCurrentGlyph = null;
		/* Begin parsing. */
		final short lNumberOfContours = this.getMappedByteBuffer().getShort();
		/* Instantiate the Glyph, depending on whether it is a SimpleGlyph for CompoundGlyph. */
		if(TrueTypeGlobal.isCompoundGlyph(lNumberOfContours)) {
			/* Initialize a Compound Glyph. */
			throw new RuntimeException("Cannot support compound glyphs yet! ("+pGlyphIndex+")");
		}
		else {
			/* Create a SimpleGlyph. */ /** TODO: Avoid these bad allocations. **/
			lCurrentGlyph = new TrueTypeGlyph.Simple(pFloatStore, pVectorPathContext, this.getMappedByteBuffer(), lNumberOfContours, new int[this.getMaximumContours()], new int[this.getMaximumSequence()], new int[this.getMaximumSequence()], new byte[this.getMaximumSequence()], new byte[this.getMaximumInstructions()]);
		}
		/* Return the CurrentGlyph. */
		return lCurrentGlyph;
	}
	
	private final void onJumpToGlyph(final int pGlyphIndex) {
		/* Move the MappedByteBuffer to the IGlyph data location. */
		this.getMappedByteBuffer().position(this.getTableParameters()[TrueTypeGlobal.TABLE_PARAMETERS_OFFSET_GLYPHS] + this.getGlyphLocations()[pGlyphIndex]);
	}

	@Override
	public final FontGlyph getFontGlyph(Character pCharacter) {
		return this.getFontGlyphMap().get(pCharacter);
	}
	
	@Override
	public final float getFontScale(final float pDotsPerInch, final float pPointSize) {
		return EVectorUnits.PT.onScaleToPixels(pPointSize, pDotsPerInch) / this.getUnitsPerEM();
	}

	@Override
	public void dispose() {
		/* Nullify dependent variables. */
		this.mMappedByteBuffer    = null;
		this.mTableParameters     = null;
		this.mGlyphLocations      = null;
		this.mStartCharacterCodes = null;
		this.mEndCharacterCodes   = null;
		this.mCharacterIdDelta    = null;
		this.mIDRangeOffset       = null;
		this.mFontGlyphMap        = null;
	}

	private final MappedByteBuffer getMappedByteBuffer() {
		return this.mMappedByteBuffer;
	}
	
	private final int[] getTableParameters() {
		return this.mTableParameters;
	}
	
	private final int[] getGlyphLocations() {
		return this.mGlyphLocations;
	}
	
	private final int getUnitsPerEM() {
		return this.mUnitsPerEM;
	}

	@Override
	public final int getAscent() {
		return this.mAscent;
	}

	@Override
	public final int getDescent() {
		return this.mDescent;
	}

	@Override
	public final int getLineGap() {
		return this.mLineGap;
	}
	
	@Override
	public final int getAdvanceWidth() {
		return this.mAdvanceWidth;
	}

	@Override
	public final boolean isBold() {
		return this.mBold;
	}

	@Override
	public final boolean isItalic() {
		return this.mItalic;
	}
	
	private final Map<Character, FontGlyph> getFontGlyphMap() {
		return this.mFontGlyphMap;
	}
	
	
	private final int[] getStartCharacterCodes() {
		return this.mStartCharacterCodes;
	}
	
	private final int[] getEndCharacterCodes() {
		return this.mEndCharacterCodes;
	}
	
	private final int[] getCharacterIdDelta() {
		return this.mCharacterIdDelta;
	}
	
	private final int[] getIDRangeOffsets() {
		return this.mIDRangeOffset;
	}
	
	private final int getIDRangeOffsetLocation() {
		return this.mIDRangeOffsetLocation;
	}
	
	private final int getMaximumSequence() {
		return this.mMaximumSequence;
	}
	
	private final int getMaximumContours() {
		return this.mMaximumContours;
	}
	
	private final int getMaximumInstructions() {
		return this.mMaximumInstructions;
	}

}
