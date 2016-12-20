package uk.ac.manchester.sisp.punch;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.media.opengl.GL2ES2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

import uk.ac.manchester.sisp.ribbon.opengl.IGLES20;

public class DesktopGLES20 implements GLEventListener, IGLES20 {
	
	/* Hold the transient GL2ES2 context. */
	private volatile GL2ES2 mGL2ES2;
	
	public DesktopGLES20() {
		/* Initialze the GL2ES2 context to null. This will only become available within the GL2ES2 context. */
		this.mGL2ES2 = null;
	}

	@Override
	public void init(final GLAutoDrawable pGLAutoDrawable) {
		/* Update the GL2ES2 context. */
		this.mGL2ES2 = pGLAutoDrawable.getGL().getGL2ES2();
	}

	@Override
	public void reshape(final GLAutoDrawable pGLAutoDrawable, final int pX, final int pY, final int pWidth, final int pHeight) {
		/* Update the GL2ES2 context. */
		this.mGL2ES2 = pGLAutoDrawable.getGL().getGL2ES2();
	}
	
	@Override
	public void display(final GLAutoDrawable pGLAutoDrawable) {
		/* Update the GL2ES2 context. */
		this.mGL2ES2 = pGLAutoDrawable.getGL().getGL2ES2();
	}

	@Override
	public void dispose(final GLAutoDrawable pGLAutoDrawable) {
		/* Update the GL2ES2 context. */
		this.mGL2ES2 = pGLAutoDrawable.getGL().getGL2ES2();
	}
	
	@Override
	public final void glActiveTexture(final int arg0) {
		this.getGL2ES2().glActiveTexture(arg0);
	}

	@Override
	public final void glBindBuffer(final int arg0, final int arg1) {
		this.getGL2ES2().glBindBuffer(arg0, arg1);
	}

	@Override
	public final void glBindFramebuffer(final int arg0, final int arg1) {
		this.getGL2ES2().glBindFramebuffer(arg0, arg1);
	}

	@Override
	public final void glBindRenderbuffer(final int arg0, final int arg1) {
		this.getGL2ES2().glBindRenderbuffer(arg0, arg1);
	}

	@Override
	public final void glBindTexture(final int arg0, final int arg1) {
		this.getGL2ES2().glBindTexture(arg0, arg1);
	}

	@Override
	public final void glBlendEquation(final int arg0) {
		this.getGL2ES2().glBlendEquation(arg0);
	}

	@Override
	public final void glBlendEquationSeparate(final int arg0, final int arg1) {
		this.getGL2ES2().glBlendEquationSeparate(arg0, arg1);
	}

	@Override
	public final void glBlendFunc(final int arg0, final int arg1) {
		this.getGL2ES2().glBlendFunc(arg0, arg1);
	}

	@Override
	public final void glBlendFuncSeparate(final int arg0, final int arg1, final int arg2, final int arg3) {
		this.getGL2ES2().glBlendFuncSeparate(arg0, arg1, arg2, arg3);
	}

	@Override
	public final void glBufferData(final int arg0, final int arg1, final Buffer arg2, final int arg3) {
		this.getGL2ES2().glBufferData(arg0, arg1, arg2, arg3);
	}

	@Override
	public final void glBufferSubData(final int arg0, final int arg1, final int arg2, final Buffer arg3) {
		this.getGL2ES2().glBufferSubData(arg0, arg1, arg2, arg3);
	}

	@Override
	public final int glCheckFramebufferStatus(final int arg0) {
		return this.getGL2ES2().glCheckFramebufferStatus(arg0);
	}

	@Override
	public final void glClear(final int arg0) {
		this.getGL2ES2().glClear(arg0);
	}

	@Override
	public final void glClearColor(final float arg0, final float arg1, final float arg2, final float arg3) {
		this.getGL2ES2().glClearColor(arg0, arg1, arg2, arg3);
	}

	@Override
	public final void glClearDepthf(final float arg0) {
		this.glClearDepthf(arg0);
	}

	@Override
	public final void glClearStencil(final int arg0) {
		this.getGL2ES2().glClearStencil(arg0);
	}

	@Override
	public final void glColorMask(final boolean arg0, final boolean arg1, final boolean arg2, boolean arg3) {
		this.getGL2ES2().glColorMask(arg0, arg1, arg2, arg3);
	}

