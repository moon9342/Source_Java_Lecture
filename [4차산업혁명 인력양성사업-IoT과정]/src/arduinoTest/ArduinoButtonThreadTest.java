package arduinoTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

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

// library의 rxtxSerial.dll은 %JAVAHOME%bin에 위치시키고
// library의 RXTXcomm.jar는 library path에 등록
public class ArduinoButtonThreadTest extends Application {

	TextArea textarea;
	Button btn;
	
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

		btn = new Button("버튼 클릭!!");
		btn.setPrefSize(250, 50);
		btn.setOnAction((e) -> {
			printMessage("[아두이노로부터 신호를 받습니다.]");
		});
		
		FlowPane flowpane = new FlowPane();
		flowpane.setPrefSize(700, 50);
		flowpane.setHgap(10);
		flowpane.getChildren().add(btn);
		
		root.setBottom(flowpane);
		
		
		// Scene 객체를 생성(BorderPane을 이용)
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("예제용 JavaFX");
		primaryStage.setOnCloseRequest(e->{
			
		});
		primaryStage.show();
				
        CommPortIdentifier portIdentifier = 
        		CommPortIdentifier.getPortIdentifier("COM14");
        
        if ( portIdentifier.isCurrentlyOwned() ) {
        	printMessage("현재 포트가 사용되고 있습니다.");
        } else {
            CommPort commPort = 
            		portIdentifier.open(this.getClass().getName(),2000);
            
            if ( commPort instanceof SerialPort ) {
                //포트 설정(통신속도 설정. 기본 9600으로 사용)
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(9600,
                		SerialPort.DATABITS_8,
                		SerialPort.STOPBITS_1,
                		SerialPort.PARITY_NONE);
                
                //Input,OutputStream 버퍼 생성 후 오픈
                InputStream in = serialPort.getInputStream();
                OutputStream out = serialPort.getOutputStream();
                
                 //읽기, 쓰기 쓰레드 작동
                (new Thread(new SerialReader(in))).start();
                (new Thread(new SerialWriter(out))).start();

            } else {
            	printMessage("Serial 포트만 사용가능합니다.");
            }
        }     
		
	}

	public static void main(String[] args) {
		launch();
	}

}

class SerialReader implements Runnable 
{
    InputStream in;
    
    public SerialReader ( InputStream in )
    {
        this.in = in;
    }
    
    public void run ()
    {
        byte[] buffer = new byte[1024];
        int len = -1;
        try
        {
            while ( ( len = this.in.read(buffer)) > -1 )
            {
                System.out.print(new String(buffer,0,len));
            }
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }            
    }
}

class SerialWriter implements Runnable 
{
    OutputStream out;
    
    public SerialWriter ( OutputStream out )
    {
        this.out = out;
    }
    
    public void run ()
    {
        try
        {
            int c = 0;
            while ( ( c = System.in.read()) > -1 )
            {
                this.out.write(c);
            }                
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }            
    }
}
