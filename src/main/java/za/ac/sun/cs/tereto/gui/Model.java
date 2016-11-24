package main.java.za.ac.sun.cs.tereto.gui;

import java.io.File;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import main.java.za.ac.sun.cs.tereto.engine.Engine;
import main.java.za.ac.sun.cs.tereto.engine.Input;
import main.java.za.ac.sun.cs.tereto.engine.InputList;
import main.java.za.ac.sun.cs.tereto.engine.Program;
import main.java.za.ac.sun.cs.tereto.engine.ProgramList;
import main.java.za.ac.sun.cs.tereto.engine.domainexperts.DomainExpert;

public class Model {

	private final ObservableList<Program> programList = FXCollections.observableArrayList();

	private final ObservableList<Input> inputList = FXCollections.observableArrayList();
	
	public Model() {
		Engine.getPrograms().addObserver((o, a) ->
			Platform.runLater(() -> {
				programList.clear();
				programList.addAll((ProgramList) o);
			}));
		Engine.getInputs().addObserver((o, a) ->
			Platform.runLater(() -> {
				InputList l = (InputList) o;
				inputList.clear();
				inputList.addAll(l.stream().filter(i -> !i.doesPass()).collect(Collectors.toList()));
			}));
	}

	public void loadPrograms(File directory) {
		Thread th = new Thread(new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				Engine.loadPrograms(directory);
				return null;
			}
		});
		th.setDaemon(true);
		th.start();
	}

	public void loadInputs(DomainExpert domainExpert, File directory) {
		Thread th = new Thread(new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				Engine.loadInputs(domainExpert, directory);
				return null;
			}
		});
		th.setDaemon(true);
		th.start();
	}

	public void filterInputs(DomainExpert domainExpert, Program program) {
		Thread th = new Thread(new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				Engine.filterInputs(domainExpert, program);
				return null;
			}
		});
		th.setDaemon(true);
		th.start();
	}
	
	public ObservableList<Program> getProgramList() {
		return programList;
	}

	public ObservableList<Input> getInputList() {
		return inputList;
	}

}
