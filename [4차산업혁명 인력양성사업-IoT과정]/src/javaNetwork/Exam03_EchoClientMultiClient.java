package javaNetwork;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class Exam03_EchoClientMultiClient extends Application {

	TextArea textarea;
	TextField textfield;
	Button connBtn;
	
	Socket socket;
	BufferedReader br;
	PrintWriter pr;
	
	private void printMsg(String name) {
		Platform.runLater(()->{
			textarea.appendText(name + "\n");
		});
	}
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {

		BorderPane root = new BorderPane();
		root.setPrefSize(700, 500);
		
		textarea = new TextArea();
		root.setCenter(textarea);
		
		connBtn = new Button("Echo 서버 접속");
		connBtn.setPrefSize(150, 40);
		connBtn.setOnAction(e->{
			textarea.clear();
			try {
				socket = new Socket("localhost",7778);
				br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				pr = new PrintWriter(socket.getOutputStream());		
				printMsg("Echo Server 연결 성공");
				textfield.setDisable(false);
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}			
		});

		textfield = new TextField();
		textfield.setPrefSize(200, 40);
		textfield.setDisable(true);
		textfield.setOnAction(e->{
			
			String msg = textfield.getText();
			pr.println(msg);
			pr.flush();
			textfield.clear();
			
			if(!msg.equals("/exit")) {
				try {
					String serverMsg = br.readLine();
					printMsg("[서버로부터 전달된 메시지] : " + serverMsg);
				} catch (IOException e1) {
					e1.printStackTrace();
				}				
			} else {
				printMsg("[서버와의 연결 종료]");
				textfield.setDisable(true);
			}
		});
		
		
		FlowPane flowPane = new FlowPane();
     	flowPane.setPadding(new Insets(10, 10, 10, 10));
		flowPane.setColumnHalignment(HPos.CENTER);
		flowPane.setPrefSize(700, 40);
		flowPane.setHgap(10);
		flowPane.getChildren().add(connBtn);
		flowPane.getChildren().add(textfield);
		
		root.setBottom(flowPane);
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Echo Server에 접속");
		primaryStage.setOnCloseRequest(e->{
			try {
				if( br != null ) br.close();
				if( pr != null ) pr.close();
				if( socket != null ) socket.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		});
		primaryStage.show();		
	}
	 
	public static void main(String[] args) {
		launch();
	}	
	
}