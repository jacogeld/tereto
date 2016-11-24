package main.java.za.ac.sun.cs.tereto.engine;

public class InputBase implements Input {
	
	private final String name;

	private final String filename;

	private boolean passes;
	
	public InputBase(String name, String filename) {
		this.name = name;
		this.filename = filename;
		this.passes = false;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getFilename() {
		return filename;
	}
	
	@Override
	public boolean doesPass() {
		return passes;
	}

	@Override
	public void setPass(boolean passes) {
		this.passes = passes;
	}

}
