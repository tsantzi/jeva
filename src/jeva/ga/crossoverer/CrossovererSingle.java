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

package jeva.ga.crossoverer;



import jeva.ga.Crossoverer;
import jeva.ga.Genome;



/**
 * Single-point crossoverer.
 * 
 * @author Nikolaos Chatzinikolaou
 */
public class CrossovererSingle implements Crossoverer
{
	/* (non-Javadoc)
	 * @see jeva.ga.Crossoverer#crossover(jeva.ga.Genome, jeva.ga.Genome)
	 */
	public Genome crossover(Genome parent1, Genome parent2)
	{
		Genome child = new Genome(parent1.length());
		int cp = (int)(Math.random() * child.length());
		System.arraycopy(parent1.getValues(), 0, child.getValues(), 0, cp);
		System.arraycopy(parent2.getValues(), cp, child.getValues(), cp, child.length() - cp);
		
		return child;
	}
}
