package main.java.za.ac.sun.cs.tereto.engine.classloader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.tools.JavaFileObject;

public class LocalJavaFileObject implements JavaFileObject {

	private final String filename;
	private final JavaFileObject.Kind kind;
	private final ByteArrayOutputStream byteArrayOutputStream;

	public LocalJavaFileObject(String filename, JavaFileObject.Kind kind, ByteArrayOutputStream byteArrayOutputStream) {
		this.filename = filename;
		this.kind = kind;
		this.byteArrayOutputStream = byteArrayOutputStream;
	}

	@Override
	public URI toUri() {
		return URI.create("string:///" + filename);
	}

	@Override
	public String getName() {
//		System.out.println(">>> LocalJavaFileObject.getName: " + filename);
		return filename;
//		throw new UnsupportedOperationException("getName");
	}

	@Override
	public InputStream openInputStream() throws IOException {
		return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
	}

	@Override
	public OutputStream openOutputStream() throws IOException {
		return byteArrayOutputStream;
	}

	@Override
	public Reader openReader(boolean ignoreEncodingErrors) throws IOException {
		throw new UnsupportedOperationException("openReader");
	}

	public byte[] getBytes() {
		return byteArrayOutputStream.toByteArray();
	}
	
	@Override
	public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
//		System.out.println(">>> LocalJavaFileObject.getCharContent: " + byteArrayOutputStream.toString().substring(0, 200));
//		System.out.println(">>> LocalJavaFileObject.getCharContent: " + filename);
		return byteArrayOutputStream.toString();
	}

	@Override
	public Writer openWriter() throws IOException {
		throw new UnsupportedOperationException("openWriter");
	}

	@Override
	public long getLastModified() {
		throw new UnsupportedOperationException("getLastModified");
	}

	@Override
	public boolean delete() {
		throw new UnsupportedOperationException("delete");
	}

	@Override
	public Kind getKind() {
		return kind;
	}

	@Override
	public boolean isNameCompatible(String simpleName, Kind kind) {
//		System.out.print(">>> LocalJavaFileObject.isNameCompatible:");
//		System.out.print(" simpleName==" + simpleName);
//		System.out.print(" kind==" + kind);
//		System.out.print(" (filename==" + filename + ")");
//		System.out.println();
//		System.out.println(">>> \"" + filename + "\".equals(\"" + simpleName + kind.extension + "\") ->" + filename.equals(simpleName + kind.extension));
		return filename.equals(simpleName + kind.extension);
//		return false;
//		throw new UnsupportedOperationException("isNameCompatible");
	}

	@Override
	public NestingKind getNestingKind() {
		throw new UnsupportedOperationException("getNestingKind");
	}

	@Override
	public Modifier getAccessLevel() {
		throw new UnsupportedOperationException("getAccessLevel");
	}

}
