package javaThread;

import java.util.stream.IntStream;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class Exam03_ThreadSleep extends Application {

	TextArea textarea;
	Button btn;
	
	private void printMessage(String msg) {
		Platform.runLater(()->{
			textarea.appendText(msg + "\n");
		});
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		// 기본 layout을 BorderPane으로 설정(동서남북중앙)
		BorderPane root = new BorderPane();
		// BorderPane의 size 설정
		root.setPrefSize(700, 500);
			
		// component 생성 및 화면 부착
		textarea = new TextArea();
		root.setCenter(textarea);

		btn = new Button("버튼 클릭!!");
		btn.setPrefSize(250, 50);
		btn.setOnAction((e) -> {
			IntStream intStream = IntStream.rangeClosed(1, 5);
			intStream.forEach(t->{
				Thread thread = new Thread(()->{
					for(int i=0; i<5; i++) {
						try {
							Thread.sleep(1000);
							printMessage(i + "-" + Thread.currentThread().getName());
						} catch (Exception e2) {
							System.out.println(e2);	
						}						
					}
				});
				thread.setName("ThreadNum-" + t);
				thread.start();
			});
		});
		
		FlowPane flowpane = new FlowPane();
		flowpane.setPrefSize(700, 50);
		flowpane.setHgap(10);
		flowpane.getChildren().add(btn);
		
		root.setBottom(flowpane);
		
		
		// Scene 객체를 생성(BorderPane을 이용)
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("예제용 JavaFX");
		primaryStage.setOnCloseRequest(e->{
			
		});
		primaryStage.show();
				
		
	}

	public static void main(String[] args) {
		launch();
	}

}
