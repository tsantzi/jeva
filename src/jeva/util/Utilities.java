/*
 * JEvA Copyright 2012 Nikolaos Chatzinikolaou nchatzi@gmail.com
 * 
 * This file is part of JEvA.
 * 
 * JEvA is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * JEvA is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with JEvA. If not, see <http://www.gnu.org/licenses/>.
 */

package jeva.util;



import java.util.ArrayList;
import java.util.Random;



/**
 * This class provides a number of convenience methods used throughout the library.
 * 
 * @author Nikolaos Chatzinikolaou
 */
public class Utilities
{
	public static final Random RNG = new Random();
	


	/**
	 * Chooses a number of unique integers from a range
	 * 
	 * @param min The minimum of the range
	 * @param max The maximum of the range
	 * @param n The number of integers to choose
	 * @return An array containing the selected integers
	 */
	public static Integer[] chooseRandom(int min, int max, int n)
	{
		ArrayList<Integer> idAll = new ArrayList<Integer>();
		ArrayList<Integer> idSubset = new ArrayList<Integer>();
		for (int i = min; i < max; i++)
			idAll.add(i);
		for (int i = 0; i < n; i++)
			idSubset.add(idAll.remove(RNG.nextInt(idAll.size())));
		
		return idSubset.toArray(new Integer[0]);
	}
	


	/**
	 * Complements the contens of a double[] array
	 * 
	 * @param array The array to complement.
	 */
	public static void complement(double[] array)
	{
		for (int i = 0; i < array.length; i++)
			array[i] = 1 - array[i];
	}
	


	/**
	 * Normalises the values in an array of doubles between 0 and 1.
	 * 
	 * @param array The array.
	 */
	public static void normalise(double[] array)
	{
		double min = findMin(array);
		double max = findMax(array);
		
		for (int i = 0; i < array.length; i++)
		{
			if (max == min)
				array[i] = 1.0 / (double)(array.length);
			else
				array[i] = (array[i] - min) / (max - min);
		}
	}
	


	/**
	 * Sums the values in an array of doubles.
	 * 
	 * @param array The array.
	 * @return The sum
	 */
	public static double sum(double[] array)
	{
		double sum = 0;
		for (int i = 0; i < array.length; i++)
			sum += array[i];
		return sum;
	}
	


	/**
	 * Searches in an array for the maximum value.
	 * 
	 * @param array The array.
	 * @return The maximum value.
	 */
	public static double findMax(double[] array)
	{
		double max = Double.NEGATIVE_INFINITY;
		
		for (int i = 0; i < array.length; i++)
			if (array[i] > max)
				max = array[i];
		
		return max;
	}
	


	/**
	 * Searches in an array for the minimum value.
	 * 
	 * @param array The array.
	 * @return The minimum value.
	 */
	public static double findMin(double[] array)
	{
		double min = Double.POSITIVE_INFINITY;
		
		for (int i = 0; i < array.length; i++)
			if (array[i] < min)
				min = array[i];
		
		return min;
	}
}
