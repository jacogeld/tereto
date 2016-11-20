package main.java.za.ac.sun.cs.tereto.engine.ga;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

public class GA {

	protected final Configuration configuration;

	protected Individual best;

	protected int lastBest;

	protected int generation;

	public GA(Configuration configuration) {
		this.configuration = configuration;
	}

	public int getGeneration() {
		return generation;
	}

	public Individual getBest() {
		return best;
	}
	
	private void updateBestIndividual(Set<Individual> pop) {
		for (Individual ind : pop) {
			if ((best == null) || (ind.getFitness().compareTo(best.getFitness()) > 0)) {
				lastBest = generation;
				best = ind;
			}
		}
	}
	
	private void updateBestIndividual(Individual... pop) {
		for (Individual ind : pop) {
			if ((best == null) || (ind.getFitness().compareTo(best.getFitness()) > 0)) {
				lastBest = generation;
				best = ind;
			}
		}
	}
	
	private Set<Individual> makeRandomPopulation(int size) {
		Set<Individual> P = new HashSet<>();
		for (int i = 0; i < size; i++) {
			P.add(configuration.getRandomIndividual());
		}
		return P;
	}

	private Set<Individual> getFittest(Set<Individual> pop, int k) {
		PriorityQueue<Individual> heap = new PriorityQueue<>(pop);
		Set<Individual> top = new HashSet<>();
		for (int i = 0; i < k; i++) {
			top.add(heap.remove());
		}
		return top;
	}

	private boolean shouldContinue() {
		int m = configuration.getMaxGeneration();
		if ((m != -1) && (generation >= m)) {
			return false;
		}
		m = configuration.getMaxSteadyStateGeneration();
		if ((m != -1) && (generation >= m)) {
			return false;
		}
		m = configuration.getQuiesence();
		if ((m != -1) && (m < generation - lastBest)) {
			return false;
		}
		return true;
	}

	private double calculateDiversity(Individual ind, Set<Individual> pop) {
		Double distance = null;
		for (Individual i : pop) {
			double d = ind.getDistance(i);
			if ((distance == null) || (d < distance)) {
				distance = d;
			}
		}
		return distance;
	}

	private Individual makeDiverseIndividual(Set<Individual> pop) {
		Individual mostDiverse = null;
		double mostDiversity = 0.0;
		for (int i = 0; i < 10; i++) {
			Individual diverse = configuration.getRandomIndividual();
			double diversity = calculateDiversity(diverse, pop);
			if ((mostDiverse == null) || (diversity > mostDiversity)) {
				mostDiverse = diverse;
				mostDiversity = diversity;
			}
		}
		return mostDiverse;
	}

	private double getSomeDiverse(Set<Individual> pop, Set<Individual> diverseSet, int diverseSize) {
		Individual[] pool = pop.toArray(new Individual[0]);
		int n = pool.length;
		int i = configuration.getRandomInt(n);
		if (i != 0) {
			Individual t = pool[0];
			pool[0] = pool[i];
			pool[i] = t;
		}
		for (int j = 1; j < diverseSize; j++) {
			int k = j;
			double e = 0.0;
			for (int h = 0; h < j; h++) {
				e += pool[h].getDistance(pool[k]);
			}
			for (int l = j + 1; l < n; l++) {
				double f = 0.0;
				for (int h = 0; h < j; h++) {
					f += pool[h].getDistance(pool[l]);
				}
				if (f > e) {
					k = l;
					e = f;
				}
			}
			if (k != j) {
				Individual t = pool[j];
				pool[j] = pool[k];
				pool[k] = t;
			}
		}
		diverseSet.clear();
		double d = 0.0;
		for (int j = 0; j < diverseSize; j++) {
			diverseSet.add(pool[j]);
			for (int k = 0; k < j; k++) {
				d += pool[k].getDistance(pool[j]);
			}
		}
		return d;
	}
	
	private Set<Individual> getMostDiverse(Set<Individual> pop, int diverseSize) {
		Set<Individual> mostDiverseSet = null;
		double mostDiversity = 0.0;
		for (int i = 0; i < 10; i++) {
			Set<Individual> someDiverseSet = new HashSet<>();
			double someDiversity = getSomeDiverse(pop, someDiverseSet, diverseSize);
			if ((mostDiverseSet == null) || (mostDiversity > someDiversity)) {
				mostDiverseSet = someDiverseSet;
				mostDiversity = someDiversity;
			}
		}
		return mostDiverseSet;
	}
	
	private Individual hillClimb(Individual ind) {
		Fitness f = ind.getFitness();
		for (int i = 0; i < 10; i++) {
			Individual alt = configuration.mutateIndividual(ind);
			if (alt.getFitness().compareTo(f) > 0) {
				ind = alt;
				f = alt.getFitness();
			}
		}
		return ind;
	}
	
	//======================================================================
	// 
	//======================================================================

	public void muLambda() {
		int mu = configuration.getSelectionSize();
		int lambda = configuration.getPopulationSize();
		Set<Individual> P = makeRandomPopulation(lambda);
		generation = 1;
		best = null;
		do {
			updateBestIndividual(P);
			Set<Individual> Q = getFittest(P, mu);
			P.clear();
			for (Individual ind : Q) {
				int lm = lambda / mu;
				for (int i = 0; (i < lm) && (P.size() < lambda); i++) {
					P.add(configuration.mutateIndividual(ind));
				}
			}
			generation++;
		} while (shouldContinue());
	}

