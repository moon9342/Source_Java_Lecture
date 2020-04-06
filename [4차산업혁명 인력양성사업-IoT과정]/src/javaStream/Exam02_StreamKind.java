package javaStream;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
 * 자바 8부터 추가된 java.util.stream 패키지에는 스트림(stream) API 들이 포진하고 있습니다.
 * 패키지 내용을 보면 BaseStream 인터페이스를 부모로 해서 자식 인터페이스들이 상속 관계를 이루고 있습니다.
 * 하위 스트림인 Stream, IntStream, LongStream, DoubleStream이 직접적으로 이용되는 스트림인데, 
 * Stream은 객체 요소를 처리하는 스트림이고, IntStream, LongStream, DoubleStream 은 각각 
 * 기본 타입인 int, long, double 요소를 처리하는 스트림입니다.
 * 
 * 이 스트림 인터페이스의 구현 객체는 다양한 소스로부터 얻을 수 있습니다.
 * 주로 컬렉션과 배열에서 얻지만, int,long범위나 랜덤값, 파일,폴더와  같이 다양한 소스로부터 
 * 스트림 구현 객체를 얻을 수도 있습니다.
 */
public class Exam02_StreamKind {

	private static List<String> myBuddy = Arrays.asList("홍길동","강감찬","신사임당","호랑이");
	private static int intArr[] = { 10,20,30,40,50,60 };
	
	private static void fromList() {
		Stream<String> stream = myBuddy.stream();
		stream.forEach(System.out::println);
	}

	private static void fromArray() {
		IntStream stream = Arrays.stream(intArr);		
		System.out.println(stream.average().getAsDouble());
	}
	
	private static void fromIntRange() {
		IntStream stream = IntStream.rangeClosed(1, 100);
		System.out.println(stream.sum());
	}
	
	private static void fromFile() {
		Path path = Paths.get("asset/readme.txt");	
		System.out.println(path.toAbsolutePath());
		try {
			Stream<String> stream = Files.lines(path,Charset.forName("UTF-8"));
			stream.forEach(System.out::println);
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {

		fromList();
		fromArray();
		fromIntRange();
		fromFile();
	}

}