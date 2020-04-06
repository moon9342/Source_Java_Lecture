package javaLambda;

import java.util.function.Consumer;
import java.util.function.Function;

/* 
 * 디폴트(default) 및 정적(static) 메소드는 추상(abstract) 메소드가 아니기 때문에
 * 함수적 인터페이스에 선언되어도 여전히 함수적 인터페이스의 성질을 잃지 않습니다.
 * 
 * 참고) default method 및 static method 
 * 원래 interface안에는 추상 method만 존재할 수 있습니다. 하지만 default keyword로 
 * 지정된 method는 추상 method가 아닌 구현 method로 interface내에 존재할 수 있습니다. 해당 method는
 * 하위 class에서 overriding도 가능합니다. 또한 인터페이스내에는 static method도 존재할 수 있습니다.
 * 
 * 여기서 함수적 인터페이스 성질이란 하나의 추상 메소드를 가지고 있고 
 * 람다식으로 익명 구현 객체를 생성할 수 있는 것을 말합니다.
 *  
 * java.util.function 패키지의 함수적 인터페이스는 하나 이상의 디폴트 및 정적 메소드를 가지고 있습니다.
 * Consumer, Function, Operator 종류의 함수적 인터페이스는 
 * andThen()과 compose() 디폴트 메소드를 가지고 있습니다.
 * 주의할 점은 compose() method는  Consumer, Function, Operator 종류의 
 * 모든 함수적 interface가 가지고 있지는 않은 반면 andThen() method는 
 * 모든 함수적 interface가 가지고 있습니다.
 * 
 * andThen() 과 compose() 디폴트 메소드는 두 개의 함수적 인터페이스를 순차적으로 연결하고
 * 첫 번째 처리 결과를 두 번째 매개값으로 제공해서 최종 결과값을 얻을 때 사용합니다.
 * andThen() 과 compose() 의 차이점은 어떤 함수적 인터페이스를 먼저 처리하느냐 입니다.
 *   
 */

public class Exam10_LambdaUsingAPIDefaultMethod {

	public static void main(String[] args) {
		
		// Consumer 종류의 함수적 인터페이스는 처리 결과를 리턴하지 않기 때문에 
		// andThen() 디폴트 메소드는 함수적 인터페이스의 호출 순서만 정합니다.
		Consumer<Exam10_Student> consumer1 = (stu) -> {
			System.out.println("consumer1 : " + stu.getName());
		};
		Consumer<Exam10_Student> consumer2 = (stu) -> {
			System.out.println("consumer2 : " + stu.getMath());
		};	
		Consumer<Exam10_Student> consumer3 = consumer1.andThen(consumer2);
		consumer3.accept(new Exam10_Student("홍길동", 90, 50));
		
		// Function과 Operator 종류의 함수적 인터페이스는 먼저 실행한 함수적 인터페이스의 결과를 
		// 다음 함수적 인터페이스의 매개값으로 넘겨주고 최종 처리 결과를 리턴합니다.
		Function<Exam10_Student,Exam10_Department> function1 = (stu) -> stu.getDept();
		Function<Exam10_Department, String> function2 = (stu) -> stu.getDeptName();

		Function<Exam10_Student,String> function3 = function1.andThen(function2);
		
		String result = function3.apply(
				new Exam10_Student("신사임당", 10, 20, new Exam10_Department("컴퓨터", "CS001")));
		
		System.out.println("학생의 학과이름 : " + result);
	}

}

// Student VO
class Exam10_Student {
	
	private String name;
	private int math;
	private int eng;
	private Exam10_Department dept;
	
	public Exam10_Student(String name, int math, int eng) {
		super();
		this.name = name;
		this.math = math;
		this.eng = eng;
	}
	
	
	
	public Exam10_Student(String name, int math, int eng, Exam10_Department dept) {
		super();
		this.name = name;
		this.math = math;
		this.eng = eng;
		this.dept = dept;
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



	public Exam10_Department getDept() {
		return dept;
	}



	public void setDept(Exam10_Department dept) {
		this.dept = dept;
	}

		
}

class Exam10_Department {
	
	private String deptName;
	private String deptCode;
	
	public Exam10_Department(String deptName, String deptCode) {
		super();
		this.deptName = deptName;
		this.deptCode = deptCode;
	}
	
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
		
}