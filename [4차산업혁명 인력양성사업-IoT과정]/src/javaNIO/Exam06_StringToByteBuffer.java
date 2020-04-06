package javaNIO;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class Exam06_StringToByteBuffer {

	public static void main(String[] args) {

		Charset charset = Charset.forName("UTF-8");
		
		String msg = "이것은 소리없는 아우성!!";
		
		ByteBuffer buffer = charset.encode(msg);
		
		String newString = charset.decode(buffer).toString();
		
		System.out.println("디코딩된 스트링 : " + newString);

	}

}