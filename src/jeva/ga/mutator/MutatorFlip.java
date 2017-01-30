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

package jeva.ga.mutator;



import jeva.ga.Genome;
import jeva.ga.Mutator;



/**
 * Bit flip mutator
 * 
 * @author Nikolaos Chatzinikolaou
 */
public class MutatorFlip implements Mutator
{
	/* (non-Javadoc)
	 * @see jeva.ga.Mutator#mutate(jeva.ga.Genome, double)
	 */
	public void mutate(Genome genome, double mr)
	{
		boolean[] values = genome.getValues();
		for (int i = 0; i < values.length; i++)
			if (Math.random() < mr)
				values[i] = !values[i];
	}
}
