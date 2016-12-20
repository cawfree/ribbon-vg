package uk.ac.manchester.sisp.ribbon.opengl.program.constants;

import uk.ac.manchester.sisp.ribbon.opengl.IGLES20;
import uk.ac.manchester.sisp.ribbon.opengl.IScreenParameters;
import uk.ac.manchester.sisp.ribbon.opengl.buffers.GLBuffer;
import uk.ac.manchester.sisp.ribbon.opengl.matrix.IGLMatrixSource;
import uk.ac.manchester.sisp.ribbon.opengl.program.GLProgram;
import uk.ac.manchester.sisp.ribbon.opengl.program.IGLUniformProvider;
import uk.ac.manchester.sisp.ribbon.opengl.shaders.GLShader;
import uk.ac.manchester.sisp.ribbon.utils.DataUtils;

/** A polarized OpenGL-ES implementation of Loop and Blinn's efficient signed distance field Bezier fragment shader. **/
public final class GLVectorProgram extends GLProgram implements IGLUniformProvider.WorldMatrix, IGLUniformProvider.Scale, IGLUniformProvider.Color, IGLUniformProvider.Opacity, IGLUniformProvider.Resolution, GLBuffer.IXY_UV {
	
	private static final String SOURCE_VERTEX_SHADER   = 
				
		"\n"+"attribute    vec2  aPosition;"+
		"\n"+"varying      vec4  vPosition;"+
		"\n"+"attribute    vec2  aBezier;"  +
		"\n"+"varying      vec2  vBezier;"  +
		"\n"+"varying      vec4  vColor;"   +

		"\n"+"uniform   int uPixelPerfect;"     +
		"\n"+"uniform   vec2 uResolution;"       +
		"\n"+"uniform  float uXScale;"           +
		"\n"+"uniform  float uYScale;"           +
		"\n"+"uniform  vec4  uColor;"            +
		"\n"+"uniform  float uShadeMix;"         +
		"\n"+"uniform  vec4  uShadeColor;"       +
		"\n"+"uniform  float uOpacity;"          +
		"\n"+"uniform  mat4  uModelMatrix;"      +
		"\n"+"uniform  mat4  uViewMatrix;"       +
		"\n"+"uniform  mat4  uProjectionMatrix;" +
		
		"\n"+"void main(void) { " +
		"\n"+"\t"+"vBezier        = aBezier;" +
		"\n"+"\t"+"vPosition      = (uProjectionMatrix * uViewMatrix * uModelMatrix) * vec4((uXScale * aPosition.x), (uYScale * aPosition.y), 0.0f, 1.0f);" +
		"\n"+"\t"+"gl_Position    = vPosition;" + 
		
//		///* Determine whether we're processing a Control Point. */
//		"\n"+"\t"+"bool lIsControl = ((aBezier.x == 0.5f) || (aBezier.x == -0.5f));" + 
//
//
//		
//		/* When we're dealing with 'hard' vertices. */
//	//	"\n"+"\t"+"if(!lIsControl) {" +
//			/* Offset from Screen Space. */
//			"\n"+"\t"+"\t"+"gl_Position.x += 1.0f; gl_Position.y += 1.0f;" +
//			/* Calculate Jump Metrics. */
//			"\n"+"\t"+"\t"+"float dW       = (2.0f / uResolution.x);" +
//			"\n"+"\t"+"\t"+"float ddW      = (dW / 2.0f);" +
//			"\n"+"\t"+"\t"+"float dH       = (2.0f / uResolution.y);" +
//			"\n"+"\t"+"\t"+"float ddH      = (dH / 2.0f);" +
//			/* Determine the Pixel difference. */
//			"\n"+"\t"+"\t"+"float dPx      = mod(gl_Position.x, dW);" +
//			"\n"+"\t"+"\t"+"float dPy      = mod(gl_Position.y, dH);" +
//			/* Compensate the Pixel Difference. */
//			//"\n"+"\t"+"\t"+"gl_Position.x += ((dPx == 0.0f) ? (0.0f) : (dPx < ddW ? -dPx : (dW - dPx)));" +
//			//"\n"+"\t"+"\t"+"gl_Position.y -=  (dPy == 0.0f) ? (0.0f) : ((dPy >= ddH) ? (dH - dPy) : (-dPy));" +
//			/* Revert to Screen Space. */
//			"\n"+"\t"+"\t"+"gl_Position.x -= 1.0f; gl_Position.y -= 1.0f;" +

			
	//	"\n"+"\t"+"}" +


		
		
		
//		/* Withdraw the Pixel Difference. */
//		"\n"+"\t"+"\t"+"gl_Position.x -= dPx;" +
		
		
		//"\n"+"\t"+"\t"+"gl_Position.y -= dH;" + /** TODO: Minus! **/
		
