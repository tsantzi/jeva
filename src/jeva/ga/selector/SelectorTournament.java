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



import java.util.Arrays;

import jeva.ga.Objective;
import jeva.ga.Selectable;
import jeva.ga.Selector;
import jeva.util.Utilities;



/**
 * Tournament selection
 * 
 * @author Nikolaos Chatzinikolaou
 */
public class SelectorTournament implements Selector
{
	private int k;
	


	/**
	 * Constructs a new SelectorTournament
	 * @param _k The tournament size
	 */
	public SelectorTournament(int _k)
	{
		k = _k;
	}
	


	/*
	 * (non-Javadoc)
	 * 
	 * @see jeva.ga.Selector#select(jeva.ga.Objective, jeva.ga.Selectable[], int)
	 */
	public Selectable[] select(Objective objective, Selectable[] selectables, int n)
	{
		// NB: Assumes selectables are sorted
		
		Selectable[] selected = new Selectable[n];
		
		for (int index = 0; index < n; index++)
		{
			Selectable[] contestants = new Selectable[k];
			Integer[] selectedIndices = Utilities.chooseRandom(0, selectables.length, k);
			
			for (int i = 0; i < k; i++)
				contestants[i] = selectables[selectedIndices[i]];
			
			Arrays.sort(contestants, objective);
			selected[index] = contestants[0];
		}
		
		return selected;
	}
}
