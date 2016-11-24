package main.java.za.ac.sun.cs.tereto.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import main.java.za.ac.sun.cs.tereto.engine.domainexperts.DomainExpert;
import main.java.za.ac.sun.cs.tereto.engine.domainexperts.subdictionaries.Subdictionaries;

public class Engine {

	/**
	 * Extension used to look for programs.
	 */
	private static final String PROGRAM_EXTENSION = ".zip";

	/**
	 * Extension used to look for inputs.
	 */
	private static final String INPUT_EXTENSION = ".in";

	private final static Logger logger = Logger.getLogger("tereto." + Engine.class.getSimpleName());

	private static final List<DomainExpert> domainExperts = new ArrayList<>();

	private static final ProgramList programs = new ProgramList();

	private static final InputList inputs = new InputList();
	
	static {
		domainExperts.add(new Subdictionaries());
	}

	public static List<DomainExpert> getDomainExperts() {
		return Collections.unmodifiableList(domainExperts);
	}

	public static ProgramList getPrograms() {
		return programs;
	}

	public static InputList getInputs() {
		return inputs;
	}
	
	public static void loadPrograms(File directory) {
		logger.info("loading programs");
		List<String> fs = new ArrayList<>();
		int e = PROGRAM_EXTENSION.length();
		for (File f : directory.listFiles()) {
			if (f.isFile() && f.getName().endsWith(PROGRAM_EXTENSION)) {
				String n = f.getName();
				fs.add(n.substring(0, n.length() - e));
			}
		}
		if (fs.size() > 0) {
			programs.clear();
			for (String f : fs) {
				String n = directory.getAbsolutePath() + "/" + f + PROGRAM_EXTENSION;
				programs.add(new ProgramBase(f, n));
			}
		}
		logger.info(String.format("found %d program(s)", fs.size()));
	}

	public static void loadInputs(DomainExpert domainExpert, File directory) {
		logger.info("loading inputs");
		List<String> fs = new ArrayList<>();
		int e = INPUT_EXTENSION.length();
		for (File f : directory.listFiles()) {
			if (f.isFile() && f.getName().endsWith(INPUT_EXTENSION)) {
				String n = f.getName();
				fs.add(n.substring(0, n.length() - e));
			}
		}
		if (fs.size() > 0) {
			inputs.clear();
			for (String f : fs) {
				String n = directory.getAbsolutePath() + "/" + f + INPUT_EXTENSION;
				inputs.add(domainExpert.createInput(f, n));
			}
		}
		logger.info(String.format("found %d input(s)", fs.size()));
	}

	public static void filterInputs(DomainExpert domainExpert, Program program) {
		logger.info("filtering inputs");
		if ((program == null) || !program.doesCompile()) {
			for (Input input : inputs) {
				input.setPass(false);
			}
		} else {
			int n = 0 ;
			for (Input input : inputs) {
				boolean p = domainExpert.passes(program, input);
				input.setPass(p);
				if (!p) {
					logger.info("(" + program.getName() + ", " + input.getName() + ") failed");
					n++;
				} else {
					logger.info("(" + program.getName() + ", " + input.getName() + ") passed");
				}
			}
			logger.info("program " + program.getName() + ", fails: " + n);
		}
		inputs.touch();
	}

}
