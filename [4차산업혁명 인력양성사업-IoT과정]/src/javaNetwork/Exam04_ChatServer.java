package javaNetwork;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
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

public class Exam04_ChatServer extends Application {

	TextArea textarea;
	Button startBtn, stopBtn;
	ServerSocket serverSocket;
	ExecutorService executorService = Executors.newCachedThreadPool();
	SharedObject sharedObject = new SharedObject();
	
	private void printMsg(String name) {
		Platform.runLater(()->{
			textarea.appendText(name + "\n");
		});
	}
	
	class SharedObject {
		
		List<ClientRunnable> clients = new ArrayList<ClientRunnable>();
		
		public synchronized void broadcast(String msg) {
			clients.stream().forEach(t -> {
				t.pw.println(msg);
				t.pw.flush();
			});
		}		
	}
	
	
	class ClientRunnable implements Runnable {

		private SharedObject sharedObject;		
		private Socket socket;
		private BufferedReader br;
		private PrintWriter pw;

		public ClientRunnable(SharedObject sharedObject,Socket socket) throws IOException {
			this.sharedObject = sharedObject;
			this.socket = socket;
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pw = new PrintWriter(socket.getOutputStream());						
		}
		
		@Override
		public void run() {
			
			String msg = null;

			try {
				while(true) {
					msg = br.readLine();
					if( msg == null ) {
						break;
					}
					sharedObject.broadcast(msg);					
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				if( pw != null ) pw.close();
				if( br != null ) br.close();				
				if( socket != null ) socket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}			
			sharedObject.clients.remove(this);
		}
		
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {

		BorderPane root = new BorderPane();
		root.setPrefSize(700, 500);
		
		textarea = new TextArea();
		textarea.setEditable(false);
		root.setCenter(textarea);
		
		
		startBtn = new Button("Chat Server 기동");
		startBtn.setPrefSize(150, 40);
		startBtn.setOnAction(e->{
			textarea.clear();
			printMsg("[ 채팅서버 기동 - 5555 ]");
			Runnable runnable = new Runnable() {
				
				@Override
				public void run() {
					try {
						serverSocket = new ServerSocket(5555);
						while(true) {
							printMsg("[ 클라이언트 접속 대기중 ]");
							Socket socket = serverSocket.accept();
							ClientRunnable cRunnable = new ClientRunnable(sharedObject,socket);
							synchronized (this) {
								sharedObject.clients.add(cRunnable);
							}
							printMsg("[ 클라이언트 접속 - 현재 클라이언트 수 : " + sharedObject.clients.size() + " ]");							
							executorService.execute(cRunnable);							
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			};
			executorService.execute(runnable);
		});

		stopBtn = new Button("Chat Server 중지");
		stopBtn.setPrefSize(150, 40);
		stopBtn.setOnAction(e->{
			printMsg("[ 서버 중지 - 프로그램 재시작 필요 ]");
			executorService.shutdownNow();
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
		primaryStage.setTitle("Chat Server ( Java IO )");
		primaryStage.setOnCloseRequest(e->{
			executorService.shutdownNow();
		});
		primaryStage.show();	
		
		 
	}
	 
	public static void main(String[] args) {
		launch();
	}	
	
}