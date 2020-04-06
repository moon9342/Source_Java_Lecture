package javaLambda;

/*
 * 자바에서 제공되는 표준 API에서 한 개의 추상 메소드를 가지는 인터페이스들은 모두 람다식을 이용해서 
 * 익명 구현 객체로 표현이 가능합니다. 
 * 예를 들어 스레드의 작업을 정의하는 Runnable 인터페이스는 
 * 매개 변수와 리턴값이 없는 run() 메소드만 존재하기 때문에 
 * 람다식을 이용해서 Runnable 인스턴스를 생성시킬 수 있습니다.
 * 
 * 자바 8 부터는 빈번하게 사용되는 함수적 인터페이스(Functional Interface)는 
 * java.util.function 표준 API 패키지로 제공합니다. 
 * 
 * 이 패키지에서 제공하는 함수적 인터페이스의 목적은 
 * 메소드 또는 생성자의 매개 타입으로 사용되어 
 * 람다식을 대입할 수 있도록 하기 위해서 입니다. ( 이 말을 이해하기가 쉽지 않아요!! )
 *  
 * java.util.function 패키지의 함수적 인터페이스는 
 * 크게 Consumer, Supplier, Function, Operator, Predicate로 구분됩니다.
 *
 */
public class Exam04_LambdaUsingAPI {

	public static void main(String[] args) {
		
/*		
		Runnable runnable = new Runnable() {			
			@Override
			public void run() {
				System.out.println("원형");
			}
		};
		new Thread(runnable).start();
*/
		
/*
		Runnable runnable = () -> { System.out.println("람다식 표현"); };
		new Thread(runnable).start();
*/
		new Thread(() -> {
			System.out.println("일반적인 람다식 사용형식");
		}).start();  
	}
}