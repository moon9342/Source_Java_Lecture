package javaNIO;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.text.NumberFormat;

public class Exam02_FileSystem {

	public static void main(String[] args) throws IOException {

		FileSystem fs = FileSystems.getDefault();
		
		NumberFormat nFormat = NumberFormat.getNumberInstance();		
		
		for( FileStore fileStore : fs.getFileStores()) {
			System.out.println("파일 시스템 : " + fileStore.type());
			System.out.println("전체공간 : " + nFormat.format(fileStore.getTotalSpace()) + "bytes");
			System.out.println("가용공간 : " + nFormat.format(fileStore.getUsableSpace()) + "bytes");
		}
	}
}