		"\n"+"}";
	
	// (float(mod((gl_Position.x + lW2), lDeltaW))) * lDeltaW;
	
	private static final String SOURCE_FRAGMENT_SHADER = /** TODO: Fragment Precision. (Important for mobile devices, crashes @desktop etc). **/
		
		"\n"+"varying   vec2 vBezier;"     +
		"\n"+"varying   vec4 vPosition;"   +
		"\n"+"uniform   vec4 uColor;"      +
		"\n"+"uniform  float uShadeMix;"   +
		"\n"+"uniform   vec4 uShadeColor;" +
		"\n"+"uniform  float uOpacity;"    +
		"\n"+"uniform   vec2 uResolution;" +
		"\n"+"uniform   int  uPixelPerfect;"     +
		
		"\n"+"void main() { " +
		"\n"+"\t"+"vec4  lReturnColor = mix(uColor, uShadeColor, uShadeMix);" + 
		"\n"+"\t"+"vec2  px           = dFdx(vBezier);" +
		"\n"+"\t"+"vec2  py           = dFdy(vBezier);" +
		"\n"+"\t"+"float lIsCurve     = float(vBezier.x != 0.0f || vBezier.y != 0.0f);" +
		"\n"+"\t"+"float fx           = lIsCurve * ((2.0f * vBezier.x) * px.x - px.y);" +
		"\n"+"\t"+"float fy           = lIsCurve * ((2.0f * vBezier.y) * py.x - py.y);" +
		"\n"+"\t"+"float sd           = lIsCurve * (((((vBezier.x * vBezier.x - vBezier.y) * inversesqrt(fx * fx + fy * fy)))));" + /** TODO: Using inversesqrt... May be more efficient? **/
		"\n"+"\t"+"lReturnColor.a    *= ((1.0f - lIsCurve) + (clamp((((float((vBezier.x <= 0.0f)))*((sd) + 0.5f) + (float((vBezier.x > 0.0f)))*((0.5f) - (sd)))), 0.0f, 1.0f))) * uOpacity;"+
		"\n"+"\t"+"gl_FragColor       = lReturnColor;"+
		//"\n"+"\t"+"gl_FragColor       = vec4(1.0f,0.0f,0.0f,1.0f);"+
		"\n"+"}";
	
	/* Shader Attributes. */
	private int mAttributePosition;
	private int mAttributeBezier;
	
	/* Shader Uniforms. */
	private int mUniformResolution;
	private int mUniformModelMatrix;
	private int mUniformViewMatrix;
	private int mUniformProjectionMatrix;
	private int mUniformXScale;
	private int mUniformYScale;
	private int mUniformColor;
	private int mUniformShadeMix;
	private int mUniformShadeColor;
	private int mUniformOpacity;
	private int mUniformPixelPerfect;

	public GLVectorProgram() {
		super(new GLShader.Vertex(GLVectorProgram.SOURCE_VERTEX_SHADER), new GLShader.Fragment(GLVectorProgram.SOURCE_FRAGMENT_SHADER));
	}
	
	@Override
	protected final void onLoaded(final IGLES20 pGLES20) {
		super.onLoaded(pGLES20);
		/* Initialize Shader Attributes. */
		this.mAttributePosition  = this.getAttributeLocation(pGLES20, "aPosition");
		this.mAttributeBezier    = this.getAttributeLocation(pGLES20, "aBezier");
		/* Initialize Shader Uniforms. */
		this.mUniformResolution       = this.getUniformLocation(pGLES20, "uResolution");
		this.mUniformModelMatrix      = this.getUniformLocation(pGLES20, "uModelMatrix");
		this.mUniformViewMatrix       = this.getUniformLocation(pGLES20, "uViewMatrix");
		this.mUniformProjectionMatrix = this.getUniformLocation(pGLES20, "uProjectionMatrix");
		this.mUniformXScale           = this.getUniformLocation(pGLES20, "uXScale");
		this.mUniformYScale           = this.getUniformLocation(pGLES20, "uYScale");
		this.mUniformColor            = this.getUniformLocation(pGLES20, "uColor");
		this.mUniformShadeMix         = this.getUniformLocation(pGLES20, "uShadeMix");
		this.mUniformShadeColor       = this.getUniformLocation(pGLES20, "uShadeColor");
		this.mUniformOpacity          = this.getUniformLocation(pGLES20, "uOpacity");
		this.mUniformPixelPerfect     = this.getUniformLocation(pGLES20, "uPixelPerfect");
	}

