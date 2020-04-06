package javaStream;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

/*
 * 스트림(Stream)은 자바 8부터 추가된 컬렉션(배열 포함)의 저장 요소를 하나씩 참조해서 
 * 람다식(functional-style)으로 처리할 수 있도록 해주는 반복자입니다.
 * 
 * Stream은 Iterator와 비슷한 역할을 하는 반복자이지만, 
 * 람다식으로 요소 처리 코드를 제공하는 점과 내부 반복자를 사용하므로 
 * 병렬 처리가 쉽다는 점, 그리고 중간 처리와 최종 처리 작업을 수행하는 점에서 많은 차이를 두고 있습니다. 
 * 
 * 
 * 내부 반복자를 사용하므로 병렬 처리가 쉽습니다.
 * 외부 반복자(external iterator)란 개발자가 코드로 직접 컬렉션의 요소를 반복해서 가져오는 코드 패턴을 말합니다.
 * index를 이용하는 for문 그리고 Iterator를 이용하는 while 문은 모두 외부 반복자를 이용하는 것입니다.
 * 반면 내부 반복자(internal Iterator)는 컬렉션 내부에서 요소들을 반복시키고, 
 * 개발자는 요소당 처리해야 할 코드만 제공하는 코드 패턴을 말합니다.
 * 내부 반복자를 사용해서 얻는 이점은 컬렉션 내부에서 어떻게 요소를 반복시킬 것인가는 컬렉션에게 맡겨두고, 
 * 개발자는 요소 처리 코드에만 집중할 수 있다는 것입니다.
 * 
 */

public class Exam01_StreamBasic {

	private static List<String> myBuddy = 
			Arrays.asList("홍길동","강감찬","신사임당","호랑이");
	
	private static List<Exam01_Student> students = Arrays.asList(
				new Exam01_Student("홍길동", 80, 90),
				new Exam01_Student("강감찬", 50, 10),
				new Exam01_Student("신사임당", 60, 40),
				new Exam01_Student("호랑이", 100, 10)
			);
	
	// 자바 7 이전까지는 List<String> 컬렉션에서 요소를 순차적으로 처리하기 위해 
	// Iterator 반복자를 사용해왔습니다.
	public static void iteratorExam() {
		
		Iterator<String> iterator = myBuddy.iterator();
		
		while(iterator.hasNext()) {
			System.out.println(iterator.next());
		}
	}
	
	// 이 코드를 Stream을 사용해서 변경하면 다음과 같습니다.
	// 컬렉션(java.util.Collection)의 stream() 메소드로 스트림 객체를 얻고 나서 
	// stream.forEach(name -> System.out.println(name)); 메소드를 통해 
	// 컬렉션의 요소를 하나씩 콘솔에 출력합니다.
	// forEach() 메소드는 다음과 같이 Consumer 함수적 인터페이스 타입을 파라미터로 가지므로 
	// 컬렉션의 요소를 소비할 코드를 람다식으로 기술 할 수 있습니다.
	public static void streamExam() {
		Stream<String> stream = myBuddy.stream();
		stream.forEach(System.out::println);
	}

	// Stream이 제공하는 대부분의 요소 처리 메소드는 함수적 인터페이스 매개 타입을 가지기 때문에 
	// 람다식 또는 메소드 참조를 이용해서 요소 처리 내용을 파라미터로 전달할 수 있습니다.
	// 다음 예제는 컬렉션에 저장된 Student를 하나씩 가져와 학생 이름과 성적을 콘솔에 출력하도록 forEach() 
	// 메소드의 파라미터로 람다식을 주었습니다.	
	public static void studentStreamExam(Consumer<Exam01_Student> consumer) {
		Stream<Exam01_Student> stream = students.stream();
		stream.forEach(consumer);
	}

	// 내부 반복자는 요소들의 반복 순서를 변경하거나, 
	// 멀티 코어 CPU를 최대한 활용하기 위해 요소들을 분배시켜 병렬 작업을 할 수 있게 
	// 도와주기 때문에 하나씩 처리하는 순차적 외부 반복자보다는 효율적으로 요소를 반복시킬 수 있습니다.
	// 스트림을 이용하면 코드로 간결해지지만, 무엇보다도 요소의 병렬 처리가 컬렉션 내부에서 처리되므로 
	// 수행속도를 획기적으로 올릴 수 있습니다.
	// 병렬(parallel) 처리란 한 가지 작업을 서브 작업으로 나누고, 
	// 서브 작업들을 분리된 스레드에서 병렬적으로 처리하는 것을 말합니다.
	// 병렬 처리 스트림을 이용하면 런타임 시 하나의 작업을 서브 작업으로 자동으로 나누고, 
	// 서브 작업의 결과를 자동으로 결합해서 최종 결과물을 생성합니다.
	// 예제는 순차 처리 스트림과 병렬 처리 스트림을 이용할 경우 사용된 스레드의 이름이 무엇인지 출력하는 예제입니다.	
	public static void studentParallelStreamExam(Consumer<Exam01_Student> consumer) {
		Stream<Exam01_Student> stream = students.parallelStream();
		stream.forEach(consumer);
	}

	// 스트림은 중간 처리와 최종 처리를 할 수 있다.
	// 스트림은 컬렉션의 요소에 대해 중간 처리와 최종 처리를 수행할 수 있는데, 
	// 중간 처리에서는 매핑, 필터링, 정렬을 수행하고 
	// 최종 처리에서는 반복, 카운팅, 평균, 총합 등의 집계 처리를 수행합니다.
	// 예를 들어 학생 객체를 요소로 가지는 컬렉션이 있다고 가정해봅시다. 
	// 중간 처리에서는 학생의 점수를 뽑아내고, 최종 처리에서는 점수의 평균값을 산출합니다.
	// 예제는 List에 저장되어 있는 Student객체를 중간처리해서 math 필드값에 매핑하고, 
	// 최종 처리에서 math의 평균 값을 산출하는 예제입니다.	
	public static void studentMapAndReduceExam() {
		Stream<Exam01_Student> stream = students.stream();
		//double avg = stream.mapToInt(e->e.getMath()).average().getAsDouble();
		double avg = stream.mapToInt(Exam01_Student::getMath).average().getAsDouble();
		System.out.println("수학 평균값 : " + avg);
	}
	
	public static void main(String[] args) {

		iteratorExam();
		streamExam();
		studentStreamExam(e ->  
			System.out.println(e.getName() + " : " + Thread.currentThread().getName())		
		);
		studentParallelStreamExam(e -> 
			System.out.println(e.getName() + " : " + Thread.currentThread().getName())	
		);
		studentMapAndReduceExam();
	}
}

class Exam01_Student {
	private String name;
	private int eng;
	private int math;
	public Exam01_Student(String name, int eng, int math) {
		super();
		this.name = name;
		this.eng = eng;
		this.math = math;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getEng() {
		return eng;
	}
	public void setEng(int eng) {
		this.eng = eng;
	}
	public int getMath() {
		return math;
	}
	public void setMath(int math) {
		this.math = math;
	}
}