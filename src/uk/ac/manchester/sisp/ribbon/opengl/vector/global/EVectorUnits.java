package uk.ac.manchester.sisp.ribbon.opengl.vector.global;


public enum EVectorUnits {
	
	UNSCALED { @Override public final    float onScaleToPixels(final float pValue, final float pDotsPerInch) { return pValue;                             } }, 
	PX       { @Override public final    float onScaleToPixels(final float pValue, final float pDotsPerInch) { return pDotsPerInch;                       } }, 
	PT       { @Override public final    float onScaleToPixels(final float pValue, final float pDotsPerInch) { return (pValue * 0.01388f) * pDotsPerInch; } }, 
	PC       { @Override public final    float onScaleToPixels(final float pValue, final float pDotsPerInch) { return (pValue * 0.16666f) * pDotsPerInch; } }, 
	M        { @Override public final    float onScaleToPixels(final float pValue, final float pDotsPerInch) { return (pValue * 0.03937f) * pDotsPerInch; } }, 
	CM       { @Override public final    float onScaleToPixels(final float pValue, final float pDotsPerInch) { return (pValue * 0.39370f) * pDotsPerInch; } }, 
	IN       { @Override public final    float onScaleToPixels(final float pValue, final float pDotsPerInch) { return (pValue)            * pDotsPerInch; } };
		
	public abstract float onScaleToPixels(final float pValue, final float pDotsPerInch);
	
}