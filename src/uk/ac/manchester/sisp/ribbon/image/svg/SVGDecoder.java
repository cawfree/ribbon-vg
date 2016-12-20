package uk.ac.manchester.sisp.ribbon.image.svg;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uk.ac.manchester.sisp.ribbon.common.IBounds2;
import uk.ac.manchester.sisp.ribbon.image.svg.exception.SVGException;
import uk.ac.manchester.sisp.ribbon.image.svg.global.SVGGlobal;
import uk.ac.manchester.sisp.ribbon.io.ArrayStore;
import uk.ac.manchester.sisp.ribbon.opengl.IScreenParameters;
import uk.ac.manchester.sisp.ribbon.opengl.vector.VectorPath;
import uk.ac.manchester.sisp.ribbon.opengl.vector.VectorPathContext;
import uk.ac.manchester.sisp.ribbon.opengl.vector.global.ELineCap;
import uk.ac.manchester.sisp.ribbon.opengl.vector.global.ELineJoin;
import uk.ac.manchester.sisp.ribbon.opengl.vector.global.IPathDefinition;
import uk.ac.manchester.sisp.ribbon.opengl.vector.global.IVectorPathGroup;
import uk.ac.manchester.sisp.ribbon.opengl.vector.global.VectorGlobal;
import uk.ac.manchester.sisp.ribbon.utils.DataUtils;
import uk.ac.manchester.sisp.ribbon.utils.FileUtils;
import uk.ac.manchester.sisp.ribbon.utils.MathUtils;

/** TODO: Currently only handles fills. **/
/** TODO: Abstract to a core XML implementation. **/
public final class SVGDecoder {
	
	/* Member Variables. */
	private final char[]          mCharBuffer;
	private final ArrayStore.Char mCharStore;
	
	public SVGDecoder() {
		/* Initialize Member Variables. */
		this.mCharBuffer = new char[1];
		this.mCharStore  = new ArrayStore.Char();
	}
	
	public final SVGImage createFromFile(final File pFile, final ArrayStore.Float pFloatStore, final VectorPathContext pVectorPathContext, final IScreenParameters pGLScreenParameters) throws IOException {
		/* Create a RandomAccessFile to parse the PNG image data. */
		final RandomAccessFile lRandomAccessFile = new RandomAccessFile(pFile, FileUtils.RANDOM_ACCESS_FILE_MODE_READ);
		/* Fetch a reference to the RandomAccessFile's total length. */
		final long lFileLength = lRandomAccessFile.length();
		/* Use a MappedByteBuffer to cache the file. */
		final MappedByteBuffer lMappedByteBuffer = lRandomAccessFile.getChannel().map(MapMode.READ_ONLY, 0, lFileLength);
		/* Close the RandomAccessFile. (The Mapping will remain valid until garbage collected.) */
		lRandomAccessFile.close();
		/* Define the PathDefinitions. */
		final List<IVectorPathGroup> lVectorPathGroups = new ArrayList<IVectorPathGroup>();
		/* Parse the XML */
		SVGDecoder.onFetchXML(lMappedByteBuffer, pFloatStore, pVectorPathContext, pGLScreenParameters, this.getCharBuffer(), this.getCharStore(), lVectorPathGroups, new IPathDefinition.Fill(new float[]{ 0.0f, 0.0f, 0.0f, 1.0f })); 
		/* Calculate the bounds of the image. */
		final IBounds2.F.W lImageBounds = new IBounds2.F.Impl();
		/* Iterate across each VectorPathGroup. */
		for(int i = 0; i < lVectorPathGroups.size(); i++) {
			/* Fetch the VectorPathGroup. */
			final IVectorPathGroup lVectorPathGroup = lVectorPathGroups.get(i);
			/* Iterate through each VectorPath. */
			for(int j = 0; j < lVectorPathGroup.getVectorPaths().length; j++) {
				/* Encapsulate the bounds of the current VectorPath. */
				MathUtils.onEncapsulateBounds(lImageBounds, VectorGlobal.onCalculateBounds(new IBounds2.F.Impl(), lVectorPathGroup.getVectorPaths()[j]));
			}
		}
		/* Return the created image. encapsulating the VectorPathGroups as a primitive Array. */
		return new SVGImage(lVectorPathGroups.toArray(new IVectorPathGroup[lVectorPathGroups.size()]), MathUtils.onCalculateWidth(lImageBounds), MathUtils.onCalculateHeight(lImageBounds));
	}
	
