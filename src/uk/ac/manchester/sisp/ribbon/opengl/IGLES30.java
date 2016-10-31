package uk.ac.manchester.sisp.ribbon.opengl;

public interface IGLES30 extends IGLES20 {
	
	public static final int GL_GREEN = 0x1904;
	public static final int GL_BLUE = 0x1905;
	public static final int GL_READ_BUFFER = 0x0C02;
	public static final int GL_COLOR = 0x1800;
	public static final int GL_DEPTH = 0x1801;
	public static final int GL_STENCIL = 0x1802;
	public static final int GL_PACK_ROW_LENGTH = 0x0D02;
	public static final int GL_PACK_SKIP_PIXELS = 0x0D04;
	public static final int GL_PACK_SKIP_ROWS = 0x0D03;
	public static final int GL_MIN = 0x8007;
	public static final int GL_MAX = 0x8008;
	public static final int GL_UNPACK_SKIP_IMAGES = 0x806D;
	public static final int GL_UNPACK_IMAGE_HEIGHT = 0x806E;
	public static final int GL_MAX_ELEMENTS_VERTICES = 0x80E8;
	public static final int GL_MAX_ELEMENTS_INDICES = 0x80E9;
	public static final int GL_TEXTURE_MIN_LOD = 0x813A;
	public static final int GL_TEXTURE_MAX_LOD = 0x813B;
	public static final int GL_TEXTURE_BASE_LEVEL = 0x813C;
	public static final int GL_TEXTURE_MAX_LEVEL = 0x813D;
	public static final int GL_MAX_TEXTURE_LOD_BIAS = 0x84FD;
	public static final int GL_STREAM_READ = 0x88E1;
	public static final int GL_STREAM_COPY = 0x88E2;
	public static final int GL_STATIC_READ = 0x88E5;
	public static final int GL_STATIC_COPY = 0x88E6;
	public static final int GL_DYNAMIC_READ = 0x88E9;
	public static final int GL_DYNAMIC_COPY = 0x88EA;
	public static final int GL_MAX_FRAGMENT_UNIFORM_COMPONENTS = 0x8B49;
	public static final int GL_MAX_VERTEX_UNIFORM_COMPONENTS = 0x8B4A;
	public static final int GL_PIXEL_PACK_BUFFER = 0x88EB;
	public static final int GL_PIXEL_UNPACK_BUFFER = 0x88EC;
	public static final int GL_PIXEL_PACK_BUFFER_BINDING = 0x88ED;
	public static final int GL_PIXEL_UNPACK_BUFFER_BINDING = 0x88EF;
	public static final int GL_FLOAT_MAT2x3 = 0x8B65;
	public static final int GL_FLOAT_MAT2x4 = 0x8B66;
	public static final int GL_FLOAT_MAT3x2 = 0x8B67;
	public static final int GL_FLOAT_MAT3x4 = 0x8B68;
	public static final int GL_FLOAT_MAT4x2 = 0x8B69;
	public static final int GL_FLOAT_MAT4x3 = 0x8B6A;
	public static final int GL_SRGB8 = 0x8C41;
	public static final int GL_MAJOR_VERSION = 0x821B;
	public static final int GL_MINOR_VERSION = 0x821C;
	public static final int GL_NUM_EXTENSIONS = 0x821D;
	public static final int GL_VERTEX_ATTRIB_ARRAY_INTEGER = 0x88FD;
	public static final int GL_MIN_PROGRAM_TEXEL_OFFSET = 0x8904;
	public static final int GL_MAX_PROGRAM_TEXEL_OFFSET = 0x8905;
	public static final int GL_MAX_VARYING_COMPONENTS = 0x8B4B;
	public static final int GL_RGB9_E5 = 0x8C3D;
	public static final int GL_UNSIGNED_INT_5_9_9_9_REV = 0x8C3E;
	public static final int GL_TRANSFORM_FEEDBACK_VARYING_MAX_LENGTH = 0x8C76;
	public static final int GL_TRANSFORM_FEEDBACK_BUFFER_MODE = 0x8C7F;
	public static final int GL_MAX_TRANSFORM_FEEDBACK_SEPARATE_COMPONENTS = 0x8C80;
	public static final int GL_TRANSFORM_FEEDBACK_VARYINGS = 0x8C83;
	public static final int GL_TRANSFORM_FEEDBACK_BUFFER_START = 0x8C84;
	public static final int GL_TRANSFORM_FEEDBACK_BUFFER_SIZE = 0x8C85;
	public static final int GL_TRANSFORM_FEEDBACK_PRIMITIVES_WRITTEN = 0x8C88;
	public static final int GL_RASTERIZER_DISCARD = 0x8C89;
	public static final int GL_MAX_TRANSFORM_FEEDBACK_INTERLEAVED_COMPONENTS = 0x8C8A;
	public static final int GL_MAX_TRANSFORM_FEEDBACK_SEPARATE_ATTRIBS = 0x8C8B;
	public static final int GL_INTERLEAVED_ATTRIBS = 0x8C8C;
	public static final int GL_SEPARATE_ATTRIBS = 0x8C8D;
	public static final int GL_TRANSFORM_FEEDBACK_BUFFER = 0x8C8E;
	public static final int GL_TRANSFORM_FEEDBACK_BUFFER_BINDING = 0x8C8F;
	public static final int GL_RGBA32UI = 0x8D70;
	public static final int GL_RGB32UI = 0x8D71;
	public static final int GL_RGBA16UI = 0x8D76;
	public static final int GL_RGB16UI = 0x8D77;
	public static final int GL_RGBA8UI = 0x8D7C;
	public static final int GL_RGB8UI = 0x8D7D;
	public static final int GL_RGBA32I = 0x8D82;
	public static final int GL_RGB32I = 0x8D83;
	public static final int GL_RGBA16I = 0x8D88;
	public static final int GL_RGB16I = 0x8D89;
	public static final int GL_RGBA8I = 0x8D8E;
	public static final int GL_RGB8I = 0x8D8F;
	public static final int GL_RED_INTEGER = 0x8D94;
	public static final int GL_RGB_INTEGER = 0x8D98;
	public static final int GL_RGBA_INTEGER = 0x8D99;
	public static final int GL_SAMPLER_2D_ARRAY_SHADOW = 0x8DC4;
	public static final int GL_SAMPLER_CUBE_SHADOW = 0x8DC5;
	public static final int GL_UNSIGNED_INT_VEC2 = 0x8DC6;
	public static final int GL_UNSIGNED_INT_VEC3 = 0x8DC7;
	public static final int GL_UNSIGNED_INT_VEC4 = 0x8DC8;
	public static final int GL_INT_SAMPLER_2D = 0x8DCA;
	public static final int GL_INT_SAMPLER_3D = 0x8DCB;
	public static final int GL_INT_SAMPLER_CUBE = 0x8DCC;
	public static final int GL_INT_SAMPLER_2D_ARRAY = 0x8DCF;
	public static final int GL_UNSIGNED_INT_SAMPLER_2D = 0x8DD2;
	public static final int GL_UNSIGNED_INT_SAMPLER_3D = 0x8DD3;
	public static final int GL_UNSIGNED_INT_SAMPLER_CUBE = 0x8DD4;
	public static final int GL_UNSIGNED_INT_SAMPLER_2D_ARRAY = 0x8DD7;
	public static final int GL_BUFFER_ACCESS_FLAGS = 0x911F;
	public static final int GL_BUFFER_MAP_LENGTH = 0x9120;
	public static final int GL_BUFFER_MAP_OFFSET = 0x9121;
	public static final int GL_DEPTH_COMPONENT32F = 0x8CAC;
	public static final int GL_DEPTH32F_STENCIL8 = 0x8CAD;
	public static final int GL_FLOAT_32_UNSIGNED_INT_24_8_REV = 0x8DAD;
	public static final int GL_FRAMEBUFFER_ATTACHMENT_RED_SIZE = 0x8212;
	public static final int GL_FRAMEBUFFER_ATTACHMENT_GREEN_SIZE = 0x8213;
	public static final int GL_FRAMEBUFFER_ATTACHMENT_BLUE_SIZE = 0x8214;
	public static final int GL_FRAMEBUFFER_ATTACHMENT_ALPHA_SIZE = 0x8215;
	public static final int GL_FRAMEBUFFER_ATTACHMENT_DEPTH_SIZE = 0x8216;
	public static final int GL_FRAMEBUFFER_ATTACHMENT_STENCIL_SIZE = 0x8217;
	public static final int GL_FRAMEBUFFER_DEFAULT = 0x8218;
	public static final int GL_FRAMEBUFFER_UNDEFINED = 0x8219;
	public static final int GL_DEPTH_STENCIL_ATTACHMENT = 0x821A;
	public static final int GL_DRAW_FRAMEBUFFER_BINDING = 0x8CA6;
	public static final int GL_READ_FRAMEBUFFER = 0x8CA8;
	public static final int GL_DRAW_FRAMEBUFFER = 0x8CA9;
	public static final int GL_READ_FRAMEBUFFER_BINDING = 0x8CAA;
	public static final int GL_RENDERBUFFER_SAMPLES = 0x8CAB;
	public static final int GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE = 0x8D56;
	public static final int GL_MAX_SAMPLES = 0x8D57;
	public static final int GL_RG_INTEGER = 0x8228;
	public static final int GL_R8I = 0x8231;
	public static final int GL_R8UI = 0x8232;
	public static final int GL_R16I = 0x8233;
	public static final int GL_R16UI = 0x8234;
	public static final int GL_R32I = 0x8235;
	public static final int GL_R32UI = 0x8236;
	public static final int GL_RG8I = 0x8237;
	public static final int GL_RG8UI = 0x8238;
	public static final int GL_RG16I = 0x8239;
	public static final int GL_RG16UI = 0x823A;
	public static final int GL_RG32I = 0x823B;
	public static final int GL_RG32UI = 0x823C;
	public static final int GL_VERTEX_ARRAY_BINDING = 0x85B5;
	public static final int GL_R8_SNORM = 0x8F94;
	public static final int GL_RG8_SNORM = 0x8F95;
	public static final int GL_RGB8_SNORM = 0x8F96;
	public static final int GL_RGBA8_SNORM = 0x8F97;
	public static final int GL_SIGNED_NORMALIZED = 0x8F9C;
	public static final int GL_COPY_READ_BUFFER = 0x8F36;
	public static final int GL_COPY_WRITE_BUFFER = 0x8F37;
	public static final int GL_UNIFORM_BUFFER = 0x8A11;
	public static final int GL_UNIFORM_BUFFER_BINDING = 0x8A28;
	public static final int GL_UNIFORM_BUFFER_START = 0x8A29;
	public static final int GL_UNIFORM_BUFFER_SIZE = 0x8A2A;
	public static final int GL_MAX_VERTEX_UNIFORM_BLOCKS = 0x8A2B;
	public static final int GL_MAX_FRAGMENT_UNIFORM_BLOCKS = 0x8A2D;
	public static final int GL_MAX_COMBINED_UNIFORM_BLOCKS = 0x8A2E;
	public static final int GL_MAX_UNIFORM_BUFFER_BINDINGS = 0x8A2F;
	public static final int GL_MAX_UNIFORM_BLOCK_SIZE = 0x8A30;
	public static final int GL_MAX_COMBINED_VERTEX_UNIFORM_COMPONENTS = 0x8A31;
	public static final int GL_MAX_COMBINED_FRAGMENT_UNIFORM_COMPONENTS = 0x8A33;
	public static final int GL_UNIFORM_BUFFER_OFFSET_ALIGNMENT = 0x8A34;
	public static final int GL_ACTIVE_UNIFORM_BLOCK_MAX_NAME_LENGTH = 0x8A35;
	public static final int GL_ACTIVE_UNIFORM_BLOCKS = 0x8A36;
	public static final int GL_UNIFORM_TYPE = 0x8A37;
	public static final int GL_UNIFORM_SIZE = 0x8A38;
	public static final int GL_UNIFORM_NAME_LENGTH = 0x8A39;
	public static final int GL_UNIFORM_BLOCK_INDEX = 0x8A3A;
	public static final int GL_UNIFORM_OFFSET = 0x8A3B;
	public static final int GL_UNIFORM_ARRAY_STRIDE = 0x8A3C;
	public static final int GL_UNIFORM_MATRIX_STRIDE = 0x8A3D;
	public static final int GL_UNIFORM_IS_ROW_MAJOR = 0x8A3E;
	public static final int GL_UNIFORM_BLOCK_BINDING = 0x8A3F;
	public static final int GL_UNIFORM_BLOCK_DATA_SIZE = 0x8A40;
	public static final int GL_UNIFORM_BLOCK_NAME_LENGTH = 0x8A41;
	public static final int GL_UNIFORM_BLOCK_ACTIVE_UNIFORMS = 0x8A42;
	public static final int GL_UNIFORM_BLOCK_ACTIVE_UNIFORM_INDICES        = 0x8A43;
	public static final int GL_UNIFORM_BLOCK_REFERENCED_BY_VERTEX_SHADER   = 0x8A44;
	public static final int GL_UNIFORM_BLOCK_REFERENCED_BY_FRAGMENT_SHADER = 0x8A46;
	public static final int GL_COPY_READ_BUFFER_BINDING       = 0x8F36;
	public static final int GL_COPY_WRITE_BUFFER_BINDING      = 0x8F37;
	public static final int GL_TRANSFORM_FEEDBACK_PAUSED      = 0x8E23;
	public static final int GL_TRANSFORM_FEEDBACK_ACTIVE      = 0x8E24;
	public static final int GL_COMPRESSED_RGBA_ASTC_4x4_KHR   = 0x93B0;
	public static final int GL_COMPRESSED_RGBA_ASTC_5x4_KHR   = 0x93B1;
	public static final int GL_COMPRESSED_RGBA_ASTC_5x5_KHR   = 0x93B2;
	public static final int GL_COMPRESSED_RGBA_ASTC_6x5_KHR   = 0x93B3;
	public static final int GL_COMPRESSED_RGBA_ASTC_6x6_KHR   = 0x93B4;
	public static final int GL_COMPRESSED_RGBA_ASTC_8x5_KHR   = 0x93B5;
	public static final int GL_COMPRESSED_RGBA_ASTC_8x6_KHR   = 0x93B6;
	public static final int GL_COMPRESSED_RGBA_ASTC_8x8_KHR   = 0x93B7;
	public static final int GL_COMPRESSED_RGBA_ASTC_10x5_KHR  = 0x93B8;
	public static final int GL_COMPRESSED_RGBA_ASTC_10x6_KHR  = 0x93B9;
	public static final int GL_COMPRESSED_RGBA_ASTC_10x8_KHR  = 0x93BA;
	public static final int GL_COMPRESSED_RGBA_ASTC_10x10_KHR = 0x93BB;
	public static final int GL_COMPRESSED_RGBA_ASTC_12x10_KHR = 0x93BC;
	public static final int GL_COMPRESSED_RGBA_ASTC_12x12_KHR = 0x93BD;
	public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_4x4_KHR   = 0x93D0;
	public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_5x4_KHR   = 0x93D1;
	public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_5x5_KHR   = 0x93D2;
	public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_6x5_KHR   = 0x93D3;
	public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_6x6_KHR   = 0x93D4;
	public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_8x5_KHR   = 0x93D5;
	public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_8x6_KHR   = 0x93D6;
	public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_8x8_KHR   = 0x93D7;
	public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_10x5_KHR  = 0x93D8;
	public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_10x6_KHR  = 0x93D9;
	public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_10x8_KHR  = 0x93DA;
	public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_10x10_KHR = 0x93DB;
	public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_12x10_KHR = 0x93DC;
	public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_12x12_KHR = 0x93DD;
	
