package uk.ac.manchester.sisp.ribbon.image.svg;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.List;

import uk.ac.manchester.sisp.ribbon.image.svg.exception.SVGException;
import uk.ac.manchester.sisp.ribbon.image.svg.global.SVGGlobal;
import uk.ac.manchester.sisp.ribbon.io.ArrayStore;
import uk.ac.manchester.sisp.ribbon.opengl.IGLScreenParameters;
import uk.ac.manchester.sisp.ribbon.opengl.vector.VectorPath;
import uk.ac.manchester.sisp.ribbon.opengl.vector.VectorPathContext;
import uk.ac.manchester.sisp.ribbon.utils.FileUtils;

/** TODO: Abstract to a core XML implementation. (Pull parser.) **/
public final class SVGDecoder {
	
	/* Member Variables. */
	
	public SVGDecoder() {
		/* Initialize Member Variables. */
	}
	
	/** TODO: Look at what happens to the data when we perform a union with 'bad' data sources. Attempt to emulate. **/
	/** TODO: This is bad for the long term... **/
	/** TODO: May be something to do with new lines. **/
	
	public final List<VectorPath> createFromFile(final File pFile, final ArrayStore.Float pFloatStore, final VectorPathContext pVectorPathContext, final IGLScreenParameters pGLScreenParameters) throws IOException {
		/* Create a RandomAccessFile to parse the PNG image data. */
		final RandomAccessFile lRandomAccessFile = new RandomAccessFile(pFile, FileUtils.RANDOM_ACCESS_FILE_MODE_READ);
		/* Fetch a reference to the RandomAccessFile's total length. */
		final long lFileLength = lRandomAccessFile.length();
		/* Use a MappedByteBuffer to cache the file. */
		final MappedByteBuffer lMappedByteBuffer = lRandomAccessFile.getChannel().map(MapMode.READ_ONLY, 0, lFileLength);
		/* Close the RandomAccessFile. (The Mapping will remain valid until garbage collected.) */
		lRandomAccessFile.close();
		/* Allocate a buffer for single characters. */
		final char[]           lCharBuffer   = new char[1];
		/* Allocate a char-based ArrayStore. */
		final ArrayStore.Char  lCharStore    = new ArrayStore.Char();
		/* Define the VectorPath List. */
		final List<VectorPath> lVectorPaths  = new ArrayList<VectorPath>();
		/* Parse the XML */
		SVGDecoder.onFetchXML(lMappedByteBuffer, pFloatStore, pVectorPathContext, pGLScreenParameters, lCharBuffer, lCharStore, lVectorPaths);
		/* Return the created image. */
		return lVectorPaths;
	}
	
