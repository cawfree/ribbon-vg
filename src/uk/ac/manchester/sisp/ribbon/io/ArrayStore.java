package uk.ac.manchester.sisp.ribbon.io;

import java.lang.reflect.Array;
import java.util.Arrays;

import uk.ac.manchester.sisp.ribbon.common.IDisposable;
import uk.ac.manchester.sisp.ribbon.common.IResetable;
import uk.ac.manchester.sisp.ribbon.utils.DataUtils;
import uk.ac.manchester.sisp.ribbon.utils.MathUtils;

public class ArrayStore<T> implements IDisposable, IResetable{
	
	public static class Integer extends ArrayStore<int[]>    { public final void store(final    int[] pArray) { this.store(pArray, 0, pArray.length); } @Override protected final    int[] onResizeArray(final    int[] pCurrentArray, final Class<?> pComponentType, final int pCapacity) { return DataUtils.isNotNull(pCurrentArray) ? Arrays.copyOf(pCurrentArray, pCapacity) : new    int[pCapacity]; } @Override protected final    int[] onSegmentArray(final    int[] pCurrentArray, final int pNumberOfElements) { return Arrays.copyOf(pCurrentArray, pNumberOfElements); } }
	public static class Byte    extends ArrayStore<byte[]>   { public final void store(final   byte[] pArray) { this.store(pArray, 0, pArray.length); } @Override protected final   byte[] onResizeArray(final   byte[] pCurrentArray, final Class<?> pComponentType, final int pCapacity) { return DataUtils.isNotNull(pCurrentArray) ? Arrays.copyOf(pCurrentArray, pCapacity) : new   byte[pCapacity]; } @Override protected final   byte[] onSegmentArray(final   byte[] pCurrentArray, final int pNumberOfElements) { return Arrays.copyOf(pCurrentArray, pNumberOfElements); } }
	public static class Char    extends ArrayStore<char[]>   { public final void store(final   char[] pArray) { this.store(pArray, 0, pArray.length); } @Override protected final   char[] onResizeArray(final   char[] pCurrentArray, final Class<?> pComponentType, final int pCapacity) { return DataUtils.isNotNull(pCurrentArray) ? Arrays.copyOf(pCurrentArray, pCapacity) : new   char[pCapacity]; } @Override protected final   char[] onSegmentArray(final   char[] pCurrentArray, final int pNumberOfElements) { return Arrays.copyOf(pCurrentArray, pNumberOfElements); } }
	public static class Float   extends ArrayStore<float[]>  { public final void store(final  float[] pArray) { this.store(pArray, 0, pArray.length); } @Override protected final  float[] onResizeArray(final  float[] pCurrentArray, final Class<?> pComponentType, final int pCapacity) { return DataUtils.isNotNull(pCurrentArray) ? Arrays.copyOf(pCurrentArray, pCapacity) : new  float[pCapacity]; } @Override protected final  float[] onSegmentArray(final  float[] pCurrentArray, final int pNumberOfElements) { return Arrays.copyOf(pCurrentArray, pNumberOfElements); } }
	public static class Double  extends ArrayStore<double[]> { public final void store(final double[] pArray) { this.store(pArray, 0, pArray.length); } @Override protected final double[] onResizeArray(final double[] pCurrentArray, final Class<?> pComponentType, final int pCapacity) { return DataUtils.isNotNull(pCurrentArray) ? Arrays.copyOf(pCurrentArray, pCapacity) : new double[pCapacity]; } @Override protected final double[] onSegmentArray(final double[] pCurrentArray, final int pNumberOfElements) { return Arrays.copyOf(pCurrentArray, pNumberOfElements); } }
	
	private static final boolean isInsufficientCapacity(final int pCapacity, final int pNumberOfElements, final int pLength) {
		return pCapacity - pNumberOfElements < pLength;
	}
	
	/* Member Variables. */
	private T   mT;
	private int mNumberOfElements;
	private int mTotalCapacity;
	
	/** TODO: Implement a type check for array types only. **/
	public ArrayStore() {
		/* Initialize Member Variables. */
		this.mT                = null;
		this.mNumberOfElements = 0;
		this.mTotalCapacity    = 0;
	}
	
	public final ArrayStore<T> store(final T pT, final int pOffset, final int pLength) {
		/* Determine if the array must be resized. */
		if(ArrayStore.isInsufficientCapacity(this.getTotalCapacity(), this.getNumberOfElements(), pLength)) {
			/* Calculate the new capacity of the array. */
			final int lCapacity = MathUtils.toNearestPowerOfTwo(this.getTotalCapacity() + pLength);
			/* Re-assign the array. */
			this.mT = this.onResizeArray(this.getBackingArray(), pT.getClass().getComponentType(), lCapacity);
			/* Update the capacity. */
			this.mTotalCapacity = lCapacity;
		}
		/* Append the array data. */
		System.arraycopy(pT, pOffset, this.getBackingArray(), this.getNumberOfElements(), pLength);
		/* Offset the pointer. */
		this.mNumberOfElements += pLength;
		/* Return the ArrayStore. */
		return this;
	}

	@SuppressWarnings("unchecked")
	protected T onResizeArray(final T pCurrentArray, final Class<?> pComponentType, final int pCapacity) {
		/* Resize the array. */
		final T lT = (T)Array.newInstance(pComponentType, pCapacity);
		/* If the array has yet to be initialized, do not attempt to copy over array elements. */
		if(DataUtils.isNotNull(pCurrentArray)) {
			/* Replace array data. */
			System.arraycopy(pCurrentArray, 0, lT, 0, this.getNumberOfElements());
		}
		/* Return the allocated array. */
		return lT;
	}
	
	public final T onProduceArray() {
		/* Segment the array. */
		final T lT             = this.onSegmentArray(this.getBackingArray(), this.getNumberOfElements());
		/* Reset the NumberOfElements count. */
		this.mNumberOfElements = 0;
		/* Return the array. */
		return lT;
	}
	
	@SuppressWarnings("unchecked")
	protected T onSegmentArray(final T pCurrentArray, final int pNumberOfElements) {
		/* Allocate large enough memory to store the array to return. */
		final T lReturnArray = (T)Array.newInstance(pCurrentArray.getClass().getComponentType(), pNumberOfElements);
		/* Copy the contents of the CurrentArray into the ReturnArray. */
		System.arraycopy(pCurrentArray, 0, lReturnArray, 0, pNumberOfElements);
		/* Return the array. */
		return lReturnArray;
	}
	
	private final T getBackingArray() {
		return this.mT;
	}
	
	public final int getNumberOfElements() {
		return this.mNumberOfElements;
	}
	
	public final int getTotalCapacity() {
		return this.mTotalCapacity;
	}

	@Override
	public final void dispose() {
		/* Nullify dependent variables. */
		this.mT = null;
	}

	@Override
	public final void onReset() {
		/* Reset the ArrayStore. */
		this.mT                = null;
		this.mNumberOfElements = 0;
		this.mTotalCapacity    = 0;
	}
	
}