	public void glBeginTransformFeedback(int primitiveMode);
	
	public void glBindBufferBase(int target, int index, int buffer);
	
	public void glBindBufferRange(int target, int index, int buffer, int offset, int size);
	
	public void glBindVertexArray(int array);
	
	public void glBlitFramebuffer(int srcX0, int srcY0, int srcX1, int srcY1, int dstX0, int dstY0, int dstX1, int dstY1, int mask, int filter);
	
	public void glClearBufferfi(int buffer, int drawbuffer, float depth, int stencil);
	
	public void glClearBufferfv(int buffer, int drawbuffer, float[] value, int value_offset);
	
	public void glClearBufferiv(int buffer, int drawbuffer, int[] value, int value_offset);
	
	public void glClearBufferuiv(int buffer, int drawbuffer, int[] value, int value_offset);
	
	public void glCopyBufferSubData(int readTarget, int writeTarget, int readOffset, int writeOffset, int size);
	
	public void glDeleteVertexArrays(int n, int[] arrays, int arrays_offset);
	
	public void glDrawArraysInstanced(int mode, int first, int count, int instancecount);
	
	public void glDrawBuffers(int n, int[] bufs, int bufs_offset);
	
	public void glDrawElementsInstanced(int mode, int count, int type, int indices_buffer_offset, int instancecount);
	