	private static final void onFetchXML(final MappedByteBuffer pMappedByteBuffer, final ArrayStore.Float pFloatStore, final VectorPathContext pVectorPathContext, final IGLScreenParameters pGLScreenParameters, final char[] pCharBuffer, final ArrayStore.Char pCharStore, final List<VectorPath> pVectorPaths) throws SVGException {
		/* Ensure we're working within the confines of the XML document. */
		while(pMappedByteBuffer.position() < pMappedByteBuffer.capacity()) {
			/* Iterate until we rest on the first tag. '<' */
			while(pCharBuffer[0] != '<' && (pMappedByteBuffer.position() < pMappedByteBuffer.capacity())) {
				/* Fetch the next character in the sequence. */
				pCharBuffer[0] = (char)pMappedByteBuffer.get();
			}
			/* Determine whether we've found a new tag. */
			if(pMappedByteBuffer.position() < pMappedByteBuffer.capacity()) {
				/* Fetch the next character in the sequence. */
				pCharBuffer[0] = (char)pMappedByteBuffer.get();
				/* Determine whether we're dealing with an unsupported type. */
				if(pCharBuffer[0] == '?' || pCharBuffer[0] == '!') {
					/* We're dealing with a compiler type. Skip this tag. */
					continue;
				}
				/* Define if we're handling an EndTag. */
				final boolean lIsEndTag = (pCharBuffer[0] == '/');
				/* Check if we're closing. */
				if(lIsEndTag) {
					/* Fetch the next character in the sequence. */
					pCharBuffer[0] = (char)pMappedByteBuffer.get();
					/* Return from the call; allow the tag to be parsed. */
					return;
				}
				/** TODO: Can define enclosing storage here. **/
				/* Grab the EnclosingTag. */
				SVGDecoder.onParseLetters(pMappedByteBuffer, pCharBuffer, pCharStore);
				/** TODO: Construct parsing operations based upon the tag. I suppose this is the pull architecture? Process all attributes then finalize the tag or something. **/
				/* Handle the closing operation. */
				if(!lIsEndTag) {
					/** TODO: Implement start of tag handling. **/
					/** TODO: This is where the tag ends. **/
					/* Remove any leading whitespace. */
					while(Character.isWhitespace(pCharBuffer[0])) {
						/* Fetch the next character in the sequence. */
						pCharBuffer[0] = (char)pMappedByteBuffer.get();
					}
					/* Determine if we're dealing with an elevated tag data. */
					if(pCharBuffer[0] == ':') {
						/* Fetch the next character in the sequence. */
						pCharBuffer[0] = (char)pMappedByteBuffer.get();
						/* Fetch the elevated tag name. */
						SVGDecoder.onParseLetters(pMappedByteBuffer, pCharBuffer, pCharStore);
						/* Remove any leading whitespace. */
						while(Character.isWhitespace(pCharBuffer[0])) {
							/* Fetch the next character in the sequence. */
							pCharBuffer[0] = (char)pMappedByteBuffer.get();
						}
					}
					/* Begin iteratively processing attributes. */
					while(pCharBuffer[0] != '>' && pCharBuffer[0] != '/') {
						/* Fetch the Attribute. */
						do {
							/* Buffer the next character in the sequence. */
							pCharStore.store(pCharBuffer);
							/* Whilst handling non-tag characters. */
						} while(!Character.isWhitespace(pCharBuffer[0] = (char)pMappedByteBuffer.get()) && pCharBuffer[0] != '=');
						/* Withdraw the Attribute from the CharStore. */
						final String lAttribute = new String(pCharStore.onProduceArray());
						/* Remove any leading whitespace after the assignment has been made. */
						while(Character.isWhitespace(pCharBuffer[0]) || pCharBuffer[0] == '=') {
							/* Fetch the next character in the sequence. */
							pCharBuffer[0] = (char)pMappedByteBuffer.get();
						}
						/* Latch onto the EnclosingChar. */
						final char lEnclosingChar = pCharBuffer[0];
						/* Fetch the next character in the sequence. */
						pCharBuffer[0] = (char)pMappedByteBuffer.get();
						/* Remove any leading whitespace after the assignment has been made. */
						while(Character.isWhitespace(pCharBuffer[0])) {
							/* Fetch the next character in the sequence. */
							pCharBuffer[0] = (char)pMappedByteBuffer.get();
						}
						/* Handle the attribute. */
						switch(lAttribute) {
							case SVGGlobal.SVG_XML_ATTRIBUTE_STYLE :
							break;
							case SVGGlobal.SVG_XML_ATTRIBUTE_PATH  :
								/* Parse the path. */
								pVectorPaths.add(SVGDecoder.onParseSVGPath(pMappedByteBuffer, pCharBuffer, lEnclosingChar, pCharStore, pFloatStore, pVectorPathContext));
							break;
							default                                :
							break;
						}
						/* Ensure that we rest upon the EnclosingChar. */
						while(pCharBuffer[0] != lEnclosingChar) {
							/* Fetch the next character in the sequence. */
							pCharBuffer[0] = (char)pMappedByteBuffer.get();
						}
						/* Fetch the next character in the sequence. */
						pCharBuffer[0] = (char)pMappedByteBuffer.get();
						/* Remove any leading whitespace. */
						while(Character.isWhitespace(pCharBuffer[0]) || pCharBuffer[0] == '=') {
							/* Fetch the next character in the sequence. */
							pCharBuffer[0] = (char)pMappedByteBuffer.get();
						}
					}
					/* Determine whether this tag is a parent or not. */
					if(pCharBuffer[0] != '/') {
						/* Parse subsequent tags. */
						SVGDecoder.onFetchXML(pMappedByteBuffer, pFloatStore, pVectorPathContext, pGLScreenParameters, pCharBuffer, pCharStore, pVectorPaths);
					}
				}
			}
		}
	}
	
