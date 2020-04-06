package javaThread;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class Exam06_ThreadInterrupt extends Application {

	TextArea textarea;
	Button startBtn, stopBtn;
	Thread countThread;
	
	private void printMsg(String name) {
		Platform.runLater(()->{
			textarea.appendText(name + "\n");
		});
	}
	
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
					//e1.printStackTrace();
					printMsg("Thread 종료!!");
				}				
			});
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