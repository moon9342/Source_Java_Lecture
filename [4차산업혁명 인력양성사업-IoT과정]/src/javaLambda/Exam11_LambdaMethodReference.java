package javaLambda;

import java.util.function.Consumer;

/*
 * 메소드 참조(Method Reference)는 말 그대로 메소드를 참조해서 매개 변수의 정보 및 리턴 타입을 알아내어, 
 * 람다식에서 불필요한 매개 변수를 제거하는 것이 목적입니다.
 * 람다식은 종종 기존 메소드를 단순히 호출하는 경우가 많습니다.
 * 예를 들어 두 개의 값을 받아 큰 수를 리턴하는 Math 클래스의 max() 정적 메소드를 호출하는 람다식은 
 * 다음과 같습니다.
 * 
 * (left, right) -> Math.max(left, right);
 *
 * 람다식은 단순히 두 개의 값을 Math.max() 메소드의 매개값으로 전달하는 역할만 합니다.  
 * 이 경우에는 다음과 같이 메소드 참조를 이용하면 깔끔하게 처리할 수 있습니다.
 * 
 * Math :: max;
 * 
 * 메소드 참조도 람다식과 마찬가지로 인터페이스의 익명 구현 객체로 생성되므로 
 * 타겟 타입인 인터페이스 추상 메소드가 어떤 매개 변수를 가지고, 리턴 타입이 무엇인가에 따라 달라집니다.
 * 
 * IntBinaryOperator 인터페이스는 두 개의 int 매개값을 받아 int 값을 리턴하므로 
 * Math :: max 메소드 참조를 대입할 수 있습니다.
 * 
 * IntBinaryOperator operator = Math :: max;
 * 
 * 메소드 참조는 정적 또는 인스턴스 메소드를 참조할 수 있고, 생성자 참조도 가능합니다.
 * 정적 메소드를 참조할 경우에는 클래스 이름 뒤에 :: 기호를 붙이고 정적 메소드 이름을 기술하면 됩니다.
 * 인스턴스 메소드일 경우에는 먼저 객체를 생성한 다음 참조 변수 뒤에 :: 기호를 붙이고 
 * 인스턴스 메소드 이름을 기술하면 됩니다.
 *  
 *  클래스 :: 메소드;
 *  참조 변수 :: 메소드;
 *
 */

public class Exam11_LambdaMethodReference {

	public static void main(String[] args) {

		Consumer<String> consumer = System.out::println;
		consumer.accept("소리없는 아우성!!");
	}

}