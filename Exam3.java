package myexamcloud;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class Exam3 {

	// new ArrayList() and new ArrayList<>() is basically the same constructors. It is done in java for compatibility
	// with versions before Generics. What happens is Type Errasure:
	// When people mention "Type Erasure" this is what they're talking about. The compiler inserts the casts for you,
	// and then 'erases' the fact that it's meant to be a list of Person not just Object
	public void question1(){

		//List<String> list1 = new List<>(); //Error - List is abstrac; cannot be instantiated
		//List<> list = new ArrayList<>(); //Error - identifier is expected

		List<String> list1 = new ArrayList();
		List<String> list2 = new ArrayList<>();
	}


	//Map:
	//public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction)
	//If the specified key is not already associated with a value or is associated with null, associates it with the
	//given non-null value. Otherwise, replaces the associated value with the results of the given remapping function,
	//or removes if the result is null.
	//So in the following example key 1 will be removed, because the result of the remappingFunction is null
	public void question2(){

		Map<Integer, String> numbers = new HashMap<>();
		numbers.put(1, "one");
		numbers.put(2, "Two");

		numbers.merge(1, "One", (k, v) -> null);

		System.out.println(numbers); //{2=Two}
	}


	//Just the example of using SimpleFileVisitor to search for files with .png extension
	public void question3() throws IOException {

		Path path = Paths.get("D:");
		Files.walkFileTree(path, new PngSearcher());
	}

	class PngSearcher extends SimpleFileVisitor{

		public FileVisitResult visitFile(Path file, BasicFileAttributes bfa) throws IOException{

			PathMatcher pm = FileSystems.getDefault().getPathMatcher("glob:*.png");
			if(pm.matches(file.getFileName())){
				System.out.println(file);
			}

			return FileVisitResult.CONTINUE;
		}
	}

	//Here hashtable will put all the values because we've override hashCode with just returning id, so hashtable
	// thinks that objects with different hashcodes are different
	public void question4() {

		Hashtable table = new Hashtable<>();
		table.put(new Person(1, "Alex"), 1);
		table.put(new Person(2, "Bob"), 2);
		table.put(new Person(3, "Alex"), 3);
		table.put(new Person(4, "Alex"), 4);

		System.out.println(table.values()); //[4, 3, 2, 1]
	}

	class Person{

		public int getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		int id;
		String name;

		public Person(int id, String name){
			this.id = id;
			this.name = name;
		}

		@Override
		public int hashCode(){
			return id; //THIS IS CORRECT
			//return name.getBytes().length; //THIS IS CORRECT
		}

		@Override
		public boolean equals(Object o){

			return ((Person)o).getName().equals(this.getName());
		}
	}
}
