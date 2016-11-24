package main.java.za.ac.sun.cs.tereto.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Observable;

public class ProgramList  extends Observable implements List<Program> {

	private final List<Program> programs = new ArrayList<>();

	@Override
	public int size() {
		return programs.size();
	}

	@Override
	public boolean isEmpty() {
		return programs.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return programs.contains(o);
	}

	@Override
	public Object[] toArray() {
		return programs.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return programs.toArray(a);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return programs.containsAll(c);
	}

	@Override
	public Program get(int index) {
		return programs.get(index);
	}

	@Override
	public int indexOf(Object o) {
		return programs.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return programs.lastIndexOf(o);
	}

	@Override
	public List<Program> subList(int fromIndex, int toIndex) {
		return Collections.unmodifiableList(programs.subList(fromIndex, toIndex));
	}

	// MODIFIERS

	@Override
	public boolean add(Program e) {
		boolean added = programs.add(e);
		setChanged();
		notifyObservers();
		return added;
	}

	@Override
	public boolean remove(Object o) {
		boolean removed = programs.remove(o);
		setChanged();
		notifyObservers();
		return removed;
	}

	@Override
	public boolean addAll(Collection<? extends Program> c) {
		boolean added = programs.addAll(c);
		setChanged();
		notifyObservers();
		return added;
	}

	@Override
	public boolean addAll(int index, Collection<? extends Program> c) {
		boolean added = programs.addAll(c);
		setChanged();
		notifyObservers();
		return added;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean removed = programs.removeAll(c);
		setChanged();
		notifyObservers();
		return removed;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		boolean retained = programs.retainAll(c);
		setChanged();
		notifyObservers();
		return retained;
	}

	@Override
	public void clear() {
		programs.clear();
		setChanged();
		notifyObservers();
	}

	@Override
	public Program set(int index, Program element) {
		Program program = programs.set(index, element);
		setChanged();
		notifyObservers();
		return program;
	}

	@Override
	public void add(int index, Program element) {
		programs.add(index, element);
		setChanged();
		notifyObservers();
	}

	@Override
	public Program remove(int index) {
		Program program = programs.remove(index);
		setChanged();
		notifyObservers();
		return program;
	}

	// ITERATORS

	@Override
	public Iterator<Program> iterator() {
		return programs.iterator();
	}

	@Override
	public ListIterator<Program> listIterator() {
		return programs.listIterator();
	}

	@Override
	public ListIterator<Program> listIterator(int index) {
		return programs.listIterator(index);
	}

}