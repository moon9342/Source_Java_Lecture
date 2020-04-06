package javaLambda;

/*
 * 람다식의 실행 블록에는 클래스의 멤버(필드와 메소드) 및 로컬 변수를 사용할 수 있습니다.
 * 클래스의 멤버는 제약 사항 없이 사용 가능하지만, 로컬 변수는 제약 사항이 따릅니다.
 * 
 * 특히 this 키워드를 사용할 때에는 주의가 필요합니다. 
 * 일반적으로 익명 객체 내부에서 this는 익명 객체의 참조이지만, 
 * 람다식에서 this는 내부적으로 생성되는 익명 객체의 참조가 아니라 람다식을 실행한 객체의 참조입니다.
 * 
 * 다음 예제는 람다식에서 바깥(outer) 객체와 중첩(Nested, Inner) 객체의 참조를 얻어 
 * 필드값을 출력하는 방법을 보여주고 있습니다.
 * 중첩 객체 Inner에서 람다식을 실행했기 때문에 람다식 내부에서의 this는 중첩 객체 Inner 입니다.
 * 
 * 람다식에서 바깥 클래스의 필드나 메소드는 제한 없이 사용할 수 있으나, 
 * 메소드의 매개 변수 또는 로컬 변수를 사용하면 이 두 변수는 final 특성을 가져야 합니다.
 * (왜 그럴까를 잘 생각해보자!! - final 특성을 가져야만 reference를 사용할 수 있기 때문)
 * 
 * 따라서 매개 변수 또는 로컬 변수를 람다식에서 읽는 것은 허용되지만, 
 * 람다식 내부 또는 외부에서 변경할 수 없습니다.
 */

@FunctionalInterface
interface Exam03_MyFunctionInterface {
	public void method();
}

class OuterClass_1 {
	public int outerField = 100;
	
	class InnerClass_1 {
		int innerField = 200;
				
		Exam03_MyFunctionInterface fieldLambda = () -> { 
			System.out.println("OuterField : " + outerField);
			System.out.println("OuterClass.this.outerField : " + OuterClass_1.this.outerField);
			System.out.println("innerField : " + innerField);
			System.out.println("this.innerField : " + this.innerField);
			this.innerField =  50;  // field는 자유롭게 사용할 수 있다.
			System.out.println("this.innerField : " + this.innerField);
		};
		
		public void innerMethod() {
			int localVar = 500;			
			Exam03_MyFunctionInterface lambda = () -> { 
				System.out.println("OuterField : " + outerField);
				System.out.println("OuterClass.this.outerField : " 
									+ OuterClass_1.this.outerField);
				System.out.println("innerField : " + innerField);
				System.out.println("this.innerField : " + this.innerField);
				System.out.println("localVar : " + localVar);
				//localVar = 50;   // final 특성을 가지므로 수정 불가( error )
			};			
			//localVar = 1000;  // LocalVar을 묵시적으로 final이 안되게끔 처리
			                    // lambda식에서 reference가 안되기 때문에 error발생 
			lambda.method();
		}
	}
}
public class Exam03_LambdaUsingVariable {

	public static void main(String[] args) {
		OuterClass_1 outer = new OuterClass_1();
		OuterClass_1.InnerClass_1 inner = outer.new InnerClass_1();
		inner.fieldLambda.method();
		inner.innerMethod();	
	}
}