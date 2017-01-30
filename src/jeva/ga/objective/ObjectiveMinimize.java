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

package jeva.ga.objective;



import jeva.ga.Objective;
import jeva.ga.Selectable;



/**
 * Minimization objective
 * 
 * @author Nikolaos Chatzinikolaou
 */
public class ObjectiveMinimize implements Objective
{
	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Selectable o1, Selectable o2)
	{
		// Order is reverse (best = smallest = first)

		if (o1.getFitness() < o2.getFitness())
			return -1;
		else if (o1.getFitness() > o2.getFitness())
			return 1;
		else
			return 0;
	}
}