	@Override
	public final void glCompressedTexImage2D(final int arg0, final int arg1, final int arg2, final int arg3, final int arg4, final int arg5, final int arg6, final Buffer arg7) {
		this.getGL2ES2().glCompressedTexImage2D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
	}

	@Override
	public final void glCompressedTexSubImage2D(final int arg0, final int arg1, final int arg2, final int arg3, final int arg4, final int arg5, final int arg6, final int arg7, final Buffer arg8) {
		this.getGL2ES2().glCompressedTexSubImage2D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
	}

	@Override
	public final void glCopyTexImage2D(final int arg0, final int arg1, final int arg2, final int arg3, final int arg4, final int arg5, final int arg6, final int arg7) {
		this.getGL2ES2().glCopyTexImage2D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
	}

	@Override
	public final void glCopyTexSubImage2D(final int arg0, final int arg1, final int arg2, final int arg3, final int arg4, final int arg5, final int arg6, final int arg7) {
		this.getGL2ES2().glCopyTexSubImage2D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
	}

	@Override
	public final void glCullFace(final int arg0) {
		this.getGL2ES2().glCullFace(arg0);
	}

	@Override
	public final void glDeleteBuffers(final int arg0, final IntBuffer arg1) {
		this.getGL2ES2().glDeleteBuffers(arg0, arg1);
	}

	@Override
	public final void glDeleteBuffers(final int arg0, final int[] arg1, final int arg2) {
		this.getGL2ES2().glDeleteBuffers(arg0, arg1, arg2);
	}

	@Override
	public final void glDeleteFramebuffers(final int arg0, final IntBuffer arg1) {
		this.getGL2ES2().glDeleteFramebuffers(arg0, arg1);
	}

	@Override
	public final void glDeleteFramebuffers(final int arg0, final int[] arg1, final int arg2) {
		this.getGL2ES2().glDeleteFramebuffers(arg0, arg1, arg2);
	}

	@Override
	public final void glDeleteRenderbuffers(final int arg0, final IntBuffer arg1) {
		this.getGL2ES2().glDeleteRenderbuffers(arg0, arg1);
	}

	@Override
	public final void glDeleteRenderbuffers(final int arg0, final int[] arg1, final int arg2) {
		this.getGL2ES2().glDeleteRenderbuffers(arg0, arg1, arg2);
	}

	@Override
	public final void glDeleteTextures(final int arg0, final IntBuffer arg1) {
		this.getGL2ES2().glDeleteTextures(arg0, arg1);
	}

	@Override
	public final void glDeleteTextures(final int arg0, final int[] arg1, final int arg2) {
		this.getGL2ES2().glDeleteTextures(arg0, arg1, arg2);
	}

	@Override
	public final void glDepthFunc(final int arg0) {
		this.getGL2ES2().glDepthFunc(arg0);
	}

	@Override
	public final void glDepthMask(final boolean arg0) {
		this.getGL2ES2().glDepthMask(arg0);
	}

	@Override
	public final void glDepthRangef(final float arg0, final float arg1) {
		this.getGL2ES2().glDepthRangef(arg0, arg1);
	}

	@Override
	public final void glDisable(final int arg0) {
		this.getGL2ES2().glDisable(arg0);
	}

	@Override
	public final void glDrawArrays(final int arg0, final int arg1, final int arg2) {
		this.getGL2ES2().glDrawArrays(arg0, arg1, arg2);
	}

	@Override
	public final void glDrawElements(final int arg0, final int arg1, final int arg2, final int arg3) {
		this.getGL2ES2().glDrawElements(arg0, arg1, arg2, arg3);
	}

	@Override
	public final void glEnable(final int arg0) {
		this.getGL2ES2().glEnable(arg0);
	}

	@Override
	public final void glFinish() {
		this.getGL2ES2().glFinish();
	}

	@Override
	public final void glFlush() {
		this.getGL2ES2().glFlush();
	}

	@Override
	public final void glFramebufferRenderbuffer(final int arg0, final int arg1, final int arg2, final int arg3) {
		this.getGL2ES2().glFramebufferRenderbuffer(arg0, arg1, arg2, arg3);
	}

	@Override
	public final void glFramebufferTexture2D(final int arg0, final int arg1, final int arg2, final int arg3, final int arg4) {
		this.getGL2ES2().glFramebufferTexture2D(arg0, arg1, arg2, arg3, arg4);
	}

	@Override
	public final void glFrontFace(final int arg0) {
		this.getGL2ES2().glFrontFace(arg0);
	}

