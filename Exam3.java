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
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;
import java.util.function.ToIntFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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


	public void question5() {


		List<Integer> list = new ArrayList<>();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);
		list.add(6);
		list.add(7);
		list.add(8);

		list.stream().parallel().limit(3).forEach(System.out::print);
	}


	//In following example a++ is a post-increment, so basically it returns a and then does increment, so output of
	// function will be the same as parameter.
	public void question6() {

		increment(Exam3::mul, 1);
		System.out.print(mul(2)); //2
	}

	public static int mul(int a){
		return a++;
		//return ++a;
	}
	public void increment(IntUnaryOperator operator, int a){
		System.out.print(operator.applyAsInt(a));
	}


	//Here orElse returns integer, not optional
	public void question7() {

		List<Integer> list = new ArrayList<>();
		list.add(1);
		list.add(2);
		list.add(3);

		//Optional<Integer> op = Optional.of(list.get(2)).orElse(0); //Error
	}


	//ToIntFunction:
	//Represents a function that produces an int-valued result. This is the int-producing primitive specialization for Function.
	//Because ToIntFunction produces primitive result - method boxed() should be called to assugn it's result to Stream<Integer>
	public void question8() {

		Stream<Double> doubleStream = Stream.of(10.0, 20.1, 30.2);
		ToIntFunction<Double> func = d -> d.intValue();
		Stream<Integer> ints = doubleStream.mapToInt(func).boxed();
		ints.forEach(System.out::print);

	}


	//To convert LocalDate to LocalDateTime, use the following method on LocalDate:
	//public java.time.LocalDateTime atTime(int hour, int minute)
	public void question9() {

		LocalDate date = LocalDate.of(2018, 11, 10);
		ZoneId zid = ZoneId.of("Europe/Paris");
		ZonedDateTime zdt = ZonedDateTime.of(date.atTime(12, 00), zid);

	}


	//ResourceBundle:
	//Resource bundles contain locale-specific objects. When your program needs a locale-specific resource, a
	// String for example, your program can load it from the resource bundle that is appropriate for the
	// current user's locale.
	//ResourceBundle.Control:
	//defines a set of callback methods that are invoked by the ResourceBundle.getBundle factory methods during
	// the bundle loading process. In other words, a ResourceBundle.Control collaborates with the factory
	// methods for loading resource bundles.
	//FORMAT_CLASS The class-only format List containing "java.class".
	//
	//So with the following ResourceBundle.Control you can't use properties files, only the class files
	public void question10() {

		ResourceBundle.Control rbc = ResourceBundle.Control.getControl(ResourceBundle.Control.FORMAT_CLASS);

	}

	//The following class creation syntax is legal:
	public void question11() {

		Exam3.Test t = new Exam3().new Test();
		//same as
		Test t1 = new Test();
	}

	class Test{

	}


	//LocalDate:
	//String format(@NotNull java.time.format.DateTimeFormatter formatter) - Formats this date using the specified
	// formatter. This date will be passed to the formatter to produce a string.
	public void question12() {

		Year year = Year.of(2014);
		String result = year.atMonthDay(MonthDay.of(3, 3)).format(DateTimeFormatter.BASIC_ISO_DATE);
		System.out.println(result);
	}


	//Basically dividing integers 7 / 2 = 3;
	public void question13() {

		Function<Integer, Double> func = i -> new Double(i/2);
		//Function<Integer, Double> func = i -> i/2; //Error
		System.out.println("Radius = " + func.apply(7)); //3.0

		int seven = 7;
		int three = seven / 2;
	}

	//java.util.OptionalDouble average() - Returns an OptionalDouble describing the arithmetic mean of elements of this
	// stream, or an empty optional if this stream is empty. This is a special case of a reduction.
	public void question14() {

		IntStream stream = IntStream.rangeClosed(1, 4);
		double average = stream.average().getAsDouble();
	}


	//LocalDate doesn't support hours!
	public void question15() {

		LocalDate date = LocalDate.parse("2014-12-31");
		date = date.plusDays(2);
		System.out.println(date);
	}


	//IntPredicate negate() - Returns a predicate that represents the logical negation of this predicate. In other
	// words - when predicate returns true, the negation is false and otherwise.
	public void question16() {

		IntStream ints = IntStream.range(1, 10);
		IntPredicate predicate = i -> i > 5;
		ints.filter(predicate.negate()).forEach(s -> System.out.print(s + "")); //12345
	}


	//interface AutoCloseable - An object that may hold resources (such as file or socket handles) until it is closed.
	// The close() method of an AutoCloseable object is called automatically when exiting a try-with-resources
	// block for which the object has been declared in the resource specification header.
	public void question17() { //123

		try(Resource rs = new Resource()){
			rs.print();
		}catch (Exception ex){

		}

		System.out.print("3");
	}

	class Resource implements AutoCloseable{

		@Override
		public void close() throws Exception {
			System.out.print("2");
		}


		public void print() {
			System.out.print("1");
		}
	}


	//1. ConcurrentHashMap - A hash table supporting full concurrency of retrievals and high expected concurrency for
	// updates.
	//2. ConcurrentSkipListMap - A scalable concurrent ConcurrentNavigableMap implementation. The map is sorted
	// according to the natural ordering of its keys, or by a Comparator provided at map creation time, depending
	// on which constructor is used. This class implements a concurrent variant of SkipLists providing expected
	// average log(n) time cost for the containsKey, get, put and remove operations and their variants. Insertion,
	// removal, update, and access operations safely execute concurrently by multiple threads.
	//ConcurrentLinkedQueue - An unbounded thread-safe queue based on linked nodes.
	public void question18() {

		ConcurrentHashMap<String, String> concurrentHashMap = new ConcurrentHashMap();
		ConcurrentSkipListMap<String, String> concurrentSkipListMap  = new ConcurrentSkipListMap<>();
		ConcurrentLinkedQueue<String> concurrentLinkedQueue = new ConcurrentLinkedQueue<>();
	}

}
