package uk.ac.manchester.sisp.ribbon.utils;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.List;

public class DataUtils {

	/* Data width constants. */
	public static final int BYTES_PER_CHAR   = Character.SIZE / Byte.SIZE;
	public static final int BYTES_PER_INT    = Integer.SIZE   / Byte.SIZE;
	public static final int BYTES_PER_DOUBLE = Double.SIZE    / Byte.SIZE;
	public static final int BYTES_PER_FLOAT  = Float.SIZE     / Byte.SIZE;
	public static final int BYTES_PER_LONG   = Long.SIZE      / Byte.SIZE;
	public static final int BYTES_PER_SHORT  = Short.SIZE     / Byte.SIZE;
	
	/* Java runtime constants. */
	public static final int JAVA_NULL_INDEX       = -1;
	public static final int JAVA_BASE_HEXADECIMAL = 16;
	
	public static final ByteBuffer delegateNative(final byte[] pBytes) {
		return ByteBuffer.wrap(pBytes).order(ByteOrder.nativeOrder());
	}
	
	public static final ByteBuffer delegateNative(final float[] pFloats) {
		/* Allocate memory for the ByteBuffer (with a backing array). */
		final ByteBuffer lReturnBuffer = ByteBuffer.allocate(pFloats.length * DataUtils.BYTES_PER_FLOAT).order(ByteOrder.nativeOrder());
		/* Load float data. */
		lReturnBuffer.asFloatBuffer().put(pFloats);
		/* Reposition the ByteBuffer pointer. */
		lReturnBuffer.position(0);
		/* Return the created ByteBuffer. */
		return lReturnBuffer;
	}
	
	public static final ByteBuffer delegateNative(final short[] pShorts) {
		/* Allocate memory for the ByteBuffer (with a backing array). */
		final ByteBuffer lReturnBuffer = ByteBuffer.allocate(pShorts.length * DataUtils.BYTES_PER_SHORT).order(ByteOrder.nativeOrder());
		/* Load float data. */
		lReturnBuffer.asShortBuffer().put(pShorts);
		/* Reposition the ByteBuffer pointer. */
		lReturnBuffer.position(0);
		/* Return the created ByteBuffer. */
		return lReturnBuffer;
	}
	
	public static final ByteBuffer reserveNativeBytes(final int pNumBytes) {
		return ByteBuffer.allocateDirect(pNumBytes).order(ByteOrder.nativeOrder());
	}
	
	public static final boolean isNull(final Object pObject) {
		return pObject == null;
	}
	
	public static final boolean isNotNull(final Object pObject) {
		return pObject != null;
	}
	
	public static final boolean isNotNull(final int pIndex) {
		return pIndex != DataUtils.JAVA_NULL_INDEX;
	}
	
	public static final boolean isZero(final int pValue) {
		return pValue == 0;
	}
	
	public static final boolean isNotZero(final int pValue) {
		return pValue != 0;
	}
	
	public static final boolean isGreaterThanZero(final float pValue) {
		return pValue > 0.0f;
	}
	
	public static final <T> T createInstanceOf(final Class<T> pClass) throws InstantiationException, IllegalAccessException {
		return pClass.newInstance();
	}
	
	public static final int booleanToInt(final boolean pBoolean) {
		return Boolean.compare(pBoolean, false);
	}
	
	public static final Boolean getCachedBoolean(final boolean pValue) {
		return Boolean.valueOf(pValue);
	}
	
	public static final Byte getCachedByte(final byte pValue) {
		return Byte.valueOf(pValue);
	}
	
	public static final Character getCachedChar(final char pChar) {
		return Character.valueOf(pChar);
	}
	
	public static final Short getCachedShort(final short pValue) {
		return Short.valueOf(pValue);
	}
	
	public static final Integer getCachedInteger(final int pValue) {
		return Integer.valueOf(pValue);
	}
	
	public static final Long getCachedLong(final long pValue) {
		return Long.valueOf(pValue);
	}
	
	public static final long asUnsigned(final int pValue) {
		return (long)pValue & 0xFFFFFFFFL;
	}
	
	public static final int asUnsigned(final short pValue) {
		return pValue & 0xFFFF;
	}
	
	public static final int parseBigEndianInt(final byte[] pBytes, final int pOffset) {
		return (pBytes[pOffset] & 0xFF) << 24 | (pBytes[pOffset + 1] & 0xFF) << 16 | (pBytes[pOffset + 2] & 0xFF) << 8 | (pBytes[pOffset + 3] & 0xFF);
	}
	
	public static final short parseBigEndianShort(final byte[] pBytes, final int pOffset) {
		return (short)((pBytes[pOffset] & 0xFF) << 8 | pBytes[pOffset + 1] & 0xFF);
	}
	
	public static final boolean isFlagSet(final int pValue, final int pFlag) {
		return (pValue & pFlag) != 0;
	}
	
	public static <T> T[] concatenate(final T[] pFirst, final T[] pSecond) {
		final T[] lResult = Arrays.copyOf(pFirst, pFirst.length + pSecond.length);
		System.arraycopy(pSecond, 0, lResult, pFirst.length, pSecond.length);
		return lResult;
	}
	
	public static final <T> T getFirstElementOf(final List<T> pList) {
		return pList.isEmpty() ? null : pList.get(0);
	}
	
	public static final <T> T getLastElementOf(final List<T> pList) {
		return pList.isEmpty() ? null : pList.get(pList.size() - 1);
	}
	
	public static final float getLastElementOf(final float[] pFloats) {
		return (pFloats.length < 0) ? null : pFloats[(pFloats.length - 1)];
	}
	
