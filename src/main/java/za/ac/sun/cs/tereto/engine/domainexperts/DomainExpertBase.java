package main.java.za.ac.sun.cs.tereto.engine.domainexperts;

import main.java.za.ac.sun.cs.tereto.engine.Input;
import main.java.za.ac.sun.cs.tereto.engine.Program;

public abstract class DomainExpertBase implements DomainExpert {

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public boolean passes(Program program, Input input) {
		if ((program == null) || !program.doesCompile()) {
			return true;
		}
		String oracleOut = computeOracleOutput(input);
		if (oracleOut == null) {
			return false;
		}
		String out = program.computeOutput(computeInputString(input));
		if (out == null) {
			return true;
		}
		return !oracleOut.equals(out);
	}

}
