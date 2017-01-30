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

package jeva.ga;



import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

import jeva.ga.crossoverer.CrossovererSingle;
import jeva.ga.initializer.InitializerRandom;
import jeva.ga.mutator.MutatorFlip;
import jeva.ga.selector.SelectorRouletteRebased;



/**
 * Aggregates all GA parameters (including operators) in a single structure
 * 
 * @author Nikolaos Chatzinikolaou
 */
public class Parameters implements Serializable
{
	public static final String INITIALIZER = "INITIALIZER";
	public static final String GENOME_LENGTH = "GENOME_LENGTH";
	public static final String POPULATION_SIZE = "POPULATION_SIZE";
	public static final String ELITE_SIZE = "ELITE_SIZE";
	public static final String CROSSOVER_RATE = "CROSSOVER_RATE";
	public static final String MUTATION_RATE = "MUTATION_RATE";
	public static final String CROSSOVERER = "CROSSOVERER";
	public static final String MUTATOR = "MUTATOR";
	public static final String SELECTOR = "SELECTOR";
	
	private transient PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	private HashMap<String, Object> parameters = new HashMap<String, Object>();
	


	/**
	 * Constructs a new Parameters object with the default values
	 */
	public Parameters()
	{
		put(INITIALIZER, new InitializerRandom());
		put(POPULATION_SIZE, 20);
		put(ELITE_SIZE, 4);
		put(CROSSOVER_RATE, 0.6);
		put(MUTATION_RATE, 0.001);
		put(CROSSOVERER, new CrossovererSingle());
		put(MUTATOR, new MutatorFlip());
		put(SELECTOR, new SelectorRouletteRebased());
	}
	


	/**
	 * Randomizes parameters [EXPERIMENTAL]
	 */
	public void damage()
	{
		put(MUTATION_RATE, Math.random());
		put(CROSSOVER_RATE, Math.random());
	}
	


	/**
	 * Checks whether the parameter with the specified key has been set.
	 * @param key The parameter to check
	 * @param value True, if the parameter with the specified key has been set; false otherwise
	 */
	public boolean contains(String key)
	{
		return parameters.containsKey(key);
	}
	


	/**
	 * Adds/updates a parameter
	 * @param key The parameter to add/update
	 * @param value The new parameter value
	 */
	public void put(String key, Object value)
	{
		Object old = parameters.get(key);
		parameters.put(key, value);
		propertyChangeSupport.firePropertyChange(key, old, value);
	}
	


	/**
	 * Returns the operator Object having the specified key
	 * @param key The key of the operator
	 * @return The operator Object
	 */
	public Object getOperator(String key)
	{
		return parameters.get(key);
	}
	


	/**
	 * Returns an integer parameter value having the specified key
	 * @param key The key of the parameter
	 * @return The value
	 */
	public int getInt(String key)
	{
		return (Integer)parameters.get(key);
	}
	


	/**
	 * Returns a double parameter value having the specified key
	 * @param key The key of the parameter
	 * @return The value
	 */
	public double getDouble(String key)
	{
		return (Double)parameters.get(key);
	}
	


	/**
	 * Returns a string parameter value having the specified key
	 * @param key The key of the parameter
	 * @return The value
	 */
	public String getString(String key)
	{
		return (String)parameters.get(key);
	}
	


	/**
	 * Returns a String representation of this object
	 * @return A String representation of this object
	 */
	public String toString()
	{
		return parameters.toString();
	}
	


	/**
	 * Returns a clone of this object
	 * @return A clone of this object
	 */
	public Object clone()
	{
		Parameters clone = new Parameters();
		Set<String> keys = this.parameters.keySet();
		for (String key : keys)
			clone.put(key, this.parameters.get(key));
		return clone;
	}
	


	/**
	 * Adds a PropertyChangeListener for listening to changes in parameter values
	 * @param listener The PropertyChangeListener to add
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener)
	{
		propertyChangeSupport.addPropertyChangeListener(listener);
	}
	


	/**
	 * Removes the specified PropertyChangeListener
	 * @param listener The PropertyChangeListener to remove
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener)
	{
		propertyChangeSupport.removePropertyChangeListener(listener);
	}
}