	public static final <T> T getFirstElementOf(final T[] pT) {
		return pT[0];
	}
	
	public static final <T> T getLastElementOf(final T[] pT) {
		return pT[pT.length - 1];
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T[] createGenericArray(final Class<T> pType, final int pSize) throws ClassCastException {
		return (T[])Array.newInstance(pType, pSize);
	}
	
	public static <T> void onReverseElements(final T[] pArray){
		/* Iterate halfway through the array. */
		for(int i = 0; i < pArray.length / 2; i++) {
			final T lT              = pArray[i];
			final int lInverseIndex = pArray.length - i - 1;
			pArray[i]               = pArray[lInverseIndex];
			pArray[lInverseIndex]   = lT;
		}
	}
	
	public static final <T> void onPushArrayElement(final T[] pArray, final T pT) {
		for(int i = pArray.length - 1; i > 0; i--) {
			pArray[i] = pArray[i - 1];
		}
		pArray[0] = pT;
	}
	
	/** TODO: Seek a cleaner implementation. **/
	public static final void onPushArrayElement(final int[] pArray, final int pT) {
		for(int i = pArray.length - 1; i > 0; i--) {
			pArray[i] = pArray[i - 1];
		}
		pArray[0] = pT;
	}
	
	/** TODO: Seek a cleaner implementation. **/
	public static final void onPushArrayElement(final float[] pArray, final float pT) {
		for(int i = pArray.length - 1; i > 0; i--) {
			pArray[i] = pArray[i - 1];
		}
		pArray[0] = pT;
	}
	
	/* Returns an element at a random index. */
	public static final <T> T getRandomElement(final T[] pArray) {
		return pArray[(int)(pArray.length * Math.random())];
	}
	
	public static final <T> int indexOf(final T[] pArray, final T pT) {
		/* Allocate a variable to track the index of the element. */
		int lIndex = DataUtils.JAVA_NULL_INDEX;
		/* Iterate the Array. */
		for(int i = 0; i < pArray.length && (lIndex == DataUtils.JAVA_NULL_INDEX); i++) {
			/* Update the Index. */
			lIndex = pArray[i].equals(pT) ? i : lIndex;
		}
		/* Return the Index. */
		return lIndex;
	}
	
	public static final void toBytes(final byte[] pReturnArray, int pOffset, final float pValue) {
		/* Convert the float to a bunch of int bits. */
		final int lFloat = Float.floatToRawIntBits(pValue);
		/* Buffer the components back into the ReturnArray. */
		pReturnArray[pOffset++] = (byte)((lFloat >> 0)  & 0xFF);
		pReturnArray[pOffset++] = (byte)((lFloat >> 8)  & 0xFF);
		pReturnArray[pOffset++] = (byte)((lFloat >> 16) & 0xFF);
		pReturnArray[pOffset++] = (byte)((lFloat >> 24) & 0xFF);
	}
	
	/* Does what it says on the tin; all whitespace is removed from the specified String and returned as a brand-spanking-new immutable String. */
	public static final String onRemoveWhitespace(final String pString) {
		/* Search for all instances of whitespace and replace these with a null String. */
		return pString.replaceAll("\\s+", "");
	}
	
	/** TODO: Implement a test for cases where System.arrayCopy() would be more preferable. (Larger arrays.) **/
	public static final void onWriteArray(final float[] pArray, final int pIndex, final float... pValues) {
		/* Iterate the Values. */
		for(int i = 0; i < pValues.length; i++) {
			/* Insert the Value at the Index. */
			pArray[pIndex + i] = pValues[i];
		}
	}
	
	/* Converts a generic List into references to it's corresponding classes for each entry. Order is preserved. */
	public static final <T> Class<?>[] toClasses(final List<T> pList) {
		/* Use the primitive implementation. */
		return DataUtils.toClasses(pList.toArray());
	}
	
	public static final <T> Class<?>[] toClasses(final T[] pArray) {
		/* Allocate an Array of the same length as the Parameterized Array. */
		final Class<?>[] lArray = new Class<?>[pArray.length];
		/* Iterate the Parameterized Array. */
		for(int i = 0; i < pArray.length; i++) {
			/* Insert the corresponding class reference. */
			lArray[i] = pArray[i].getClass();
		}
		/* Return the Array. */
		return lArray;
	}
	
	/* Counts the number of times instance of type U is contained within the array of instances of type T. */
	public static final<T, U> int count(final T[] pT, final U pU) {
		/* Allocate the Result. */
		int lCount = 0;
		/* Iterate the Array. */
		for(int i = 0; i < pT.length; i++) {
			/* Update the Result Metric. */
			lCount += DataUtils.booleanToInt(pT[i].equals(pU));
		}
		/* Return the Result. */
		return lCount;
	}
	
	/* Defines whether any instances of type U exist within the array of element type T. */
	public static final<T, U> boolean contains(final T[] pT, final U pU) {
		/* Determine if we calculate a non-zero count. */
		return (DataUtils.count(pT, pU) > 0);
	}
	
	/* Defines whether an array contains duplicate entries. */
	public static final<T> boolean isContainsDuplicates(final T[] pT) {
		/* Allocate a variable to track whether a duplicate is contained. */
		boolean lIsContainsDuplicates = false;
		/* Iterate the Array. */
		for(int i = 0; i < pT.length - 1 && !lIsContainsDuplicates; i++) {
			/* Iterate the remainder of the array. */
			for(int j = i + 1; j < pT.length && !lIsContainsDuplicates; j++) {
				/* Update the search metric. */
				lIsContainsDuplicates = pT[i].equals(pT[j]);
			}
		}
		/* Return the search metric. */
		return lIsContainsDuplicates;
	}
}