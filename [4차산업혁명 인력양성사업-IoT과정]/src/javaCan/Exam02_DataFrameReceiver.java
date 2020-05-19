package javaCan;

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

public class Exam02_DataFrameReceiver extends Application {

	TextArea textarea;
	// 포트접속버튼, 환경쓰기버튼, 데이터수신가능버튼, 데이터수신불가능버튼
	Button connBtn, envBtn, revEnableBtn, revDisableBtn;
		
	private CommPortIdentifier portIdentifier;
	private CommPort commPort;
	private SerialPort serialPort;
	
	private BufferedInputStream bin;
	private OutputStream out;
	
	
	class MyPortListener implements SerialPortEventListener {
		@Override
		public void serialEvent(SerialPortEvent event) {
			if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
				byte[] readBuffer = new byte[128];
				try { 
					while (bin.available() > 0) {
						bin.read(readBuffer);
					}
					String revData = new String(readBuffer);
					printMSG("Receive Low Data:" + revData);	
					if(revData.trim().equals(":G01A8")) {
						printMSG("지금부터 데이터 수신 가능합니다.");
					}
					if(revData.trim().equals(":G00A7")) {
						printMSG("지금부터 데이터 수신이 불가능합니다.");
					}	
					// CAN 수신 데이터 읽기 
					// U2800000001111100000000000044
					// 시작문자 => :
					// 명령코드 => U
					// 수신데이터 특성코드 : 0010 1000 => 28
					// CAN 수신 ID : 00000001
					// CAN 수신 데이터 : 1111000000000000
					// checksum : 44
					if(revData.charAt(1) == 'U') {
						printMSG("데이터를 수신했습니다.");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}				
			}		
		}		
	}
	
	// TextArea에 문자열 출력하기 위한 method
	private void printMSG(String msg) {
		Platform.runLater(()->{
			textarea.appendText(msg + "\n");
		});
	}
	
