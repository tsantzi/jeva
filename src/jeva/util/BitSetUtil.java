/*
 * JEvA Copyright 2012 Nikolaos Chatzinikolaou nchatzi@gmail.com
 * 
 * This file is part of JEvA.
 * 
 * JEvA is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * JEvA is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with JEvA. If not, see <http://www.gnu.org/licenses/>.
 */

package jeva.util;
import java.util.BitSet;



/**
 * This utility class contains functions to encode <tt>int</tt> or <tt>double</tt> values in a <tt>BitSet</tt>.
 * 
 * @see BitSet
 */
public class BitSetUtil
{
	/**
	 * Extracts an integer value from the given <tt>BitSet</tt> from offset of the given amount of bits (given by <tt>lenght</tt>).
	 * 
	 * @param bitSet the bit set to operate on
	 * @param offset the offset in the bit set
	 * @param length the length of the bit string that represents the encoded int
	 * @return the extracted integer value
	 * @throws RuntimeException if <tt>length</tt> is greater than the bit-count of int
	 */
	public static int bitSetToInt(BitSet bitSet, int offset, int length)
	{
		// checking the bit length
		if (length > Integer.SIZE)
			throw new RuntimeException("You can not set a higher length than " + Integer.SIZE + " bits.");
		
		int newValue = 0;
		int mask = 1;
		for (int i = 0; i < length - 1; ++i, mask <<= 1)
			if (bitSet.get(offset + i))
				newValue |= mask;
		
		if (bitSet.get(offset + length - 1))
			newValue *= -1;
		
		return newValue;
	}
	


	/**
	 * Encodes the given integer value in the <tt>BitSet</tt> from the position offset by the given amount of bits.
	 * 
	 * @param bitSet the <tt>BitSet</tt> to operate on
	 * @param offset the offset in the bit set
	 * @param length the length of the bit string that should represent the given value
	 * @param value the value to encode in the bit set
	 * @return the modified bit set
	 * @throws RuntimeException if <tt>length</tt> is greater than the amount of bits in an integer value
	 * @throws RuntimeException if <tt>value</tt> is greather than the value encodeable by the given amount of bits or if value == Integer.MIN_VALUE (no absolute value awailable as int)
	 */
	public static BitSet intToBitSet(BitSet bitSet, int offset, int length, int value)
	{
		// checking the bit length
		if (length > Integer.SIZE)
			throw new RuntimeException("You can not set a higher length than " + Integer.SIZE + " bits.");
		
		// checking whether the value fits into the bit string of length - 1
		int absValue = Math.abs(value);
		if (absValue > Math.pow(2.0, length - 1 - 1) * 2 - 1 || value == Integer.MIN_VALUE)
			throw new RuntimeException("The value of " + value + " does not fit into a bit string of " + (length - 1) + " bits.");
		
		// setting all bits to zero
		bitSet.clear(offset, offset + length - 1);
		
		// setting up the number in reverse order
		int mask = 1;
		for (int i = 0; i < length; ++i, mask <<= 1)
			if ((mask & absValue) > 0)
				bitSet.set(offset + i);
		
		// setting up the sign
		if (value < 0)
			bitSet.set(offset + length - 1);
		
		return bitSet;
	}
	


	/**
	 * Extracts a double value from <tt>bitSet</tt> from the offset position to the given offset+length-1. It does the reverse scaling done by <tt>public static BitSet doubleToBitSet(BitSet bitSet, int offset, int length, double minValue,
	 double maxValue, double value)</tt>.
	 * 
	 * @param bitSet the bit set to operate on
	 * @param offset the offset posistion in the bit set
	 * @param length the number of bit that represent the value
	 * @param minValue the same value set in <tt>doubleToBitSet(...)</tt>
	 * @param maxValue the same value set in <tt>doubleToBitSet(...)</tt>
	 * @return the back scaled value from the bit set
	 * @see public static BitSet doubleToBitSet(BitSet bitSet, int offset, int length, double minValue, double maxValue, double value)
	 * @throws RuntimeException if the number of bits given by <tt>length</tt> is greater than Long.SIZE
	 */
	public static double bitSetToDouble(BitSet bitSet, int offset, int length, double minValue, double maxValue)
	{
		// checking the bit length
		if (length > Long.SIZE)
			throw new RuntimeException("You can not set a higher length than " + Long.SIZE + " bits.");
		
		// getting the value as natural number from the bitSet
		long longValue = 0;
		long mask = 1;
		for (int i = 0; i < length; ++i, mask <<= 1)
			if (bitSet.get(offset + i))
				longValue |= mask;
		
		// hint: without sign!
		long max = (int)(Math.pow(2.0, length - 1) * 2 - 1);
		
		// returning the scaled back double value
		return (longValue / (max / (maxValue - minValue)) + minValue);
	}
	


