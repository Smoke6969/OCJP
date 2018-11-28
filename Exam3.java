package myexamcloud;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Exam3 {

	// new ArrayList() and new ArrayList<>() is basically the same constructors. It is done in java for compatibility
	// with versions before Generics. Waht happens is Type Errasure:
	// When people mention "Type Erasure" this is what they're talking about. The compiler inserts the casts for you,
	// and then 'erases' the fact that it's meant to be a list of Person not just Object
	public void question1(){

		//List<String> list1 = new List<>(); //Error - List is abstrac; cannot be instantiated
		//List<> list = new ArrayList<>(); //Error - identifier is expected

		List<String> list1 = new ArrayList();
		List<String> list2 = new ArrayList<>();
	}
}
