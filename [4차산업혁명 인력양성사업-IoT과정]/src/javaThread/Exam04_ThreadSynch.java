package javaThread;

class SharedObject {
	private int number;

	public int getNumber() {
		return number;
	}

//	public synchronized void setNumber(int number) {	
	public void setNumber(int number) {
//		synchronized (SharedObject.this) {
			this.number = number;
			try {
				Thread.sleep(2000);
				System.out.println("현재 number : " + getNumber());
			} catch (Exception e) {
				e.printStackTrace();
			}			
//		}
	}
}

class MyRunnable implements Runnable {
	
	private SharedObject obj;
	private int number;
	
	public MyRunnable(SharedObject obj, int number) {
		this.obj = obj;
		this.number = number;
	}
	
	@Override
	public void run() {
		obj.setNumber(number);
	}
	
}

public class Exam04_ThreadSynch {

	public static void main(String[] args) {

		// 공유객체 생성
		SharedObject obj = new SharedObject();
		
		// Thread 생성
		Thread t1 = new Thread(new MyRunnable(obj, 100));
		Thread t2 = new Thread(new MyRunnable(obj, 200));
		t1.start();
		t2.start();
	}

}