	/**
	 * Encodes a double value in a <tt>BitSet</tt> by scaling the interval (given by minValue and maxValue) to the co-domain of the natual number representable by n bits (n=lenght). <tt>minValue</tt> and <tt>maxValue</tt> are necessary to calculate a scaling function between the two co-domains.
	 * 
	 * @param bitSet the <tt>BitSet</tt> to operate on
	 * @param offset the offset in the given bit set
	 * @param length the number of bits used to encode <tt>value</tt>
	 * @param minValue minimal value of <tt>value</tt>
	 * @param maxValue maximal value of <tt>value</tt>
	 * @param value the value to encode
	 * @return the manipulated bit set
	 * @throws RuntimeException if the value of <tt>value</tt> does not fit in ]minValue, maxValue[
	 * @throws RuntimeException if the number of bits (<tt>length</tt>) is bigger than <tt>Long.SIZE</tt>
	 */
	public static BitSet doubleToBitSet(BitSet bitSet, int offset, int length, double minValue, double maxValue, double value)
	{
		if (value < minValue || value > maxValue)
			throw new RuntimeException("Value out of bounds.");
		
		if (length > Long.SIZE)
			throw new RuntimeException("You can not set a higher length than " + Long.SIZE + " bits.");
		
		// scaling function:
		// x element of ]minValue, maxValue[
		// ==> f(x) element of ]0, (2^(length-1)*2 -1)[
		//
		// so:
		// f(x) = (x-minValue) * m
		// m = max / (maxValue - minValue)
		
		double intervalLength = maxValue - minValue;
		// hint: without sign!
		int max = (int)(Math.pow(2.0, length - 1) * 2 - 1);
		
		// scaling the input interval to the output interval of 0...2^(length-1)*2 -1
		long scaledValue = Math.round((value - minValue) * max / intervalLength);
		
		// putting the scaled value to the bitset (without any sign!)
		long mask = 1;
		for (int i = 0; i < length; ++i, mask <<= 1)
			if ((mask & scaledValue) > 0)
				bitSet.set(offset + i);
		
		return bitSet;
	}
	


	/**
	 * Temporary <tt>BitSet</tt> for the gray en-/decoding process. This is just for speeding up.
	 */
	static private BitSet tmpBitSet = new BitSet();
	


	/**
	 * Converts the <tt>BitSet</tt> from the binary representation to the gray-code presentation.
	 * 
	 * @param bitSet the <tt>BitSet</tt> to operate on
	 */
	public static void doGrayCoding(BitSet bitSet)
	{
		// pseudo code
		// ---------------------------
		// G[n] = B[n]
		// for i = n-1 downto 0
		// ....G[i] = B[i+1] XOR B[i]
		
		synchronized (tmpBitSet)
		{
			int n = bitSet.size() - 1;
			
			// doing the gray coding
			tmpBitSet.set(n, bitSet.get(n));
			for (int i = n - 1; i >= 0; --i)
				tmpBitSet.set(i, (bitSet.get(i + 1) ^ bitSet.get(i)));
			
			// copying the result to the original BitSet
			for (int i = 0; i < bitSet.length(); ++i)
				bitSet.set(i, tmpBitSet.get(i));
		}
	}
	


	/**
	 * Converts the <tt>BitSet</tt> from the gray-code representation to the binary presentation.
	 * 
	 * @param bitSet the <tt>BitSet</tt> to operate on
	 */
	public static void doInvGrayCoding(BitSet bitSet)
	{
		// pseudo code
		// ---------------------------
		// B[n] = G[n]
		// for i = n-1 downto 0
		// ....B[i] = B[i+1] XOR G[i]
		
		synchronized (tmpBitSet)
		{
			int n = bitSet.size() - 1;
			
			// doing the encoding
			tmpBitSet.set(n, bitSet.get(n));
			for (int i = n - 1; i >= 0; --i)
				tmpBitSet.set(i, (tmpBitSet.get(i + 1) ^ bitSet.get(i)));
			
			// copying the result to the original BitSet
			for (int i = 0; i < bitSet.length(); ++i)
				bitSet.set(i, tmpBitSet.get(i));
		}
	}
	
	public static void bitSetToBytes(BitSet bitSet)
	{
		
	}
}
