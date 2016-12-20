package uk.ac.manchester.sisp.ribbon.opengl.matrix;

import uk.ac.manchester.sisp.ribbon.global.EReleaseMode;
import uk.ac.manchester.sisp.ribbon.global.RibbonGlobal;
import uk.ac.manchester.sisp.ribbon.utils.MathUtils;

public class GLMatrix {
	

	/** Temporary memory for operations that need temporary matrix data. */
	private final static float[] sTemp  = new float[16];
	private final static float[] sTemp2 = new float[16];
    
    /**
     * Rotates matrix m by angle a (in degrees) around the axis (x, y, z)
     * @param rm returns the result
     * @param rmOffset index into rm where the result matrix starts
     * @param m source matrix
     * @param mOffset index into m where the source matrix starts
     * @param a angle to rotate in degrees
     * @param x scale factor x
     * @param y scale factor y
     * @param z scale factor z
     */
    public static void rotateM(float[] rm,
            float[] m,
            float a, float x, float y, float z) {
            setRotateM(sTemp, a, x, y, z);
            multiplyMM(rm, m, sTemp);
    }

    /**
     * Rotates matrix m in place by angle a (in degrees)
     * around the axis (x, y, z)
     * @param m source matrix
     * @param mOffset index into m where the matrix starts
     * @param a angle to rotate in degrees
     * @param x scale factor x
     * @param y scale factor y
     * @param z scale factor z
     */
    public static void rotateM(float[] m,
            double a, float x, float y, float z) {
            setRotateM(sTemp, a, x, y, z);
            multiplyMM(sTemp2, m, sTemp);
            System.arraycopy(sTemp, 0, m, 0, 16);
    }

    /**
     * Rotates matrix m by angle a (in degrees) around the axis (x, y, z)
     * @param rm returns the result
     * @param rmOffset index into rm where the result matrix starts
     * @param a angle to rotate in degrees
     * @param x scale factor x
     * @param y scale factor y
     * @param z scale factor z
     */
    private static void setRotateM(float[] rm,
            double a, float x, float y, float z) {
        rm[3] = 0;
        rm[7] = 0;
        rm[11]= 0;
        rm[12]= 0;
        rm[13]= 0;
        rm[14]= 0;
        rm[15]= 1;
        a *= (float) (Math.PI / 180.0f);
        float s = (float) Math.sin(a);
        float c = (float) Math.cos(a);
        if (1.0f == x && 0.0f == y && 0.0f == z) {
            rm[ 5] = c;   rm[ 10]= c;
            rm[ 6] = s;   rm[ 9] = -s;
            rm[ 1] = 0;   rm[ 2] = 0;
            rm[ 4] = 0;   rm[ 8] = 0;
            rm[ 0] = 1;
        } else if (0.0f == x && 1.0f == y && 0.0f == z) {
            rm[ 0] = c;   rm[ 10]= c;
            rm[ 8] = s;   rm[ 2] = -s;
            rm[ 1] = 0;   rm[ 4] = 0;
            rm[ 6] = 0;   rm[ 9] = 0;
            rm[ 5] = 1;
        } else if (0.0f == x && 0.0f == y && 1.0f == z) {
            rm[ 0] = c;   rm[ 5] = c;
            rm[ 1] = s;   rm[ 4] = -s;
            rm[ 2] = 0;   rm[ 6] = 0;
            rm[ 8] = 0;   rm[ 9] = 0;
            rm[ 10]= 1;
        } else {
            float len = MathUtils.calculateMagnitudeOf(x, y, z);
            if (1.0f != len) {
                float recipLen = 1.0f / len;
                x *= recipLen;
                y *= recipLen;
                z *= recipLen;
            }
            float nc = 1.0f - c;
            float xy = x * y;
            float yz = y * z;
            float zx = z * x;
            float xs = x * s;
            float ys = y * s;
            float zs = z * s;
            rm[  0] = x*x*nc +  c;
            rm[  4] =  xy*nc - zs;
            rm[  8] =  zx*nc + ys;
            rm[  1] =  xy*nc + zs;
            rm[  5] = y*y*nc +  c;
            rm[  9] =  yz*nc - xs;
            rm[  2] =  zx*nc - ys;
            rm[  6] =  yz*nc + xs;
            rm[ 10] = z*z*nc +  c;
        }
    }
	