	private static final void onFetchXML(final MappedByteBuffer pMappedByteBuffer, final ArrayStore.Float pFloatStore, final VectorPathContext pVectorPathContext, final IScreenParameters pGLScreenParameters, final char[] pCharBuffer, final ArrayStore.Char pCharStore, final List<IVectorPathGroup> pVectorPathGroups, IPathDefinition.Fill pCurrentFill) throws SVGException {
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
					/* We're dealing with an unsupported type. Skip this tag. */
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
				/* Initialize the VectorPath storage. */
				final List<VectorPath> lVectorPaths = new ArrayList<VectorPath>();
				/* Grab the EnclosingTag. */
				SVGDecoder.onParseLetters(pMappedByteBuffer, pCharBuffer, pCharStore);
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
								/* Re-initialize the CurrentFill and CurrentStroke. */
								pCurrentFill   = new IPathDefinition.Fill(new float[]{0.0f, 0.0f, 0.0f, 1.0f});
								/* Parse the style. */
								SVGDecoder.onParseSVGStyle(pMappedByteBuffer, pCharBuffer, lEnclosingChar, pCharStore, pCurrentFill);
							break;
							case SVGGlobal.SVG_XML_ATTRIBUTE_PATH  :
								/* Parse the path. */
								lVectorPaths.add(SVGDecoder.onParseSVGPath(pMappedByteBuffer, pCharBuffer, lEnclosingChar, pCharStore, pFloatStore, pVectorPathContext));
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
						/* Parse subsequent tags using our recursive search algorithm. */
						SVGDecoder.onFetchXML(pMappedByteBuffer, pFloatStore, pVectorPathContext, pGLScreenParameters, pCharBuffer, pCharStore, pVectorPathGroups, pCurrentFill);
					}
					/* Determine if there are any paths that we need to buffer. */
					if(!lVectorPaths.isEmpty()) {
						/* Buffer into the VectorPathGroup. */
						pVectorPathGroups.add(new IVectorPathGroup.Impl(lVectorPaths.toArray(new VectorPath[lVectorPaths.size()]), new IPathDefinition[]{ pCurrentFill }));
					}
				}
			}
		}
	}

	private static final void onParseSVGStyle(final MappedByteBuffer pMappedByteBuffer, final char[] pCharBuffer, final char pEnclosingChar, final ArrayStore.Char pCharStore, final IPathDefinition.Fill pCurrentFill) throws SVGException {
		/* The SVGDecoder rests on the starting character. */
		do {
			/* Check we're resting on a letter. */
			while(!Character.isLetter(pCharBuffer[0])) {
				/* Fetch the next character in the sequence. */
				pCharBuffer[0] = (char)pMappedByteBuffer.get();
			}
			/* Fetch the style. */
			do {
				/* Buffer the character. */
				pCharStore.store(pCharBuffer);
				/* Fetch the next character in the sequence. */
				pCharBuffer[0] = (char)pMappedByteBuffer.get();
				/* Whilst handling tag-style characters. */
			} while(Character.isLetter(pCharBuffer[0]) || pCharBuffer[0] == '-');
			/* Store the style to a local reference. */
			final String lStyle = new String(pCharStore.onProduceArray());
			/* Jump to the assignment. */
			while(pCharBuffer[0] != ':') {
				/* Fetch the next character in the sequence. */
				pCharBuffer[0] = (char)pMappedByteBuffer.get();
			}
			/* Process the style. */
			switch(lStyle) {
				case SVGGlobal.SVG_STYLE_TOKEN_OPACITY          : 
					/* Assign a global opacity. */
					//pCurrentStroke.getColor()[3] = pCurrentFill.getColor()[3] ...
					pCurrentFill.getColor()[3] = SVGDecoder.onParseXMLFloat(pMappedByteBuffer, pCharBuffer, pCharStore);
				break;
				case SVGGlobal.SVG_STYLE_TOKEN_FILL_OPACITY     : 
					pCurrentFill.getColor()[3] = SVGDecoder.onParseXMLFloat(pMappedByteBuffer, pCharBuffer, pCharStore);
				break;
				case SVGGlobal.SVG_STYLE_TOKEN_STROKE_OPACITY   : 
					//pCurrentStroke.getColor()[3] = 
					SVGDecoder.onParseXMLFloat(pMappedByteBuffer, pCharBuffer, pCharStore);
				break;
				case SVGGlobal.SVG_STYLE_TOKEN_STROKE_WIDTH     : 
					//pCurrentStroke.setStrokeWidth(
					SVGDecoder.onParseXMLFloat(pMappedByteBuffer, pCharBuffer, pCharStore);
				break;
				case SVGGlobal.SVG_STYLE_TOKEN_FILL             : 
					SVGDecoder.onParseSVGColor(pCurrentFill.getColor(), pMappedByteBuffer, pCharBuffer, pCharStore);
				break;
				case SVGGlobal.SVG_STYLE_TOKEN_STROKE           : 
					//SVGDecoder.onParseSVGColor(pCurrentStroke.getColor(), pMappedByteBuffer, pCharBuffer, pCharStore);
					SVGDecoder.onParseSVGColor(new float[]{0.0f, 0.0f, 0.0f, 0.0f }, pMappedByteBuffer, pCharBuffer, pCharStore);
				break;
				case SVGGlobal.SVG_STYLE_TOKEN_STROKE_LINE_CAP  : 
					// pCurrentStroke.setLineCap(SVGDecoder.onParseSVGLineCap(pMappedByteBuffer, pCharBuffer, pCharStore));
					SVGDecoder.onParseSVGLineCap(pMappedByteBuffer, pCharBuffer, pCharStore);
				break;
				case SVGGlobal.SVG_STYLE_TOKEN_STROKE_LINE_JOIN : 
					//pCurrentStroke.setLineJoin(SVGDecoder.onParseSVGLineJoin(pMappedByteBuffer, pCharBuffer, pCharStore));
					SVGDecoder.onParseSVGLineJoin(pMappedByteBuffer, pCharBuffer, pCharStore);
				break;
				default                                         : 
					throw new SVGException("Style not supported! "+"("+lStyle+")");
			}
			/* Ensure we rest on the assignment before continuing. */
			while((pCharBuffer[0] != ';' && pCharBuffer[0] != pEnclosingChar)) {
				/* Fetch the next character in the sequence. */
				pCharBuffer[0] = (char)pMappedByteBuffer.get();
			}
			/* Determine if we rest upon an end sequence. */
			while((pCharBuffer[0] != pEnclosingChar && (pCharBuffer[0] == ';' || Character.isWhitespace(pCharBuffer[0])))) {
				/* Fetch the next character in the sequence. */
				pCharBuffer[0] = (char)pMappedByteBuffer.get();
			}
			/* Until the whole path has been processed. */
		} while(pCharBuffer[0] != pEnclosingChar);
	}
	
	private static final void onParseSVGColor(final float[] pColor, final MappedByteBuffer pMappedByteBuffer, final char[] pCharBuffer, final ArrayStore.Char pCharStore) throws SVGException {
		/* Perform an initial offset from the assignment. */
		pCharBuffer[0] = (char)pMappedByteBuffer.get();
		/* Ensure we lie upon a non-whitespace character. */
		while(Character.isWhitespace(pCharBuffer[0])) {
			/* Fetch the next character in the sequence. */
			pCharBuffer[0] = (char)pMappedByteBuffer.get();
		}
		/* Ascertain the initial colour text String. */
		final String lColorText = SVGDecoder.onParseLetters(pMappedByteBuffer, pCharBuffer, pCharStore);
		/* Process the ColorText.  */
		if(lColorText.isEmpty()) {
			/* Iterate through the sequence of hexadecimal characters. */
			for(int i = 0; i < 6; i++) {
				/* Fetch the next character in the sequence. */
				pCharBuffer[0] = (char)pMappedByteBuffer.get();
				/* Buffer the character. */
				pCharStore.store(pCharBuffer);
			}
			/* Parse the Color. */
			final int lColor   = Integer.parseInt(new String(pCharStore.onProduceArray()), DataUtils.JAVA_BASE_HEXADECIMAL);
			/* Calculate the corresponding floating point values. */
			final float lBlue  = (float)((lColor >>  0) & 0xFF) / 255.0f;
			final float lGreen = (float)((lColor >>  8) & 0xFF) / 255.0f;
			final float lRed   = (float)((lColor >> 16) & 0xFF) / 255.0f;
			/* Buffer the parsed Color into pColor. */
			pColor[0] = lRed;
			pColor[1] = lGreen;
			pColor[2] = lBlue;
			/* Correct alignment by applying an offset to the MappedByteBuffer. */
			pCharBuffer[0] = (char)pMappedByteBuffer.get();
		}
		else {
			/* Process the ColorText. */
			switch(lColorText) {
				case SVGGlobal.SVG_NONE : 
					/* Assign an empty colour to the return. */
					Arrays.fill(pColor, 0.0f);
				break;
				default : 
					throw new SVGException("Color not supported! ("+lColorText+")");
			}
		}
	}
	
	private static final ELineJoin onParseSVGLineJoin(final MappedByteBuffer pMappedByteBuffer, final char[] pCharBuffer, final ArrayStore.Char pCharStore) throws SVGException {
		/* Ensure we rest upon a letter. */
		while(!Character.isLetter(pCharBuffer[0])) {
			/* Fetch the next character in the sequence. */
			pCharBuffer[0] = (char)pMappedByteBuffer.get();
		}
		/* Parse the LineJoin text. */
		final String lLineJoin = SVGDecoder.onParseLetters(pMappedByteBuffer, pCharBuffer, pCharStore);
		/* Return the corresponding LineJoin. */
		switch(lLineJoin) {
			case SVGGlobal.SVG_TOKEN_LINE_JOIN_MITER : return ELineJoin.MITER;
			case SVGGlobal.SVG_TOKEN_LINE_JOIN_BEVEL : return ELineJoin.BEVEL;
			case SVGGlobal.SVG_TOKEN_LINE_JOIN_ROUND : return ELineJoin.ROUND;
			default                                  :
				throw new SVGException("Unsupported LineJoin!"+"("+lLineJoin+")");
		}
	}
	
	private static final ELineCap onParseSVGLineCap(final MappedByteBuffer pMappedByteBuffer, final char[] pCharBuffer, final ArrayStore.Char pCharStore) throws SVGException {
		/* Ensure we rest upon a letter. */
		while(!Character.isLetter(pCharBuffer[0])) {
			/* Fetch the next character in the sequence. */
			pCharBuffer[0] = (char)pMappedByteBuffer.get();
		}
		/* Parse the LineCap text. */
		final String lLineCap = SVGDecoder.onParseLetters(pMappedByteBuffer, pCharBuffer, pCharStore);
		/* Return the corresponding LineJoin. */
		switch(lLineCap) {
			case SVGGlobal.SVG_TOKEN_LINE_CAP_BUTT   : return ELineCap.BUTT;
			case SVGGlobal.SVG_TOKEN_LINE_CAP_ROUND  : return ELineCap.ROUND;
			case SVGGlobal.SVG_TOKEN_LINE_CAP_SQUARE : return ELineCap.SQUARE;
			default                                  :
				throw new SVGException("Unsupported LineCap!"+"("+lLineCap+")");
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
	
	private final char[] getCharBuffer() {
		return this.mCharBuffer;
	}
	
	private final ArrayStore.Char getCharStore() {
		return this.mCharStore;
	}
	
}
