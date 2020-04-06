package javaNetwork;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Exam02_EchoServer {

	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		Socket socket = null;
		BufferedReader br = null;
		PrintWriter pr = null;
		try {
			serverSocket = new ServerSocket(5555);
			System.out.println("클라이언트 접속 대기");
			socket = serverSocket.accept();
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pr = new PrintWriter(socket.getOutputStream());
			String msg = null;
			while(true) {
				msg = br.readLine();
				if( (msg==null) || (msg.equals("/exit")) ) {
					break;
				}
				pr.println(msg);
				pr.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if( br != null ) br.close();
				if( pr != null ) pr.close();
				if( socket != null ) socket.close();
				if( serverSocket != null ) serverSocket.close();
				System.out.println("Echo Server 종료");
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
	}

}