	public static final void setMatrixOrthographic(final float[] pReturnMatrix, final float pLeft, final float pRight, final float pBottom, final float pTop, final float pNear, final float pFar) {
		/* Compute projection scaling. */
		final float lWidth  = 1.0f / (pRight - pLeft);
		final float lHeight = 1.0f / (pTop - pBottom);
		final float lDepth  = 1.0f / (pFar - pNear);
		final float lScaleX = -(pRight + pLeft) * lWidth;
		final float lScaleY = -(pTop + pBottom) * lHeight;
		final float lScaleZ = -(pFar + pNear) * lDepth;
		/* Define the orthographic projection matrix. */
		pReturnMatrix[0]    =  2.0f * (lWidth);
		pReturnMatrix[5]    =  2.0f * (lHeight);
		pReturnMatrix[10]   = -2.0f * (lDepth);
		pReturnMatrix[12]   = lScaleX;
		pReturnMatrix[13]   = lScaleY;
		pReturnMatrix[14]   = lScaleZ;
		pReturnMatrix[15]   = 1.0f;
		pReturnMatrix[1]    = 0.0f;
		pReturnMatrix[2]    = 0.0f;
		pReturnMatrix[3]    = 0.0f;
		pReturnMatrix[4]    = 0.0f;
		pReturnMatrix[6]    = 0.0f;
		pReturnMatrix[7]    = 0.0f;
		pReturnMatrix[8]    = 0.0f;
		pReturnMatrix[9]    = 0.0f;
		pReturnMatrix[11]   = 0.0f;
	}
	
	public static final void setMatrixFrustrum(final float[] pReturnMatrix, final float pLeft, final float pRight, final float pBottom, final float pTop, final float pNear, final float pFar) {
		if(RibbonGlobal.isReleaseModeSupported(EReleaseMode.DEVELOPMENT)) {
			if(pLeft == pRight || pTop == pBottom || pNear == pFar || pNear <= 0.0f || pFar <= 0.0f) {
				throw new IllegalArgumentException("Bad frustrum matrix parameters!");
			}
		}
		/* Compute projection scaling. */
		final float lWidth  = 1.0f / (pRight - pLeft);
		final float lHeight = 1.0f / (pTop - pBottom);
		final float lDepth  = 1.0f / (pNear - pFar);
		final float lA      = (pRight + pLeft) * lWidth;
		final float lB      = (pTop + pBottom) * lHeight;
		final float lC      = (pFar + pNear) * lDepth;
		final float lD      = 2.0f * (pFar * pNear * lDepth);
		/* Define frustrum matrix. */
		pReturnMatrix[0]    = 2.0f * (pNear * lWidth);
		pReturnMatrix[5]    = 2.0f * (pNear * lHeight);
		pReturnMatrix[8]    = lA;
		pReturnMatrix[9]    = lB;
		pReturnMatrix[10]   = lC;
		pReturnMatrix[14]   = lD;
		pReturnMatrix[11]   = -1.0f;
		pReturnMatrix[1]    = 0.0f;
		pReturnMatrix[2]    = 0.0f;
		pReturnMatrix[3]    = 0.0f;
		pReturnMatrix[4]    = 0.0f;
		pReturnMatrix[6]    = 0.0f;
		pReturnMatrix[7]    = 0.0f;
		pReturnMatrix[12]   = 0.0f;
		pReturnMatrix[13]   = 0.0f;
		pReturnMatrix[15]   = 0.0f;
	}
	
    public static final void setMatrixPerspective(final float[] pReturnMatrix, final float pYFOV, final float pAspect, final float pZNear, final float pZFar) {
		float lTheta = 1.0f / (float) Math.tan(pYFOV * (Math.PI / 360.0));
		float lRange = 1.0f / (pZNear - pZFar);
		/* Define perspective matrix. */
		pReturnMatrix[0]  = lTheta / pAspect;
		pReturnMatrix[1]  = 0.0f;
		pReturnMatrix[2]  = 0.0f;
		pReturnMatrix[3]  = 0.0f;
		pReturnMatrix[4]  = 0.0f;
		pReturnMatrix[5]  = lTheta;
		pReturnMatrix[6]  = 0.0f;
		pReturnMatrix[7]  = 0.0f;
		pReturnMatrix[8]  = 0.0f;
		pReturnMatrix[9]  = 0.0f;
		pReturnMatrix[10] = (pZFar + pZNear) * lRange;
		pReturnMatrix[11] = -1.0f;
		pReturnMatrix[12] = 0.0f;
		pReturnMatrix[13] = 0.0f;
		pReturnMatrix[14] = 2.0f * pZFar * pZNear * lRange;
		pReturnMatrix[15] = 0.0f;
	}

