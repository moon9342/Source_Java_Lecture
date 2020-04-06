package javaNetwork;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class Exam01_DateClient extends Application {

	TextArea textarea;
	Button connBtn;
	
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
		
		connBtn = new Button("Date 서버 접속");
		connBtn.setPrefSize(150, 40);
		connBtn.setOnAction(e->{
			textarea.clear();
			try {
				Socket socket = new Socket("localhost",5555);
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				
				String msg = br.readLine();
				
				printMsg(msg);
				
				br.close();
				socket.close();
				printMsg("서버와의 연결 종료");
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		});
		
		
		
		FlowPane flowPane = new FlowPane();
     	flowPane.setPadding(new Insets(10, 10, 10, 10));
		flowPane.setColumnHalignment(HPos.CENTER);
		flowPane.setPrefSize(700, 40);
		flowPane.setHgap(10);
		flowPane.getChildren().add(connBtn);
		
		root.setBottom(flowPane);
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Date Server로 부터 현재 날짜 가져오기");
		primaryStage.setOnCloseRequest(e->{
		});
		primaryStage.show();
		
	}
	 
	public static void main(String[] args) {
		launch();
	}	
	

}