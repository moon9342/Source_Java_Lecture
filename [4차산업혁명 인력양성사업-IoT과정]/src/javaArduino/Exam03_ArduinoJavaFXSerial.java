package javaArduino;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

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

class Exam03_ArduinoSerialListener implements SerialPortEventListener {

	InputStream in;
	Exam03_ArduinoJavaFXSerial window;

	Exam03_ArduinoSerialListener(InputStream in,Exam03_ArduinoJavaFXSerial window) {
		this.in = in;
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

				String result = new String(chunk);
				System.out.println("아두이노에서 받은 메시지 : " + result);

				if(result.equals("O")) {
					window.printMessage("아두이노에서 버튼부품 클릭해서 LED 켜기");	
				}

			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}		

	}	
}

public class Exam03_ArduinoJavaFXSerial extends Application {

	TextArea textarea;
	Button ledOnBtn, ledOffBtn;
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
						new Exam03_ArduinoSerialListener(in,this));
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

		ledOnBtn = new Button("LED 켜기");
		ledOnBtn.setPrefSize(250, 50);
		ledOnBtn.setOnAction((e) -> {
			String line = "LED_ON";
			printMessage("LED 켜기버튼 클릭!!");
			try {
				bw.write(line,0,line.length());
				bw.flush();				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		ledOffBtn = new Button("LED 끄기");
		ledOffBtn.setPrefSize(250, 50);
		ledOffBtn.setOnAction((e) -> {
			String line = "LED_OFF";
			printMessage("LED 끄기버튼 클릭!!");
			try {
				bw.write(line,0,line.length());
				bw.flush();				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}			
		});

		FlowPane flowpane = new FlowPane();
		flowpane.setPrefSize(700, 50);
		flowpane.setHgap(10);
		flowpane.getChildren().add(ledOnBtn);
		flowpane.getChildren().add(ledOffBtn);

		root.setBottom(flowpane);

		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("javaFX Arduino Serial 통신");
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