	@Override
	public final void glGenBuffers(final int arg0, final IntBuffer arg1) {
		this.getGL2ES2().glGenBuffers(arg0, arg1);
	}

	@Override
	public final void glGenBuffers(final int arg0, final int[] arg1, final int arg2) {
		this.getGL2ES2().glGenBuffers(arg0, arg1, arg2);
	}

	@Override
	public final void glGenFramebuffers(final int arg0, final IntBuffer arg1) {
		this.getGL2ES2().glGenFramebuffers(arg0, arg1);
	}

	@Override
	public final void glGenFramebuffers(final int arg0, final int[] arg1, final int arg2) {
		this.getGL2ES2().glGenFramebuffers(arg0, arg1, arg2);
	}

	@Override
	public final void glGenRenderbuffers(final int arg0, final IntBuffer arg1) {
		this.getGL2ES2().glGenBuffers(arg0, arg1);
	}

	@Override
	public final void glGenRenderbuffers(final int arg0, final int[] arg1, final int arg2) {
		this.getGL2ES2().glGenRenderbuffers(arg0, arg1, arg2);
	}

	@Override
	public final void glGenTextures(final int arg0, final IntBuffer arg1) {
		this.getGL2ES2().glGenTextures(arg0, arg1);
	}

	@Override
	public final void glGenTextures(final int arg0, final int[] arg1, final int arg2) {
		this.getGL2ES2().glGenTextures(arg0, arg1, arg2);
	}

	@Override
	public final void glGenerateMipmap(final int arg0) {
		this.getGL2ES2().glGenerateMipmap(arg0);
	}

	@Override
	public final void glGetBufferParameteriv(final int arg0, final int arg1, final IntBuffer arg2) {
		this.getGL2ES2().glGetBufferParameteriv(arg0, arg1, arg2);
	}

	@Override
	public final void glGetBufferParameteriv(final int arg0, final int arg1, final int[] arg2, final int arg3) {
		this.getGL2ES2().glGetBufferParameteriv(arg0, arg1, arg2, arg3);
	}

	@Override
	public final int glGetError() {
		return this.getGL2ES2().glGetError();
	}

	@Override
	public final void glGetFloatv(final int arg0, final FloatBuffer arg1) {
		this.getGL2ES2().glGetFloatv(arg0, arg1);
	}

	@Override
	public final void glGetFloatv(final int arg0, final float[] arg1, final int arg2) {
		this.getGL2ES2().glGetFloatv(arg0, arg1, arg2);
	}

	@Override
	public final void glGetFramebufferAttachmentParameteriv(final int arg0, final int arg1, final int arg2, final IntBuffer arg3) {
		this.getGL2ES2().glGetFramebufferAttachmentParameteriv(arg0, arg1, arg2, arg3);
	}

	@Override
	public final void glGetFramebufferAttachmentParameteriv(final int arg0, final int arg1, final int arg2, final int[] arg3, final int arg4) {
		this.getGL2ES2().glGetFramebufferAttachmentParameteriv(arg0, arg1, arg2, arg3, arg4);
	}

	@Override
	public final void glGetIntegerv(final int arg0, final IntBuffer arg1) {
		this.getGL2ES2().glGetIntegerv(arg0, arg1);
	}

	@Override
	public final void glGetIntegerv(final int arg0, final int[] arg1, final int arg2) {
		this.getGL2ES2().glGetIntegerv(arg0, arg1, arg2);
	}

	@Override
	public final void glGetRenderbufferParameteriv(final int arg0, final int arg1, final IntBuffer arg2) {
		this.getGL2ES2().glGetRenderbufferParameteriv(arg0, arg1, arg2);
	}

	@Override
	public final void glGetRenderbufferParameteriv(final int arg0, final int arg1, final int[] arg2, final int arg3) {
		this.getGL2ES2().glGetRenderbufferParameteriv(arg0, arg1, arg2, arg3);
	}

	@Override
	public final String glGetString(final int arg0) {
		return this.getGL2ES2().glGetString(arg0);
	}

	@Override
	public final void glGetTexParameterfv(final int arg0, final int arg1, final FloatBuffer arg2) {
		this.getGL2ES2().glGetTexParameterfv(arg0, arg1, arg2);
	}

	@Override
	public final void glGetTexParameterfv(final int arg0, final int arg1, final float[] arg2, final int arg3) {
		this.getGL2ES2().glGetTexParameterfv(arg0, arg1, arg2, arg3);
	}

