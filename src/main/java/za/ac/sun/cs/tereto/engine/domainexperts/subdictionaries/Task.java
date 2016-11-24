package main.java.za.ac.sun.cs.tereto.engine.domainexperts.subdictionaries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

public class Task {

	private Map<String, Node> nodes = new HashMap<>();
	private List<Node> nodeList = new ArrayList<>();
	private String solution = null;
	private String stringVersion = null;

	public Task(Scanner s) {
		int k = s.nextInt();
		for (int i = 0; i < k; i++) {
			String w = s.next();
			Node n = getNode(w);
			for (w = s.next(); !w.equals("."); w = s.next()) {
				Node m = getNode(w);
				n.addEdge(m);
			}
			nodeList.add(n);
		}
	}

	private Node getNode(String word) {
		Node node = nodes.get(word);
		if (node == null) {
			node = new Node(word);
			nodes.put(word, node);
		}
		return node;
	}

	public String getSolution() {
		if (solution == null) {
			solution = computeSolution();
		}
		return solution;
	}

	private int indexCounter = 0;
	private final Stack<Node> stack = new Stack<>();
	private final Map<Node, Integer> index = new HashMap<>();
	private final Map<Node, Integer> lowlink = new HashMap<>();
	private final Map<Node, Boolean> onStack = new HashMap<>();
	private final Map<Node, Node> roots = new HashMap<>();
	private final Map<Node, Integer> sizes = new HashMap<>();

	private String computeSolution() {
		for (Node v : nodeList) {
			if (!index.containsKey(v)) {
				strongConnect(v);
			}
		}
		for (Node v : nodeList) {
			Node vr = roots.get(v);
			for (Node w : v.getChildren()) {
				Node wr = roots.get(w);
				if (vr != wr) {
					sizes.put(vr, 0);
				}
			}
		}
		Node smallest = null;
		int n = -1;
		for (Map.Entry<Node, Integer> e : sizes.entrySet()) {
			int m = e.getValue();
			if ((m > 0) && ((n == -1) || (m < n))) {
				smallest = e.getKey();
				n = m;
			}
		}
		StringBuilder b = new StringBuilder();
		b.append(n);
		int k = 0;
		for (Node v : nodeList) {
			if (roots.get(v) == smallest) {
				b.append(' ').append(v.getWord());
				k++;
				if (k == 4) {
					break;
				}
			}
		}
		stack.clear();
		index.clear();
		lowlink.clear();
		onStack.clear();
		roots.clear();
		sizes.clear();
		return b.toString();
	}

	private void strongConnect(Node v) {
		index.put(v, indexCounter);
		lowlink.put(v, indexCounter);
		indexCounter++;
		stack.push(v);
		onStack.put(v, true);
		for (Node w : v.getChildren()) {
			if (!index.containsKey(w)) {
				strongConnect(w);
				lowlink.put(v, Math.min(lowlink.get(v), lowlink.get(w)));
			} else if (onStack.get(w)) {
				lowlink.put(v, Math.min(lowlink.get(v), index.get(w)));
			}
		}
		if (lowlink.get(v) == index.get(v)) {
			sizes.put(v, 0);
			while (true) {
				Node w = stack.pop();
				onStack.put(w, false);
				sizes.put(v, 1 + sizes.get(v));
				roots.put(w, v);
				if (w == v) {
					break;
				}
			}
		}
	}

	@Override
	public String toString() {
		if (stringVersion == null) {
			StringBuilder b = new StringBuilder();
			b.append(nodeList.size()).append('\n');
			for (Node node : nodeList) {
				b.append(node.toString());
			}
			stringVersion = b.toString();
		}
		return stringVersion;
	}

}