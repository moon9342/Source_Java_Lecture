package javaStream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/*
 * 리덕션(Reduction) 
 * ==> 대량의 데이터를 가공해서 축소하는 것. 
 *     예) 데이터의 합계, 평균값, 카운팅, 최대값, 최소값 등.
 *     
 * 그러나 컬렉션의 요소를 리덕션의 결과물로 바로 집계할 수 없을 경우에는 
 * 집계하기 좋도록 필터링, 매핑, 정렬, 그룹핑 등의 중간 처리가 필요.
 * 
 * 중간 처리와 최종 처리
 * 스트림은 데이터의 필터링, 매핑, 정렬, 그룹핑 등의 중간 처리와 
 * 합계, 평균, 카운팅, 최대값, 최소값 등의 최종 처리를 파이프라인(pipelines)으로 해결.
 *  
 * 파이프 라인
 * ==> 여러 개의 스트림이 연결되어 있는 구조.
 *  
 * 파이프라인에서 최종 처리를 제외하고는 모두 중간 처리 스트림.
 * 
 * 중간 스트림이 생성될 때 요소들이 바로 중간 처리(필터링, 매핑, 정렬)되는 것이 아니라 
 * 최종 처리가 시작되기 전까지 중간 처리는 지연(lazy)된다.
 * 
 * Stream 인터페이스에는 
 * 필터링, 매핑, 정렬 등의 많은 중간 처리 메소드가 있는데 
 * 이 메소드들은 중간 처리된 스트림을 리턴하고 이 스트림에서 다시 중간 처리 메소드를 호출해서 
 * 파이프라인을 형성.
 * 
 * 
 * 예를 들어 직원 컬렉션에서 "IT" 직종의 남자만 필터링하는 중간 스트림을 연결하고, 
 * 다시 남자직원만 필터링하는 중간 스트림을 연결한 후, 
 * 최종적으로 평균 연봉를 집계하는 처리를 해 봅시다.
 * 
 */
public class Exam03_StreamPipeline {

	private static List<Exam03_Employee> employees = Arrays.asList(
			new Exam03_Employee("홍길동", "IT", 5000, 25,"male"),
			new Exam03_Employee("장동건", "SALES", 7000, 30,"male"),
			new Exam03_Employee("김연아", "IT", 10000,28,"female"),
			new Exam03_Employee("김하늘", "SALES",3000, 35,"female"),
			new Exam03_Employee("원빈", "IT", 6000, 50,"male")			
		);
	
	private static void pipeExam() {
		Stream<Exam03_Employee> stream = employees.stream();
		double result = stream.filter(e->e.getDept().equals("IT"))
				    		  .filter(e->e.getGender().equals("male"))
							  .mapToInt(e->e.getSalary())
							  .average().getAsDouble();
		System.out.println("IT부서의 남자 평균 연봉 : " + result);
	}
	
	public static void main(String[] args) {
		pipeExam();	
	}

}

class Exam03_Employee {
	private String name;
	private String dept;
	private int salary;
	private int age;
	private String gender;
	public Exam03_Employee(String name, String dept, int salary, int age, String gender) {
		super();
		this.name = name;
		this.dept = dept;
		this.salary = salary;
		this.age = age;
		this.gender = gender;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public int getSalary() {
		return salary;
	}
	public void setSalary(int salary) {
		this.salary = salary;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	
}