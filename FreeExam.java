package myexamcloud;

import java.io.Console;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.time.Period;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import myexamcloud.supportclasses.Class_q14;

public class FreeExam {


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

		//Set set = new TreeSet<>(); // compilation error
		TreeSet set = new TreeSet<>();
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
	//3. abstract long count() - Returns the count of elements in this stream. NOTE that it's a Long, not an int.
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
		}catch(Exception e){ System.out.println(e);
		}
	}


	//1. The derived most class should be caught first, so the correct order is here, otherwise compilation error
	public void question12(){

		try {

			testMethod();

		} catch (ExC exC) {

		} catch (ExB exB){

		} catch (ExA exB){

		}catch (Exception ex){

		}

	}

	public void testMethod() throws ExC {
		throw new ExC();
	}

	class ExA extends Exception {}
	class ExB extends ExA {}
	class ExC extends ExB {}

	//1. The following declaration of abstract class is legal because non-abstract method can be declared inside
	//abstract class
	//2. Abstract class can be instantiated like the following
	public void question13(){

		Abstraction a = new Abstraction() {
			@Override
			public void meth() {
				super.meth();
				System.out.println("Inside overrided");
			}
		};

		a.meth();

	}

	abstract class Abstraction {
		public void meth(){
			System.out.println("Inside abstraction");
		}
	}


	//1. Static parts executes before any non-static parts. So first variable x will be created with value 0, then it
	// will become 15, then 10 and non-static constructor will be the last to change it to 5
	public void question14(){

		Class_q14 class_q14 = new Class_q14();
		System.out.println(Class_q14.x);
	}


	//1. public int read() throws java.io.IOException - Reads a SINGLE character and returns an integer
	// representation of it. So as long as first letter in file is A - it will print 65
	public void question15() throws IOException{

		FileReader fr = new FileReader("D:\\prj\\OCJP\\src\\myexamcloud\\new.txt");
		System.out.println(fr.read());
		fr.close();
	}


	//1. This way of using synchronized keyword is correct because we've synchronized instances before setting up the
	// variables. So only one thread will be setting/reading variables at given moment. So there won't be memory
	// inconsistency.
	public void question16(){

	}

	private int i;
	private int j;

	public double divide(){

		synchronized (this){
			return i/j;
		}
	}

	public void set(int i, int j){

		synchronized (this){
			this.i = i;
			this.j = j;
		}
	}

	//1. ResultSet.first() - Moves the cursor to the first row in this ResultSet object.
	//2. ResultSet.next() - Moves the cursor forward one row from its current position
	//3. ResultSet.getstring(int) - Retrieves the value of the designated column in the current row of this
	// ResultSet object as a String in the Java programming language.
	public void question17() throws ClassNotFoundException, SQLException{

		Class.forName("com.mysql.jdbc.Driver");

		Connection conn = null;
		Statement stmt = null;

		conn = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/sonoo","root","root");
		stmt = conn.createStatement();

		ResultSet rs = stmt.executeQuery("DESC person");
		rs.first(); //moves to the first row
		rs.next();  //moves to the second row
		System.out.println(rs.getString(1)); //prints value of first column second row
	}


	//1. K ceilingKey(K key) of TreeMap returns the least key greater or equal to the given key, so in this case
	// it'll return Three because P goes after O and before T
	public void question18() {

		Map map = new HashMap<>();
		map.put("One", 1);
		map.put("Two", 2);
		map.put("Three", 3);
		map.put("Four", 4);

		TreeMap tMap = new TreeMap(map);
		System.out.println(tMap.ceilingKey("P")); //Three
	}


	//1. Period: int getDays() - Gets the amount of days of this period. So from Period.of(1, 2, 3); - getDays() will
	// return only the days, so it'll return 3
	public void question19() {

		Period period = Period.of(1, 2, 3);
		LocalDate loc = LocalDate.of(2015, 1, 1);
		int days = period.getDays();
		loc = loc.plusDays(days);
		System.out.println(loc); //2015-01-04
	}


	//1. ArrayDeque:
	//add(E e) Inserts the specified element at the end of this deque.
	//offerLast(E e) Inserts the specified element at the end of this deque.
	//offer(E e) Inserts the specified element at the end of this deque.
	//So in ArrayDeque add, offerLast and offer ARE EQUIVALENT
	//2. ArrayDeque:
	//E poll() Retrieves and removes the head of the queue represented by this deque (in other words, the first
	// element of this deque), or returns null if this deque is empty.
	public void question20() {

		ArrayDeque ad = new ArrayDeque<>();
		ad.add(6);
		ad.add(2);
		ad.offerLast(3);
		ad.offer(4);
		ad.poll();
		System.out.println(ad); //2, 3, 4
	}


	//1. We’re only allowed to perform a SINGLE OPERATION that consumes a Stream, otherwise, we’ll get an
	// IllegalStateException exception that states that the Stream has already been operated upon or closed.
	// Operation may contain multiple functions, but after ";" sign the stream is closed
	//2. To use the stream multiple times we can use the Supplier functional interface like we do below
	//3. Stream:
	// 3.1 Stream<T> sorted() - Returns a stream consisting of the elements of this stream, sorted
	// according to natural order.
	// 3.2 Stream<T> peek(java.util.function.Consumer<? super T> action) - Returns a stream consisting of the
	// elements of this stream, additionally performing the provided action on each element as
	// elements are consumed from the resulting stream. This method exists mainly to support debugging, where you
	// want to see the elements as they flow past a certain point in a pipeline, like .peek(System.out::println) will
	// print all the elements to the console
	// 3.3 java.util.Optional<T> findFirst() - Returns an Optional describing the first element of this stream,
	// or an empty Optional if the stream is empty. If the stream has no encounter order, then any element may be
	// returned. NOTE: streamSupplier.get().peek(it -> System.out.println(it)).findFirst(); - will print only the
	// first element of the stream and not all of it
	public void question21() {

		Stream ints = Stream.of(3, 6, 0, 4);
		ints.sorted().peek(System.out::println).findFirst(); //prints 0
		//---------------------------

		Supplier<Stream> streamSupplier = () -> Stream.of(3, 6, 0, 4);
		List test = new ArrayList<>();
		List another = new ArrayList<>();

		test = Arrays.asList(streamSupplier.get().toArray()); //3, 6, 0, 4
		test = Arrays.asList(streamSupplier.get().sorted().toArray()); //0, 3, 4, 6
		test = Arrays.asList(streamSupplier.get().peek(it -> another.add(it)).toArray()); //3, 6, 0, 4
		test = Arrays.asList(streamSupplier.get().findFirst()); //Optional[3]

		//------
		streamSupplier.get().peek(it -> System.out.println(it)).findFirst(); //3
		Stream.of("one", "two", "three", "four")
		      .filter(e -> e.length() > 3)
		      .peek(e -> System.out.println("Filtered value: " + e))
		      .map(String::toUpperCase)
		      .peek(e -> System.out.println("Mapped value: " + e))
		      .collect(Collectors.toList());

	}


	//1. Files.readAllLines() method returns List<String> so it can't be directly assigned to Stream. To make the
	// Stream from the List just call .stream() method on it.
	//2. Stream:
	//Stream<T> limit(long maxSize) - Returns a stream consisting of the elements of this stream,
	// truncated to be no longer than maxSize in length. maxSize can be bigger than size of the stream - no errors.
	public void question22() throws IOException{

		try{
			Path path = Paths.get("D:\\prj\\OCJP\\src\\myexamcloud\\input.txt");
			//Stream<String> stream = Files.readAllLines(path); COMPILATION ERROR
			Stream<String> stream = Files.readAllLines(path).stream();
			stream.limit(1).forEach(System.out::println);
		}catch (IOException ex){
			System.out.println(ex.getMessage());
		}
	}

	//1. You may or may not specify variable type as parameter in lambda expression, so:
	// it -> System.out.print(it * 2)
	//is the same as
	// (Integer it) -> System.out.print(it * 2)
	public void question23() throws IOException {

		Stream<Integer> ints = Stream.of(1, 2, 3);
		ints.forEach(it -> System.out.print(it * 2));

		Stream<Integer> ints1 = Stream.of(1, 2, 3);
		ints.forEach((Integer it) -> System.out.print(it * 2));
	}


	//1. To use methods from Collections class, your classes has to implement Comparable<T> interface. This interface
	// imposes a total ordering on the objects of each class that implements it. This ordering is referred to as the
	// class's natural ordering, and the class's compareTo method is referred to as its natural comparison method.
	//2. With implementing this method you should override int compareTo(T) method. So compareTo method takes an
	// Object and returns int.
	public void question24() throws IOException {

		Student st1 = new Student("Alex", 32);
		Student st2 = new Student("Alex", 31);
		Student st3 = new Student("Alex", 29);
		Student st4 = new Student("Alex", 33);
		List<Student> list = new ArrayList<>();
		list.add(st1);
		list.add(st2);
		list.add(st3);
		list.add(st4);
		Collections.sort(list); //will sort the list by age
	}

	class Student implements Comparable<Student>{
		String name;
		int age;
		public Student(String name, int age){
			this.name = name;
			this.age = age;
		}

		@Override
		public int compareTo(Student student) {
			if(age == student.age)
				return 0;
			else if(age > student.age)
				return 1;
			else
				return -1;
		}
	}

	//1. MonthDay is an immutable date-time object that represents the combination of a month and day-of-month. Any field
	// that can be derived from a month and day, such as quarter-of-year, can be obtained.
	// This class does not store or represent a year, time or time-zone. For example, the value "December 3rd" can be
	// stored in a MonthDay. Since a MonthDay does not possess a year, the leap day of February 29th is considered
	// valid.
	// There are 2 implementations of MonthDay.of:
	// static MonthDay of(int month, int dayOfMonth)
	// static MonthDay of(@NotNull java.time.Month month, int dayOfMonth)
	public void question25()  {

		MonthDay monthDay1 = MonthDay.of(2, 14);
		MonthDay monthDay2 = MonthDay.of(Month.FEBRUARY, 14);
	}


	//1. public static final class Builder - it's a static nested class inside Locale. It has different methods like
	// setLanguage, setRegion etc. The class itself is static nested, but the variables and methods inside are
	// non-static, so to use them you're calling Locale.Builder() constructor. What those methods does - they're
	// setting fields of private variable of type InternalLocaleBuilder and inside build() method getting an instance
	// of Locale by calling getInstance method of Locale from passed InternalLocaleBuilder variable.
	//2. String getDisplayLanguage(Locale inLocale) Returns a name for the locale's language that is appropriate for
	// display to the user. If possible, the name returned will be localized according to inLocale. For example,
	// if the locale is fr_FR and inLocale is en_US, getDisplayLanguage() will return "French";
	// if the locale is en_US and inLocale is fr_FR, getDisplayLanguage() will return "anglais".
	public void question26()  {

		Locale ENUS = new Locale.Builder().setLanguage("en").setRegion("US").build();
		System.out.println(ENUS.getDisplayLanguage(new Locale("fr"))); //anglais
	}


	//1. Statement - The object used for executing a static SQL statement and returning the results it produces.
	//2. PreparedStatement extends Statement - An object that represents a precompiled SQL statement. A SQL statement
	// is precompiled and stored in a PreparedStatement object. This object can then be used to efficiently execute
	// this statement multiple times.
	//3. CallableStatement extends PreparedStatement - The interface used to execute SQL stored procedures. The JDBC
	// API provides a stored procedure SQL escape syntax that allows stored procedures to be called in a standard way
	// for all RDBMSs. This escape syntax has one form that includes a result parameter and one that does not.
	// If used, the result parameter must be registered as an OUT parameter. The other parameters can be used for
	// input, output or both. Parameters are referred to sequentially, by number, with the first parameter being 1.
	//3.1 registerOutParameter - Registers the OUT parameter in ordinal position parameterIndex to the JDBC type
	// sqlType. All OUT parameters must be registered before a stored procedure is executed.
	//3.2 callableStatement.getString -  Retrieves the value of the designated JDBC CHAR, VARCHAR, or LONGVARCHAR
	// parameter as a String in the Java programming language.
	public void question27()  {

		try{
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/sonoo","root","root");

			Statement stmt = con.createStatement();
			PreparedStatement preparedStmt = con.prepareStatement("select * from emp");
			//---------
			CallableStatement callableStatement;
			String getDBUSERByUserIdSql = "{call getDBUSERByUserId(?,?,?,?)}";
			callableStatement = con.prepareCall(getDBUSERByUserIdSql);
			callableStatement.setInt(1, 10);
			callableStatement.registerOutParameter(2, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(3, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(4, java.sql.Types.DATE);

            // execute getDBUSERByUserId store procedure
			callableStatement.executeUpdate();

			String userName = callableStatement.getString(2);
			String createdBy = callableStatement.getString(3);
			Date createdDate = callableStatement.getDate(4);

		}catch(Exception e){
			System.out.println(e);
		}
	}


	//String 6d passed into Double.parseDouble returns 6.0 double
	public void question28()  {

		double d = 0;
		try{
			d = Double.parseDouble("6d");
			System.out.println(d);
		}catch (NumberFormatException ex){
			System.out.println(ex);
		}
	}


	//1. ResultSet executeQuery(String sql) - Executes the given SQL statement, which returns a single ResultSet
	// object. Typically used for SELECT statements. Can not be used for Data Manipulation commands (DML)
	//2. boolean execute(String sql) - Executes any SQL statement. Can be used for DML. ou must then use the methods
	// getResultSet or getUpdateCount to retrieve the result, and getMoreResults to move to any subsequent result(s).
	public void question29() throws SQLException, ClassNotFoundException  {

		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = null;
		Statement stmt = null;
		String sql;

		conn = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/sonoo","root","root");
		stmt = conn.createStatement();

		ResultSet rs = stmt.executeQuery("DESC person");
		sql = "create database db";
		System.out.println(stmt.executeQuery(sql)); //an EXCEPTION
		//-------------------------
		System.out.println(stmt.execute(sql)); //CORRECT
		stmt.execute(sql);
		ResultSet res = stmt.getResultSet();
	}

	//1. When dividing by zero an ArithmeticException is thrown. In general it's thrown when an exceptional arithmetic
	// condition has occurred.
	public void question30()  {

		try{
			int i = 5 / 0;
		} catch (ArithmeticException ex){
			System.out.println(ex.getMessage());
		}
	}

}
