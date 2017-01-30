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

package jeva.ga;



import java.io.Serializable;
import java.util.BitSet;



/**
 * Defines an individual's genome
 * 
 * @author Nikolaos Chatzinikolaou
 */
public class Genome implements Selectable, Serializable, Cloneable
{
	private boolean[] values;
	private Double fitness = null;
	


	/**
	 * Constructs a new Genome with the specified number of bits.
	 * @param length The number of bits in this Genome.
	 */
	public Genome(int length)
	{
		values = new boolean[length];
	}
	


	/**
	 * Constructs a new Genome with the specified bits.
	 * @param _values The genome bits.
	 */
	public Genome(boolean[] _values)
	{
		values = _values;
	}
	


	/**
	 * Constructs a new Genome with the specified bits.
	 * @param bitSet The genome bits as a BitSet.
	 */
	public Genome(BitSet bitSet)
	{
		values = new boolean[bitSet.size()];
		for (int i = 0; i < bitSet.size(); i++)
			values[i] = bitSet.get(i);
	}
	


	/**
	 * Returns the number of bits in this Genome.
	 * @return The number of bits in this Genome.
	 */
	public int length()
	{
		return values.length;
	}
	


	/**
	 * Returns the bits in this Genome.
	 * @return The bits in this Genome.
	 */
	public boolean[] getValues()
	{
		return values;
	}
	


	/**
	 * Returns the bits in this Genome as a BitSet.
	 * @return The bits in this Genome as a BitSet.
	 */
	public BitSet toBitSet()
	{
		BitSet bitSet = new BitSet(values.length);
		for (int i = 0; i < values.length; i++)
			bitSet.set(i, values[i]);
		
		return bitSet;
	}
	


	/**
	 * Returns the bit at the specified index.
	 * @param i The index of the required bit.
	 * @return The ith bit.
	 */
	public boolean getValue(int i)
	{
		return values[i];
	}
	


	/**
	 * Sets the fitness of this genome.
	 * @param _fitness Thefitness value.
	 */
	public void setFitness(double _fitness)
	{
		fitness = _fitness;
	}
	


	/* (non-Javadoc)
	 * @see jeva.ga.Selectable#getFitness()
	 */
	public double getFitness()
	{
		if (!isEvaluated())
			throw new IllegalStateException("Error accessing genome fitness: The genome has not been evaluated.");
		
		return fitness;
	}
	


	/**
	 * Checks whether this Genome has been evaluated (i.e. has had its fitness score set).
	 * @return True, if the Genome has been evaluated; false otherwise.
	 */
	public boolean isEvaluated()
	{
		return fitness != null;
	}
	


	/**
	 * Damages this genome [EXPERIMENTAL]
	 */
	public void damage()
	{
		for (int i = 0; i < values.length; i++)
			values[i] = Math.random() > 0.5;
	}

	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		for (boolean value : values)
			sb.append(value ? "1" : "0");
		return sb.toString();
	}
	


	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone()
	{
		Genome clone = new Genome(this.values.length);
		System.arraycopy(this.values, 0, clone.values, 0, this.values.length);
		clone.fitness = this.fitness;
		return clone;
	}
}
