package javaNetwork;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

public class Exam04_ChatClient extends Application {

	TextArea textarea;
	TextField idfield, msgfield;
	Button connBtn, disconnBtn;
	
	Socket socket;
	BufferedReader br;
	PrintWriter pw;
	ExecutorService executorService = Executors.newCachedThreadPool();
	
	
	private void printMsg(String name) {
		Platform.runLater(()->{
			textarea.appendText(name + "\n");
		});
	}
	
	class ReceiveRunnable implements Runnable {

		private BufferedReader br;
		
		public ReceiveRunnable(BufferedReader br) {
			this.br = br;
		}
		
		@Override
		public void run() {
			String msg = null;
			try {
				while(true) {
					msg = br.readLine();
					if( msg == null ) {
						break;
					}
					printMsg(msg);					
				}
			} catch (IOException e) {
				// e.printStackTrace();
			} finally {
				try {
					if( br != null ) br.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {

		BorderPane root = new BorderPane();
		root.setPrefSize(700, 500);
		
		textarea = new TextArea();
		textarea.setEditable(false);
		root.setCenter(textarea);
		
		connBtn = new Button("Chat 서버 접속");
		connBtn.setPrefSize(150, 40);
		connBtn.setOnAction(e->{
			textarea.clear();
			disconnBtn.setDisable(false);
			try {
				socket = new Socket("localhost",5555);
				br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				pw = new PrintWriter(socket.getOutputStream());		
				printMsg("Chat Server 연결 성공");
				
				idfield.setDisable(false);
				msgfield.setDisable(false);
				
				ReceiveRunnable receiver = new ReceiveRunnable(br);
				executorService.execute(receiver);
				connBtn.setDisable(true);
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}			
		});

		idfield = new TextField();
		idfield.setPrefSize(70, 40);
		idfield.setDisable(true);
		
		msgfield = new TextField();
		msgfield.setPrefSize(200, 40);
		msgfield.setDisable(true);
		msgfield.setOnAction(e->{
			
			String msg = "[" + idfield.getText() + "] : " + msgfield.getText();
			pw.println(msg);
			pw.flush();
			msgfield.clear();
			
		});
		
		disconnBtn = new Button("Chat 서버 접속 종료");
		disconnBtn.setPrefSize(150, 40);
		disconnBtn.setDisable(true);
		
		disconnBtn.setOnAction(e->{
			connBtn.setDisable(false);
			idfield.setDisable(false);
			msgfield.setDisable(false);
			try {
				printMsg("[서버연결 종료]");
				// br은 blocking중이므로 pw를 먼저 close.
				if( pw != null ) pw.close();
				if( br != null ) br.close();				
				if( socket != null ) socket.close();
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}	
			disconnBtn.setDisable(true);
		});
		
		FlowPane flowPane = new FlowPane();
     	flowPane.setPadding(new Insets(10, 10, 10, 10));
		flowPane.setColumnHalignment(HPos.CENTER);
		flowPane.setPrefSize(700, 40);
		flowPane.setHgap(10);
		flowPane.getChildren().add(connBtn);
		flowPane.getChildren().add(idfield);
		flowPane.getChildren().add(msgfield);
		flowPane.getChildren().add(disconnBtn);
		
		root.setBottom(flowPane);
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Echo Server에 접속");
		primaryStage.setOnCloseRequest(e->{
			try {
				// br은 blocking중이므로 pw를 먼저 close.
				if( pw != null ) pw.close();
				if( br != null ) br.close();				
				if( socket != null ) socket.close();
				executorService.shutdownNow();
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