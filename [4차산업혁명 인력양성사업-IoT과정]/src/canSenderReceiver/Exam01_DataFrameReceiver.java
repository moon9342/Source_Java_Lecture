package canSenderReceiver;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class Exam01_DataFrameReceiver extends Application {

	TextArea textarea;        // 메시지창 
	Button connBtn;           // 포트 연결 버튼
	
	private CommPortIdentifier portIdentifier;
	private CommPort commPort;
	private SerialPort serialPort;
	
	private BufferedInputStream bin;
	private OutputStream out;
	
	
	class MyPortListener implements SerialPortEventListener {
		@Override
		public void serialEvent(SerialPortEvent event) {
			switch (event.getEventType()) {
			case SerialPortEvent.BI:
			case SerialPortEvent.OE:
			case SerialPortEvent.FE:
			case SerialPortEvent.PE:
			case SerialPortEvent.CD:
			case SerialPortEvent.CTS:
			case SerialPortEvent.DSR:
			case SerialPortEvent.RI:
			case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
				break;
			case SerialPortEvent.DATA_AVAILABLE:
				byte[] readBuffer = new byte[128];

				try {
					while (bin.available() > 0) {
						int numBytes = bin.read(readBuffer);
					}
					String ss = new String(readBuffer);
					printMsg("Receive Low Data:" + ss + "||");
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}		
		}		
	}
	
	// TextArea에 문자열 출력하기 위한 method
	private void printMsg(String name) {
		Platform.runLater(()->{
			textarea.appendText(name + "\n");
		});
	}
	
	private void connectPort(String portName) {
		try {
			portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
			printMsg(portName + "에 연결을 시도합니다!!");
			
			if(portIdentifier.isCurrentlyOwned()) {
				printMsg(portName + "가 현재 다른 프로그램에 의해 사용중입니다. ");				
			} else {
				// 포트를 열고 포트객체를 얻는다.
				// open()의 첫번째 인자 : Name of application making this call.
				// open()의 두번째 인자 : Time in milliseconds to block waiting for port open.
				commPort = portIdentifier.open(this.getClass().getName(), 5000);
				if( commPort instanceof SerialPort ) {
					serialPort = (SerialPort) commPort;
					serialPort.setSerialPortParams(921600, // 통신속도
							SerialPort.DATABITS_8, // 데이터 비트 
							SerialPort.STOPBITS_1, // Stop 비트 
							SerialPort.PARITY_NONE);  // Parity 비트
				    serialPort.addEventListener(new MyPortListener());
				    serialPort.notifyOnDataAvailable(true);
			        printMsg(portName + "에 이벤트 리스너가 등록되었습니다.");
					// Stream 생성
					bin = new BufferedInputStream(serialPort.getInputStream());
					out = serialPort.getOutputStream(); 
					printMsg("성공적으로 " + portName + "에 접속되었습니다.!!");
					// CAN 데이터 수신 여부 환경 읽기 및 설정
					String msg = ":G11A9\r";
					try {
						byte[] inputData = msg.getBytes();
						out.write(inputData);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					printMsg("Serial Port만 사용할 수 있습니다.!!");	
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {

		BorderPane root = new BorderPane();
		root.setPrefSize(700, 500);
		
		textarea = new TextArea();
		root.setCenter(textarea);
		
		connBtn = new Button("Serial 포트 접속");
		connBtn.setPrefSize(200, 50);
		connBtn.setPadding(new Insets(10));
		connBtn.setOnAction(e->{
			String portName = "COM11";
			// 포트 접속
			connectPort(portName);
		});				
		
		FlowPane flowPane = new FlowPane();
		flowPane.setPrefSize(700, 50);
		flowPane.setHgap(10);
		flowPane.getChildren().add(connBtn);
		
		root.setBottom(flowPane);
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("CAN DataFrame Receive Test");
		primaryStage.setOnCloseRequest(e->{
			System.exit(0);
		});
		primaryStage.show();
		
	}
	 
	public static void main(String[] args) {
		launch();
	}	
	
}