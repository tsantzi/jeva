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

package jeva.ga.evaluator;



import java.util.BitSet;

import jeva.ga.Evaluator;
import jeva.ga.Genome;
import jeva.util.BitSetUtil;




/**
 * Sphere (De Jong F1) function evaluator
 * 
 * @author Nikolaos Chatzinikolaou
 */
public class EvaluatorSphere implements Evaluator
{
	private static final double VAR_MIN = -5.12;
	private static final double VAR_MAX = 5.12;
	
	int nVars = 0;
	int nVarBits = 0;
	


	/**
	 * Constructs a new EvaluatorSphere
	 * @param _nVars The number of variables
	 * @param _nVarBits The number of bits per variable
	 */
	public EvaluatorSphere(int _nVars, int _nVarBits)
	{
		nVars = _nVars;
		nVarBits = _nVarBits;
	}
	


	/* (non-Javadoc)
	 * @see jeva.ga.Evaluator#evaluateGenome(jeva.ga.Genome)
	 */
	public void evaluateGenome(Genome genome)
	{
		boolean[] values = genome.getValues();
		double x[] = new double[nVars];
		
		int bitSetOffset = 0;
		for (int varIndex = 0; varIndex < nVars; varIndex++)
		{
			BitSet bs = new BitSet();
			for (int bitIndex = 0; bitIndex < nVarBits; bitIndex++)
				bs.set(bitIndex, values[bitSetOffset++]);
			BitSetUtil.doInvGrayCoding(bs);
			
			x[varIndex] = BitSetUtil.bitSetToDouble(bs, 0, nVarBits, VAR_MIN, VAR_MAX);
		}
		
		double sum = 0;
		for (int i = 0; i < x.length; i++)
			sum += Math.pow(x[i], 2);
		
		genome.setFitness(sum);
	}
}
