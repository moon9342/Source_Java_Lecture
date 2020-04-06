package javaNetwork;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

class EchoRunnable implements Runnable {
	Socket s;
	BufferedReader br;
	PrintWriter out;
	
	
	public EchoRunnable(Socket s) {
		super();
		this.s = s;
		try {
			this.br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			this.out = new PrintWriter(s.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	@Override
	public void run() {
		String line = "";		
		try {
			while((line = br.readLine()) != null ) {
				if(line.equals("/EXIT/")) {
					break;
				}
				out.println(line);
				out.flush();				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
public class Exam03_EchoServerMultiClient extends Application {

	TextArea textarea;
	Button startBtn, stopBtn;
	ExecutorService executorService = Executors.newCachedThreadPool();
	
	ServerSocket server;
	BufferedReader br;
	PrintWriter pr;
	
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
		
		startBtn = new Button("Echo 서버 시작");
		startBtn.setPrefSize(150, 40);
		startBtn.setOnAction(e->{
			//textarea.clear();
			printMsg("Echo 서버 시작!!");
			
			Runnable serverThread = () -> {
				try {
					server = new ServerSocket(7778);
					while(true) {
						Socket s = server.accept();
						EchoRunnable runnable = new EchoRunnable(s);
						executorService.execute(runnable);
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}							
			};
			executorService.execute(serverThread);
		});

		stopBtn = new Button("Echo 서버 중지");
		stopBtn.setPrefSize(150, 40);
		stopBtn.setOnAction(e->{
			printMsg("Echo 서버 중지!!");
			executorService.shutdown();			
		});
		
		FlowPane flowPane = new FlowPane();
     	flowPane.setPadding(new Insets(10, 10, 10, 10));
		flowPane.setColumnHalignment(HPos.CENTER);
		flowPane.setPrefSize(700, 40);
		flowPane.setHgap(10);
		flowPane.getChildren().add(startBtn);
		flowPane.getChildren().add(stopBtn);
		
		root.setBottom(flowPane);
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("다중 Echo Server");
		primaryStage.setOnCloseRequest(e->{
		});
		primaryStage.show();		
	}
	 
	public static void main(String[] args) {
		launch();
	}	
	
}