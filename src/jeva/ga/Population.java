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
import java.util.ArrayList;
import java.util.Collections;



/**
 * Defines a population of individuals
 * 
 * @author Nikolaos Chatzinikolaou
 */
public class Population implements Serializable
{
	private Objective objective;
	private ArrayList<Genome> genomes = new ArrayList<Genome>();
	


	/**
	 * Creates a new Population object with the specified optimisation objective
	 * @param _objective The optimisation objective
	 */
	public Population(Objective _objective)
	{
		objective = _objective;
	}
	


	/**
	 * Initialises the Population object
	 * @param size The population size
	 * @param genomeLength The genome length in bits
	 * @param initializer The initializer operator to use
	 */
	public void initialise(int size, int genomeLength, Initializer initializer)
	{
		genomes.clear();
		
		for (int i = 0; i < size; i++)
		{
			Genome newGenome = new Genome(genomeLength);
			initializer.initialize(newGenome);
			genomes.add(newGenome);
		}
	}
	


	/**
	 * Applies damage to the population's individual genomes [EXPERIMENTAL]
	 */
	public void damage()
	{
		for (Genome genome : genomes)
			genome.damage();
	}
	


	/**
	 * Returns the population size
	 * @return The population size
	 */
	public int getSize()
	{
		return genomes.size();
	}
	


	/**
	 * Returns the Genome at the specified index
	 * @param i The index of the Genome
	 * @return The ith Genome
	 */
	public Genome getGenome(int i)
	{
		return genomes.get(i);
	}
	


	/**
	 * Updates the Genome at the specified index 
	 * @param i The index of the Genome
	 * @param genome The new Genome
	 */
	public void setGenome(int i, Genome genome)
	{
		genomes.set(i, genome);
	}
	


	/**
	 * Returns an ArrayList with all the Genomes in this Population 
	 * @return An ArrayList with all the Genomes in this Population
	 */
	public ArrayList<Genome> getGenomes()
	{
		return genomes;
	}
	


	/**
	 * Adds the specified Genome into this Population
	 * @param genome The new Genome to add
	 */
	public void addGenome(Genome genome)
	{
		genomes.add(genome);
	}
	


	/**
	 * Returns the Genome with the best fitness in this population
	 * @return The Genome with the best fitness
	 */
	public Genome getBestGenome()
	{
		return genomes.get(0);
	}
	


	/**
	 * Returns the Genome with the worst fitness in this population
	 * @return The Genome with the worst fitness
	 */
	public Genome getWorstGenome()
	{
		return genomes.get(genomes.size() - 1);
	}
	


	/**
	 * Returns the fitness score of the best Genome in this population.
	 * @return The fitness score of the best Genome in this population.
	 */
	public double getFitnessBest()
	{
		return getBestGenome().getFitness();
	}
	


	/**
	 * Returns the mean fitness of all the Genomes in this Population.
	 * @return The mean fitness of all the Genomes in this Population.
	 */
	public double getFitnessMean()
	{
		double fitness = 0;
		for (Genome genome : genomes)
			fitness += genome.getFitness();
		fitness /= (double)(genomes.size());
		return fitness;
	}
	


	/**
	 * Returns the fitness score of the worst Genome in this population.
	 * @return The fitness score of the worst Genome in this population.
	 */
	public double getFitnessWorst()
	{
		return getWorstGenome().getFitness();
	}
	


	/**
	 * Sorts the Genomes in this Population according to the optimisation objective.
	 */
	public void sort()
	{
		Collections.sort(genomes, objective);
	}
	


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < genomes.size(); i++)
			sb.append(i + ":\t" + genomes.get(i).getFitness() + "\t" + genomes.get(i).toString() + "\n");
		return sb.toString();
	}
	


	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone()
	{
		Population clone = new Population(this.objective);
		clone.genomes = new ArrayList<Genome>();
		for (Genome genome : this.genomes)
			clone.genomes.add((Genome)genome.clone());
		return clone;
	}
}
