package supportclasses;

public class MyExamCloud implements Runnable {

	public static void main(String[] args) throws InterruptedException {

		Thread t = new Thread(new MyExamCloud());

		t.start();

		System.out.print("Started");

		t.join();

		System.out.print("Completed");
	}

	@Override
	public void run() {
		for (int i = 0; i < 4; i++){
			System.out.print(i);
		}
	}
}