	public void glDrawRangeElements(int mode, int start, int end, int count, int type, int indices_buffer_offset);
	
	public void glEndTransformFeedback();
	
	public void glFramebufferTextureLayer(int target, int attachment, int texture, int level, int layer);
	
	public void glGenVertexArrays(int n, int[] arrays, int arrays_offset);
	
	public void glGetActiveUniformBlockName(int program, int uniformBlockIndex, int bufSize, int[] length, int length_offset, byte[] uniformBlockName, int uniformBlockName_offset);
	
	public void glGetActiveUniformBlockiv(int program, int uniformBlockIndex, int pname, int[] params, int params_offset);
	
	public void glGetActiveUniformsiv(int program, int uniformCount, int[] uniformIndices, int uniformIndices_offset, int pname, int[] params, int params_offset);
	
	public int glGetFragDataLocation(int program, String name);
	
	public void glGetIntegeri_v(int target, int index, int[] data, int data_offset);
	
	public void glGetInternalformativ(int target, int internalformat, int pname, int bufSize, int[] params, int params_offset);
	
	public String glGetStringi(int name, int index);
	
	public void glGetTransformFeedbackVarying(int program, int index, int bufSize, int[] length, int length_offset, int[] size, int size_offset, int[] type, int type_offset, byte[] name, int name_offset);
	
