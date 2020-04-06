package javaNIO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Exam03_Files {

	public static void main(String[] args) throws IOException {

		Path path = Paths.get("asset/readme.txt");
		
		System.out.println("디렉토리 여부 : " + Files.isDirectory(path));
		System.out.println("파일 여부 : " + Files.isRegularFile(path));
		System.out.println("수정시간 : " + Files.getLastModifiedTime(path));
		System.out.println("파일크기 : " + Files.size(path));
		System.out.println("소유자 : " + Files.getOwner(path));
		System.out.println("숨김파일여부 : " + Files.isHidden(path));
		System.out.println("읽기여부 : " + Files.isReadable(path));
		System.out.println("쓰기여부 : " + Files.isWritable(path));
		
		Path path1 = Paths.get("asset/myNewFolder");
		Path path2 = Paths.get("asset/myNewFile.txt");
		
		if(Files.notExists(path1)) {
			Files.createDirectory(path1);
		}
		
		if(Files.notExists(path2)) {
			Files.createFile(path2);
		}
		

	}

}