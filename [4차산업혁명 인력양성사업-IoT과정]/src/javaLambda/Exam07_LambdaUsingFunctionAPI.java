package javaLambda;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToIntFunction;

/*
 * Function 함수적 인터페이스의 특징은 매개값과 리턴값이 있는 applyXXX() 메소드를 가지고 있습니다. 
 * 이 메소드들은 매개값을 리턴값으로 매핑하는 역할을 합니다. 
 * 매개 변수 타입과 리턴 타입에 따라서 여러종류의 Function 함수적 인터페이스가 있습니다.
 * 
 * Function<T, R> 인터페이스를 타겟 타입으로 하는 람다식은 다음과 같이 작성할 수 있습니다. 
 * apply() 메소드는 매개값으로 T 객체 하나를 가지므로 람다식도 한 개의 매개 변수를 사용합니다. 
 * 그리고 apply()메소드의 리턴 타입이 R이므로 람다식 중괄호 {}의 리턴값은 R 객체가 됩니다.
 * 
 * Function<Student, String> function = t -> t.getName();
 * 
 */
public class Exam07_LambdaUsingFunctionAPI {

	private static List<Exam07_Student> students = Arrays.asList(
			new Exam07_Student("홍길동",100,20),
			new Exam07_Student("이순신",80,50),
			new Exam07_Student("강감찬",70,90)
	);
	
	public static void printName(Function<Exam07_Student,String> function) {
		
		for(Exam07_Student s : students) {
			System.out.println(function.apply(s));	
		}		
	}
	
	public static double getAvg(ToIntFunction<Exam07_Student> function) {
		int sum = 0;
		for(Exam07_Student s : students) {
			sum += function.applyAsInt(s); 	
		}			
		return sum / students.size();
	}
	
	public static void main(String[] args) {
		System.out.println("학생이름 출력");
		printName(t -> t.getName());
		
		System.out.print("영어평균 출력 : ");
		System.out.println(getAvg(t -> t.getEng()));

		System.out.print("수학평균 출력 : ");
		System.out.println(getAvg(t -> t.getMath()));
		
	}
}

class Exam07_Student {
	
	private String name;
	private int math;
	private int eng;
	
	public Exam07_Student(String name, int math, int eng) {
		super();
		this.name = name;
		this.math = math;
		this.eng = eng;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMath() {
		return math;
	}

	public void setMath(int math) {
		this.math = math;
	}

	public int getEng() {
		return eng;
	}

	public void setEng(int eng) {
		this.eng = eng;
	}
}