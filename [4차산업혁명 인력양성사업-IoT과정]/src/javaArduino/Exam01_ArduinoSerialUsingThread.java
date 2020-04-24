package javaArduino;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

// library의 rxtxSerial.dll은 %JAVAHOME%bin에 위치시키고
// library의 RXTXcomm.jar는 library path에 등록
public class Exam01_ArduinoSerialUsingThread {

						
	public static void main(String[] args) {
        CommPortIdentifier portIdentifier = null;		
		try {
			portIdentifier = CommPortIdentifier.getPortIdentifier("COM14");
			
	        if ( portIdentifier.isCurrentlyOwned() ) {
	        	System.out.println("현재 포트가 사용되고 있습니다.");
	        } else {
//	            CommPort commPort = 
//	            		portIdentifier.open(this.getClass().getName(),2000);

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
		            OutputStream out = serialPort.getOutputStream();
		                
		            //읽기, 쓰기 쓰레드 작동
		            (new Thread(new SerialReader(in))).start();
		            (new Thread(new SerialWriter(out))).start();
	                                
	            } else {
	            	System.out.println("Serial 포트만 사용가능합니다.");
	            }
	        }     
			
		} catch(Exception e) {
			System.out.println(e);
		}
	}

}

class SerialReader implements Runnable {
    InputStream in;
    
    public SerialReader(InputStream in) {
        this.in = in;
    }
    
    public void run() {
        byte[] buffer = new byte[1024];
        int len = -1;
        try {
            while ( ( len = this.in.read(buffer)) > -1 ) {
                System.out.print(new String(buffer,0,len));
            }
        } catch ( IOException e ) {
            e.printStackTrace();
        }            
    }
}

class SerialWriter implements Runnable {
    OutputStream out;
    
    public SerialWriter(OutputStream out) {
        this.out = out;
    }
    
    public void run() {
        try {
            int c = 0;
            while ( ( c = System.in.read()) > -1 ) {
                this.out.write(c);
            }                
        } catch ( IOException e ) {
            e.printStackTrace();
        }            
    }
}
