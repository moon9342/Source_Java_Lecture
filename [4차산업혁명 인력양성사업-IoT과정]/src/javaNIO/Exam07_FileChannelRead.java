package javaNIO;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Exam07_FileChannelRead {

	public static void main(String[] args) {
		
		Path path = Paths.get("asset/readme.txt");
		
		try {
			FileChannel fileChannel = 
					FileChannel.open(path, StandardOpenOption.READ);
			
			//ByteBuffer buffer = ByteBuffer.allocate((int)fileChannel.size());
			ByteBuffer buffer = ByteBuffer.allocate(100);
			Charset charset = Charset.forName("UTF-8");
			
			int bytes = 0;
			
			while(true) {
				bytes = fileChannel.read(buffer);
				if(bytes == -1) {
					break;
				}			
				buffer.flip();				
				String msg = charset.decode(buffer).toString();
				System.out.print(msg);
				buffer.clear();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}