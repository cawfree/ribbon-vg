package uk.ac.manchester.sisp.ribbon.font.global;

import uk.ac.manchester.sisp.ribbon.font.FontGlyph;
import uk.ac.manchester.sisp.ribbon.font.IFont;
import uk.ac.manchester.sisp.ribbon.utils.DataUtils;


public final class FontGlobal {
	
	/** TODO: Support multiline configurations. **/
	public static final float onCalculateTextBounds(final IFont pFont, final float pFontScale, final String pString) {
		/* Declare the width variable. */
		float lTextWidth = 0.0f;
		/* Iterate through each character in the String. */
		for(int i = 0; i < pString.length(); i++) {
			/* Fetch the current FontGlyph. */
			final FontGlyph lFontGlyph  = pFont.getFontGlyph(DataUtils.getCachedChar(pString.charAt(i)));
			/* Increment the TextWidth. */
			lTextWidth                 += lFontGlyph.onCalculateScaledWidth(pFontScale);
		}
		/* Return the TextWidth. */
		return lTextWidth;
	}
	
	/* Prevent instantiation of this class. */
	private FontGlobal() {}
	
}