    /**
     * Sets matrix m to the identity matrix.
     * @param sm returns the result
     * @param smOffset index into sm where the result matrix starts
     */
    public static void setIdentityM(float[] sm) {
        for (int i=0 ; i<16 ; i++) {
            sm[i] = 0;
        }
        for(int i = 0; i < 16; i += 5) {
            sm[i] = 1.0f;
        }
    }

    /**
     * Scales matrix  m by x, y, and z, putting the result in sm
     * @param sm returns the result
     * @param smOffset index into sm where the result matrix starts
     * @param m source matrix
     * @param mOffset index into m where the source matrix starts
     * @param x scale factor x
     * @param y scale factor y
     * @param z scale factor z
     */
    public static void scaleM(float[] sm, int smOffset,
            float[] m, int mOffset,
            float x, float y, float z) {
        for (int i=0 ; i<4 ; i++) {
            int smi = smOffset + i;
            int mi = mOffset + i;
            sm[     smi] = m[     mi] * x;
            sm[ 4 + smi] = m[ 4 + mi] * y;
            sm[ 8 + smi] = m[ 8 + mi] * z;
            sm[12 + smi] = m[12 + mi];
        }
    }

    /**
     * Scales matrix m in place by sx, sy, and sz
     * @param m matrix to scale
     * @param mOffset index into m where the matrix starts
     * @param x scale factor x
     * @param y scale factor y
     * @param z scale factor z
     */
    public static void scaleM(float[] m,
            float x, float y, float z) {
        for (int i=0 ; i<4 ; i++) {
            int mi = i;
            m[     mi] *= x;
            m[ 4 + mi] *= y;
            m[ 8 + mi] *= z;
        }
    }

    

    /**
     * Translates matrix m by x, y, and z in place.
     * @param m matrix
     * @param mOffset index into m where the matrix starts
     * @param x translation factor x
     * @param y translation factor y
     * @param z translation factor z
     */
    public static void translateM(
            float[] m,
            float x, float y, float z) {
        for (int i=0 ; i<4 ; i++) {
            int mi = i;
            m[12 + mi] += m[mi] * x + m[4 + mi] * y + m[8 + mi] * z;
        }
    }

    public static void multiplyMM(final float[] pResult, final float[] pLeft, final float[] pRight) {		
    	pResult[0]  = pLeft[0]  * pRight[0] + pLeft[1]  *  pRight[4]  + pLeft[2]  * pRight[8]   + pLeft[3]  * pRight[12];
    	pResult[1]  = pLeft[0]  * pRight[1] + pLeft[1]  *  pRight[5]  + pLeft[2]  * pRight[9]   + pLeft[3]  * pRight[13];
    	pResult[2]  = pLeft[0]  * pRight[2] + pLeft[1]  *  pRight[6]  + pLeft[2]  * pRight[10]  + pLeft[3]  * pRight[14];
    	pResult[3]  = pLeft[0]  * pRight[3] + pLeft[1]  *  pRight[7]  + pLeft[2]  * pRight[11]  + pLeft[3]  * pRight[15];
    	pResult[4]  = pLeft[4]  * pRight[0] + pLeft[5]  *  pRight[4]  + pLeft[6]  * pRight[8]   + pLeft[7]  * pRight[12];
    	pResult[5]  = pLeft[4]  * pRight[1] + pLeft[5]  *  pRight[5]  + pLeft[6]  * pRight[9]   + pLeft[7]  * pRight[13];
    	pResult[6]  = pLeft[4]  * pRight[2] + pLeft[5]  *  pRight[6]  + pLeft[6]  * pRight[10]  + pLeft[7]  * pRight[14];
    	pResult[7]  = pLeft[4]  * pRight[3] + pLeft[5]  *  pRight[7]  + pLeft[6]  * pRight[11]  + pLeft[7]  * pRight[15];
    	pResult[8]  = pLeft[8]  * pRight[0] + pLeft[9]  *  pRight[4]  + pLeft[10] * pRight[8]   + pLeft[11] * pRight[12];
    	pResult[9]  = pLeft[8]  * pRight[1] + pLeft[9]  *  pRight[5]  + pLeft[10] * pRight[9]   + pLeft[11] * pRight[13];
    	pResult[10] = pLeft[8]  * pRight[2] + pLeft[9]  *  pRight[6]  + pLeft[10] * pRight[10]  + pLeft[11] * pRight[14];
    	pResult[11] = pLeft[8]  * pRight[3] + pLeft[9]  *  pRight[7]  + pLeft[10] * pRight[11]  + pLeft[11] * pRight[15];
    	pResult[12] = pLeft[12] * pRight[0] + pLeft[13] *  pRight[4]  + pLeft[14] * pRight[8]   + pLeft[15] * pRight[12];
    	pResult[13] = pLeft[12] * pRight[1] + pLeft[13] *  pRight[5]  + pLeft[14] * pRight[9]   + pLeft[15] * pRight[13];
    	pResult[14] = pLeft[12] * pRight[2] + pLeft[13] *  pRight[6]  + pLeft[14] * pRight[10]  + pLeft[15] * pRight[14];
    	pResult[15] = pLeft[12] * pRight[3] + pLeft[13] *  pRight[7]  + pLeft[14] * pRight[11]  + pLeft[15] * pRight[15];
    }

