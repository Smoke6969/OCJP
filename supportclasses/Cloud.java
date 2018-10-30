package myexamcloud.supportclasses;

public class Cloud {

	final static int x;
	final int y;

	static {
		x = 12;
	}

	Cloud(){
		y = 3;
	}

	public static void main(String[] args){
		System.out.println(x + new Cloud().y);
		System.out.println(3 + 5);
	}
}
