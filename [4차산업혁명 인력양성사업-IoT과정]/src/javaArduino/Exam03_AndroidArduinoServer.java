package javaArduino;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;


public class Exam03_AndroidArduinoServer extends Application {

	TextArea textarea;
	Button btn;
	ServerSocket server;
	ExecutorService executorService = Executors.newCachedThreadPool();
	
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

		btn = new Button("서버기동");
		btn.setPrefSize(250, 50);
		btn.setOnAction((e) -> {
			Runnable runnable = () -> {
				try {
					server = new ServerSocket(7890);
					printMessage("[Android Arduino Server 기동-클라이언트 접속 대기중]");
					Socket s = server.accept();
					printMessage("[Android Arduino Server 기동-클라이언트 접속 성공]");
					BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
					PrintWriter pr = new PrintWriter(s.getOutputStream());
					String line = "";
					while(true) {
						if((line = br.readLine()) != null) {
							if(line.equals("LED_ON")) {
								printMessage("불이켜져요!!");
							}
						}
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			};
			executorService.execute(runnable);
		});
		
		FlowPane flowpane = new FlowPane();
		flowpane.setPrefSize(700, 50);
		flowpane.setHgap(10);
		flowpane.getChildren().add(btn);
		
		root.setBottom(flowpane);
		
		
		// Scene 객체를 생성(BorderPane을 이용)
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Android Arduino Server");
		primaryStage.setOnCloseRequest(e->{
			
		});
		primaryStage.show();
		
	}

	public static void main(String[] args) {
		launch();
	}

}
