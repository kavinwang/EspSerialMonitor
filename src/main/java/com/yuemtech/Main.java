package com.yuemtech;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.yuemtech.datas.Item;
import com.yuemtech.settings.PortSetter;
import com.yuemtech.support.SerialController;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Pagination;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.util.StringConverter;

public class Main extends Application {
	Boolean monitorPortOpened;
	SerialController sc = new SerialController(PortSetter.getPortBandRate(PortSetter.PORT_MONITOR));

	@Override
	public void init() throws Exception {
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		 HBox bottomControls;
		 ProgressBar pb;
		 Label messageLabel;
	
		 TextField tfURL;
	
		 ListView<String>  contents;
		VBox vbox = new VBox();
        vbox.setPadding( new Insets(10) );
        vbox.setSpacing( 10 );

        HBox topControls = new HBox();
        topControls.setAlignment(Pos.CENTER_LEFT);
        topControls.setSpacing( 4 );

        Label label = new Label("URL");
        tfURL = new TextField();
        HBox.setHgrow( tfURL, Priority.ALWAYS );
        Button btnGetHTML = new Button("Get HTML");
        // btnGetHTML.setOnAction( this::getHTML );
        topControls.getChildren().addAll(label, tfURL, btnGetHTML);

		contents = new ListView<>();
        VBox.setVgrow( contents, Priority.ALWAYS );

        bottomControls = new HBox();
        bottomControls.setVisible(false);
        bottomControls.setSpacing( 4 );
        HBox.setMargin( bottomControls, new Insets(4));

        pb = new ProgressBar();
        messageLabel = new Label("");
        bottomControls.getChildren().addAll(pb, messageLabel);

        vbox.getChildren().addAll(topControls, contents, bottomControls);
		Scene scene = new Scene(vbox);

        primaryStage.setTitle("ProgressBarApp");
        primaryStage.setWidth( 667 );
        primaryStage.setHeight( 376 );
        primaryStage.setScene( scene );
		
		primaryStage.setOnShown(e -> {
			if (PortSetter.getPortName(PortSetter.PORT_MONITOR) == null) return;
			try {
				sc.openPort(PortSetter.getPortName(PortSetter.PORT_MONITOR), (datas) -> {
					String d = new String(datas).replace("\033[0;32m", "").replace("\033[0m", "");

					Platform.runLater(()->contents.getItems().addAll(d.split("\r\n")));
				});
				monitorPortOpened = true;
			} catch (Exception e1) {
				SerialController.findPorts().forEach((p) -> System.out.println(p));
			}

		});
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
