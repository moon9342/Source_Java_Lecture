package javaThread.procon;

public class ProConTest {

	public static void main(String[] args) {
		
		// 1. 데이터에 대한 동기화 처리가 있어야 해요!
		//    공유객체를 하나 만들어야 할 거 같아요!
		//    공유객체를 만들때 이 객체가 가지고 있어야 할 자료구조를 생각
		//    이 자료구조를 각 Thread를 이용할때 method를 이용해야 하는데
		//    어떤 method가 필요한지를 생각해야 해요!
		SharedObject obj = SharedObject.getInstance();
		// 2. 4개의 Thread를 생성해야 해요!!
		//    1개의 Producer와 3개의 Consumer Thread를 생성
		//    class를 define해야 해요!
		Thread producer = new Thread(new Producer(obj));
		Thread con1 = new Thread(new Consumer(obj));
		Thread con2 = new Thread(new Consumer(obj));
		Thread con3 = new Thread(new Consumer(obj));
		
		// 3. 각 thread를 시작
		con1.start();
		con2.start();
		con3.start();
		
		producer.start();
		
		try {
			Thread.sleep(2);   // main thread를 잠깐 쉬어요!!
			producer.interrupt();
			Thread.sleep(2);   // main thread를 잠깐 쉬어요!!
			con1.interrupt();
			con2.interrupt();
			con3.interrupt();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
