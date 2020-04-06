package javaThread;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class Exam07_ThreadDaemon extends Application {

	TextArea textarea;
	Button startBtn, stopBtn;
	Thread countThread;
	
	private void printMsg(int msg, String name) {
		Platform.runLater(()->{
			textarea.appendText("count : " + msg + ", name : " + name + "\n");
		});
	}
	@Override
	public void start(Stage primaryStage) throws Exception {

		BorderPane root = new BorderPane();
		root.setPrefSize(700, 500);
		
		textarea = new TextArea();
		root.setCenter(textarea);
		
		startBtn = new Button("Thread 생성");
		startBtn.setPrefSize(200, 50);
		startBtn.setOnAction(e->{
			countThread = new Thread(() ->{
				try {
					for(int i=0; i<100; i++) {
						Thread.sleep(1000);
						final int k = i;
						printMsg(k, Thread.currentThread().getName());						
					}					
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				
			});
			// Daemon thread로 지정하면 Window의 close버튼을 눌러 종료했을 때
			// 파생된 countThread도 같이 종료된다. Eclipse에서 모든 Thread가 
			// 종료되었는지를 확인하면 Daemon Thread의 특징을 확인할 수 있다.
			countThread.setDaemon(true);
			countThread.start();
			
		});
		
		stopBtn = new Button("Thread 중지");
		stopBtn.setPrefSize(200, 50);
		stopBtn.setOnAction(e->{
			countThread.interrupt();
		});
		
		
		FlowPane flowPane = new FlowPane();
		flowPane.setPrefSize(700, 50);
		flowPane.setHgap(10);
		flowPane.getChildren().add(startBtn);
		flowPane.getChildren().add(stopBtn);
		
		root.setBottom(flowPane);
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Thread 연습");
		primaryStage.setOnCloseRequest(e->{
		});
		primaryStage.show();
		
	}
	 
	public static void main(String[] args) {
		launch();
	}	
}