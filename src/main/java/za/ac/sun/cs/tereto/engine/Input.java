package main.java.za.ac.sun.cs.tereto.engine;

public interface Input {
	
	String getName();

	String getFilename();
	
	boolean doesPass();

	void setPass(boolean passes);

}
