package main.java.za.ac.sun.cs.tereto.gui;

import java.io.File;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import main.java.za.ac.sun.cs.tereto.engine.Engine;
import main.java.za.ac.sun.cs.tereto.engine.Input;
import main.java.za.ac.sun.cs.tereto.engine.Program;
import main.java.za.ac.sun.cs.tereto.engine.domainexperts.DomainExpert;
import main.java.za.ac.sun.cs.tereto.gui.util.LogLevel;
import main.java.za.ac.sun.cs.tereto.gui.util.LogView;

public class Tereto extends Application {

	private final static Logger root = Logger.getLogger("tereto");

	private final static Logger logger = Logger.getLogger("tereto." + Tereto.class.getSimpleName());

	private final Model model = new Model();
	
	private final ObservableList<DomainExpert> domainExpertList = FXCollections.observableArrayList(Engine.getDomainExperts());

	private final ComboBox<DomainExpert> domainExperts = new ComboBox<>(domainExpertList);

	private final ListView<Program> programList = new ListView<>(model.getProgramList());

	private final ListView<Input> inputList = new ListView<>(model.getInputList());
	
	private final DirectoryChooser directoryChooser = new DirectoryChooser();

	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		HBox domainExpertPane = makeDomainExpertPane();
		HBox controlPane = makeControlPane();
		VBox programListPane = makeProgramListPane(primaryStage);
		VBox inputListPane = makeInputListPane(primaryStage);
		TabPane tabPane = makeTabPane();
		BorderPane b = new BorderPane(tabPane, domainExpertPane, inputListPane, controlPane, programListPane);
		Scene s = new Scene(b, 1200, 800);
		primaryStage.setTitle("TERETO");
		primaryStage.setScene(s);
		logger.info("starting GUI");
		primaryStage.show();
	}

	private HBox makeDomainExpertPane() {
		HBox domainExpertPane = new HBox(5);
		domainExpertPane.setStyle("-fx-background-color: #336699;");
		domainExpertPane.setPadding(new Insets(10, 5, 10, 5));
		domainExpertPane.setAlignment(Pos.CENTER_LEFT);
		Text title = new Text("Domain:");
		title.setFill(Color.WHITE);
		domainExperts.setCellFactory(param -> new ListCell<DomainExpert>() {
			@Override
			protected void updateItem(DomainExpert item, boolean empty) {
				super.updateItem(item, empty);
				if (item != null) {
					setText(item.getName());
				} else {
					setText(null);
				}
			}
		});
		domainExperts.getSelectionModel().selectedItemProperty().addListener((o, ov, nv) -> {
			if (nv != null) {
				logger.info("selected domain expert " + nv.getName());
			}
		});
		domainExpertPane.getChildren().addAll(title, domainExperts);
		return domainExpertPane;
	}

	private HBox makeControlPane() {
		HBox controlPane = new HBox(10);
		logger.fine("creating control pane");
		controlPane.setPadding(new Insets(15, 12, 15, 12));
		controlPane.setStyle("-fx-background-color: #336699;");
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		Button reduceButton = new Button("Reduce");
		Button quitButton = new Button("Quit");
		quitButton.setOnAction(e -> Platform.exit());
		controlPane.getChildren().addAll(reduceButton, spacer, quitButton);
		return controlPane;
	}

	private VBox makeProgramListPane(Stage stage) {
		VBox programListPane = new VBox();
		HBox buttonPane = new HBox(5);
		buttonPane.setPadding(new Insets(10, 5, 10, 5));
		buttonPane.setAlignment(Pos.CENTER_LEFT);
		buttonPane.setStyle("-fx-background-color: #99ccff;");
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		Text title = new Text("Programs");
		Image addIcon = new Image(getClass().getResourceAsStream("/main/resources/za/ac/sun/cs/tereto/gui/open_16.png"));
		Button addButton = new Button("", new ImageView(addIcon));
		addButton.disableProperty().bind(Bindings.isNull(domainExperts.valueProperty()));
		addButton.setOnAction(e -> {
			directoryChooser.setTitle("Open program directory");
			File directory = directoryChooser.showDialog(stage);
			if ((directory != null) && directory.isDirectory()) {
				Engine.loadPrograms(directory);	
			}
		});
		Image clearIcon = new Image(getClass().getResourceAsStream("/main/resources/za/ac/sun/cs/tereto/gui/clear_16.png"));
		Button clearButton = new Button("", new ImageView(clearIcon));
		clearButton.disableProperty().bind(Bindings.isEmpty(programList.getSelectionModel().getSelectedItems()));
		clearButton.setOnAction(e -> programList.getSelectionModel().clearSelection());
		buttonPane.getChildren().addAll(title, spacer, addButton, clearButton);
		VBox.setVgrow(programList, Priority.ALWAYS);
		Image check = new Image(getClass().getResourceAsStream("/main/resources/za/ac/sun/cs/tereto/gui/check_16.png"));
		Image cross = new Image(getClass().getResourceAsStream("/main/resources/za/ac/sun/cs/tereto/gui/cross_16.png"));
		programList.setCellFactory(param -> new ListCell<Program>() {
			@Override
			protected void updateItem(Program item, boolean empty) {
				super.updateItem(item, empty);
				if (item != null) {
					setText(item.getName());
					setGraphic(new ImageView(item.doesCompile() ? check : cross));
				} else {
					setText(null);
				}
			}
		});
		programList.getSelectionModel().selectedItemProperty().addListener((o, ov, nv) -> {
			model.filterInputs(domainExperts.getSelectionModel().getSelectedItem(), nv);
		});
		programListPane.getChildren().addAll(buttonPane, programList);
		return programListPane;
	}

	private VBox makeInputListPane(Stage stage) {
		VBox inputListPane = new VBox();
		HBox buttonPane = new HBox(5);
		buttonPane.setPadding(new Insets(10, 5, 10, 5));
		buttonPane.setAlignment(Pos.CENTER_LEFT);
		buttonPane.setStyle("-fx-background-color: #99ccff;");
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		Text title = new Text("Inputs");
		Image addIcon = new Image(getClass().getResourceAsStream("/main/resources/za/ac/sun/cs/tereto/gui/open_16.png"));
		Button addButton = new Button("", new ImageView(addIcon));
		addButton.disableProperty().bind(Bindings.isEmpty(model.getProgramList()));
		addButton.setOnAction(e -> {
			directoryChooser.setTitle("Open input directory");
			File directory = directoryChooser.showDialog(stage);
			if ((directory != null) && directory.isDirectory()) {
				 model.loadInputs(domainExperts.getSelectionModel().getSelectedItem(), directory);	
			}
		});
		buttonPane.getChildren().addAll(title, spacer, addButton);
		VBox.setVgrow(inputList, Priority.ALWAYS);
		inputList.setCellFactory(param -> new ListCell<Input>() {
			@Override
			protected void updateItem(Input item, boolean empty) {
				super.updateItem(item, empty);
				if (item != null) {
					setText(item.getName());
				} else {
					setText(null);
				}
			}
		});
		inputListPane.getChildren().addAll(buttonPane, inputList);
		return inputListPane;
	}
	
	private TabPane makeTabPane() {
		TabPane tabPane = new TabPane();
		logger.fine("Creating tabbed pane");
		tabPane.getTabs().add(makeLogTab());
		return tabPane;
	}

	private Tab makeLogTab() {
		Tab logTab = new Tab();
		logTab.setText("Log");
		logTab.setClosable(false);
		LogView logView = new LogView(root);
		ChoiceBox<LogLevel> levelChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList(LogLevel.LEVELS));
		levelChoiceBox.getSelectionModel().select(LogLevel.DEBUG);
		levelChoiceBox.setStyle("-fx-font-size:80%;");
		logView.filterLevelProperty().bind(levelChoiceBox.getSelectionModel().selectedItemProperty());
		ToggleButton pauseButton = new ToggleButton("Pause");
		pauseButton.setStyle("-fx-font-size:80%;");
		logView.pausedProperty().bind(pauseButton.selectedProperty());
		HBox buttonPane = new HBox(5, levelChoiceBox, pauseButton);
		buttonPane.setPadding(new Insets(5));
		logTab.setContent(new BorderPane(logView, buttonPane, null, null, null));
		return logTab;
	}
	
}