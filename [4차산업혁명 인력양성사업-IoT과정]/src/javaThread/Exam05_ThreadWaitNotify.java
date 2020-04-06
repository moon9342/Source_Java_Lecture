package javaThread;

class Shared {

	public synchronized void printNum() {	
//	public void printNum() {
		
		try {
			for(int i=0; i < 10; i++) {
				Thread.sleep(1000);
				System.out.println(Thread.currentThread().getName() + " : " + i);
				notify();
				wait();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

class Exam03_MyRunnable implements Runnable {

	private Shared shared;
	
	public Exam03_MyRunnable(Shared shared) {
		this.shared = shared;
	}
	
	@Override
	public void run() {			
		shared.printNum();
	}		
}

public class Exam05_ThreadWaitNotify {

	public static void main(String[] args) {

		Shared obj = new Shared();
		
		Thread t1 = new Thread(new Exam03_MyRunnable(obj));
		Thread t2 = new Thread(new Exam03_MyRunnable(obj));
		
		t1.setName("첫번째 Thread!!");
		t2.setName("두번째 Thread!!");

		t1.start();
		t2.start();
	}
}