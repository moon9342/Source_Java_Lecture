package javaLambda;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.ObjIntConsumer;

/*
 * Consumer 함수적 인터페이스의 특징은 리턴값이 없는 accept() 메소드를 가지고 있습니다. 
 * accept() 메소드는 단지 매개값을 소비하는 역할만 합니다. 
 * 여기서 소비한다는 말은 사용만 할 뿐 리턴값이 없다는 뜻입니다.
 * 매개 변수의 타입과 수에 따라서 여러개의  Consumer들이 있습니다.
 * 
 * Consumer<T> 인터페이스를 타켓 타입으로 하는 람다식은 다음과 같이 작성할 수 있습니다. 
 * accept() 메소드는 매개값으로 T 객체 하나를 가지므로 람다식도 한 개의 매개 변수를 사용합니다. 
 * 타입 파라미터 T에 String이 대입되었기 때문에 람다식의 t 매개변수 타입은 String이 됩니다.
 * 
 * Consumer<String> consumer = t -> { t를 소비하는 실행문; };
 * 
 */
public class Exam05_LambdaUsingConsumerAPI {

	public static void main(String[] args) {

		Consumer<String> consumer = t -> System.out.println(t);
		consumer.accept("Consumer Functional Interface");
		
		BiConsumer<String, String> biConsumer = 
				(a,b) -> System.out.println(a+b);
		biConsumer.accept("너무", "어렵다");
		
		IntConsumer intConsumer = i -> System.out.println(i);
		intConsumer.accept(100);
		
		ObjIntConsumer<String> objIntConsumer = (a,b) -> {
			System.out.println(a + b);
		};
		objIntConsumer.accept("Java", 8);		
	}
}