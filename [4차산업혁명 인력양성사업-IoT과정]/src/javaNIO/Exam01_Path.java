package javaNIO;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Exam01_Path {

	public static void main(String[] args) {

		Path path = Paths.get("asset/readme.txt");
		
		System.out.println("File명 : " + path.getFileName());
		System.out.println("상위폴더명 : " + path.getParent().getFileName());
		System.out.println("절대경로명 : " + path.toAbsolutePath());
	}
}