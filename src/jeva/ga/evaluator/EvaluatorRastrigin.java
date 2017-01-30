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
 * Rastrigin function evaluator
 * 
 * @author Nikolaos Chatzinikolaou
 */
public class EvaluatorRastrigin implements Evaluator
{
	private static final int RASTRIGIN_A = 10;
	private static final double RASTRIGIN_VAR_MIN = -0.5;
	private static final double RASTRIGIN_VAR_MAX = 0.5;
	
	int nVars = 0;
	int nVarBits = 0;
	


	/**
	 * Constructs a new EvaluatorRastrigin
	 * @param _nVars The number of variables
	 * @param _nVarBits The number of bits per variable
	 */
	public EvaluatorRastrigin(int _nVars, int _nVarBits)
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
			
			x[varIndex] = BitSetUtil.bitSetToDouble(bs, 0, nVarBits, RASTRIGIN_VAR_MIN, RASTRIGIN_VAR_MAX);
		}
		
		double ras = 0;
		for (int i = 0; i < x.length; i++)
			ras += RASTRIGIN_A + Math.pow(x[i], 2) - RASTRIGIN_A * Math.cos(2 * Math.PI * x[i]);
		
		genome.setFitness(ras);
	}
}
