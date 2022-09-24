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
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Pagination;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.util.StringConverter;

public class Main extends Application {
    SerialController sc = new SerialController(PortSetter.getPortBandRate(PortSetter.PORT_MONITOR));

    @Override
    public void start(Stage primaryStage) throws Exception {

        VBox interactPanel = new VBox();
        {
            HBox interactTop = new HBox();
            Label title = new Label("串口通讯：COM5");
            Button settingBtn = new Button("设置");
            interactTop.getChildren().addAll(title, settingBtn);
            HBox interactDatas = new HBox();
            interactDatas.getChildren().add(new Label("内容"));
            interactPanel.getChildren().addAll(interactTop, interactDatas);
        }
        VBox monitorPanel = new VBox();
        {
            HBox monitorTop = new HBox();
            monitorTop.setAlignment(Pos.BOTTOM_LEFT);// 设置靠哪边
            Label title = new Label("串口通讯：COM4");
            title.setAlignment(Pos.BOTTOM_LEFT);
            HBox.setHgrow(title, Priority.ALWAYS);
            Button settingBtn = new Button("设置");
            monitorTop.getChildren().addAll(title, settingBtn);
            HBox monitorDatas = new HBox();
            monitorDatas.getChildren().add(new Label("内容"));
            monitorPanel.getChildren().addAll(monitorTop, monitorDatas);
        }

        SplitPane pane = new SplitPane();
        pane.setOrientation(Orientation.VERTICAL);
		pane.getItems().addAll(interactPanel, monitorPanel);
        Scene scene = new Scene(pane);
        primaryStage.setTitle("Esp32串口交互与监控");
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);
        primaryStage.setScene(scene);

        primaryStage.setOnShown(e -> {
            if (PortSetter.getPortName(PortSetter.PORT_MONITOR) == null) return;
            try {
                sc.openPort(PortSetter.getPortName(PortSetter.PORT_MONITOR), (datas) -> {
//                    String d = new String(datas).replace("\033[0;32m", "").replace("\033[0m", "");
//                    Platform.runLater(() -> contents.getItems().addAll(d.split("\r\n")));
					System.out.println(new String(datas));
                });
				Thread t = new Thread(()->{
					int count = 0;
					while (true){
						try {
							Thread.sleep(1000);
							sc.directSend((count++ + ": 12324324324325324434").getBytes());
						} catch (Exception ignore) {}
					}
				});
				t.setDaemon(true);
				t.start();
            } catch (Exception e1) {
                SerialController.findPorts().forEach(System.out::println);
            }
        });
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
