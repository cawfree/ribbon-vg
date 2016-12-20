package uk.ac.manchester.sisp.ribbon.image.svg.global;

public final class SVGGlobal {
	
	/* SVG Global Definitions. */
	public static final String SVG_NONE			  = "none";         
	
	/* SVG XML Tag Definitions. */
	public static final String SVG_XML_TAG_HEADER = "svg";
	public static final String SVG_XML_TAG_GROUP  = "g";
	public static final String SVG_XML_TAG_PATH   = "path";
	
	/* SVG XML Attribute Definitions. */
	public static final String SVG_XML_ATTRIBUTE_XMLNS       = "xmlns";
	public static final String SVG_XML_ATTRIBUTE_XMLNS_XLINK = SVG_XML_ATTRIBUTE_XMLNS + ":" + "xlink";
	public static final String SVG_XML_ATTRIBUTE_HEIGHT      = "height";
	public static final String SVG_XML_ATTRIBUTE_TRANSFORM   = "transform";
	public static final String SVG_XML_ATTRIBUTE_STYLE       = "style";
	public static final String SVG_XML_ATTRIBUTE_PATH        = "d";
	
	/* SVG Path Commands. */
	public static final char SVG_PATH_MOVETO_ABSOLUTE     = 'M';
	public static final char SVG_PATH_MOVETO_RELATIVE     = 'm';
	public static final char SVG_PATH_LINETO_ABSOLUTE     = 'L';
	public static final char SVG_PATH_LINETO_RELATIVE     = 'l';
	public static final char SVG_PATH_QUADTO_ABSOLUTE     = 'Q';
	public static final char SVG_PATH_CUBICTO_ABSOLUTE    = 'C';
	public static final char SVG_PATH_CUBICTO_RELATIVE    = 'c';
	public static final char SVG_PATH_HORIZONTAL_ABSOLUTE = 'H';
	public static final char SVG_PATH_HORIZONTAL_RELATIVE = 'h';
	public static final char SVG_PATH_VERTICAL_ABSOLUTE   = 'V';
	public static final char SVG_PATH_VERTICAL_RELATIVE   = 'v';
	public static final char SVG_PATH_CLOSE_ABSOLUTE      = 'Z';
	public static final char SVG_PATH_CLOSE_RELATIVE      = 'z';
	
	/* SVG Tokens. */
	public static final String SVG_STYLE_TOKEN_OPACITY          = "opacity";
	public static final String SVG_STYLE_TOKEN_FILL             = "fill";
	public static final String SVG_STYLE_TOKEN_FILL_OPACITY     = "fill-opacity";
	public static final String SVG_STYLE_TOKEN_STROKE           = "stroke";
	public static final String SVG_STYLE_TOKEN_STROKE_WIDTH     = "stroke-width";
	public static final String SVG_STYLE_TOKEN_STROKE_LINE_CAP  = "stroke-linecap";
	public static final String SVG_STYLE_TOKEN_STROKE_LINE_JOIN = "stroke-linejoin";
	public static final String SVG_STYLE_TOKEN_STROKE_OPACITY   = "stroke-opacity";
	
	/* SVG Token Values. */
	public static final String SVG_TOKEN_LINE_CAP_BUTT   = "butt";    
	public static final String SVG_TOKEN_LINE_CAP_ROUND  = "round";    
	public static final String SVG_TOKEN_LINE_CAP_SQUARE = "square";   
	public static final String SVG_TOKEN_LINE_JOIN_MITER = "miter";    
	public static final String SVG_TOKEN_LINE_JOIN_BEVEL = "bevel";    
	public static final String SVG_TOKEN_LINE_JOIN_ROUND = "round";    
	
	/* SVG Units. */
	public static final String SVG_UNIT_UNSCALED   = "";  
	public static final String SVG_UNIT_PIXELS     = "px";

}
