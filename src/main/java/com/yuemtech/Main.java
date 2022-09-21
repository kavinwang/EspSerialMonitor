package com.yuemtech;

import com.yuemtech.settings.PortSetter;
import com.yuemtech.support.SerialController;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application{
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
//		SerialController sc = new SerialController(PortSetter.getPortBandRate(PortSetter.PORT_LEFT));
//		sc.openPort("tty.usbserial-1440", new SerialController.SerialPortListener() {
//			@Override
//			public void dataRecieved(byte[] datas) {
//				System.out.println(new String(datas));
//			}
//		});

		BorderPane root = new BorderPane();
		Scene scene = new Scene(root,400,400);
		primaryStage.setScene(scene);
		primaryStage.show();
	
//		Thread.ofVirtual().start(()-> System.out.println("in virtual"));
		
	}
}
