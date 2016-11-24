package main.java.za.ac.sun.cs.tereto.engine.domainexperts.subdictionaries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Node {
	
	private final String word;
	private final List<Node> children = new ArrayList<>();
//	private int incoming = 0;
	private String stringVersion = null;
	
	public Node(String word) {
		this.word = word;
	}

	public String getWord() {
		return word;
	}

	public void addEdge(Node node) {
		children.add(node);
//		node.incoming++;
	}

	public List<Node> getChildren() {
		return Collections.unmodifiableList(children);
	}

	@Override
	public String toString() {
		if (stringVersion == null) {
			StringBuilder b = new StringBuilder();
			b.append(word).append(' ');
			for (Node node : children) {
				b.append(node.word).append(' ');
			}
			stringVersion = b.append(".\n").toString();
		}
		return stringVersion;
	}

}