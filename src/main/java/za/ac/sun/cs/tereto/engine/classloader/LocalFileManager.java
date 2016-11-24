package main.java.za.ac.sun.cs.tereto.engine.classloader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;

public class LocalFileManager extends ForwardingJavaFileManager<StandardJavaFileManager> {

	private final Map<String, LocalJavaFileObject> localFiles = new HashMap<>();

	public LocalFileManager(StandardJavaFileManager fileManager, String zipFile) {
		super(fileManager);
		loadZipContents(zipFile);
	}

	private void loadZipContents(String zipFile) {
		File f = new File(zipFile);
		try (InputStream inputStream = new FileInputStream(f)) {
			try (ZipInputStream zip = new ZipInputStream(inputStream)) {
				ZipEntry entry = zip.getNextEntry();
				while (entry != null) {
//					int n = (int) entry.getSize();
//					System.out.println(">>> LOADING: " + entry.getName() + " " + n + " bytes");
					LocalJavaFileObject fileObject = loadOrCreateFile(entry.getName());
					OutputStream out = fileObject.openOutputStream();
					byte[] content = new byte[1024];
					int read = 0;
					while ((read = zip.read(content)) != -1) {
						out.write(content, 0, read);
					}
//					System.out.println(">>> " + fileObject.openOutputStream().toString());
					entry = zip.getNextEntry();
				}
			}
		} catch (FileNotFoundException x) {
			x.printStackTrace();
		} catch (IOException x) {
			x.printStackTrace();
		}
	}

	private LocalJavaFileObject loadOrCreateFile(String filename) {
		LocalJavaFileObject localJavaFileObject = localFiles.get(filename);
		if (localJavaFileObject == null) {
			if (filename.endsWith(".class")) {
				localJavaFileObject = new LocalJavaFileObject(filename, JavaFileObject.Kind.CLASS, new ByteArrayOutputStream());
			} else if (filename.endsWith(".java")) {
				localJavaFileObject = new LocalJavaFileObject(filename, JavaFileObject.Kind.SOURCE, new ByteArrayOutputStream());
			} else {
				localJavaFileObject = new LocalJavaFileObject(filename, JavaFileObject.Kind.OTHER, new ByteArrayOutputStream());
			}
			localFiles.put(filename, localJavaFileObject);
		}
		return localJavaFileObject;
	}

	public LocalJavaFileObject loadFile(String filename) {
		return localFiles.get(filename);
	}
	
	@Override
	public JavaFileObject getJavaFileForOutput(JavaFileManager.Location location, String className,
			JavaFileObject.Kind kind, FileObject sibling) throws IOException {
//		System.out.print(">>> LocalFileManager.getJavaFileForOutput:");
//		System.out.print(" Location==" + location);
//		System.out.print(" className==" + className);
//		System.out.print(" kind==" + kind);
//		System.out.print(" sibling==" + sibling);
//		System.out.println();
		if ((location == StandardLocation.CLASS_OUTPUT) && (kind == JavaFileObject.Kind.CLASS)) {
			return loadOrCreateFile(className + ".class");
		} else {
			return super.getJavaFileForOutput(location, className, kind, sibling);
		}
	}

	@Override
	public JavaFileObject getJavaFileForInput(JavaFileManager.Location location, String className, JavaFileObject.Kind kind) throws IOException {
//		System.out.print(">>> LocalFileManager.getJavaFileForInput:");
//		System.out.print(" Location==" + location);
//		System.out.print(" className==" + className);
//		System.out.print(" kind==" + kind);
//		System.out.println();
		if ((location == StandardLocation.CLASS_PATH) && (kind == JavaFileObject.Kind.SOURCE)) {
			return loadOrCreateFile(className + ".java");
		} else {
			return super.getJavaFileForInput(location, className, kind);
		}
	}

//	@Override
//	public FileObject getFileForInput(JavaFileManager.Location location, String packageName, String relativeName) throws IOException {
//		System.out.print(">>> LocalFileManager.getFileForInput:");
//		System.out.print(" Location==" + location);
//		System.out.print(" packageName==" + packageName);
//		System.out.print(" relativeName==" + relativeName);
//		System.out.println();
//		return null;
//	}
//
//	@Override
//	public FileObject getFileForOutput(JavaFileManager.Location location, String packageName, String relativeName, FileObject sibling) throws IOException {
//		System.out.print(">>> LocalFileManager.getFileForOutput:");
//		System.out.print(" Location==" + location);
//		System.out.print(" packageName==" + packageName);
//		System.out.print(" relativeName==" + relativeName);
//		System.out.print(" sibling==" + sibling);
//		System.out.println();
//		return null;
//	}

}