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



import java.util.ArrayList;



/**
 * This class implements the GA
 * 
 * @author Nikolaos Chatzinikolaou
 */
public class Breeder
{
	private Evaluator evaluator;
	private Objective objective;
	private Parameters parameters;
	private int nEvaluationThreads = 1;
	private int generation = 0;
	private int stableGenerations = 0;
	private Population lastPopulation = null;
	private Population bestPopulation = null;
	private ArrayList<Double> fitnessHistoryBest = new ArrayList<Double>();
	private ArrayList<Double> fitnessHistoryMean = new ArrayList<Double>();
	private ArrayList<Double> fitnessHistoryWorst = new ArrayList<Double>();
	private int historyLength = 1;
	


	/**
	 * Constructs a new Breeder instance with the specified parameters.
	 * @param _evaluator The evaluation function to use.
	 * @param _objective The optimisation objective.
	 * @param _parameters The set of GA parameters to use.
	 * @param _nEvaluationThreads The number of concurrent evaluation threads.
	 * @param _historyLength The maximum size of history to retain.
	 */
	public Breeder(Evaluator _evaluator, Objective _objective, Parameters _parameters, int _nEvaluationThreads, int _historyLength)
	{
		evaluator = _evaluator;
		objective = _objective;
		parameters = _parameters;
		nEvaluationThreads = _nEvaluationThreads;
		historyLength = _historyLength;
	}
	


	/**
	 * Constructs a new Breeder instance with the specified parameters.
	 * @param _evaluator The evaluation function to use.
	 * @param _objective The optimisation objective.
	 * @param _parameters The set of GA parameters to use.
	 */
	public Breeder(Evaluator _evaluator, Objective _objective, Parameters _parameters)
	{
		this(_evaluator, _objective, _parameters, Runtime.getRuntime().availableProcessors(), 0);
	}
	


	/**
	 * Returns the GA parameters.
	 * @return The GA parameters.
	 */
	public Parameters getParameters()
	{
		return parameters;
	}
	


	/**
	 * Returns the current generation.
	 * @return The current generation.
	 */
	public synchronized int getGeneration()
	{
		return generation;
	}
	


	/**
	 * Returns the number of stable generations.
	 * @return The number of stable generations.
	 */
	public synchronized int getStableGenerations()
	{
		return stableGenerations;
	}
	


	/**
	 * Returns the last (current) population.
	 * @return The last (current) population.
	 */
	public synchronized Population getLastPopulation()
	{
		return lastPopulation;
	}
	


	/**
	 * Returns the best (stored) population.
	 * @return The best (stored) population.
	 */
	public synchronized Population getBestPopulation()
	{
		return bestPopulation;
	}
	


	/**
	 * Returns the history of best fitnesses as an ArrayList.
	 * @return The history of best fitnesses.
	 */
	public synchronized ArrayList<Double> getFitnessHistoryBest()
	{
		return fitnessHistoryBest;
	}
	


	/**
	 * Returns the history of mean fitnesses as an ArrayList.
	 * @return The history of mean fitnesses.
	 */
	public synchronized ArrayList<Double> getFitnessHistoryMean()
	{
		return fitnessHistoryMean;
	}
	


	/**
	 * Returns the history of worst fitnesses as an ArrayList.
	 * @return The history of worst fitnesses.
	 */
	public synchronized ArrayList<Double> getFitnessHistoryWorst()
	{
		return fitnessHistoryWorst;
	}
	


	/**
	 * Steps the GA for one generation.
	 * @throws BreederException If something goes wrong
	 */
	public synchronized void step() throws BreederException
	{
		// Create baby population
		Population babyPopulation = (generation == 0 ? createNewPopulation() : breedNewPopulation());
		
		// Evaluate baby population
		evaluatePopulation(babyPopulation);
		
		// Update populations
		lastPopulation = babyPopulation;
		if (bestPopulation == null)
			bestPopulation = babyPopulation;
		else if (objective.compare(babyPopulation.getBestGenome(), bestPopulation.getBestGenome()) == -1)
		{
			bestPopulation = babyPopulation;
			stableGenerations = 0;
		}
		else
			stableGenerations++;
		
		// Update fitness history
		pushHistory();
		
		// Update generation
		generation++;
	}
	