	public int glGetUniformBlockIndex(int program, String uniformBlockName);
	
	public void glGetUniformIndices(int program, int uniformCount, String[] uniformNames, int[] uniformIndices, int uniformIndices_offset);
	
	public void glGetUniformuiv(int program, int location, int[] params, int params_offset);
	
	public void glGetVertexAttribIiv(int index, int pname, int[] params, int params_offset);
	
	public void glGetVertexAttribIuiv(int index, int pname, int[] params, int params_offset);
	
	public boolean glIsVertexArray(int array);
	
	public void glReadBuffer(int mode);
	
	public void glRenderbufferStorageMultisample(int target, int samples, int internalformat, int width, int height);
	
	public void glTransformFeedbackVaryings(int program, int count, String[] varyings, int bufferMode);
	
	public void glUniform1ui(int location, int v0);
	
	public void glUniform1uiv(int location, int count, int[] value, int value_offset);
	
	public void glUniform2ui(int location, int v0, int v1);
	
	public void glUniform2uiv(int location, int count, int[] value, int value_offset);
	
	public void glUniform3ui(int location, int v0, int v1, int v2);
	
	public void glUniform3uiv(int location, int count, int[] value, int value_offset);
	
	public void glUniform4ui(int location, int v0, int v1, int v2, int v3);
	