	private static final VectorPath onParseSVGPath(final MappedByteBuffer pMappedByteBuffer, final char[] pCharBuffer, final char pEnclosingChar, final ArrayStore.Char pCharStore, final ArrayStore.Float pFloatStore, final VectorPathContext pVectorPathContext) throws SVGException {
		/* Ensure the SVGDecoder rests on the EnclosingChar. */
		do {
			/* Latch onto the instruction character. */
			      char    lInstruction           = pCharBuffer[0];
			/* Determine if the character indicates a relative movement. */
			final boolean lIsRelativeCoordinates = Character.isLowerCase(lInstruction);
			/* Allocate variables for tracking relative transitions. */
			      float   lRelativeX             = 0.0f;
			      float   lRelativeY             = 0.0f;
			/* Handle the instruction. */
			if(Character.isLetter(lInstruction)) {
				/* Check if the Character is a special case. */
				switch(pCharBuffer[0]) {
					case SVGGlobal.SVG_PATH_MOVETO_ABSOLUTE :
					case SVGGlobal.SVG_PATH_MOVETO_RELATIVE : 
						/* Update the RelativeX and RelativeY. */
						lRelativeX = lIsRelativeCoordinates ? pVectorPathContext.getCurrentX() : 0.0f;
						lRelativeY = lIsRelativeCoordinates ? pVectorPathContext.getCurrentY() : 0.0f;
						/* Move to the defined position. */
						pVectorPathContext.onMoveTo(pFloatStore, lRelativeX + SVGDecoder.onParseXMLFloat(pMappedByteBuffer, pCharBuffer, pCharStore), lRelativeY + SVGDecoder.onParseXMLFloat(pMappedByteBuffer, pCharBuffer, pCharStore));
					break;
				}
				/* Determine if further path components are specified by the MOVE_TO instruction. */
				if(pCharBuffer[0] != pEnclosingChar && !Character.isLetter(pCharBuffer[0])) {
					/* We will treat further components as a LINE_TO segment. */
					lInstruction = lIsRelativeCoordinates ? SVGGlobal.SVG_PATH_LINETO_RELATIVE : SVGGlobal.SVG_PATH_LINETO_ABSOLUTE;
				}
				/* Handle the path data. */
				do {
					/* Update the RelativeX and RelativeY. */
					lRelativeX = lIsRelativeCoordinates ? pVectorPathContext.getCurrentX() : 0.0f;
					lRelativeY = lIsRelativeCoordinates ? pVectorPathContext.getCurrentY() : 0.0f;
					/* Handle the instruction. */
					switch(lInstruction) {
						case SVGGlobal.SVG_PATH_MOVETO_ABSOLUTE    : 
						case SVGGlobal.SVG_PATH_MOVETO_RELATIVE    : 
							/* These instructions have already been handled. */
						break;
						case SVGGlobal.SVG_PATH_LINETO_ABSOLUTE    : 
						case SVGGlobal.SVG_PATH_LINETO_RELATIVE    : 
							pVectorPathContext.onLineTo(pFloatStore,  lRelativeX + SVGDecoder.onParseXMLFloat(pMappedByteBuffer, pCharBuffer, pCharStore), lRelativeY + SVGDecoder.onParseXMLFloat(pMappedByteBuffer, pCharBuffer, pCharStore));
						break;
						case SVGGlobal.SVG_PATH_CUBICTO_ABSOLUTE : 
						case SVGGlobal.SVG_PATH_CUBICTO_RELATIVE : 
							pVectorPathContext.onCubicTo(pFloatStore, lRelativeX + SVGDecoder.onParseXMLFloat(pMappedByteBuffer, pCharBuffer, pCharStore), lRelativeY +  SVGDecoder.onParseXMLFloat(pMappedByteBuffer, pCharBuffer, pCharStore), lRelativeX + SVGDecoder.onParseXMLFloat(pMappedByteBuffer, pCharBuffer, pCharStore), lRelativeY + SVGDecoder.onParseXMLFloat(pMappedByteBuffer, pCharBuffer, pCharStore), lRelativeX + SVGDecoder.onParseXMLFloat(pMappedByteBuffer, pCharBuffer, pCharStore), lRelativeY + SVGDecoder.onParseXMLFloat(pMappedByteBuffer, pCharBuffer, pCharStore));
						break;
						case SVGGlobal.SVG_PATH_HORIZONTAL_ABSOLUTE : 
						case SVGGlobal.SVG_PATH_HORIZONTAL_RELATIVE : 
							pVectorPathContext.onLineTo(pFloatStore,  lRelativeX + SVGDecoder.onParseXMLFloat(pMappedByteBuffer, pCharBuffer, pCharStore), pVectorPathContext.getCurrentY());
						break;
						case SVGGlobal.SVG_PATH_VERTICAL_ABSOLUTE   : 
						case SVGGlobal.SVG_PATH_VERTICAL_RELATIVE   : 
							pVectorPathContext.onLineTo(pFloatStore, pVectorPathContext.getCurrentX(), lRelativeY + SVGDecoder.onParseXMLFloat(pMappedByteBuffer, pCharBuffer, pCharStore));
						break;
						case SVGGlobal.SVG_PATH_CUBICTO_SMOOTH_ABSOLUTE     : 
						case SVGGlobal.SVG_PATH_CUBICTO_SMOOTH_RELATIVE     : 
							System.out.println("Implement me! " + lInstruction);
							SVGDecoder.onParseXMLFloat(pMappedByteBuffer, pCharBuffer, pCharStore);
							SVGDecoder.onParseXMLFloat(pMappedByteBuffer, pCharBuffer, pCharStore);
							SVGDecoder.onParseXMLFloat(pMappedByteBuffer, pCharBuffer, pCharStore);
							SVGDecoder.onParseXMLFloat(pMappedByteBuffer, pCharBuffer, pCharStore);
						break;
						case SVGGlobal.SVG_PATH_QUADTO_SMOOTH_ABSOLUTE     : 
						case SVGGlobal.SVG_PATH_QUADTO_SMOOTH_RELATIVE     : 
							System.out.println("Implement me! " + lInstruction);
							SVGDecoder.onParseXMLFloat(pMappedByteBuffer, pCharBuffer, pCharStore);
							SVGDecoder.onParseXMLFloat(pMappedByteBuffer, pCharBuffer, pCharStore);
						break;
						case SVGGlobal.SVG_PATH_ELLIPTICAL_ABSOLUTE : 
						case SVGGlobal.SVG_PATH_ELLIPTICAL_RELATIVE : 
							System.out.println("Implement me! " + lInstruction);
							SVGDecoder.onParseXMLFloat(pMappedByteBuffer, pCharBuffer, pCharStore);
							SVGDecoder.onParseXMLFloat(pMappedByteBuffer, pCharBuffer, pCharStore);
							SVGDecoder.onParseXMLFloat(pMappedByteBuffer, pCharBuffer, pCharStore);
							SVGDecoder.onParseXMLFloat(pMappedByteBuffer, pCharBuffer, pCharStore);
							SVGDecoder.onParseXMLFloat(pMappedByteBuffer, pCharBuffer, pCharStore);
							SVGDecoder.onParseXMLFloat(pMappedByteBuffer, pCharBuffer, pCharStore);
							SVGDecoder.onParseXMLFloat(pMappedByteBuffer, pCharBuffer, pCharStore);
						break;
						case SVGGlobal.SVG_PATH_QUADTO_ABSOLUTE : 
						case SVGGlobal.SVG_PATH_QUADTO_RELATIVE : 
							System.out.println("Implement me! " + lInstruction);
							SVGDecoder.onParseXMLFloat(pMappedByteBuffer, pCharBuffer, pCharStore);
							SVGDecoder.onParseXMLFloat(pMappedByteBuffer, pCharBuffer, pCharStore);
							SVGDecoder.onParseXMLFloat(pMappedByteBuffer, pCharBuffer, pCharStore);
							SVGDecoder.onParseXMLFloat(pMappedByteBuffer, pCharBuffer, pCharStore);
						break;
						case SVGGlobal.SVG_PATH_CLOSE_ABSOLUTE      : 
						case SVGGlobal.SVG_PATH_CLOSE_RELATIVE      : 
							/* Close the path. */
							pVectorPathContext.onClosePath(pFloatStore);
							/* Read the next character in the sequence. (Force an offset past this parameter.) */
							pCharBuffer[0] = (char)pMappedByteBuffer.get();
						break;
						default                                  :
							/* Path command not supported! (Arcs etc.) */
							throw new SVGException("Path instruction not supported! ("+lInstruction+")");
					}
					/* Whilst there are cubic curve components still present. */
				} while(pCharBuffer[0] != pEnclosingChar && !Character.isLetter(pCharBuffer[0]));
			}
			else {
				/* Attempt to recover parsing by handling subsequent characters. */
				pCharBuffer[0] = (char)pMappedByteBuffer.get();
			}
			/* Until the whole path has been processed. */
		} while(pCharBuffer[0] != pEnclosingChar);
		/* Return the created path. */
		return pVectorPathContext.onCreatePath(pFloatStore);
	}
	
