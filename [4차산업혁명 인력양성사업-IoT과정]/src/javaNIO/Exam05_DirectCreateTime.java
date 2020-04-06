package javaNIO;

import java.nio.ByteBuffer;
import java.text.NumberFormat;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class Exam05_DirectCreateTime extends Application {

	TextArea textarea;
	Button directBtn, nonDirectBtn;
	
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
		
		directBtn = new Button("Direct Buffer 생성");
		directBtn.setPrefSize(150, 40);
		directBtn.setOnAction(e->{
			int capacity = 5*1024*1024;
			NumberFormat nFormat = NumberFormat.getNumberInstance();
			long start,end;
			
			start = System.nanoTime();
			@SuppressWarnings("unused")
			ByteBuffer buffer = ByteBuffer.allocateDirect(capacity);
			end = System.nanoTime();
			
			printMsg("Direct Buffer 생성 시간 : " + nFormat.format(end-start));
		});

		nonDirectBtn = new Button("Non-Direct Buffer 생성");
		nonDirectBtn.setPrefSize(150, 40);
		nonDirectBtn.setOnAction(e->{
			int capacity = 5*1024*1024;
			NumberFormat nFormat = NumberFormat.getNumberInstance();
			long start,end;
			
			start = System.nanoTime();
			@SuppressWarnings("unused")
			ByteBuffer buffer = ByteBuffer.allocate(capacity);
			end = System.nanoTime();
			
			printMsg("Non-direct buffer 생성 시간 : " + nFormat.format(end-start));			
		});
		
		FlowPane flowPane = new FlowPane();
		flowPane.setPadding(new Insets(10, 10, 10, 10));
		flowPane.setPrefSize(700, 40);
		flowPane.setHgap(10);
		flowPane.getChildren().add(directBtn);
		flowPane.getChildren().add(nonDirectBtn);
		
		root.setBottom(flowPane);
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Direct vs Non-Direct");
		primaryStage.setOnCloseRequest(e->{
		});
		primaryStage.show();
		
	}
	 
	public static void main(String[] args) {
		launch();
	}	

}