package myexamcloud;

import java.io.Console;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Optional;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.Stream;

public class FreeExam {

	//1. When initializing a String array - values in array are null
	//2. You can't put null as value into ConcurrentHashMap due to RUNTIME NullPointerException
	public void question1(){

		String s[] = new String[2];
		s[1] = "B";
		ConcurrentHashMap<String, Integer> cMap = new ConcurrentHashMap<>();
		cMap.put("A", new Integer(1));
		cMap.put(s[0], new Integer(2));
		cMap.put("C", 3);
		cMap.putIfAbsent("D", new Integer(4));

		System.out.println(cMap);
	}


	//1. ForkJoinTask<V> implements Future<V>, Serializable
	//2. Has a lots of methods like complete, getPool, tryUnfork, isDone
	public void question2(){

		ForkJoinPool fjp = new ForkJoinPool();
		DemoTask task = new DemoTask();
		ForkJoinTask<String> fjt = ForkJoinTask.adapt(task);
		fjp.invoke(fjt);
		System.out.println(fjt.getRawResult());
		System.out.println(fjt.isDone());
	}

	class DemoTask implements Callable<String> {
		public String call() {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.out.println(e);
			}
			return "Task Done";
		}
	}

	//1. When initializing a String array - values in array are null
	//2. You can't get Optional.of(null) due to RUNTIME NullPointerException
	public void question3(){

		String in[] = new String[3];
		String op1 = Optional.of(in[2]).orElse("Empty");
		System.out.println(op1);
	}


	//1. You can't use Duration.between method with LocalDate class - you'll get RUNTIME
	//UnsupportedTemporalTypeException: Unsupported unit: Seconds
	//2. You need class with seconds to use with Duration.between, like LocalDateTime
	//3. You don't have to specify seconds for LocalDateTime, just minutes and Duration.between will work fine
	public void question4(){

		LocalDate ld0 = LocalDate.of(2015, 1, 27);
		LocalDate ld1 = LocalDate.of(2015, 1, 31);

		//LocalDateTime ld0 = LocalDateTime.of(2016, Month.AUGUST, 31, 10, 20, 12);
		//LocalDateTime ld1 = LocalDateTime.of(2016, Month.NOVEMBER, 9, 10, 21, 14);

		Duration due = Duration.between(ld0, ld1);
		System.out.println(due.getSeconds());
	}


	//1. If a class implements interface - this class is an instance of this interface
	//2. If a class extends class that implements interface - this class is an instance of this interface
	public void question5(){

		I i = new B();
		A a = new B();
		boolean t1 = i instanceof I, t2 = a instanceof I;
		System.out.println(t1 + " " + t2);
	}

	interface I {}

	abstract class A implements I {}

	class B extends A {}


	//1. pollFirst() and ceiling are not the methods of Set interface, those are implemented in TreeSet only
	//2. pollFirst() retreives and removes first element (A in this case)
	//3. ceiling() Returns the least element in this set greater than or equal to the given element,
	// or null if there is no such element.
	public void question6(){

		//Set set = new TreeSet();
		TreeSet set = new TreeSet();
		set.add("A");
		set.add("B");
		set.add("E");
		set.add("F");
		set.add("G");
		set.pollFirst();
		System.out.println(set.ceiling("C")); //return E
	}

	//1. Comparator.nullsLast Returns a null-friendly comparator that considers null to be greater than non-null.
	// When both are null, they are considered equal.
	// If both are non-null, the specified Comparator is used to determine the order (Integer::compare in this case)
	//2. Comparator.compare Returns a negative integer, zero, or a positive integer as the first argument is less than,
	// equal to, or greater than the second.
	public void question7(){

		Comparator comp = Comparator.nullsLast(Integer::compare);
		System.out.println(comp.compare(null, 25)); //return 1
	}


	//1. char[] readPassword(String fmt, Object... args) Provides a formatted prompt, then reads a password or
	// passphrase from the console with echoing disabled.
	// 2. In this exact case c will be null
	public void question8(){

		Console c = System.console();
		char[] pass = c.readPassword("password: ");
	}


	//1. abstract Stream<T> skip(long n) - Returns a stream consisting of the remaining elements of this stream after
	// discarding the first n elements of the stream.
	//2. abstract Stream<T> limit(long maxSize) - Returns a stream consisting of the elements of this stream,
	// truncated to be no longer than maxSize in length.
	//3. abstract long count() - Returns the count of elements in this stream.
	//4. In this case skip(3) will remove first 3 element and limit 2 will take only first 2 elements
	public void question9(){

		Stream ints = Stream.of(1, 2, 3, 4, 5, 6);
		//long i = ints.skip(3).limit(2).count();
		//int i = ints.skip(1).limit(1).count();
		//System.out.println(i); // 2

		ints.skip(3).limit(2).forEach(System.out::println); // 4 5


	}

	//1. Resolve the given path against this path.
	// If the other parameter is an absolute path then this method trivially returns other.
	// 2. abstract boolean isAbsolute() - Tells whether or not this path is absolute.
	// An absolute path is complete in that it doesn't need to be combined with other path information in order to
	// locate a file
	public void question10(){

		Path p1 = Paths.get("in\\new");
		Path p2 = Paths.get("file.txt");
		Path p3 = Paths.get("D:\\file.txt");

		boolean abs1 = p1.isAbsolute(); //false
		boolean abs2 = p2.isAbsolute(); //false
		boolean abs3 = p3.isAbsolute(); //true
		System.out.println(p1.resolve(p2)); //in\new\file.txt
		System.out.println(p1.resolve(p3)); //D:\file.txt
	}


	// In general, to process an SQL statement with JDBC, you follow the next steps:
	//1. Establishing connection
	//2. Create a statement
	//3. Execute the query
	//4. Process the ResultSet object
	//5. Close connection
	public void question11(){

		try{
			//1. Establishing connection
			Class.forName("com.mysql.jdbc.Driver");
			Connection con=DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/sonoo","root","root");
			//2. Create a statement
			Statement stmt=con.createStatement();
			//3. Execute the query
			ResultSet rs=stmt.executeQuery("select * from emp");
			//4. Process the ResultSet object
			while(rs.next())
				System.out.println(rs.getInt(1)+"  "+rs.getString(2)+"  "+rs.getString(3));
			//5. Close connection
			con.close();
		}catch(Exception e){ System.out.println(e);}
	}

}
