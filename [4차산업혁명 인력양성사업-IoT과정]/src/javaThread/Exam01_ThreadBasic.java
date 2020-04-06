
package javaThread;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class Exam01_ThreadBasic extends Application {

	TextArea textarea;
	Button startBtn;
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		System.out.println("2번 Thread : " + Thread.currentThread().getName());
		
		// 현재 사용되고 있는 Thread의 이름을 설정.
		Thread.currentThread().setName("화면그림그리는 Thread");
		BorderPane root = new BorderPane();
		root.setPrefSize(700, 500);
		
		textarea = new TextArea();
		root.setCenter(textarea);
		
		startBtn = new Button("Thread 생성");
		startBtn.setPrefSize(200, 50);

		startBtn.setOnAction(e->{
			Thread t = new Thread(() -> {
				System.out.println("3번 Thread : " + Thread.currentThread().getName());
				Platform.runLater(() -> {
					System.out.println("4번 Thread : " + Thread.currentThread().getName());
					textarea.appendText("버튼이 클릭되었어요!! \n");
				});				
			});
			t.start();
		});

		
		FlowPane flowPane = new FlowPane();
		flowPane.setPrefSize(700, 50);
		flowPane.setHgap(10);
		flowPane.getChildren().add(startBtn);
				
		root.setBottom(flowPane);
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Thread 연습");
		primaryStage.setOnCloseRequest(e->{
			
		});
		primaryStage.show();
		
	}
	 
	public static void main(String[] args) {
		// 현재 사용되고 있는 Thread의 이름을 알아온다.
		System.out.println("1번 Thread : " + Thread.currentThread().getName());
		launch();
	}
}
