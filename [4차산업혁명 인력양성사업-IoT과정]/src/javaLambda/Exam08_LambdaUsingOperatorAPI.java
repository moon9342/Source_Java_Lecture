package javaLambda;

import java.util.function.IntBinaryOperator;

/*
 * Operator 함수적 인터페이스는 Function과 동일하게 
 * 매개 변수와 리턴값이 있는 applyXXX() 메소드를 가지고 있습니다. 
 * 하지만 이 메소드들은 매개값을 리턴값으로 매핑(타입 변환)하는 역할보다는 매개값을 이용해서 연산을 수행한 후 
 * 동일한 타입으로 리턴값을 제공하는 역할을 합니다.
 * 다음 예제는 int[] 배열에서 최대값과 최소값을 얻습니다. 
 * maxOrMin() 메소드는 IntBinaryOperator 매개 변수를 가지고 있습니다. 
 * 따라서 maxOrMin() 메소드를 호출할 때 람다식을 이용할 수 있습니다.
 */

public class Exam08_LambdaUsingOperatorAPI {

	private static int arr[] = { 100, 92, 81, 78, 88, 96, 55,94 };
	
	public static int MinOrMax(IntBinaryOperator op) {
		int result = arr[0];
		
		for(int val : arr) {
			result = op.applyAsInt(result, val);
		}
		
		return result;
		
	}
	public static void main(String[] args) {
		
		int max = MinOrMax((a,b) -> {
			return a >= b ? a : b;
		});
		
		System.out.println("최대값 : " + max);

		int min = MinOrMax((a,b) -> {
			return a < b ? a : b;
		});
		
		System.out.println("최소값 : " + min);
	}
}