	@Override
	public final void glGetTexParameteriv(final int arg0, final int arg1, final IntBuffer arg2) {
		this.getGL2ES2().glGetTexParameteriv(arg0, arg1, arg2);
	}

	@Override
	public final void glGetTexParameteriv(final int arg0, final int arg1, final int[] arg2, final int arg3) {
		this.getGL2ES2().glGetTexParameteriv(arg0, arg1, arg2, arg3);
	}

	@Override
	public final void glHint(final int arg0, final int arg1) {
		this.getGL2ES2().glHint(arg0, arg1);
	}

	@Override
	public final boolean glIsBuffer(final int arg0) {
		return this.getGL2ES2().glIsBuffer(arg0);
	}

	@Override
	public final boolean glIsEnabled(final int arg0) {
		return this.getGL2ES2().glIsEnabled(arg0);
	}

	@Override
	public final boolean glIsFramebuffer(final int arg0) {
		return this.getGL2ES2().glIsFramebuffer(arg0);
	}

	@Override
	public final boolean glIsRenderbuffer(final int arg0) {
		return this.getGL2ES2().glIsRenderbuffer(arg0);
	}

	@Override
	public final boolean glIsTexture(final int arg0) {
		return this.getGL2ES2().glIsTexture(arg0);
	}

	@Override
	public final void glLineWidth(final float arg0) {
		this.getGL2ES2().glLineWidth(arg0);
	}

	@Override
	public final void glPixelStorei(final int arg0, final int arg1) {
		this.getGL2ES2().glPixelStorei(arg0, arg1);
	}

	@Override
	public final void glPolygonOffset(final float arg0, final float arg1) {
		this.getGL2ES2().glPolygonOffset(arg0, arg1);
	}

