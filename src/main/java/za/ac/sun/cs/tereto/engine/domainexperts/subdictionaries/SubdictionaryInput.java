package main.java.za.ac.sun.cs.tereto.engine.domainexperts.subdictionaries;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import main.java.za.ac.sun.cs.tereto.engine.InputBase;

public class SubdictionaryInput extends InputBase {

	private final List<Task> tasks = new ArrayList<>();
	private String solution = null;
	private String stringVersion = null;

	public SubdictionaryInput(String name, String filename) {
		super(name, filename);
		try {
			Scanner s = new Scanner(new File(filename));
			int n = s.nextInt();
			for (int i = 0; i < n; i++) {
				tasks.add(new Task(s));
			}
		} catch (FileNotFoundException x) {
		}
	}

	public String getSolution() {
		if (solution == null) {
			solution = computeSolution();
		}
		return solution;
	}

	private String computeSolution() {
		StringBuilder b = new StringBuilder();
		int k = 0;
		for (Task task : tasks) {
			b.append(++k).append(": ");
			b.append(task.getSolution());
			b.append('\n');
		}
		return b.toString().trim();
	}

	public List<Task> getTasks() {
		return Collections.unmodifiableList(tasks);
	}

	@Override
	public String toString() {
		if (stringVersion == null) {
			StringBuilder b = new StringBuilder();
			b.append(tasks.size()).append('\n');
			for (Task task : tasks) {
				b.append(task.toString());
			}
			stringVersion = b.toString();
		}
		return stringVersion;
	}

}