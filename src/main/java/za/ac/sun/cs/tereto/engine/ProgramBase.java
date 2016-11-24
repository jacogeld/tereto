package main.java.za.ac.sun.cs.tereto.engine;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import main.java.za.ac.sun.cs.tereto.engine.classloader.LocalClassLoader;
import main.java.za.ac.sun.cs.tereto.engine.classloader.LocalFileManager;

import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject.Kind;

public class ProgramBase implements Program {
	
	/**
	 * The Java compiler used to compile programs.
	 */
	private static final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

	private final String name;

	private final String filename;

	private final boolean compiles;
	
	private final LocalFileManager fileManager;
	
	private final Map<String, String> outputs = new HashMap<>();

	public ProgramBase(String name, String filename) {
		this.name = name;
		this.filename = filename;
		fileManager = new LocalFileManager(compiler.getStandardFileManager(null, null, null), filename);
		compiles = compile();
	}

	private boolean compile() {
		try {
			JavaFileObject main = fileManager.getJavaFileForInput(StandardLocation.CLASS_PATH, "Main", Kind.SOURCE);
			Iterable<JavaFileObject> compilationUnits = Arrays.asList(main);
			DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
			Iterable<String> options = null; // Arrays.asList(COMPILER_OPTIONS);
			CompilationTask task = compiler.getTask(null, fileManager, diagnostics, options, null, compilationUnits);
			if (task.call()) {
				return true;
			} else {
				// for (Diagnostic<? extends JavaFileObject> d : diagnostics.getDiagnostics()) {
				// 	System.out.println(">> ERROR: " + d.getLineNumber() + ": " + d.getMessage(null));
				// }
			}
		} catch (IOException x) {
			// IGNORE
		}
		return false;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean doesCompile() {
		return compiles;
	}

	@Override
	public String getFilename() {
		return filename;
	}

	@Override
	public String computeOutput(String input) {
		if (!compiles) {
			return null;
		}
		if (!outputs.containsKey(input)) {
			outputs.put(input, computeOutput0(input));
		}
		return outputs.get(input);
	}

	private String computeOutput0(String input) {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		LocalClassLoader localClassLoader = new LocalClassLoader(classLoader, fileManager);
		ByteArrayInputStream newIn = new ByteArrayInputStream(input.getBytes());
		ByteArrayOutputStream newOut = new ByteArrayOutputStream();
		ByteArrayOutputStream newErr = new ByteArrayOutputStream();
		InputStream oldIn = System.in;
		PrintStream oldOut = System.out;
		PrintStream oldErr = System.err;
		System.setIn(newIn);
		System.setOut(new PrintStream(newOut));
		System.setErr(new PrintStream(newErr));
		String result = null;
		try {
			Class<?> c = localClassLoader.loadClass("Main");
			Method m = c.getMethod("main", String[].class);
			m.invoke(null, new Object[] { new String[] {} });
			result = newOut.toString();
		} catch (ClassNotFoundException x) {
			// x.printStackTrace();
		} catch (IllegalAccessException x) {
			// x.printStackTrace();
		} catch (IllegalArgumentException x) {
			// x.printStackTrace();
		} catch (InvocationTargetException x) {
			// x.printStackTrace();
		} catch (SecurityException x) {
			// x.printStackTrace();
		} catch (NoSuchMethodException x) {
			// x.printStackTrace();
		} finally {
			System.setIn(oldIn);
			System.setOut(oldOut);
			System.setErr(oldErr);
		}
		return result.trim();
	}

}
