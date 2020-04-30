package javaArduino;

import java.io.InputStream;
import java.io.OutputStream;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

// library의 rxtxSerial.dll은 %JAVAHOME%bin에 위치시키고
// library의 RXTXcomm.jar는 library path에 등록

class Exam02_SerialListener implements SerialPortEventListener {

	InputStream in;
	OutputStream out;
	
	Exam02_SerialListener(InputStream in, OutputStream out) {
		this.in = in;
		this.out = out;
	}
	
	@Override
	public void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                int available = in.available();
                byte chunk[] = new byte[available];
                in.read(chunk, 0, available);

                System.out.print("받은 메시지 : " + new String(chunk));
            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }		
		
	}
	
}

public class Exam02_ArduinoSerialUsingEvent {
		
	public static void main(String[] args) {
 
		try {

	        CommPortIdentifier portIdentifier = 
	        		CommPortIdentifier.getPortIdentifier("COM14");
	        
	        if ( portIdentifier.isCurrentlyOwned() ) {
	        	System.out.println("현재 포트가 사용되고 있습니다.");
	        } else {
	            CommPort commPort = 
	            		portIdentifier.open("HELLO",2000);
	            
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
	                
	                
	                serialPort.addEventListener(new Exam02_SerialListener(in,out));
	                serialPort.notifyOnDataAvailable(true);

	            } else {
	            	System.out.println("Serial 포트만 사용가능합니다.");
	            }
	        }     
			
		} catch(Exception e) {
			System.out.println(e);
		}
		
	}
	
}
