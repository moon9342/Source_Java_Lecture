package javaLambda;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

/*
 * Supplier 함수적 인터페이스의 특징은 
 * 매개 변수가 없고 리턴값이 있는 getXXX() 메소드를 가지고 있습니다. 
 * 이 메소드들은 실행 후 호출한 곳으로 데이터를 리턴(공급)하는 역할을 합니다.
 * 리턴 타입에 따라서 여러개의 Supplier 함수적 인터페이스들이 있습니다.
 * 
 */
public class Exam06_LambdaUsingSupplierAPI {

	public static void main(String[] args) {

		// 랜덤으로 친구목록 중 1명을 선택해서 출력
		final List<String> myBuddy = 
				Arrays.asList("홍길동","이순신","강감찬","신사임당");
		
		Supplier<String> supplier = () -> {
			return myBuddy.get((int)(Math.random() * myBuddy.size()));
		};
		
		System.out.println(supplier.get());
		
		// 자동 로또 숫자 생성기
		Set<Integer> set = new HashSet<Integer>();
		IntSupplier intSupplier = () -> (int)(Math.random()*45 + 1);
		
		while(set.size() != 6) {
			set.add(intSupplier.getAsInt());
		}
		System.out.println(set);	
	}
}