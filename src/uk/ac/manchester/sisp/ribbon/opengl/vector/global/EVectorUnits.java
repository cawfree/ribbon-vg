package uk.ac.manchester.sisp.ribbon.opengl.vector.global;

import uk.ac.manchester.sisp.ribbon.opengl.IGLScreenParameters;

public enum EVectorUnits {
	
	UNSCALED { @Override public final    float onScaleToPixels(final float pValue, final IGLScreenParameters pGLScreenParameters) { return pValue;                                                                } }, 
	PX       { @Override public final    float onScaleToPixels(final float pValue, final IGLScreenParameters pGLScreenParameters) { return EVectorUnits.UNSCALED.onScaleToPixels(pValue, pGLScreenParameters);    } }, 
	PT       { @Override public final    float onScaleToPixels(final float pValue, final IGLScreenParameters pGLScreenParameters) { return (pValue * 0.01388f) * pGLScreenParameters.getDotsPerInch();            } }, 
	PC       { @Override public final    float onScaleToPixels(final float pValue, final IGLScreenParameters pGLScreenParameters) { return (pValue * 0.16666f) * pGLScreenParameters.getDotsPerInch();            } }, 
	M        { @Override public final    float onScaleToPixels(final float pValue, final IGLScreenParameters pGLScreenParameters) { return (pValue * 0.03937f) * pGLScreenParameters.getDotsPerInch();            } }, 
	CM       { @Override public final    float onScaleToPixels(final float pValue, final IGLScreenParameters pGLScreenParameters) { return (pValue * 0.39370f) * pGLScreenParameters.getDotsPerInch();            } }, 
	IN       { @Override public final    float onScaleToPixels(final float pValue, final IGLScreenParameters pGLScreenParameters) { return (pValue)            * pGLScreenParameters.getDotsPerInch();            } };
		
	public abstract float onScaleToPixels(final float pValue, final IGLScreenParameters pGLScreenParameters);
	
}