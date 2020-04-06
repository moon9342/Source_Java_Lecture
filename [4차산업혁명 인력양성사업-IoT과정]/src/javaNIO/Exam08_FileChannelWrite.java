package javaNIO;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Exam08_FileChannelWrite {

	public static void main(String[] args) {

		Path path = Paths.get("asset/newFle.txt");
		
		try {
			FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.CREATE,
					StandardOpenOption.WRITE);
			
			String msg = "이것은 소리없는 아우성!!";
			Charset charset = Charset.forName("UTF-8");
			
			ByteBuffer buffer = charset.encode(msg);
			
			fileChannel.write(buffer);
			
			fileChannel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}