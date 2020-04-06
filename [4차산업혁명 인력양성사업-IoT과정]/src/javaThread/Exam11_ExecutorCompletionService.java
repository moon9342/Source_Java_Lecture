package javaThread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class Exam11_ExecutorCompletionService extends Application {

	TextArea textarea;
	Button startBtn, initBtn, stopBtn;
	ExecutorService executorService;
	ExecutorCompletionService<Integer> executorCompletionService;	
	private int total;
	
	
	private void printMsg(String name) {
		Platform.runLater(()->{
			textarea.appendText(name + "\n");
		});
	}
	
	private void createThread() {
		for(int i=1; i< 101; i=i+10) {
			final int k = i;
			Callable<Integer> callable = new Callable<Integer>() {

				@Override
				public Integer call() throws Exception {
					IntStream intStream = IntStream.rangeClosed(k, k+9);
					int sum = intStream.sum();
					printMsg(Thread.currentThread().getName() + "의 SUM : " + sum);
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					return sum;
				}
				
			};
			executorCompletionService.submit(callable);			
		}
		
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				try {
					for(int i=0; i<10; i++) {
						Future<Integer> future = executorCompletionService.take();
						try {
							total += future.get();
						} catch (ExecutionException e) {
							e.printStackTrace();
						}
					}
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				printMsg("최종 결과값은  : " + total);
			}			
		};
		executorService.execute(runnable);
	}
	
	private void createThreadPool() {
		executorService = Executors.newCachedThreadPool();
		executorCompletionService = new ExecutorCompletionService<Integer>(executorService); 		
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