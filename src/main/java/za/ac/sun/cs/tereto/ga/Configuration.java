package main.java.za.ac.sun.cs.tereto.ga;

import java.util.Random;
import java.util.Set;

/**
 * Parameters for the Genetic Algorithm. Some of the parameters have default
 * values (and corresponding default methods; these are "standard" and usually
 * relatively common across different problem domains. Other parameters are
 * very domain-specific and are kept abstract.
 * 
 * @author jaco
 */
public abstract class Configuration {

	protected Random random = new Random();

	/**
	 * Size of the population.
	 */
	protected int populationSize = 100;

	/**
	 * Size of the population selected for 
	 */
	protected int selectionSize = 50;

	protected int eliteSize = 30;
	protected int seedSize = 30;
	protected int diverseSize = 30;
	protected double directProbability = 0.1;
	protected int maxGeneration = 1000;
	protected int maxSteadyStateGeneration = -1;
	protected int quiesence = 100;

	public int getPopulationSize() {
		return populationSize;
	}

	public void setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
	}

	public int getSelectionSize() {
		return selectionSize;
	}

	public void setSelectionSize(int selectionSize) {
		this.selectionSize = selectionSize;
	}

	public int getEliteSize() {
		return eliteSize;
	}

	public void setEliteSize(int eliteSize) {
		this.eliteSize = eliteSize;
	}

	public int getSeedSize() {
		return seedSize;
	}

	public void setSeedSize(int seedSize) {
		this.seedSize = seedSize;
	}

	public int getDiverseSize() {
		return diverseSize;
	}

	public void setDiverseSize(int diverseSize) {
		this.diverseSize = diverseSize;
	}

	public double getDirectProbability() {
		return directProbability;
	}

	public void setDirectProbability(double directProbability) {
		this.directProbability = directProbability;
	}

	public int getMaxGeneration() {
		return maxGeneration;
	}

	public void setMaxGeneration(int maxGeneration) {
		this.maxGeneration = maxGeneration;
	}

	public int getMaxSteadyStateGeneration() {
		return maxSteadyStateGeneration;
	}

	public void setMaxSteadyStateGeneration(int maxSteadyStateGeneration) {
		this.maxSteadyStateGeneration = maxSteadyStateGeneration;
	}

	public int getQuiesence() {
		return quiesence;
	}

	public void setQuiesence(int quiesence) {
		this.quiesence = quiesence;
	}

	public Random getRandom() {
		return random;
	}

	public void setRandom(Random random) {
		this.random = random;
	}

	public double getRandomDouble() {
		return random.nextDouble();
	}
	
	public int getRandomInt(int bound) {
		return random.nextInt(bound);
	}

	public abstract Individual getRandomIndividual();

	public abstract Individual mutateIndividual(Individual ind);

	public abstract Set<Individual> crossover(Individual pa, Individual pb);

	public abstract Individual select(Set<Individual> p);

	public abstract Individual selectForDeath(Set<Individual> p);

}
