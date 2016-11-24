package main.java.za.ac.sun.cs.tereto.engine.domainexperts.subdictionaries;

import main.java.za.ac.sun.cs.tereto.engine.Input;
import main.java.za.ac.sun.cs.tereto.engine.domainexperts.DomainExpertBase;

public class Subdictionaries extends DomainExpertBase {

	@Override
	public String getName() {
		return "Subdictionaries";
	}

	@Override
	public Input createInput(String name, String filename) {
		return new SubdictionaryInput(name, filename);
	}

	@Override
	public String computeOracleOutput(Input input) {
		return ((SubdictionaryInput) input).getSolution();
	}

	@Override
	public String computeInputString(Input input) {
		return input.toString();
	}

}