	//======================================================================
	// 
	//======================================================================

	public void muPlusLambda() {
		int mu = configuration.getSelectionSize();
		int lambda = configuration.getPopulationSize();
		Set<Individual> P = makeRandomPopulation(lambda);
		generation = 1;
		best = null;
		do {
			updateBestIndividual(P);
			Set<Individual> Q = getFittest(P, mu);
			P = new HashSet<>(Q);
			for (Individual ind : Q) {
				int lm = lambda / mu;
				for (int i = 0; (i < lm) && (P.size() < lambda); i++) {
					P.add(configuration.mutateIndividual(ind));
				}
			}
			generation++;
		} while (shouldContinue());
	}
	
	//======================================================================
	// 
	//======================================================================

	public void basic() {
		int lambda = configuration.getPopulationSize();
		lambda += (lambda % 2);
		Set<Individual> P = makeRandomPopulation(lambda);
		generation = 1;
		best = null;
		do {
			updateBestIndividual(P);
			Set<Individual> Q = new HashSet<>();
			for (int i = 0; i < lambda / 2; i++) {
				Individual pa = configuration.select(P);
				Individual pb = configuration.select(P);
				Set<Individual> children = configuration.crossover(pa, pb);
				for (Individual child : children) {
					Q.add(configuration.mutateIndividual(child));
				}
			}
			P = Q;
			generation++;
		} while (shouldContinue());
	}
	
	//======================================================================
	// 
	//======================================================================

	public void elitism() {
		int lambda = configuration.getPopulationSize();
		int elite = configuration.getEliteSize();
		lambda += (lambda % 2);
		Set<Individual> P = makeRandomPopulation(lambda);
		generation = 1;
		best = null;
		do {
			updateBestIndividual(P);
			Set<Individual> Q = getFittest(P, elite);
			for (int i = 0; i < lambda / 2; i++) {
				Individual pa = configuration.select(P);
				Individual pb = configuration.select(P);
				Set<Individual> children = configuration.crossover(pa, pb);
				for (Individual child : children) {
					Q.add(configuration.mutateIndividual(child));
				}
			}
			P = Q;
			generation++;
		} while (shouldContinue());
	}
	
	//======================================================================
	// 
	//======================================================================

	public void steadyState() {
		int lambda = configuration.getPopulationSize();
		Set<Individual> P = makeRandomPopulation(lambda);
		generation = 1;
		best = null;
		updateBestIndividual(P);
		do {
			Individual pa = configuration.select(P);
			Individual pb = configuration.select(P);
			Individual da = configuration.selectForDeath(P); P.remove(da);
			Individual db = configuration.selectForDeath(P); P.remove(db);
			Set<Individual> children = configuration.crossover(pa, pb);
			for (Individual child : children) {
				Individual mutated = configuration.mutateIndividual(child);
				updateBestIndividual(mutated);
				P.add(mutated);
			}
			generation++;
		} while (shouldContinue());
	}
	
	//======================================================================
	// 
	//======================================================================
	
	public void treeStyle() {
		int lambda = configuration.getPopulationSize();
		double r = configuration.getDirectProbability();
		Set<Individual> P = makeRandomPopulation(lambda);
		generation = 1;
		best = null;
		updateBestIndividual(P);
		do {
			Set<Individual> Q = new HashSet<>();
			do {
				if (r > configuration.getRandomDouble()) {
					Individual p = configuration.select(P);
					Q.add(p);
				} else {
					Individual pa = configuration.select(P);
					Individual pb = configuration.select(P);
					Set<Individual> children = configuration.crossover(pa, pb);
					for (Individual child : children) {
						if (Q.size() >= lambda) {
							break;
						}
						Q.add(child);
					}
				}
			} while (Q.size() < lambda);
			P = Q;
			generation++;
		} while (shouldContinue());
	}
	
	//======================================================================
	// 
	//======================================================================
	
	public void scatterSearch() {
		int seedSize = configuration.getSeedSize();
		int popSize = configuration.getPopulationSize();
		int eliteSize = configuration.getEliteSize();
		int diverseSize = configuration.getDiverseSize();
		Set<Individual> P = makeRandomPopulation(seedSize);
		while (P.size() < popSize) {
			P.add(makeDiverseIndividual(P));
		}
		Set<Individual> Q = new HashSet<>();
		for (Individual ind : P) {
			Q.add(hillClimb(ind));
		}
		P = Q;
		generation = 1;
		best = null;
		updateBestIndividual(P);
		do {
			Set<Individual> B = getFittest(P, eliteSize);
			Set<Individual> D = getMostDiverse(P, diverseSize);
			P = B;
			P.addAll(D);
			Q.clear();
			for (Individual pa : P) {
				for (Individual pb : P) {
					if (pa == pb) { continue; }
					Set<Individual> children = configuration.crossover(pa, pb);
					for (Individual child : children) {
						Individual improved = hillClimb(configuration.mutateIndividual(child));
						updateBestIndividual(improved);
						Q.add(improved);
					}
				}
			}
			P.addAll(Q);
			generation++;
		} while (shouldContinue());
	}

}