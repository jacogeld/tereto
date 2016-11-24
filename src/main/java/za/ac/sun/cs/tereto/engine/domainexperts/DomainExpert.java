package main.java.za.ac.sun.cs.tereto.engine.domainexperts;

import main.java.za.ac.sun.cs.tereto.engine.Input;
import main.java.za.ac.sun.cs.tereto.engine.Program;

public interface DomainExpert {

	String getName();

	Input createInput(String name, String filename);

	boolean passes(Program program, Input input);

	String computeOracleOutput(Input input);

	String computeInputString(Input input);

}
