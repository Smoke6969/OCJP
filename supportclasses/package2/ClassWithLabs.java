package myexamcloud.supportclasses.package2;

public class ClassWithLabs extends Labs  implements A  {

	public static void main(String[] args){
		new ClassWithLabs().print();
	}
}

class Labs{
	public void print(){
		System.out.println("Labs");
	}
}

interface A{
	default void print(){
		System.out.println("Labs");
	}
}
