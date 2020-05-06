package javaCan;

import java.io.IOException;
import java.io.OutputStream;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class Exam01_DataFrameSender extends Application{

	TextArea textarea;    // 메시지창 
	Button connBtn, sendBtn;    // 포트 연결 버튼, Data Frame 보내기 버튼
	
	private CommPortIdentifier portIdentifier;
	private CommPort commPort;
	private SerialPort serialPort;
	
	private OutputStream out;
	
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
					serialPort.setSerialPortParams(9600, // 통신속도
							SerialPort.DATABITS_8, // 데이터 비트 
							SerialPort.STOPBITS_1, // Stop 비트 
							SerialPort.PARITY_NONE);  // Parity 비트
					// OutputStream 생성
					out = serialPort.getOutputStream(); 
					printMSG("성공적으로 " + portName + "에 접속되었습니다.!!");
				} else {
					printMSG("Serial Port만 사용할 수 있습니다.!!");	
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void sendDataFrame(String msg) {
		// String msg = "W" + "28" + "10003B01" + "0000000000005011";
		// 데이터프레임 전송
		// CAN 네트워크상에 특정 CAN Message를 보내고자 할 때
		// 시작문자 => 1문자 => ":" 이용
		// 명령코드 => 1문자 => "W" (전송)
		// 송신데이터 특성코드 => 2문자
		//                   5번째 bit가 0이면 CAN2.0A, 1이면 CAN2.0B를 나타냄
		//                   4번째 bit가 0이면 Data Frame을 지칭, 1이면 Remote Frame
		//                   3번째~0번째는 송신데이터의 길이(0부터 8까지의 값을 가지게 된다.)
		//                   예) 28 => 00101000 => CAN2.0B이면서 데이터 길이가 8인
		//                             Data Frame을 의미.
		// CAN 송신 ID => 4문자(11bit사용시) 혹은 8문자(29bit사용시)
		//               16진수로 표현하고 4bit가 1문자로 표현되기때문에 29bit를 표현하기 위해서는
		//               8개의 16진수가 필요하고 문자로 표현.
		//               예) 041C0800 => 0번부터 28번 비트중에
		//                            => 26번, 20번, 19번, 18번, 11번 bit가 check
		// CAN 송신 Data => 위에서 정의한 송신데이터 특성 코드 중 송신데이터의 길이에 따라
		//              => 0 ~ 16문자를 표현
		//              => 예) 데이터 프레임 개수(송신데이터의 길이)가 8이면 ASCII형식으로는
		//                     8개의 문자로 Hex표현이면 16개의 문자로 표현
		// Checksum => 2문자(수식에 의한 Checksum계산)                       
		// 끝 문자 => 1문자 ( "\r" 사용 )
		
		msg = msg.toUpperCase();
		char c[] = msg.toCharArray();
		int checksumData = 0;
		for (char cc : c) {
			checksumData += cc;
		}
		checksumData = (checksumData & 0xFF);
		String sendMsg = ":" + msg + 
				Integer.toHexString(checksumData).toUpperCase() + "\r";
		
		printMSG("생성된 전송 메시지 : " + sendMsg);
		
		// 전송할 byte배열 생성
		byte[] inputData = sendMsg.getBytes();
		try {
			out.write(inputData);
			//out.flush();
			printMSG("성공적으로 전송되었습니다.!!");
		} catch (IOException e1) {
			e1.printStackTrace();
		}					
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {

		BorderPane root = new BorderPane();
		root.setPrefSize(700, 500);
		
		textarea = new TextArea();
		root.setCenter(textarea);
			
		connBtn = new Button("COM 포트 접속");
		connBtn.setPrefSize(200, 50);
		connBtn.setPadding(new Insets(10));
		connBtn.setOnAction(e->{
			String portName = "COM16";
			// 포트 접속
			connectPort(portName);
		});
		
		sendBtn = new Button("Data Frame 전송");
		sendBtn.setPrefSize(200, 50);
		sendBtn.setPadding(new Insets(10));
		sendBtn.setOnAction(e->{
			// Data Frame 전송
			String msg = "W" + "28" + "10003B01" + "0000000000005011";
			sendDataFrame(msg);			
		});				
		
		FlowPane flowPane = new FlowPane();
		flowPane.setPrefSize(700, 50);
		flowPane.setHgap(10);
		flowPane.getChildren().add(connBtn);
		flowPane.getChildren().add(sendBtn);
		
		root.setBottom(flowPane);
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("CAN DataFrame Send Test");
		primaryStage.setOnCloseRequest(e->{
			System.exit(0);
		});
		primaryStage.show();
		
	}
	 
	public static void main(String[] args) {
		launch();
	}		
}