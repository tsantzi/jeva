import java.text.DecimalFormat;

import jeva.ga.Breeder;
import jeva.ga.Evaluator;
import jeva.ga.Parameters;
import jeva.ga.evaluator.EvaluatorZero;
import jeva.ga.objective.ObjectiveMinimize;



public class Main
{
	private static final DecimalFormat D_FORMATTER = new DecimalFormat("0.00E00");
	private static final DecimalFormat I_FORMATTER = new DecimalFormat("000000");
	private static final int TEST_N_VARS = 4;
	private static final int TEST_N_BITS = 16;
	


	public static void main(String[] args)
	{
		// Initialise evaluator
		Evaluator evaluator = new EvaluatorZero(TEST_N_VARS, TEST_N_BITS);
		
		// Initialise parameters
		Parameters parameters = new Parameters(); // (defaults)
		parameters.put(Parameters.GENOME_LENGTH, TEST_N_VARS * TEST_N_BITS);
		parameters.put(Parameters.MUTATION_RATE, 0.01);
		
		// Initialise breeder
		Breeder breeder = new Breeder(evaluator, new ObjectiveMinimize(), parameters);
		
		// Run GA
		try
		{
			do
			{
				//Step the breeder once
				breeder.step();
				
				// Display info
				System.out.print("Generation = " + I_FORMATTER.format(breeder.getGeneration()));
				System.out.print("\tFitness = " + D_FORMATTER.format(breeder.getLastPopulation().getBestGenome().getFitness()));
				System.out.print("\t" + breeder.getLastPopulation().getBestGenome());
				System.out.println("");
				
				// Yield
				Thread.sleep(100);
			} while (breeder.getLastPopulation().getBestGenome().getFitness() > 0);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("Something went wrong: " + e.getMessage());
			System.exit(1);
		}
	}
}
