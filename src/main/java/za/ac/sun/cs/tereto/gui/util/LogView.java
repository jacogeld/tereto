package main.java.za.ac.sun.cs.tereto.gui.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Duration;

public class LogView extends ListView<LogRecord> {

	private static final int MAX_ENTRIES = 10_000;

	private static final Paint ERROR_PAINT = Color.RED;
	private static final Paint WARNING_PAINT = Color.PURPLE;
	private static final Paint INFO_PAINT = Color.GREEN;
	private static final Paint CONFIG_PAINT = Color.BLUE;
	private static final Paint DEBUG_PAINT = Color.GRAY;
	private static final Paint OTHER_PAINT = Color.BLACK;

	private static final SimpleDateFormat timestampFormatter = new SimpleDateFormat("HH:mm:ss.SSS");

	private final BooleanProperty showTimestamp = new SimpleBooleanProperty(true);
	private final ObjectProperty<LogLevel> filterLevel = new SimpleObjectProperty<>();
	private final BooleanProperty tail = new SimpleBooleanProperty(true);
	private final BooleanProperty paused = new SimpleBooleanProperty(false);
	private final DoubleProperty refreshRate = new SimpleDoubleProperty(60);

	private final ObservableList<LogRecord> logItems = FXCollections.observableArrayList();

	public BooleanProperty showTimeStampProperty() {
		return showTimestamp;
	}

	public ObjectProperty<LogLevel> filterLevelProperty() {
		return filterLevel;
	}

	public BooleanProperty tailProperty() {
		return tail;
	}

	public BooleanProperty pausedProperty() {
		return paused;
	}

	public DoubleProperty refreshRateProperty() {
		return refreshRate;
	}

	public LogView(Logger logger) {
		logger.addHandler(new LogHandler());
		logger.setUseParentHandlers(false);
		getStyleClass().add("log-view");
		Timeline logTransfer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
			logRecords.drainTo(logItems);
			if (logItems.size() > MAX_ENTRIES) {
				logItems.remove(0, logItems.size() - MAX_ENTRIES);
			}
			if (tail.get()) {
				scrollTo(logItems.size());
			}
		}));
		logTransfer.setCycleCount(Timeline.INDEFINITE);
		logTransfer.rateProperty().bind(refreshRateProperty());
		this.pausedProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue && logTransfer.getStatus() == Animation.Status.RUNNING) {
				logTransfer.pause();
			}
			if (!newValue && logTransfer.getStatus() == Animation.Status.PAUSED && getParent() != null) {
				logTransfer.play();
			}
		});
		this.parentProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue == null) {
				logTransfer.pause();
			} else if (!paused.get()) {
				logTransfer.play();
			}
		});
		filterLevel.addListener((observable, oldValue, newValue) -> {
			setItems(new FilteredList<LogRecord>(logItems,
					logRecord -> (logRecord.getLevel().intValue() >= filterLevel.get().getValue().intValue())));
		});
		filterLevel.set(LogLevel.DEBUG);
		setCellFactory(param -> new ListCell<LogRecord>() {
			{
				showTimestamp.addListener(observable -> updateItem(this.getItem(), this.isEmpty()));
			}

			@Override
			protected void updateItem(LogRecord item, boolean empty) {
				super.updateItem(item, empty);
				if ((item == null) || empty) {
					setText(null);
					return;
				}
				String context = (item.getLoggerName() == null) ? "" : " [" + item.getLoggerName() + "] ";
				if (showTimestamp.get()) {
					String timestamp = timestampFormatter.format(new Date(item.getMillis()));
					setText(timestamp + context + item.getMessage());
				} else {
					setText(context + item.getMessage());
				}
				setBackground(Background.EMPTY);
				int l = item.getLevel().intValue();
				if (l >= Level.SEVERE.intValue()) {
					setTextFill(ERROR_PAINT);
				} else if (l >= Level.WARNING.intValue()) {
					setTextFill(WARNING_PAINT);
				} else if (l >= Level.INFO.intValue()) {
					setTextFill(INFO_PAINT);
				} else if (l >= Level.CONFIG.intValue()) {
					setTextFill(CONFIG_PAINT);
				} else if (l >= Level.FINE.intValue()) {
					setTextFill(DEBUG_PAINT);
				} else {
					setTextFill(OTHER_PAINT);
				}
			}
		});
	}

	private static final int MAX_LOG_ENTRIES = 1_000_000;

	private final BlockingDeque<LogRecord> logRecords = new LinkedBlockingDeque<>(MAX_LOG_ENTRIES);
	
	public class LogHandler extends Handler {

		@Override
		public void publish(LogRecord record) {
			logRecords.offer(record);
		}

		@Override
		public void flush() {
		}

		@Override
		public void close() throws SecurityException {
		}
		
	}
	
}