	private void connectPort(String portName) {
		try {
			portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
			printMSG(portName + "에 연결을 시도합니다!!");
			
			if(portIdentifier.isCurrentlyOwned()) {
				printMSG(portName + "가 현재 다른 프로그램에 의해 사용중입니다. ");				
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
			        printMSG(portName + "에 이벤트 리스너가 등록되었습니다.");
					// Stream 생성
					bin = new BufferedInputStream(serialPort.getInputStream());
					out = serialPort.getOutputStream(); 
					printMSG("성공적으로 " + portName + "에 접속되었습니다.!!");
				    serialPort.addEventListener(new MyPortListener());
				    serialPort.notifyOnDataAvailable(true);
				} else {
					printMSG("Serial Port만 사용할 수 있습니다.!!");	
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {

		BorderPane root = new BorderPane();
		root.setPrefSize(850, 500);
		
		textarea = new TextArea();
		root.setCenter(textarea);
		
		connBtn = new Button("Serial 포트 접속");
		connBtn.setPrefSize(200, 50);
		connBtn.setPadding(new Insets(10));
		connBtn.setOnAction(e->{
			String portName = "COM16";
			connectPort(portName);
		});				
		
		revEnableBtn = new Button("데이터수신가능");
		revEnableBtn.setPrefSize(200, 50);
		revEnableBtn.setPadding(new Insets(10));
		revEnableBtn.setOnAction(e->{
			// CAN 데이터 수신 여부 설정
			// 시작문자 => 1문자 => ":" 이용
			// 명령코드 => 1문자 => "G"
			// 수신 여부 명령 코드 => 2문자
			//                   00 : 현재 CANPro의 CAN 데이터 수신 여부 환경을 읽어온다.
			//                   10 : CANPro의 CAN 데이터 수신 동작을 중지한다.
			//                   11 : CANPro의 CAN 데이터 수신 동작을 시작한다.
			// Check Sum => 2문자
			String str = "G11";
			String tmpStr = str.toUpperCase();
			char c[] = tmpStr.toCharArray();
			int checksumData = 0;
			for (char cc : c) {
				checksumData += cc;
			}
			checksumData = (checksumData & 0xFF);
			String checksumHexString = Integer.toHexString(checksumData).toUpperCase();
			//printMSG("checksum계산값 : " + checksumHexString);
			
			// 끝문자 => 1문자 => "\r" 이용			
			// String msg = ":G11A9\r";
			String msg = ":" + str + checksumHexString + "\r";
			
			try {
				byte[] inputData = msg.getBytes();
				out.write(inputData);
				// 정상응답이 올경우
				// ":G01A8"   => 00의 의미는 현재 CAN 데이터 수신 동작이 중지.
				//            => 01의 의미는 현재 CAN 데이터 수신 동작이 시작.
				//            => A8의 의미는 G01에 대한 checksum         
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});				
		
		revDisableBtn = new Button("데이터수신중지");
		revDisableBtn.setPrefSize(200, 50);
		revDisableBtn.setPadding(new Insets(10));
		revDisableBtn.setOnAction(e->{
			// CAN 데이터 수신 여부 설정
			// 시작문자 => 1문자 => ":" 이용
			// 명령코드 => 1문자 => "G"
			// 수신 여부 명령 코드 => 2문자
			//                   00 : 현재 CANPro의 CAN 데이터 수신 여부 환경을 읽어온다.
			//                   10 : CANPro의 CAN 데이터 수신 동작을 중지한다.
			//                   11 : CANPro의 CAN 데이터 수신 동작을 시작한다.
			// Check Sum => 2문자
			String str = "G10";
			String tmpStr = str.toUpperCase();
			char c[] = tmpStr.toCharArray();
			int checksumData = 0;
			for (char cc : c) {
				checksumData += cc;
			}
			checksumData = (checksumData & 0xFF);
			String checksumHexString = Integer.toHexString(checksumData).toUpperCase();
			//printMSG("checksum계산값 : " + checksumHexString);
			
			// 끝문자 => 1문자 => "\r" 이용			
			// String msg = ":G10A9\r";
			String msg = ":" + str + checksumHexString + "\r";
			
			try {
				byte[] inputData = msg.getBytes();
				out.write(inputData);
				// 정상응답이 올경우
				// ":G00A7"   => 00의 의미는 현재 CAN 데이터 수신 동작이 중지.
				//            => 01의 의미는 현재 CAN 데이터 수신 동작이 시작.
				//            => A7의 의미는 G00에 대한 checksum         
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});				
		
		envBtn = new Button("환경설정");
		envBtn.setPrefSize(200, 50);
		envBtn.setPadding(new Insets(10));
		envBtn.setOnAction(e->{
			// CAN 데이터 수신 여부 설정
			// 시작문자 => 1문자 => ":" 이용
			// 명령코드 => 1문자 => "Z"
			// 자세한 설정내용은 CANPro User Manual 참조
			// CAN bit time에 대한 내용은 User Manual 3page
			// 250k => 0x0F, 0x34로 지정되어 있음.
			// https://www.mathsisfun.com/binary-decimal-hexadecimal-converter.html
			// 해당사이트를 이용하면 4bit의 값을 hexa로 표현 가능.
			// String str = "Z 1C 0F34 00000001 00000001";
			String str = "Z1C0F340000000100000001";
			String tmpStr = str.toUpperCase();
			char c[] = tmpStr.toCharArray();
			int checksumData = 0;
			for (char cc : c) {
				checksumData += cc;
			}
			checksumData = (checksumData & 0xFF);
			String checksumHexString = Integer.toHexString(checksumData).toUpperCase();
			//printMSG("checksum계산값 : " + checksumHexString);
			
			// 끝문자 => 1문자 => "\r" 이용			
			String msg = ":" + str + checksumHexString + "\r";
			
			try {
				byte[] inputData = msg.getBytes();
				out.write(inputData);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});				
		
		FlowPane flowPane = new FlowPane();
		flowPane.setPrefSize(850, 50);
		flowPane.setHgap(10);
		flowPane.getChildren().add(connBtn);
		flowPane.getChildren().add(revEnableBtn);
		flowPane.getChildren().add(revDisableBtn);
		flowPane.getChildren().add(envBtn);
		
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