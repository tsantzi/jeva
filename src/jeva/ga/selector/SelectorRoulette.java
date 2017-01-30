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

package jeva.ga.selector;



import jeva.ga.Objective;
import jeva.ga.Selectable;
import jeva.ga.Selector;



/**
 * Roulette-wheel selection
 * 
 * @author Nikolaos Chatzinikolaou
 */
public class SelectorRoulette implements Selector
{
	private Selectable[] storedSelectables;
	private double[] summedSelectionProbabilities;
	


	/*
	 * (non-Javadoc)
	 * 
	 * @see jeva.ga.Selector#select(jeva.ga.Objective, jeva.ga.Selectable[], int)
	 */
	public Selectable[] select(Objective objective, Selectable[] selectables, int n)
	{
		// NB: Assumes selectables are sorted
		
		storedSelectables = selectables;
		
		// Copy fitnesses so that we don't have to invoke getFitness() twice for each individual (in case it's slow)
		double[] fitnesses = new double[storedSelectables.length];
		double fitnessesSum = 0;
		for (int i = 0; i < fitnesses.length; i++)
		{
			// Check if indeed sorted
			if (i > 0 && (objective.compare(storedSelectables[i - 1], storedSelectables[i]) == 1))
			{
				System.out.println("ERROR: Selectables not sorted: " + storedSelectables[i - 1].getFitness() + " < " + storedSelectables[i].getFitness());
				System.exit(-1);
			}
			
			fitnesses[i] = storedSelectables[i].getFitness();
			fitnessesSum += fitnesses[i];
		}
		
		// Calculate probabilities
		summedSelectionProbabilities = new double[storedSelectables.length];
		for (int i = 0; i < summedSelectionProbabilities.length; i++)
			summedSelectionProbabilities[i] = fitnesses[i] / fitnessesSum + (i == 0 ? 0 : summedSelectionProbabilities[i - 1]);
		
		// Perform n selections
		Selectable[] selected = new Selectable[n];
		for (int index = 0; index < n; index++)
			selected[index] = select();
		
		return selected;
	}
	


	/**
	 * Perform a single selection
	 * @return The selected individual
	 */
	private Selectable select()
	{
		double p = Math.random();
		for (int i = 0; i < summedSelectionProbabilities.length; i++)
			if (p < summedSelectionProbabilities[i])
				return storedSelectables[i];
		
		return storedSelectables[storedSelectables.length - 1];
	}
}