	@Override
	public final void glReadPixels(final int arg0, final int arg1, final int arg2, final int arg3, final int arg4, final int arg5, final Buffer arg6) {
		this.getGL2ES2().glReadPixels(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
	}

	@Override
	public final void glRenderbufferStorage(final int arg0, final int arg1, final int arg2, final int arg3) {
		this.getGL2ES2().glRenderbufferStorage(arg0, arg1, arg2, arg3);
	}

	@Override
	public final void glSampleCoverage(final float arg0, final boolean arg1) {
		this.getGL2ES2().glSampleCoverage(arg0, arg1);
	}

	@Override
	public final void glScissor(final int arg0, final int arg1, final int arg2, final int arg3) {
		this.getGL2ES2().glScissor(arg0, arg1, arg2, arg3);
	}

	@Override
	public final void glStencilFunc(final int arg0, final int arg1, final int arg2) {
		this.getGL2ES2().glStencilFunc(arg0, arg1, arg2);
	}

	@Override
	public final void glStencilMask(final int arg0) {
		this.getGL2ES2().glStencilMask(arg0);
	}

	@Override
	public final void glStencilOp(final int arg0, final int arg1, final int arg2) {
		this.getGL2ES2().glStencilOp(arg0, arg1, arg2);
	}

	@Override
	public final void glTexImage2D(final int arg0, final int arg1, final int arg2, final int arg3, final int arg4, final int arg5, final int arg6, final int arg7, final Buffer arg8) {
		this.getGL2ES2().glTexImage2D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
	}

	@Override
	public final void glTexParameterf(final int arg0, final int arg1, final float arg2) {
		this.getGL2ES2().glTexParameterf(arg0, arg1, arg2);
	}

	@Override
	public final void glTexParameterfv(final int arg0, final int arg1, final FloatBuffer arg2) {
		this.getGL2ES2().glTexParameterfv(arg0, arg1, arg2);
	}

	@Override
	public final void glTexParameterfv(final int arg0, final int arg1, final float[] arg2, final int arg3) {
		this.getGL2ES2().glTexParameterfv(arg0, arg1, arg2, arg3);
	}

	@Override
	public final void glTexParameteri(final int arg0, final int arg1, final int arg2) {
		this.getGL2ES2().glTexParameteri(arg0, arg1, arg2);
	}

	@Override
	public final void glTexParameteriv(final int arg0, final int arg1, final IntBuffer arg2) {
		this.getGL2ES2().glTexParameteriv(arg0, arg1, arg2);
	}

	@Override
	public final void glTexParameteriv(final int arg0, final int arg1, final int[] arg2, final int arg3) {
		this.getGL2ES2().glTexParameteriv(arg0, arg1, arg2, arg3);
	}

	@Override
	public final void glTexSubImage2D(final int arg0, final int arg1, final int arg2, final int arg3, final int arg4, final int arg5, final int arg6, final int arg7, final Buffer arg8) {
		this.getGL2ES2().glTexSubImage2D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
	}

	@Override
	public final void glViewport(final int arg0, final int arg1, final int arg2, final int arg3) {
		this.getGL2ES2().glViewport(arg0, arg1, arg2, arg3);
	}

	@Override
	public final void glAttachShader(final int arg0, final int arg1) {
		this.getGL2ES2().glAttachShader(arg0, arg1);
	}

	@Override
	public final void glBindAttribLocation(final int arg0, final int arg1, final String arg2) {
		this.getGL2ES2().glBindAttribLocation(arg0, arg1, arg2);
	}

	@Override
	public final void glBlendColor(final float arg0, final float arg1, final float arg2, final float arg3) {
		this.getGL2ES2().glBlendColor(arg0, arg1, arg2, arg3);
	}

	@Override
	public final void glCompileShader(final int arg0) {
		this.getGL2ES2().glCompileShader(arg0);
	}

	@Override
	public final int glCreateProgram() {
		return this.getGL2ES2().glCreateProgram();
	}

	@Override
	public final int glCreateShader(final int arg0) {
		return this.getGL2ES2().glCreateShader(arg0);
	}

	@Override
	public final void glDeleteProgram(final int arg0) {
		this.getGL2ES2().glDeleteProgram(arg0);
	}

	@Override
	public final void glDeleteShader(final int arg0) {
		this.getGL2ES2().glDeleteShader(arg0);
	}

	@Override
	public final void glDetachShader(final int arg0, final int arg1) {
		this.getGL2ES2().glDetachShader(arg0, arg1);
	}

	@Override
	public final void glDisableVertexAttribArray(final int arg0) {
		this.getGL2ES2().glDisableVertexAttribArray(arg0);
	}

	@Override
	public final void glEnableVertexAttribArray(final int arg0) {
		this.getGL2ES2().glEnableVertexAttribArray(arg0);
	}

	@Override
	public final void glGetActiveAttrib(final int arg0, final int arg1, final int arg2, final int[] arg3, final int arg4, final int[] arg5, final int arg6, final int[] arg7, final int arg8, final byte[] arg9, final int arg10) {
		this.getGL2ES2().glGetActiveAttrib(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
	}

	@Override
	public final void glGetActiveUniform(final int arg0, final int arg1, final int arg2, final int[] arg3, final int arg4, final int[] arg5, final int arg6, final int[] arg7, final int arg8, final byte[] arg9, final int arg10) {
		this.getGL2ES2().glGetActiveUniform(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
	}

	@Override
	public final void glGetAttachedShaders(final int arg0, final int arg1, final IntBuffer arg2, final IntBuffer arg3) {
		this.getGL2ES2().glGetAttachedShaders(arg0, arg1, arg2, arg3);
	}

	@Override
	public final void glGetAttachedShaders(final int arg0, final int arg1, final int[] arg2, final int arg3, final int[] arg4, final int arg5) {
		this.getGL2ES2().glGetAttachedShaders(arg0, arg1, arg2, arg3, arg4, arg5);
	}

	@Override
	public final int glGetAttribLocation(final int arg0, final String arg1) {
		return this.getGL2ES2().glGetAttribLocation(arg0, arg1);
	}

	@Override
	public final void glGetProgramiv(final int arg0, final int arg1, final IntBuffer arg2) {
		this.getGL2ES2().glGetProgramiv(arg0, arg1, arg2);
	}

	@Override
	public final void glGetProgramiv(final int arg0, final int arg1, final int[] arg2, final int arg3) {
		this.getGL2ES2().glGetProgramiv(arg0, arg1, arg2, arg3);
	}

	@Override
	public final void glGetShaderSource(final int arg0, final int arg1, final int[] arg2, final int arg3, final byte[] arg4, final int arg5) {
		this.getGL2ES2().glGetShaderSource(arg0, arg1, arg2, arg3, arg4, arg5);
	}

	@Override
	public final void glGetShaderiv(final int arg0, final int arg1, final IntBuffer arg2) {
		this.getGL2ES2().glGetShaderiv(arg0, arg1, arg2);
	}

	@Override
	public final void glGetShaderiv(final int arg0, final int arg1, final int[] arg2, final int arg3) {
		this.getGL2ES2().glGetShaderiv(arg0, arg1, arg2, arg3);
	}

	@Override
	public final int glGetUniformLocation(final int arg0, final String arg1) {
		return this.getGL2ES2().glGetUniformLocation(arg0, arg1);
	}

	@Override
	public final void glGetUniformfv(final int arg0, final int arg1, final FloatBuffer arg2) {
		this.getGL2ES2().glGetUniformfv(arg0, arg1, arg2);
	}

	@Override
	public final void glGetUniformfv(final int arg0, final int arg1, final float[] arg2, final int arg3) {
		this.getGL2ES2().glGetUniformfv(arg0, arg1, arg2, arg3);
	}

	@Override
	public final void glGetUniformiv(final int arg0, final int arg1, final IntBuffer arg2) {
		this.getGL2ES2().glGetUniformiv(arg0, arg1, arg2);
	}

	@Override
	public final void glGetUniformiv(final int arg0, final int arg1, final int[] arg2, final int arg3) {
		this.getGL2ES2().glGetUniformiv(arg0, arg1, arg2, arg3);
	}

	@Override
	public final void glGetVertexAttribfv(final int arg0, final int arg1, final FloatBuffer arg2) {
		this.getGL2ES2().glGetVertexAttribfv(arg0, arg1, arg2);
	}

	@Override
	public final void glGetVertexAttribfv(final int arg0, final int arg1, final float[] arg2, final int arg3) {
		this.getGL2ES2().glGetVertexAttribfv(arg0, arg1, arg2, arg3);
	}

	@Override
	public final void glGetVertexAttribiv(final int arg0, final int arg1, final IntBuffer arg2) {
		this.getGL2ES2().glGetVertexAttribiv(arg0, arg1, arg2);
	}

	@Override
	public final void glGetVertexAttribiv(final int arg0, final int arg1, final int[] arg2, final int arg3) {
		this.getGL2ES2().glGetVertexAttribiv(arg0, arg1, arg2, arg3);
	}

	@Override
	public final boolean glIsProgram(final int arg0) {
		return this.getGL2ES2().glIsProgram(arg0);
	}

	@Override
	public final boolean glIsShader(final int arg0) {
		return this.getGL2ES2().glIsShader(arg0);
	}

	@Override
	public final void glLinkProgram(final int arg0) {
		this.getGL2ES2().glLinkProgram(arg0);
	}
	
	/** TODO: Reuse the allocated String Array? **/
	@Override
	public final void glShaderSource(final int arg0, final String arg2) {
		this.getGL2ES2().glShaderSource(arg0, 1, new String[]{arg2}, null);
	}

	@Override
	public final void glStencilFuncSeparate(final int arg0, final int arg1, final int arg2, final int arg3) {
		this.getGL2ES2().glStencilFuncSeparate(arg0, arg1, arg2, arg3);
	}

	@Override
	public final void glStencilMaskSeparate(final int arg0, final int arg1) {
		this.getGL2ES2().glStencilMaskSeparate(arg0, arg1);
	}

	@Override
	public final void glStencilOpSeparate(final int arg0, final int arg1, final int arg2, final int arg3) {
		this.getGL2ES2().glStencilOpSeparate(arg0, arg1, arg2, arg3);
	}

	@Override
	public final void glUniform1f(final int arg0, final float arg1) {
		this.getGL2ES2().glUniform1f(arg0, arg1);
	}

	@Override
	public final void glUniform1fv(final int arg0, final int arg1, final FloatBuffer arg2) {
		this.getGL2ES2().glUniform1fv(arg0, arg1, arg2);
	}

	@Override
	public final void glUniform1fv(final int arg0, final int arg1, final float[] arg2, final int arg3) {
		this.getGL2ES2().glUniform1fv(arg0, arg1, arg2, arg3);
	}

	@Override
	public final void glUniform1i(final int arg0, final int arg1) {
		this.getGL2ES2().glUniform1i(arg0, arg1);
	}

	@Override
	public final void glUniform1iv(final int arg0, final int arg1, final IntBuffer arg2) {
		this.getGL2ES2().glUniform1iv(arg0, arg1, arg2);
	}

	@Override
	public final void glUniform1iv(final int arg0, final int arg1, final int[] arg2, final int arg3) {
		this.getGL2ES2().glUniform1iv(arg0, arg1, arg2, arg3);
	}

	@Override
	public void glUniform2i(int arg0, int arg1, int arg2) {
		this.getGL2ES2().glUniform2i(arg0, arg1, arg2);
	}

	@Override
	public final void glUniform2f(final int arg0, final float arg1, final float arg2) {
		this.getGL2ES2().glUniform2f(arg0, arg1, arg2);
	}

	@Override
	public final void glUniform2fv(final int arg0, final int arg1, final FloatBuffer arg2) {
		this.getGL2ES2().glUniform2fv(arg0, arg1, arg2);
	}

	@Override
	public final void glUniform2fv(final int arg0, final int arg1, final float[] arg2, final int arg3) {
		this.getGL2ES2().glUniform2fv(arg0, arg1, arg2, arg3);
	}

	@Override
	public final void glUniform3fv(final int arg0, final int arg1, final FloatBuffer arg2) {
		this.getGL2ES2().glUniform3fv(arg0, arg1, arg2);
	}

	@Override
	public final void glUniform3fv(final int arg0, final int arg1, final float[] arg2, final int arg3) {
		this.getGL2ES2().glUniform3fv(arg0, arg1, arg2, arg3);
	}

	@Override
	public final void glUniform3i(final int arg0, final int arg1, final int arg2, final int arg3) {
		this.getGL2ES2().glUniform3i(arg0, arg1, arg2, arg3);
	}

	@Override
	public final void glUniform3iv(final int arg0, final int arg1, final IntBuffer arg2) {
		this.getGL2ES2().glUniform3iv(arg0, arg1, arg2);
	}

	@Override
	public final void glUniform3iv(final int arg0, final int arg1, final int[] arg2, final int arg3) {
		this.getGL2ES2().glUniform3iv(arg0, arg1, arg2, arg3);
	}

	@Override
	public final void glUniform4f(final int arg0, final float arg1, final float arg2, final float arg3, final float arg4) {
		this.getGL2ES2().glUniform4f(arg0, arg1, arg2, arg3, arg4);
	}

	@Override
	public final void glUniform4fv(final int arg0, final int arg1, final FloatBuffer arg2) {
		this.getGL2ES2().glUniform4fv(arg0, arg1, arg2);
	}

	@Override
	public final void glUniform4fv(final int arg0, final int arg1, final float[] arg2, final int arg3) {
		this.getGL2ES2().glUniform4fv(arg0, arg1, arg2, arg3);
	}

	@Override
	public final void glUniform4i(final int arg0, final int arg1, final int arg2, final int arg3, final int arg4) {
		this.getGL2ES2().glUniform4i(arg0, arg1, arg2, arg3, arg4);
	}

	@Override
	public final void glUniform4iv(final int arg0, final int arg1, final IntBuffer arg2) {
		this.getGL2ES2().glUniform4iv(arg0, arg1, arg2);
	}

	@Override
	public final void glUniform4iv(final int arg0, final int arg1, final int[] arg2, final int arg3) {
		this.getGL2ES2().glUniform4iv(arg0, arg1, arg2, arg3);
	}

	@Override
	public final void glUniformMatrix2fv(final int arg0, final int arg1, final boolean arg2, final FloatBuffer arg3) {
		this.getGL2ES2().glUniformMatrix2fv(arg0, arg1, arg2, arg3);
	}

	@Override
	public final void glUniformMatrix2fv(final int arg0, final int arg1, final boolean arg2, final float[] arg3, final int arg4) {
		this.getGL2ES2().glUniformMatrix2fv(arg0, arg1, arg2, arg3, arg4);
	}

	@Override
	public final void glUniformMatrix3fv(final int arg0, final int arg1, final boolean arg2, final FloatBuffer arg3) {
		this.getGL2ES2().glUniformMatrix3fv(arg0, arg1, arg2, arg3);
	}

	@Override
	public final void glUniformMatrix3fv(final int arg0, final int arg1, final boolean arg2, final float[] arg3, final int arg4) {
		this.getGL2ES2().glUniformMatrix3fv(arg0, arg1, arg2, arg3, arg4);
	}

	@Override
	public final void glUniformMatrix4fv(final int arg0, final int arg1, final boolean arg2, final FloatBuffer arg3) {
		this.getGL2ES2().glUniformMatrix4fv(arg0, arg1, arg2, arg3);
	}

	@Override
	public final void glUniformMatrix4fv(final int arg0, final int arg1, final boolean arg2, final float[] arg3, final int arg4) {
		this.getGL2ES2().glUniformMatrix4fv(arg0, arg1, arg2, arg3, arg4);
	}

	@Override
	public final void glUseProgram(final int arg0) {
		this.getGL2ES2().glUseProgram(arg0);
	}

	@Override
	public final void glValidateProgram(final int arg0) {
		this.getGL2ES2().glValidateProgram(arg0);
	}

	@Override
	public final void glVertexAttrib1f(final int arg0, final float arg1) {
		this.getGL2ES2().glVertexAttrib1f(arg0, arg1);
	}

	@Override
	public final void glVertexAttrib1fv(final int arg0, final FloatBuffer arg1) {
		this.getGL2ES2().glVertexAttrib1fv(arg0, arg1);
	}

	@Override
	public final void glVertexAttrib1fv(final int arg0, final float[] arg1, final int arg2) {
		this.getGL2ES2().glVertexAttrib1fv(arg0, arg1, arg2);
	}

	@Override
	public final void glVertexAttrib2f(final int arg0, final float arg1, final float arg2) {
		this.getGL2ES2().glVertexAttrib2f(arg0, arg1, arg2);
	}

	@Override
	public final void glVertexAttrib2fv(final int arg0, final FloatBuffer arg1) {
		this.getGL2ES2().glVertexAttrib2fv(arg0, arg1);
	}

	@Override
	public final void glVertexAttrib2fv(final int arg0, final float[] arg1, final int arg2) {
		this.getGL2ES2().glVertexAttrib2fv(arg0, arg1, arg2);
	}

	@Override
	public final void glVertexAttrib3f(final int arg0, final float arg1, final float arg2, final float arg3) {
		this.getGL2ES2().glVertexAttrib3f(arg0, arg1, arg2, arg3);
	}

	@Override
	public final void glVertexAttrib3fv(final int arg0, final FloatBuffer arg1) {
		this.getGL2ES2().glVertexAttrib3fv(arg0, arg1);
	}

	@Override
	public final void glVertexAttrib3fv(final int arg0, final float[] arg1, final int arg2) {
		this.getGL2ES2().glVertexAttrib3fv(arg0, arg1, arg2);
	}

	@Override
	public final void glVertexAttrib4f(final int arg0, final float arg1, final float arg2, final float arg3, final float arg4) {
		this.getGL2ES2().glVertexAttrib4f(arg0, arg1, arg2, arg3, arg4);
	}

	@Override
	public final void glVertexAttrib4fv(final int arg0, final FloatBuffer arg1) {
		this.getGL2ES2().glVertexAttrib4fv(arg0, arg1);
	}

	@Override
	public final void glVertexAttrib4fv(final int arg0, final float[] arg1, final int arg2) {
		this.getGL2ES2().glVertexAttrib4fv(arg0, arg1, arg2);
	}

	@Override
	public final void glVertexAttribPointer(final int arg0, final int arg1, final int arg2, final boolean arg3, final int arg4, final int arg5) {
		this.getGL2ES2().glVertexAttribPointer(arg0, arg1, arg2, arg3, arg4, arg5);
	}

	@Override
	public final void glReleaseShaderCompiler() {
		this.getGL2ES2().glReleaseShaderCompiler();
	}

	@Override
	public final void glShaderBinary(final int arg0, final IntBuffer arg1, final int arg2, final Buffer arg3, final int arg4) {
		this.getGL2ES2().glShaderBinary(arg0, arg1, arg2, arg3, arg4);
	}

	@Override
	public final void glShaderBinary(final int arg0, final int[] arg1, final int arg2, final int arg3, final Buffer arg4, final int arg5) {
		this.getGL2ES2().glShaderBinary(arg0, arg1, arg2, arg3, arg4, arg5);
	}

	@Override
	public final void glGetShaderPrecisionFormat(final int arg0, final int arg1, final IntBuffer arg2, final IntBuffer arg3) {
		this.getGL2ES2().glGetShaderPrecisionFormat(arg0, arg1, arg2, arg3);
	}

	@Override
	public final void glGetShaderPrecisionFormat(final int arg0, final int arg1, final int[] arg2, final int arg3, final int[] arg4, final int arg5) {
		this.getGL2ES2().glGetShaderPrecisionFormat(arg0, arg1, arg2, arg3, arg4, arg5);
	}

	private final GL2ES2 getGL2ES2() {
		return this.mGL2ES2;
	}

}
