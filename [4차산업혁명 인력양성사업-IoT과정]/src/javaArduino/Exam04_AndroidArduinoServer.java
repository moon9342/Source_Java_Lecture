package javaArduino;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;


class Exam04_ArduinoSerialListener implements SerialPortEventListener {

	InputStream in;
	BufferedWriter bw;
	Exam04_AndroidArduinoServer window;
	

	Exam04_ArduinoSerialListener(InputStream in, BufferedWriter bw, Exam04_AndroidArduinoServer window) {
		this.in = in;
		this.bw = bw;
		this.window = window;
	}
	
	@Override
	public void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
            	
            	int available = in.available();
            	System.out.println("받은 바이트수 : " + available);
                byte chunk[] = new byte[available];
                in.read(chunk, 0, available);
                 
                System.out.println("아두이노에서 받은 메시지 : " + new String(chunk));
                
                window.pr.println(new String(chunk));
                window.pr.flush();
                
            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }		
		
	}	
}

public class Exam04_AndroidArduinoServer extends Application {

	TextArea textarea;
	Button btn;
	ServerSocket server;
	ExecutorService executorService = Executors.newCachedThreadPool();
	BufferedReader br;
	BufferedWriter bw;
	PrintWriter pr;
	
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
					pr = new PrintWriter(s.getOutputStream());
					String line = "";
					while(true) {
						if((line = br.readLine()) != null) {
							if(line.equals("LED_ON")) {
								printMessage("불이켜져요!!");
								bw.write(line,0,line.length());								
								bw.flush();
							}
							if(line.equals("LED_OFF")) {
								printMessage("불이꺼져요!!");
								bw.write(line,0,line.length());								
								bw.flush();
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
			System.exit(0);
		});
		primaryStage.show();
		
        CommPortIdentifier portIdentifier = null;		
		try {
			portIdentifier = CommPortIdentifier.getPortIdentifier("COM14");
			
	        if ( portIdentifier.isCurrentlyOwned() ) {
	        	System.out.println("현재 포트가 사용되고 있습니다.");
	        } else {

	          CommPort commPort = 
	    		portIdentifier.open("TT",2000);
	        	
	            if ( commPort instanceof SerialPort ) {
	                //포트 설정(통신속도 설정. 기본 9600으로 사용)
	                SerialPort serialPort = (SerialPort) commPort;
						serialPort.setSerialPortParams(9600,
								SerialPort.DATABITS_8,
								SerialPort.STOPBITS_1,
								SerialPort.PARITY_NONE);

					//Input,OutputStream 버퍼 생성 후 오픈
					InputStream in = serialPort.getInputStream();
					br = new BufferedReader(new InputStreamReader(in));	
		            bw = new BufferedWriter(new OutputStreamWriter(serialPort.getOutputStream()));
		                
		            serialPort.addEventListener(new Exam04_ArduinoSerialListener(in,bw,this));
	                serialPort.notifyOnDataAvailable(true);
	                                
	            } else {
	            	System.out.println("Serial 포트만 사용가능합니다.");
	            }
	        }     
			
		} catch(Exception e) {
			System.out.println(e);
		}
		
	}

	public static void main(String[] args) {
		launch();
	}

}
