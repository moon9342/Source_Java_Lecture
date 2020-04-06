package javaLambda;

/*
 * 자바는 함수적 프로그래밍을 위해 자바 8부터 람다식(Lambda Expressions)을 지원 
 * ==> 기존의 코드 패턴과 많이 달라짐.
 * 
 * 원래 람다식은 익명 함수(anonymous function)를 생성하기 위한 식 
 * ==> 객체 지향 언어보다는 함수 지향 언어에서 사용이 됨. 
 * 
 * 객체 지향 프로그래밍에 익숙한 개발자들에게는 약간 생소한 개념이지만 
 * 자바에서 람다식을 수용한 이유는 
 * ==> 1. 코드가 매우 간결해진다. 
 * ==> 2. Java Stream과 같이 사용하면 컬렉션의 요소를 필터링하거나 매핑해서 
 * ==>    원하는 결과를 쉽게 집계할 수 있다. (병렬처리를 지원 포함)
 * 
 * 람다식의 형태
 * ==> (매개변수) -> {실행코드}
 * ==> 함수 정의 형태를 띄고 있지만 런타임 시에는 인터페이스의 익명 구현 객체로 생성.
 * 
 * 람다식이 어떤 인터페이스를 구현할 것인가는 람다식이 대입되는 인터페이스가 무엇이냐에 달려있다.
 * 
 * 예제1) 람다식이 Runnable 변수에 대입되므로 람다식은 Runnable의 익명 구현 객체를 생성.
 * 예제2) 람다식이 Exam01_LambdaIF 변수에 대입되므로 람다식은 Exam01_LambdaIF의 익명 구현 객체를 생성
 */

interface Exam01_LambdaIF {
	public void myMethod();
}

public class Exam01_LambdaBasic {

	public static void main(String[] args) {

/*		
        Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				System.out.println("Hello~ Lambda");
			}
		};		
*/		
		// 예제1)
		Runnable runnable = () -> { 
			System.out.println("Hello~ Lambda"); 
		};
		new Thread(runnable).start();
		
		// 예제2)
		Exam01_LambdaIF exam01_lambdaIF = () -> {
			System.out.println("인터페이스 구현객체");
		};
		exam01_lambdaIF.myMethod();
	}
}