	@Override
	public final void onSupplyModelMatrix(final IGLES20 pGLES20,final IGLMatrixSource pGLMatrixSource) {
		pGLES20.glUniformMatrix4fv(this.getUniformModelMatrix(), 1, false, pGLMatrixSource.getModelMatrix(), 0);
	}

	@Override
	public final void onSupplyViewMatrix(final IGLES20 pGLES20, final IGLMatrixSource pGLMatrixSource) {
		pGLES20.glUniformMatrix4fv(this.getUniformViewMatrix(), 1, false, pGLMatrixSource.getViewMatrix(), 0);
	}

	@Override
	public final void onSupplyProjectionMatrix(final IGLES20 pGLES20, final IGLMatrixSource pGLMatrixSource) {
		pGLES20.glUniformMatrix4fv(this.getUniformProjectionMatrix(), 1, false, pGLMatrixSource.getProjectionMatrix(), 0);
	}

	@Override
	public final void onSupplyScale(final IGLES20 pGLES20, final float ... pScale) {
		pGLES20.glUniform1f(this.getUniformXScale(), pScale[0]);
		pGLES20.glUniform1f(this.getUniformYScale(), pScale[1]);
	}

	@Override
	public final void onSupplyColor(final IGLES20 pGLES20, final float[] pColorRGBA) {
		pGLES20.glUniform4fv(this.getUniformColor(), 1, pColorRGBA, 0);
	}

	@Override
	public final void onSupplyOpacity(final IGLES20 pGLES20, final float pOpacity) {
		pGLES20.glUniform1f(this.getUniformOpacity(), pOpacity);
	}

	@Override
	public final void onSupplyResolution(final IGLES20 pGLES20, final IScreenParameters pGLScreenParameters) {
		pGLES20.glUniform2f(this.getUniformResolution(), pGLScreenParameters.getScreenWidth(), pGLScreenParameters.getScreenHeight());
	}
	
	/** TODO: Interface. **/
	public final void onSupplyShadeColor(final IGLES20 pGLES20, final float[] pColorRGBA) {
		pGLES20.glUniform4fv(this.getUniformShadeColor(), 1, pColorRGBA, 0);
	}
	
	/** TODO: Interface. **/
	public final void onSupplyShadeMix(final IGLES20 pGLES20, final float pMix) {
		pGLES20.glUniform1f(this.getUniformShadeMix(), pMix);
	}
	
	/** TODO: Interface **/
	public final void onSupplyPixelPerfect(final IGLES20 pGLES20, final boolean pIsPixelPerfect) {
		pGLES20.glUniform1i(this.getUniformPixelPerfect(), DataUtils.booleanToInt(pIsPixelPerfect));
	}
	
	@Override
	protected final void onDispose() {
		super.onDispose();
		/* Destroy the dedicated shaders. */
		this.getVertexShader().dispose();
		this.getFragmentShader().dispose();
	}

	@Override
	public final int getAttributePosition() {
		return this.mAttributePosition;
	}

	@Override
	public final int getAttributeProcedural() {
		return this.mAttributeBezier;
	}
	
	private final int getUniformModelMatrix() {
		return this.mUniformModelMatrix;
	}
	
	private final int getUniformViewMatrix() {
		return this.mUniformViewMatrix;
	}
	
	private final int getUniformProjectionMatrix() {
		return this.mUniformProjectionMatrix;
	}
	
	private final int getUniformXScale() {
		return this.mUniformXScale;
	}
	
	private final int getUniformYScale() {
		return this.mUniformYScale;
	}
	
	private final int getUniformColor() {
		return this.mUniformColor;
	}
	
	private final int getUniformShadeMix() {
		return this.mUniformShadeMix;
	}
	
	private final int getUniformShadeColor() {
		return this.mUniformShadeColor;
	}
	
	private final int getUniformOpacity() {
		return this.mUniformOpacity;
	}
	
	private final int getUniformResolution() {
		return this.mUniformResolution;
	}
	
	private final int getUniformPixelPerfect() {
		return this.mUniformPixelPerfect;
	}
	
}