package main.java.za.ac.sun.cs.tereto.engine.classloader;

import javax.tools.JavaFileObject.Kind;

public class LocalClassLoader extends ClassLoader {

	private final LocalFileManager fileManager;

	public LocalClassLoader(ClassLoader classLoader, LocalFileManager fileManager) {
		super(classLoader);
		this.fileManager = fileManager;
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		//		System.out.println(">>> LOADING: " + name);
		LocalJavaFileObject o = fileManager.loadFile(name + Kind.CLASS.extension);
		if (o != null) {
			byte[] data = o.getBytes();
			if (data != null) {
				return defineClass(name, data, 0, data.length);
			}
		}
		return loadClass(name, false);
	}

}