	private static final String onParseLetters(final MappedByteBuffer pMappedByteBuffer, final char[] pCharBuffer, final ArrayStore.Char pCharStore) {
		/* Ensure we're handling letter-style characters.*/
		while(Character.isLetter(pCharBuffer[0])) {
			/* Buffer the character. */
			pCharStore.store(pCharBuffer);
			/* Fetch the next character in the sequence. */
			pCharBuffer[0] = (char)pMappedByteBuffer.get();
		}
		/* Return the fetched String. */
		return new String(pCharStore.onProduceArray());
	}
	
	private static final float onParseXMLFloat(final MappedByteBuffer pMappedByteBuffer, final char[] pCharBuffer, final ArrayStore.Char pCharStore) {
		/* Jump to the next point in text which corresponds to the beginning of a number. */
		while((!(Character.isDigit(pCharBuffer[0])) && pCharBuffer[0] != '-')) {
			/* Read the next character. */
			pCharBuffer[0] = (char)pMappedByteBuffer.get();
		}
		/* Define a buffer for tracking the last character. */ /** TODO: Better design than this... **/
		char lLastChar = pCharBuffer[0];
		/* Continuously buffer each character. */
		do {
			/* Buffer the character. */
			pCharStore.store(pCharBuffer);
			/* Keep track of the last character. */ /** TODO: Better design than this... **/
			lLastChar = pCharBuffer[0];
			/* Whilst handling floating point style characters. */
		} while(((Character.isDigit((pCharBuffer[0] = (char)pMappedByteBuffer.get())) || (pCharBuffer[0] == '.' || (pCharBuffer[0] == 'e') || (pCharBuffer[0] == '-' && lLastChar == 'e'))))); // if negative and start of number... 
		/* Fetch the float. */
		final float lFloat = Float.parseFloat(new String(pCharStore.onProduceArray()));
		/* Remove any additional whitespace. */
		while(Character.isWhitespace(pCharBuffer[0])) {
			/* Fetch a new character. */
			pCharBuffer[0] = (char)pMappedByteBuffer.get();
		};
		/* Return the parsed float. */
		return lFloat;
	}
	
}
