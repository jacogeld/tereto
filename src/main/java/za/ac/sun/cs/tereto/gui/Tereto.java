package main.java.za.ac.sun.cs.tereto.gui;

import java.util.logging.Logger;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import main.java.za.ac.sun.cs.tereto.gui.util.LogLevel;
import main.java.za.ac.sun.cs.tereto.gui.util.LogView;

public class Tereto extends Application {

	private final static Logger root = Logger.getLogger("tereto");

	private final static Logger logger = Logger.getLogger("tereto." + Tereto.class.getSimpleName());
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		TabPane t = new TabPane();
		{
			Tab lt = new Tab();
			lt.setText("Log");
			lt.setClosable(false);
			LogView l = new LogView(root);
			ChoiceBox<LogLevel> f = new ChoiceBox<>(FXCollections.observableArrayList(LogLevel.LEVELS));
			f.getSelectionModel().select(LogLevel.DEBUG);
			f.setStyle("-fx-font-size:80%;");
			l.filterLevelProperty().bind(f.getSelectionModel().selectedItemProperty());
			ToggleButton b = new ToggleButton("Pause");
			b.setStyle("-fx-font-size:80%;");
			l.pausedProperty().bind(b.selectedProperty());
			HBox h = new HBox(5, f, b);
			h.setPadding(new Insets(5));
			lt.setContent(new BorderPane(l, h, null, null, null));
			t.getTabs().add(lt);
		}
		BorderPane b = new BorderPane(t);
		Scene s = new Scene(b);
		primaryStage.setTitle("TERETO");
		primaryStage.setScene(s);
		logger.info("Starting GUI");
		primaryStage.show();
	}

}