package javaThread;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class Exam09_ThreadPoolRunnable extends Application {

	TextArea textarea;
	Button startBtn, initBtn, stopBtn;
	ExecutorService executorService;
	
	
	private void printMsg(String name) {
		Platform.runLater(()->{
			textarea.appendText(name + "\n");
		});
	}
	
	private void createThread() {
		for(int i=0; i< 3; i++) {
			final int k = i;
			Runnable runnable = new Runnable() {
				
				@Override
				public void run() {		
					Thread.currentThread().setName("MyThread-" + k);
					String msg = "Thread Name : " + Thread.currentThread().getName();
					msg += ", Thread Pool안의 Thread 개수 : " + ((ThreadPoolExecutor)executorService).getPoolSize();
					System.out.println("Thread Name : " + Thread.currentThread().getName());					
					printMsg(msg);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
			Future<?> future = executorService.submit(runnable);
			try {
				future.get();   // blocking (순차처리가 된다.)
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void createThreadPool() {
		//executorService = Executors.newFixedThreadPool(5);
		executorService = Executors.newCachedThreadPool();
		printMsg("Thread Pool안의 Thread 개수 : " + ((ThreadPoolExecutor)executorService).getPoolSize());
	}
	@Override
	public void start(Stage primaryStage) throws Exception {

		BorderPane root = new BorderPane();
		root.setPrefSize(700, 500);
		
		textarea = new TextArea();
		root.setCenter(textarea);
		
		startBtn = new Button("Thread Pool 생성");
		startBtn.setPrefSize(200, 50);
		startBtn.setOnAction(e->{
			createThreadPool();
		});
		
		initBtn = new Button("Thread 생성");
		initBtn.setPrefSize(200, 50);
		initBtn.setOnAction(e->{
			createThread();
		});
		
		stopBtn = new Button("Thread Pool 중지");
		stopBtn.setPrefSize(200, 50);
		stopBtn.setOnAction(e->{
			executorService.shutdown();
		});
		
		
		FlowPane flowPane = new FlowPane();
		flowPane.setPrefSize(700, 50);
		flowPane.setHgap(10);
		flowPane.getChildren().add(startBtn);
		flowPane.getChildren().add(initBtn);
		flowPane.getChildren().add(stopBtn);
		
		root.setBottom(flowPane);
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Thread 연습");
		primaryStage.setOnCloseRequest(e->{
			executorService.shutdown();
		});
		primaryStage.show();
		
	}
	 
	public static void main(String[] args) {
		launch();
	}	

}