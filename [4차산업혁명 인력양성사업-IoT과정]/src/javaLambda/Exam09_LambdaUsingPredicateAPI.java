package javaLambda;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

/*
 * Predicate 함수적 인터페이스는 매개 변수와 boolean 리턴값이 있는 
 * testXXX() 메소드를 가지고 있습니다. 
 * 이 메소드들은 매개값을 조사해서 true 또는 false를 리턴하는 역할을 합니다
 * 
 * 다음 예제는 List에 저장된 남자 또는 여자 학생들의 평균 점수를 출력하는 예제입니다.
 * 
 */
public class Exam09_LambdaUsingPredicateAPI {

	private static List<Exam09_Student> students = Arrays.asList(
			new Exam09_Student("홍길동",30,20,"남자"),
			new Exam09_Student("강감찬",50,90,"남자"),
			new Exam09_Student("신사임당",20,100,"여자"),
			new Exam09_Student("이순신",10,10,"남자"),
			new Exam09_Student("유관순",100,40,"여자")
			);
	
	public static double avg(Predicate<Exam09_Student> predicate, ToIntFunction<Exam09_Student> function) {
		
		int sum = 0;
		int count = 0;
		for(Exam09_Student student : students) {
			
			if(predicate.test(student)) {
				count++;
				sum += function.applyAsInt(student);
			}
		}
		return sum / count;
	}
	public static void main(String[] args) {

		double result = avg(t -> t.getGender().equals("남자"), u -> u.getEng());
		System.out.println("남자들의 영어성적 평균 : " + result);
		result = avg(t -> t.getGender().equals("여자"), u -> u.getMath());
		System.out.println("여자들의 수학성적 평균 : " + result);
	}

}

class Exam09_Student {
	
	private String name;
	private int eng;
	private int math;
	private String gender;
	
	public Exam09_Student(String name, int eng, int math, String gender) {
		super();
		this.name = name;
		this.eng = eng;
		this.math = math;
		this.gender = gender;
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
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	
}