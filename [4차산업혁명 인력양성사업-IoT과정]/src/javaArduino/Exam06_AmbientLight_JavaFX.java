package javaArduino;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

class Exam06_ArduinoSerialListener implements SerialPortEventListener {

	InputStream in;
	BufferedReader br;
	Exam06_AmbientLight_JavaFX window;

	Exam06_ArduinoSerialListener(InputStream in,Exam06_AmbientLight_JavaFX window) {
		this.in = in;
		this.br = new BufferedReader(new InputStreamReader(this.in));
		this.window = window;
	}

	@Override
	public void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {

				int available = in.available();
				System.out.println("받은 바이트수 : " + available);
//				byte chunk[] = new byte[available];
//				in.read(chunk, 0, available);
//
//				String result = new String(chunk);
				String result = br.readLine();
				System.out.println("아두이노에서 받은 메시지 : " + result);

				window.printMessage("조도 : " + result);	

			} catch (Exception e) {
				//System.err.println(e.toString());
			}
		}		

	}	
}

public class Exam06_AmbientLight_JavaFX extends Application {

	TextArea textarea;
	InputStream in;
	BufferedWriter bw;

	public void printMessage(String msg) {
		Platform.runLater(()->{
			textarea.appendText(msg + "\n");
		});
	}

	private void connectSerial() throws Exception {
		CommPortIdentifier portIdentifier = null;		
		portIdentifier = 
				CommPortIdentifier.getPortIdentifier("COM14");

		if ( portIdentifier.isCurrentlyOwned() ) {
			System.out.println("현재 포트가 사용되고 있습니다.");
		} else {

			CommPort commPort = 
					portIdentifier.open("PORT_OPEN",2000);

			if ( commPort instanceof SerialPort ) {
				//포트 설정(통신속도 설정. 기본 9600으로 사용)
				SerialPort serialPort = (SerialPort) commPort;
				serialPort.setSerialPortParams(9600,
						SerialPort.DATABITS_8,
						SerialPort.STOPBITS_1,
						SerialPort.PARITY_NONE);

				//Input,OutputStream 버퍼 생성 후 오픈
				in = serialPort.getInputStream();					
				bw = new BufferedWriter(
						new OutputStreamWriter(
								serialPort.getOutputStream()));

				serialPort.addEventListener(
						new Exam06_ArduinoSerialListener(in,this));
				serialPort.notifyOnDataAvailable(true);

			} else {
				System.out.println("Serial 포트만 사용가능합니다.");
			}
		}     

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane root = new BorderPane();
		root.setPrefSize(700, 500);

		textarea = new TextArea();
		root.setCenter(textarea);

		FlowPane flowpane = new FlowPane();
		flowpane.setPrefSize(700, 50);
		flowpane.setHgap(10);

		root.setBottom(flowpane);

		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("javaFX Arduino Ambient Light");
		primaryStage.setOnCloseRequest(e->{
			System.exit(0);
		});
		primaryStage.show();

		try {
			connectSerial();	
		} catch (Exception e) {
			System.out.println(e);
		}


	}

	public static void main(String[] args) {
		launch();
	}

}

