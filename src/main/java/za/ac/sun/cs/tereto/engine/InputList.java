package main.java.za.ac.sun.cs.tereto.engine;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Observable;
import java.util.concurrent.CopyOnWriteArrayList;

public class InputList extends Observable implements List<Input> {

	private final List<Input> inputs = new CopyOnWriteArrayList<>();

	@Override
	public int size() {
		return inputs.size();
	}

	@Override
	public boolean isEmpty() {
		return inputs.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return inputs.contains(o);
	}

	@Override
	public Object[] toArray() {
		return inputs.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return inputs.toArray(a);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return inputs.containsAll(c);
	}

	@Override
	public Input get(int index) {
		return inputs.get(index);
	}

	@Override
	public int indexOf(Object o) {
		return inputs.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return inputs.lastIndexOf(o);
	}

	@Override
	public List<Input> subList(int fromIndex, int toIndex) {
		return Collections.unmodifiableList(inputs.subList(fromIndex, toIndex));
	}

	// MODIFIERS

	@Override
	public boolean add(Input e) {
		boolean added = inputs.add(e);
		setChanged();
		notifyObservers();
		return added;
	}

	@Override
	public boolean remove(Object o) {
		boolean removed = inputs.remove(o);
		setChanged();
		notifyObservers();
		return removed;
	}

	@Override
	public boolean addAll(Collection<? extends Input> c) {
		boolean added = inputs.addAll(c);
		setChanged();
		notifyObservers();
		return added;
	}

	@Override
	public boolean addAll(int index, Collection<? extends Input> c) {
		boolean added = inputs.addAll(c);
		setChanged();
		notifyObservers();
		return added;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean removed = inputs.removeAll(c);
		setChanged();
		notifyObservers();
		return removed;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		boolean retained = inputs.retainAll(c);
		setChanged();
		notifyObservers();
		return retained;
	}

	@Override
	public void clear() {
		inputs.clear();
		setChanged();
		notifyObservers();
	}

	@Override
	public Input set(int index, Input element) {
		Input input = inputs.set(index, element);
		setChanged();
		notifyObservers();
		return input;
	}

	@Override
	public void add(int index, Input element) {
		inputs.add(index, element);
		setChanged();
		notifyObservers();
	}

	@Override
	public Input remove(int index) {
		Input input = inputs.remove(index);
		setChanged();
		notifyObservers();
		return input;
	}

	public void touch() {
		setChanged();
		notifyObservers();
	}
	
	// ITERATORS

	@Override
	public Iterator<Input> iterator() {
		return inputs.iterator();
	}

	@Override
	public ListIterator<Input> listIterator() {
		return inputs.listIterator();
	}

	@Override
	public ListIterator<Input> listIterator(int index) {
		return inputs.listIterator(index);
	}

}