package myexamcloud;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Exam2 {

	// public abstract T reduce(T identity, BinaryOperator<T> accumulator) - Performs a reduction on the elements of
	// this stream, using the provided identity value and an associative accumulation function, and returns the reduced value.
	// This is equivalent to:
	// T result = identity;
	// for (T element : this stream)
	//     result = accumulator.apply(result, element)
	// return result;
	// but is not constrained to execute sequentially.
	public void question1(){

		Stream<Integer> ints = Stream.of(33, 234, 1, 25, -5, 100);

		int min = ints.reduce(Integer.MIN_VALUE, Integer::min); //integer minimum value

		System.out.println(min);
	}


	public void question2(){

		List<Trade> list = new ArrayList<>();
		list.add(new Trade(4, 1000));
		list.add(new Trade(9, 1220));
		list.add(new Trade(1, 12000));

		Trade highQ = list.get(0);

		BiPredicate<Trade, Trade> highest = (t1, t2) -> t1.getQuantity() > t2.getQuantity();

		for(int i = 1; i <= list.size(); i++){
			if(highest.test(list.get(i - 1), list.get(i))){
				highQ = list.get(i);
			}
		}

		System.out.println(highQ);
	}

	class Trade{

		int quantity;
		double price;
		double total;

		public Trade(int qty, double price){
			this.quantity = qty;
			this.price = price;
			this.total = quantity * price;
		}

		public int getQuantity() {
			return quantity;
		}

		@Override
		public String toString(){
			return quantity + "," + price + "," + total;
		}
	}

	//YOU CAN INSTANTIATE AN INTERFACE. But in this case you have to override and implement methods of this interface.
	public void question3(){

		Validator<String> validator = new Validator<String>() {
			@Override
			public boolean test(String in) {
				return in.matches("[A]{1}[a-zA-Z]");
			}
		};

		Validator<String> validator1 = in -> in.matches("[A]{1}[a-zA-Z]");

	}

	interface Validator<T>{
		public boolean test(T t);
	}


	//1. public abstract java.util.Collection<V> values() - Returns a Collection view of the values contained in this
	// map. The collection is backed by the map, so changes to the map are reflected in the collection, and vice-versa.
	public void question4(){

		Map<Integer, String> map = new HashMap<>();
		map.put(1, "1");
		map.put(2, "2");
		map.put(3, "3");

		Stream<Double> ds = map.values().stream().map(s -> Double.parseDouble(s));
		System.out.println(ds.findFirst().get()); //1.0
	}


	// Collection:
	// 1. public java.util.stream.Stream<E> parallelStream() - Returns a possibly parallel Stream with this
	// collection as its source. It is allowable for this method to return a sequential stream. So when calling
	// forEach() on parallel stream you can't be sure of order.
	public void question5(){

		List<Integer> list = new ArrayList<>();
		list.add(10);
		list.add(30);
		list.add(5);
		list.add(40);
		list.add(0);
		list.add(20);

		Predicate<Integer> filter = it -> it > 15;
		Consumer<Integer> cons = it -> {
			if(filter.test(it))
				System.out.print(it);
		};

		list.parallelStream().forEach(cons); //40, 30 and 20 in random order
	}


	//Stream:
	//1. IntStream mapToInt(java.util.function.ToIntFunction<? super T> mapper) - Returns an IntStream consisting of
	// the results of applying the given function to the elements of this stream. mapToInt(i -> i) is legal.
	//2. IntStream public abstract int sum() - Returns the sum of elements in this stream.
	public void question6(){

		List<Integer> list = Arrays.asList(1,2,3,4,4,6,6);
		System.out.println(list.stream().filter(e -> e%2 == 0).mapToInt(i -> i).sum()); //22
	}


	//1. Map:
	// public void forEach(@NotNull java.util.function.BiConsumer<? super K, ? super V> action) - Performs the given
	// action for each entry in this map until all entries have been processed or the action throws an exception. So
	// shortly - forEach() on map takes BiConsumer
	//2. The order in which you see the entries in the HashMap depends on the hash codes of the keys. So HashMap where
	// key is integer will be sorted bu key.
	public void question7(){

		Map map = new HashMap();
		map.put(1, "One");
		map.put(2, "Two");
		map.put(0, "Zero");
		map.put(3, "Three");
		map.put(7, "Seven");
		map.put(5, "Five");

		BiConsumer cons = (k, v) -> System.out.print("[K = " + k + " V = " + v + "]");
		map.forEach(cons);
	}


	//1. LocalDateTime
	// public LocalDateTime plusHours(long hours) - Returns a copy of this LocalDateTime with the specified number of
	// hours added.
	// public static LocalDateTime parse(CharSequence text) - Obtains an instance of LocalDateTime from a text
	// string such as 2007-12-03T10:15:30.
	public void question8(){

		LocalDate date = LocalDate.parse("2014-12-31");
		date = date.plusDays(2);
		//date = date.plusHours(24); ERROR
		System.out.println(date); //2015-01-02

		LocalDateTime dateTime = LocalDateTime.parse("2014-12-31T00:00:00");
		dateTime = dateTime.plusDays(2);
		dateTime = dateTime.plusHours(24);
		System.out.println(dateTime.toLocalDate()); //2015-01-03
	}


	//Predicate:
	//1. public static <T> Predicate<T> isEqual(@Nullable Object targetRef) - Returns a predicate that tests if two
	// arguments are equal according to Objects.equals(Object, Object). So the following example will works fine
	// because we've override equals
	public void question9(){

		List<Employee> list = new ArrayList();
		list.add(new Employee("Alex", 12000));
		list.add(new Employee("Alice", 10000));
		list.add(new Employee("Dean", 9000));

		Employee emp = new Employee("Dean", 9000);
		Predicate predict = Predicate.isEqual(emp);

		for(Employee e : list){
			if(predict.test(e)){
				System.out.print(e.salary);
			}
		}
	}

	class Employee{
		String name;
		double salary;

		public Employee(String name, double salary) {
			this.name = name;
			this.salary = salary;
		}

		@Override
		public boolean equals(Object employee){
			return ((Employee)employee).name.equals(this.name);
		}
	}

	// 1. Duration:
	// 1.1 public static Duration ofHours(long hours) - Obtains a Duration representing a number of standard hours.
	// 1.2 public Duration plusMinutes(long minutesToAdd) - Returns a copy of this duration with the specified duration
	// in minutes added.
	// 2. Instant:
	// 2.1 public Instant plus(TemporalAmount amountToAdd) - Returns a copy of this instant
	// with the specified amount added. Duration implements TemporalAmount so you can pass duration to Plus method of
	// Instant
	// 2.2 public Instant plus(long amountToAdd, TemporalUnit unit) - Returns a copy of
	// this instant with the specified amount added.
	public void question10(){

		Instant instant = Instant.now();

		instant.plus(Duration.ofHours(5).plusMinutes(5));
		instant.plus(5, ChronoUnit.HOURS).plus(5, ChronoUnit.MINUTES);
	}


	//To use filter method correctly, you should invoke it on Stream and then collect
	public void question11(){

		List<Integer> list = new ArrayList<>();
		list.add(1);
		list.add(3);
		list.add(4);
		list.add(5);
		list.add(7);
		list.add(8);

		Predicate<Integer> predicate = p -> p > 4;

		list = list.stream().filter(predicate).collect(Collectors.toList());

		System.out.println(list);
	}


	//In the following example variable x will be taken from arguments
	public void question12(){


	}

	static int x = 7;
	public static int getSum(int x, IntStream ints){
		//x = 4;
		IntPredicate pre = in -> in > x;
		return ints.filter(pre).sum();
	}

	//File:
	// public boolean renameTo(@NotNull File dest) - Renames the file denoted by this abstract pathname.
	public void question13(){

		File file = new File("file.txt");
		System.out.println(file.renameTo(new File("file1.txt")));

	}


	//You can actually cast creation of new object to Supplier, but you DO need to cast it.
	public void question14(){

		Supplier<User> sup = (Supplier<User>)new User("Test");

	}

	class User{

		String name = "Unknown";

		public User(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

}