	/**
	 * Skips one generation.
	 */
	public synchronized void skip()
	{
		// Update populations (not much to do here)
		stableGenerations++;
		
		// Update fitness history
		pushHistory();
		
		// Update generation
		generation++;
		
		// Yield
		try
		{
			Thread.sleep(10);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	


	/**
	 * Adds a new history entry (set), deletes an old one if necessary.
	 */
	private void pushHistory()
	{
		fitnessHistoryBest.add(lastPopulation.getFitnessBest());
		fitnessHistoryMean.add(lastPopulation.getFitnessMean());
		fitnessHistoryWorst.add(lastPopulation.getFitnessWorst());
		if (fitnessHistoryBest.size() > historyLength)
		{
			fitnessHistoryBest.remove(0);
			fitnessHistoryMean.remove(0);
			fitnessHistoryWorst.remove(0);
		}
	}
	


	/**
	 * Creates a new Population.
	 * @return The new Population.
	 * @throws BreederException If something goes wrong
	 */
	private Population createNewPopulation() throws BreederException
	{
		Population babyPopulation = new Population(objective);
		
		if (!parameters.contains(Parameters.GENOME_LENGTH))
			throw new BreederException("The genome length has not been set");
		int populationSize = parameters.getInt(Parameters.POPULATION_SIZE);
		int genomeLength = parameters.getInt(Parameters.GENOME_LENGTH);
		Initializer initializer = (Initializer)parameters.getOperator(Parameters.INITIALIZER);
		babyPopulation.initialise(populationSize, genomeLength, initializer);
		
		return babyPopulation;
	}
	


	/**
	 * Breeds the next generation Population.
	 * @return The next generation Population.
	 */
	private Population breedNewPopulation()
	{
		// Create next population
		Population babyPopulation = new Population(objective);
		
		// Copy elite population
		int populationSize = parameters.getInt(Parameters.POPULATION_SIZE);
		int eliteSize = parameters.getInt(Parameters.ELITE_SIZE);
		int babyIndex = 0;
		for (; babyIndex < eliteSize && babyIndex < populationSize; babyIndex++)
			babyPopulation.addGenome((Genome)lastPopulation.getGenome(babyIndex).clone());
		
		// Breed non-elite population
		Selectable[] selectables = lastPopulation.getGenomes().toArray(new Selectable[] {});
		Selector selector = (Selector)parameters.getOperator(Parameters.SELECTOR);
		Crossoverer crossoverer = (Crossoverer)parameters.getOperator(Parameters.CROSSOVERER);
		Mutator mutator = (Mutator)parameters.getOperator(Parameters.MUTATOR);
		for (; babyIndex < populationSize; babyIndex++)
		{
			// Select parents
			Selectable[] parents = selector.select(objective, selectables, 2);
			Genome parent1 = (Genome)parents[0];
			Genome parent2 = (Genome)parents[1];
			
			// Perform crossover
			Genome child;
			if (Math.random() < parameters.getDouble(Parameters.CROSSOVER_RATE))
				child = crossoverer.crossover(parent1, parent2);
			else
				child = (Genome)parent1.clone();
			
			// Perform mutation
			mutator.mutate(child, parameters.getDouble(Parameters.MUTATION_RATE));
			
			babyPopulation.addGenome(child);
		}
		
		return babyPopulation;
	}
	


	/**
	 * Evaluates the specified Population.
	 * @param population The Population to evaluate.
	 */
	private void evaluatePopulation(Population population)
	{
		if (nEvaluationThreads > 1)
			evaluatePopulationMany(population);
		else
			evaluatePopulationSingle(population);
		
		// Sort the newly evaluated population (selector needs this, also sorting makes it easier to access best & worst individuals).
		population.sort();
	}
	


	/**
	 * Evaluates the specified Population (using a single thread).
	 * @param population The Population to evaluate.
	 */
	private void evaluatePopulationSingle(Population population)
	{
		for (int i = 0; i < population.getSize(); i++)
			evaluator.evaluateGenome(population.getGenome(i));
	}
	


	/**
	 * Evaluates the specified Population (using multiple concurent threads).
	 * @param population The Population to evaluate.
	 */
	private void evaluatePopulationMany(Population population)
	{
		// Create the evaluation threads 
		EvaluationThread[] evaluationThreads = new EvaluationThread[nEvaluationThreads];
		for (int i = 0; i < nEvaluationThreads; i++)
			evaluationThreads[i] = new EvaluationThread();
		
		// Fill the evaluation threads
		for (int i = 0; i < population.getSize(); i++)
			evaluationThreads[i % nEvaluationThreads].addGenome(population.getGenome(i));
		
		// Run the evaluation threads
		for (int i = 0; i < nEvaluationThreads; i++)
			evaluationThreads[i].start();
		
		// Wait for evaluation threads to finish
		for (int i = 0; i < nEvaluationThreads; i++)
		{
			try
			{
				evaluationThreads[i].join();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
	


	/**
	 * This Thread subclass performs evaluations.
	 */
	private class EvaluationThread extends Thread
	{
		private ArrayList<Genome> genomes = new ArrayList<Genome>();
		


		/**
		 * Add a Genome in the list for evaluation.
		 * @param genome The Genome to evaluate.
		 */
		public void addGenome(Genome genome)
		{
			genomes.add(genome);
		}
		


		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Thread#run()
		 */
		public void run()
		{
			for (int i = 0; i < genomes.size(); i++)
				evaluator.evaluateGenome(genomes.get(i));
		}
	}
}
