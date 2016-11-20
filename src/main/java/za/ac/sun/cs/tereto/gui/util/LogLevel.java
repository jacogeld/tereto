package main.java.za.ac.sun.cs.tereto.gui.util;

import java.util.logging.Level;

import javafx.util.Pair;

@SuppressWarnings("serial")
public class LogLevel extends Pair<String, Level> {

	public static final LogLevel DEBUG = new LogLevel("debug", Level.FINE);
	public static final LogLevel CONFIG = new LogLevel("config", Level.CONFIG);
	public static final LogLevel INFO = new LogLevel("info", Level.INFO);
	public static final LogLevel WARNING = new LogLevel("warning", Level.WARNING);
	public static final LogLevel ERROR = new LogLevel("error", Level.SEVERE);

	public static final LogLevel[] LEVELS = new LogLevel[] { DEBUG, CONFIG, INFO, WARNING, ERROR };

	private LogLevel(String key, Level value) {
		super(key, value);
	}
	
	
}