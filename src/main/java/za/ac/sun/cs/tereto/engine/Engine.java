package main.java.za.ac.sun.cs.tereto.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import main.java.za.ac.sun.cs.tereto.engine.domainexperts.DomainExpert;
import main.java.za.ac.sun.cs.tereto.engine.domainexperts.subdictionaries.Subdictionaries;

public class Engine {

	private static final List<DomainExpert> domainExperts = new ArrayList<>();

	static {
		domainExperts.add(new Subdictionaries());
	}
	
	public static List<DomainExpert> getDomainExperts() {
		return Collections.unmodifiableList(domainExperts);
	}

}
