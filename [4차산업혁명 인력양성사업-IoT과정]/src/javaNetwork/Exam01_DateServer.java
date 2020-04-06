package javaNetwork;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.util.Date;

public class Exam01_DateServer {

	public static void main(String[] args) {

		try {
			ServerSocket serverSocket = new ServerSocket(5555);
			System.out.println("Date Server 기동 - 5555");
			Socket socket = serverSocket.accept();
			
			PrintWriter pr = new PrintWriter(socket.getOutputStream());
			
			DateFormat dateFormat = DateFormat.getInstance();
			pr.println(dateFormat.format(new Date()));
			pr.flush();
			pr.close();
			socket.close();
			serverSocket.close();
			System.out.println("Date Server 종료");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}