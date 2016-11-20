package main.java.za.ac.sun.cs.tereto.gui;

import java.util.logging.Logger;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.java.za.ac.sun.cs.tereto.engine.Engine;
import main.java.za.ac.sun.cs.tereto.engine.domainexperts.DomainExpert;
import main.java.za.ac.sun.cs.tereto.gui.util.LogLevel;
import main.java.za.ac.sun.cs.tereto.gui.util.LogView;

public class Tereto extends Application {

	private final static Logger root = Logger.getLogger("tereto");

	private final static Logger logger = Logger.getLogger("tereto." + Tereto.class.getSimpleName());

	public static void main(String[] args) {
		launch(args);
	}

	private final ObservableList<DomainExpert> domainExpertList = FXCollections.observableArrayList(Engine.getDomainExperts());

	private final ComboBox<DomainExpert> domainExperts = new ComboBox<>(domainExpertList);

	@Override
	public void start(Stage primaryStage) throws Exception {
		HBox domainExpertPane = makeDomainExpertPane();
		HBox controlPane = makeControlPane();
		TabPane tabPane = makeTabPane();
		BorderPane b = new BorderPane(tabPane, domainExpertPane, null, controlPane, null);
		Scene s = new Scene(b, 1200, 800);
		primaryStage.setTitle("TERETO");
		primaryStage.setScene(s);
		logger.info("starting GUI");
		primaryStage.show();
	}

	private HBox makeDomainExpertPane() {
		HBox p = new HBox(5);
		p.setStyle("-fx-background-color: #336699;");
		p.setPadding(new Insets(10, 5, 10, 5));
		p.setAlignment(Pos.CENTER_LEFT);
		Text t = new Text("Domain:");
		t.setFill(Color.WHITE);
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
		domainExperts.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<DomainExpert>() {
			@Override
			public void changed(ObservableValue<? extends DomainExpert> observable, DomainExpert oldValue,
					DomainExpert newValue) {
				if (newValue != null) {
					logger.info("selected domain expert " + newValue.getName());
				}
			}
		});
		p.getChildren().addAll(t, domainExperts);
		return p;
	}

	private HBox makeControlPane() {
		HBox p = new HBox(10);
		logger.fine("creating control pane");
		p.setPadding(new Insets(15, 12, 15, 12));
		p.setStyle("-fx-background-color: #336699;");
		Region s = new Region();
		HBox.setHgrow(s, Priority.ALWAYS);
		Button r = new Button("Reduce");
		//			BooleanBinding p = Bindings.isNull(teretoEngine.getProgram());
		//			BooleanBinding i = Bindings.isNull(teretoEngine.getInput());
		//			reduce.setOnAction(e -> teretoEngine.reduce());
		//			reduce.disableProperty().bind(Bindings.or(p, i));
		Button q = new Button("Quit");
		q.setOnAction(e -> Platform.exit());
		p.getChildren().addAll(r, s, q);
		return p;
	}

	private TabPane makeTabPane() {
		TabPane p = new TabPane();
		logger.fine("Creating tabbed pane");
		Tab t = new Tab();
		t.setText("Log");
		t.setClosable(false);
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
		t.setContent(new BorderPane(l, h, null, null, null));
		p.getTabs().add(t);
		return p;
	}

}