    /**
     * Converts Euler angles to a rotation matrix
     * @param rm returns the result
     * @param rmOffset index into rm where the result matrix starts
     * @param x angle of rotation, in degrees
     * @param y angle of rotation, in degrees
     * @param z angle of rotation, in degrees
     */
    public static void setRotateEulerM(float[] rm, int rmOffset,
            float x, float y, float z) {
        x *= (float) (Math.PI / 180.0f);
        y *= (float) (Math.PI / 180.0f);
        z *= (float) (Math.PI / 180.0f);
        float cx = (float) Math.cos(x);
        float sx = (float) Math.sin(x);
        float cy = (float) Math.cos(y);
        float sy = (float) Math.sin(y);
        float cz = (float) Math.cos(z);
        float sz = (float) Math.sin(z);
        float cxsy = cx * sy;
        float sxsy = sx * sy;

        rm[ 0]  =   cy * cz;
        rm[ 1]  =  -cy * sz;
        rm[ 2]  =   sy;
        rm[ 3]  =  0.0f;

        rm[ 4]  =  cxsy * cz + cx * sz;
        rm[ 5]  = -cxsy * sz + cx * cz;
        rm[ 6]  =  -sx * cy;
        rm[ 7]  =  0.0f;

        rm[ 8]  = -sxsy * cz + sx * sz;
        rm[ 9]  =  sxsy * sz + sx * cz;
        rm[ 10] =  cx * cy;
        rm[ 11] =  0.0f;

        rm[ 12] =  0.0f;
        rm[ 13] =  0.0f;
        rm[ 14] =  0.0f;
        rm[ 15] =  1.0f;
    }

    /**
     * Define a viewing transformation in terms of an eye point, a center of
     * view, and an up vector.
     *
     * @param rm returns the result
     * @param rmOffset index into rm where the result matrix starts
     * @param eyeX eye point X
     * @param eyeY eye point Y
     * @param eyeZ eye point Z
     * @param centerX center of view X
     * @param centerY center of view Y
     * @param centerZ center of view Z
     * @param upX up vector X
     * @param upY up vector Y
     * @param upZ up vector Z
     */
    public static void setLookAtM(float[] rm,
            float eyeX, float eyeY, float eyeZ,
            float centerX, float centerY, float centerZ, float upX, float upY,
            float upZ) {

        // See the OpenGL GLUT documentation for gluLookAt for a description
        // of the algorithm. We implement it in a straightforward way:

        float fx = centerX - eyeX;
        float fy = centerY - eyeY;
        float fz = centerZ - eyeZ;

        // Normalize f
        float rlf = 1.0f / MathUtils.calculateMagnitudeOf(fx, fy, fz);
        fx *= rlf;
        fy *= rlf;
        fz *= rlf;

        // compute s = f x up (x means "cross product")
        float sx = fy * upZ - fz * upY;
        float sy = fz * upX - fx * upZ;
        float sz = fx * upY - fy * upX;

        // and normalize s
        float rls = 1.0f / MathUtils.calculateMagnitudeOf(sx, sy, sz);
        sx *= rls;
        sy *= rls;
        sz *= rls;

        // compute u = s x f
        float ux = sy * fz - sz * fy;
        float uy = sz * fx - sx * fz;
        float uz = sx * fy - sy * fx;

        rm[ 0] = sx;
        rm[ 1] = ux;
        rm[ 2] = -fx;
        rm[ 3] = 0.0f;

        rm[ 4] = sy;
        rm[ 5] = uy;
        rm[ 6] = -fy;
        rm[ 7] = 0.0f;

        rm[ 8] = sz;
        rm[ 9] = uz;
        rm[ 10] = -fz;
        rm[ 11] = 0.0f;

        rm[ 12] = 0.0f;
        rm[ 13] = 0.0f;
        rm[ 14] = 0.0f;
        rm[ 15] = 1.0f;

        translateM(rm, -eyeX, -eyeY, -eyeZ);
    }
}
