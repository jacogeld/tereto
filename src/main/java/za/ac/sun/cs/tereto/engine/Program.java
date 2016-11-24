package main.java.za.ac.sun.cs.tereto.engine;

public interface Program {
	
	String getName();

	String getFilename();
	
	boolean doesCompile();
	
	String computeOutput(String inputString);
	
}
