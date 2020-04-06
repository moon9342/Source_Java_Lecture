package javaStream;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Exam04_StreamMethod {

	private static List<String> names = Arrays.asList("홍길동","장동건","김연아","김하늘","홍길동");
	
	private static List<Exam04_Employee> employees = Arrays.asList(
			new Exam04_Employee("홍길동", "IT", 5000, 25,"male"),
			new Exam04_Employee("장동건", "SALES", 7000, 30,"male"),
			new Exam04_Employee("김연아", "IT", 10000,28,"female"),
			new Exam04_Employee("김하늘", "SALES",3000, 35,"female")
			//new Exam04_Employee("홍길동", "IT", 5000, 25,"male")			
		);
	private static List<String> flatNames = Arrays.asList("홍길동 IT 5000 25 male",
			                               "장동건 SALES 7000 30 male",
			                               "김연아 IT 10000 28 female",
			                               "김하늘 SALES 3000 35 female"
			                               //"홍길동 IT 5000 25 male"
			                               );

	// 필터링( distinct(), filter() )
	// 필터링은 중간 처리 기능으로 요소를 걸러내는 역할을 한다. 
	// 필터링 메소드인 distinct()와 filter() 메소드는 모든 스트림이 가지고 있는 공통 메소드.
	
	// distinct() 메소드는 중복을 제거하는 데, 
	// Object.equals(Object)가 true 이면 동일한 객체로 판단하고 중복을 제거.
	// IntStream, LongStream, DoubleStream은 동일값일 경우 중복을 제거합니다.
	// filter() 메소드는 파라미터로 주어진 Predicate가 true를 리턴하는 요소만 필터링.	
	private static void distinctNames() {
		System.out.println("==== distinctNames =====");
		names.stream().distinct().forEach(System.out::println);
		
		System.out.println();
		
		employees.stream()
		         .distinct()
		         .forEach(e -> System.out.println(e.getName()));
		System.out.println();
	}

	private static void filterNames() {
		System.out.println("==== filterNames =====");
		employees.stream()
		         .filter(e->e.getDept().equals("IT"))
		         .forEach(e -> System.out.println(e.getName()));
		System.out.println();
	}
	
	// 매핑(mapping)은 중간 처리 기능으로 스트림의 요소를 다른 요소로 대체하는 작업.

	// 스트림에서 제공하는 매핑 메소드는 
	// flatXXX(), mapXXX(), asDoubleStream, asLongStream, boxed가 있다.
	
	// flatMapXXX() 메소드는 요소를 대체하는 복수 개의 요소들로 구성된 새로운 스트림을 리턴.
	// mapXXX() 메소드는 요소를 대체하는 요소로 구성된 새로운 스트림을 리턴.
	// asDoubleStream() 메소드는 IntStream의 int 요소 또는 LongStream의 long 요소를 
	// double 요소로 타입 변환해서 DoubleStream을 생성한 후 리턴.	
	// asLongStream() 메소드는 IntStream의 int 요소를 long 요소로 타입 변환해서 
	// LongStream을 생성한 후 리턴. 	
	// boxed() 메소드는 int, long, double 요소를 Integer, Long, Double 요소로 박싱해서 
	// Stream을 생성한 후 리턴.	
	private static void flatNames() {
		System.out.println("==== flatNames =====");
		flatNames.stream()
		         .flatMap(data -> Arrays.stream(data.split(" ")))
		         .forEach(System.out::println);
		System.out.println();         
	}
	
	private static void mapNames() {
		System.out.println("==== mapNames =====");
		int maxAge = employees.stream()
		                      .mapToInt(e->e.getAge())
		                      .max().getAsInt();
		System.out.println("최고 연령자의 나이 : " + maxAge);
		System.out.println();  
	}
	
	// 정렬(sorted())
	// 스트림은 요소가 최종 처리되기 전에 중간 단계에서 요소를 정렬해서 최종 처리 순서를 변경할 수 있다.
	// 객체 요소일 경우에는 클래스가 Comparable을 구현하지 않으면 
	// sorted() 메소드를 호출했을 때 ClassCastException이 발생하기 때문에 
	// Comparable을 구현한 요소에서만 sorted() 메소드를 호출해야 한다. 	
	private static void sortSalayPrintNames() {
		System.out.println("==== sortSalayPrintNames =====");
		employees.stream()
		         .sorted( Comparator.reverseOrder() )       
		         .forEach(e -> {
		        	 System.out.println(e.getName() + ", " + e.getSalary());
		         });
		System.out.println(); 
		         		
	}

	// 루핑(looping)은 요소 전체를 반복하는 것.
	// 루핑하는 메소드 : peek(), forEach() 
	// 이 두 메소드는 루핑한다는 기능에서는 동일하지만, 동작 방식은 다르다.	
	// peek() 는 중간 처리 메소드이고, forEach()는 최종 처리 메소드 입니다.
	
	// peek() 메소드는 중간 처리 단계에서 전체 요소를 루핑하면서 추가적인 작업을 하기 위해 사용. 	
	// 최종 처리 메소드가 실행되지 않으면 지연되기 때문에 반드시 최종 처리 메소드가 호출되어야 동작한다.
	
	// 예를 들어 필터링 후 어떤 요소만 남았는지 확인하기 위해 다음과 같이 peek()를 마지막에 호출할 경우, 
	// 스트림은 동작하지 않는다.
	// 하지만 forEach() 는 최종 처리 메소드이기 때문에 파이프라인 마지막에 루핑하면서 요소를 하나씩 처리한다. 
	
	// forEach()는 요소를 소비하는 최종 처리 메소드이므로 이후에 sum()과 같은 다른 최종 메소드를 
	// 호출할 수 없다.	
	private static void peekMethod() {
		System.out.println("==== peekMethod =====");
		double avg = employees.stream()
		         			  .filter(e-> (e.getAge() > 30) ) 
		         			  .peek(e -> {
		         				  System.out.println(e.getName());
		         			  })
		         			  .mapToInt(e->e.getSalary())
		         			  .average().getAsDouble();
		System.out.println(avg);
		System.out.println(); 
	}

	// 스트림 클래스는 최종 처리 단계에서 요소들이 특정 조건에 만족하는지 조사할 수 있도록 
	// 세 가지 매칭 메소드를 제공.
	// allMatch() 메소드는 모든 요소들이 파라미터로 주어진 Predicate의 조건을 만족하는지 조사. 
	// anyMatch() 메소드는 최소한 한 개의 요소가 파라미터로 주어진 Predicate의 조건을 만족하는지 조사.  
	// noneMatch()는 모든 요소들이 파라미터로 주어진 Predicate의 조건을 만족하지 않는지 조사.	
	private static void matchMethod() {
		System.out.println("==== matchMethod =====");
		boolean result = employees.stream()
		         			  .filter(e-> (e.getAge() > 30) )
		         			  .allMatch(e -> e.getAge() > 34);
		         			  
		System.out.println(result);
		System.out.println(); 
	}

	// 집계(Aggregate)는 최종 처리 기능으로 요소들을 처리해서 
	// 카운팅, 합계, 평균, 최대값, 최소값 등과 같이 하나의 값으로 산출하는 것을 의미.
	// 집계는 대량의 데이터를 가공해서 축소하는 리덕션(Reduction) 이라고 볼 수 있습니다.
	
	// 이 집계 메소드에서 리턴하는 OptionalXX는 자바 8에서 추가한 
	// java.util 패키지의 Optional, OptionalDouble, OptionalInt, OptionalLong 
	// 클래스 타입을 지칭. 
	// 이들은 값을 저장하는 값 기반 클래스(value-based class)들 입니다.
	// 이 객체에서 값을 얻기 위해서는 get(), getAsDouble(), getAsInt(), getAsLong() 을 호출.
	 
	// 이 클래스들은 저장하는 값의 타입만 다를 뿐 제공하는 기능은 거의 동일합니다.
	// Optional 클래스는 단순히 집계 값만 저장하는 것이 아니라, 
	// 집계 값이 존재하지 않을 경우 디폴트 값을 설정할 수도 있고, 
	// 집계 값을 처리하는 Consumer 도 등록할 수 있다. 	
	private static void optionalMethod() {
		System.out.println("==== optionalMethod =====");
		employees.stream()
		         .filter(e->e.getGender().equals("female"))
		         .mapToInt(e->e.getSalary())
		         .findFirst()
		         .ifPresent(System.out::println);
		System.out.println(); 
	}

	// 스트림은 기본 집계 메소드인 sum(), average(), count(), max(), min()을 제공하지만, 
	// 프로그램화해서 다양한 집계 결과물을 만들 수 있도록 reduce() 메소드를 제공.
	// 스트림에 요소가 전혀 없을 경우 디폴트 값인 identity 파라미터가 리턴.	
	private static void customMethod() {
		System.out.println("==== customMethod =====");
		int sum = employees.stream()
		         		   .filter(e->e.getGender().equals("male"))
		         		   .map(Exam04_Employee::getSalary)
		         		   .reduce(0,(a,b) -> a*2+b);		         		   
		System.out.println(sum);
		System.out.println(); 
	}

	// 스트림은 요소들을 필터링 또는 매핑한 후 요소들을 수집하는 최종 처리 메소드인 
	// collect()를 제공.
	// 이 메소드를 이용하면 필요한 요소만 켈렉션으로 담을 수 있고, 
	// 요소들을 그룹핑 한 후 집계(리덕션) 할 수 있다.	
	private static void collectMethod() {
		System.out.println("==== collectMethod =====");
		List<Integer> salary = employees.stream()
		         		   				.filter(e->e.getGender().equals("male"))
		         		   				.map(Exam04_Employee::getSalary)
		         		   				.collect(Collectors.toList());		
		
		salary.forEach(System.out::println);
		System.out.println();
		
		Set<Exam04_Employee> set = employees.stream()
											.filter(e->e.getGender().equals("female"))
											.collect(Collectors.toCollection(HashSet :: new));
		set.stream().forEach(e -> {
			System.out.println(e.getName());
		});
		
		Map<String,?> map = employees.stream()
				                     .filter(e->e.getDept().equals("IT"))
				                     .collect(Collectors.toConcurrentMap(Exam04_Employee::getName, 
				                    		 e -> e.getSalary()));
		Set<String> keys = map.keySet();
		keys.stream().forEach(e -> {
			System.out.println(map.get(e).toString());
		});
	}
	
	public static void main(String[] args) {
		
		distinctNames();
		filterNames();
		flatNames();
		mapNames();
		sortSalayPrintNames();
		peekMethod();
		matchMethod();
		optionalMethod();
		customMethod();
		collectMethod();
		
	}

}

class Exam04_Employee implements Comparable<Exam04_Employee> {
	private String name;
	private String dept;
	private int salary;
	private int age;
	private String gender;
	public Exam04_Employee(String name, String dept, int salary, int age, String gender) {
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
	
	@Override
	public int compareTo(Exam04_Employee other) {
		int result = 0;
		if( this.getSalary() > other.getSalary() ) {
			result = 1;   // 양수가 리턴되면 순서를 바꾼다.( 결국 오름차순으로 처리 )
		} else if( this.getSalary() == other.getSalary() ) {
			result = 0;
		} else {
			result = -1;
		}				
		return result;

		//return Integer.compare(this.getSalary(), other.getSalary()); 
	}
	
}