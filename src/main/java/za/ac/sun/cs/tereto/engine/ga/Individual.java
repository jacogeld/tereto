package main.java.za.ac.sun.cs.tereto.engine.ga;

public abstract class Individual implements Comparable<Individual> {

	@Override
	public int compareTo(Individual individual) {
		assert individual != null;
		return individual.getFitness().compareTo(getFitness());
	}

	public abstract Fitness getFitness();

	public abstract double getDistance(Individual candidate);

}