	public void glUniform4uiv(int location, int count, int[] value, int value_offset);
	
	public void glUniformBlockBinding(int program, int uniformBlockIndex, int uniformBlockBinding);
	
	public void glUniformMatrix2x3fv(int location, int count, boolean transpose, float[] value, int value_offset);
	
	public void glUniformMatrix2x4fv(int location, int count, boolean transpose, float[] value, int value_offset);
	
	public void glUniformMatrix3x2fv(int location, int count, boolean transpose, float[] value, int value_offset);
	
	public void glUniformMatrix3x4fv(int location, int count, boolean transpose, float[] value, int value_offset);
	
	public void glUniformMatrix4x2fv(int location, int count, boolean transpose, float[] value, int value_offset);
	
	public void glUniformMatrix4x3fv(int location, int count, boolean transpose, float[] value, int value_offset);
	
	public void glVertexAttribI4i(int index, int x, int y, int z, int w);
	
	public void glVertexAttribI4iv(int index, int[] v, int v_offset);
	
	public void glVertexAttribI4ui(int index, int x, int y, int z, int w);
	
	public void glVertexAttribI4uiv(int index, int[] v, int v_offset);
	
	public void glVertexAttribIPointer(int index, int size, int type, int stride, int pointer_buffer_offset);
	
	public static final int GL_INVALID_INDEX    = 0xFFFFFFFF;
	public static final long GL_TIMEOUT_IGNORED = 0xFFFFFFFFFFFFFFFFL;
	public static final int GL_ALL_BARRIER_BITS = 0xFFFFFFFF;

}
