package javaThread;

import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class Exam12_ThreadPoolCallback extends Application {

	TextArea textarea;
	Button startBtn, initBtn, stopBtn;
	ExecutorService executorService;
	private int total, callCount;
	
	
	private void printMsg(String name) {
		Platform.runLater(()->{
			textarea.appendText(name + "\n");
		});
	}
	
	CompletionHandler<Integer, Void> handler = 
			new CompletionHandler<Integer, Void>() {

				@Override
				public void completed(Integer result, Void attachment) {
					synchronized (this) {
						callCount++;
						total += result;
						System.out.println("callCount : " + callCount);
						if(callCount == 10) {
							printMsg("최종 합은 : " + total);
						}						
					}
				}

				@Override
				public void failed(Throwable exc, Void attachment) {
					System.out.println("이상발생!!");
					
				}
	};
	
	private void createThread() {
		for(int i=1; i< 101; i=i+10) {
			final int k = i;
			Runnable callable = new Runnable() {

				@Override
				public void run() {
					IntStream intStream = IntStream.rangeClosed(k, k+9);
					int sum = intStream.sum();
					printMsg(Thread.currentThread().getName() + "의 SUM : " + sum);
					try {
						Thread.sleep(3000);
						handler.completed(sum, null);
					} catch (InterruptedException e) {
						e.printStackTrace();
						handler.failed(e, null);
					}
				}
				
			};
			executorService.execute(callable);			
		}
		
	}
	
	private void createThreadPool() {
		executorService = Executors.newCachedThreadPool();
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