package javaIO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Exam01_KeyboardInput {

	public static void main(String[] args) {

		System.out.println("키보드로 한줄을 입력합니다.!!");
		
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		
		try {
			String input = br.readLine();
			System.out.println("입력받은 데이터는 : " + input);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}