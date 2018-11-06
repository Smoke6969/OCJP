/*
package myexamcloud;

import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import myexamcloud.supportclasses.Single;
import myexamcloud.supportclasses.package1.ClassFromP1;
import myexamcloud.supportclasses.package2.ClassFromP2;

public class Exam1 {

	//1. When initializing a String array - values in array are null
	//2. You can't put null as value into ConcurrentHashMap due to RUNTIME NullPointerException
	//3. NOTE that HashMap allow nulls, so this code would work fine with HashMap, but not with ConcurrentHashMap
	public void question1(){

		Single single = Single.getInstance();

		String s[] = new String[2];
		s[1] = "B";
		ConcurrentHashMap<String, Integer> cMap = new ConcurrentHashMap<>();
		cMap.put("A", new Integer(1));
		cMap.put(s[0], new Integer(2));
		cMap.put("C", 3);
		cMap.putIfAbsent("D", new Integer(4));

		System.out.println(cMap);
	}

	//1. static Duration ofDays(long days) - can be used with negative numbers like you see below.
	// When Duration.ofDays(-8) is used, then Duration's method toDays returns negative long number, just the same as
	// used in ofDays method (-8 in this case)
	//2. LocalDate:
	// 2.1 LocalDate plusDays(long daysToAdd) - Returns a copy of this LocalDate with the specified number of days
	// added.
	// 2.2 LocalDate minusDays(long daysToSubtract) - Returns a copy of this LocalDate with the specified number of
	// days
	// subtracted.
	// plusDays() and minusDays() works the same as normal arithmetic (+) (-), including negative rules.
	//2.3 LocalDate plus(TemporalAmount amountToAdd) - This returns a LocalDate,
	// based on this one, with the specified amount added. The amount is typically
	// Period but may be any other type implementing the TemporalAmount interface.
	// LocalDate plus(long amountToAdd, TemporalUnit unit) - This returns a LocalDate, based on this one,
	// with the amount in terms of the unit added.
	public void question2(){

		Duration due = Duration.ofDays(-8);
		LocalDate ld = LocalDate.of(2015, 7, 7);

		System.out.println(ld); //2015-07-07
		System.out.println(ld.plusDays(due.toDays())); //2015-06-29
		//System.out.println(ld.plus(due.toDays())); //error
	}

	//1. When you don't specify generic type in functional interface like this - Changer test - you're passing an
	// object, so that's why toLowerCase doesn't work, because it's overloaded with toLowerCase(Locale locale) and
	// gives an error due to incompatible types. But Changer<String> test works with it, because it knows that there
	// is a String.
	public void question3(){

		//Changer test = String::toLowerCase; //Doesn't work
		Changer<String> test = String::toLowerCase;
	}

	interface Changer<T> {
		T change (T t);
	}

	//1. Methods inside interface can have a word abstract, BUT this is OBSOLETE all methods inside interfaces are
	// PUBLIC AND ABSTRACT
	public void question4(){

		Funcational<String, Integer> fun = String::length;
	}

	interface Funcational<K, V> {
		abstract V accept(K t);
	}


	//1. Enum<ChronoUnit> implements TemporalUnit - A standard set of date periods units. This set of units provide
	// unit-based access to manipulate a date, time or date-time. The standard set of units can be extended by
	// implementing TemporalUnit.
	//Can be used for example to get count of seconds in hour - ChronoUnit.HOURS.getDuration().getSeconds();
	public void question5(){

		LocalDate ld = LocalDate.of(2015, 12, 30);
		long c = ChronoUnit.YEARS.between(ld, ld.minusDays(3)); //0
		System.out.println(c);

		System.out.println(ChronoUnit.HOURS.getDuration().getSeconds());
	}


	//1. If a class implements interface - this class is an instance of this interface
	//2. If a class extends class that implements interface - this class is an instance of this interface
	public void question6(){

		I i = new B();
		A a = new B();
		boolean t1 = i instanceof I, t2 = a instanceof I;
		System.out.println(t1 + " " + t2);
	}

	interface I {}

	abstract class A implements I {}

	class B extends A {}


	//1.LocalDate:
	// 1.1 LocalDate withYear(int year) - Returns a copy of this LocalDate with the year altered.
	public void question7(){

		LocalDate date = LocalDate.of(2015, 11, 21);
		date = date.withYear(2014);
		System.out.println(date);
	}


	//1. DateFormat getInstance() - Get a default date/time formatter that uses the SHORT style for both the date and
	// the time. This method doesn't accept arguments.
	//2. DateFormat getDateInstance(int style) - Gets the date formatter with the given formatting style for the
	// default FORMAT locale.
	//2.1 DateFormat getDateInstance() - Gets the date formatter with the default formatting style for the default
	// FORMAT locale.
	//3. When specifying DateFormat.SHORT - the format will be M/d/yy
	//4. When parsing date with DateFormat.SHORT (M/d/yy) the following mechanism works:
	// 4.1 If you parsing the amount of month > than 12 - the rest goes for years, e.g. 13/11/89 become 1/11/90
	// 4.2 If you parsing the amount of days > than 31(30) - the rest goes for month, e.g. 11/32/89 become 12/2/89
	// 4.3 If you parsing the amount of numbers in years > than 2 - it takes last 2 numbers e.g. 11/21/123 become
	// 11/21/23
	public void question8(){

		//DateFormat df = DateFormat.getInstance(DateFormat.SHORT);
		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
		try{
			Date date2 = df.parse("28/11/89"); //1/11/90
			System.out.println(df.format(date2));
		} catch (ParseException pe){
			System.out.println("Parse exception");
		}
	}


	//1.static int variable have default value of 0
	//2. static initialization blocks are executed from top to bottom
	//3. static initialization blocks are executed ONLY ONCE when class is used FIRST time
	//4. non-static initialization blocks are executed EVERY time you created an object
	public void question9(){

		StaticTest.x = 5;

		StaticTest st = new StaticTest();
		StaticTest st1 = new StaticTest();
	}

	static class StaticTest{

		static int x;

		static
		{
			System.out.println("Static initialization block");
		}
		{
			System.out.println("Non-static initialization block");
		}
	}


	//1. Functional interface ToLongBiFunction<T, U> - Represents a function that accepts two arguments and produces a
	// long-valued result. This is the long-producing PRIMITIVE specialization for BiFunction. This is a functional
	// interface whose functional method is applyAsLong(Object, Object).
	//2. Note that it produces primitive, so no methods can be called on it.
	public void question10(){

		ToLongBiFunction<String, Integer> biFunction = (s, i) -> Long.parseLong(s) + i.longValue();
		double d = biFunction.applyAsLong("4", 10);
		System.out.println(d);
	}


	//1. Connection:
	//  java.sql.Statement createStatement arguments:
	// 1.1 createStatement(); - NO ARGUMENTS
	// 1.2 createStatement(int resultSetType, int resultSetConcurrency);
	// 1.3 createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability);
	//-------------------------
	// int resultSetType:
	// ResultSet.TYPE_FORWARD_ONLY - The constant indicating the type for a ResultSet object whose cursor may move only forward.
	// ResultSet.TYPE_SCROLL_INSENSITIVE - The constant indicating the type for a ResultSet object that is scrollable
	// but generally not sensitive to changes to the data that underlies the ResultSet.
	// ResultSet.TYPE_SCROLL_SENSITIVE - The constant indicating the type for a ResultSet object that is scrollable
	// and generally sensitive to changes to the data that underlies the ResultSet.
	//-------------------------
	// int resultSetConcurrency:
	// ResultSet.CONCUR_READ_ONLY - The constant indicating the concurrency mode for a ResultSet object that may NOT be updated.
	// ResultSet.CONCUR_UPDATABLE - The constant indicating the concurrency mode for a ResultSet object that may be updated.
	//-------------------------
	// int resultSetHoldability:
	// ResultSet.HOLD_CURSORS_OVER_COMMIT - The constant indicating that open ResultSet objects with this holdability
	// will remain open when the current transaction is committed.
	// ResultSet.CLOSE_CURSORS_AT_COMMIT - The constant indicating that open ResultSet objects with this holdability
	// will be closed when the current transaction is committed.
	public void question11(){

		try{

			Class.forName("com.mysql.jdbc.Driver");
			Connection con= DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/sonoo","root","root");

			Statement stmt=con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
			                                   ResultSet.CONCUR_UPDATABLE,
			                                   ResultSet.CLOSE_CURSORS_AT_COMMIT);

			//..............

		}catch(Exception e){ System.out.println(e);
		}
	}


	//1. interface DoubleBinaryOperator - Represents an operation upon two double-valued operands and producing a
	// double-valued result. This is the primitive type specialization of BinaryOperator for double.
	// This is a functional interface whose functional method is applyAsDouble(double, double).
	// In current example double compare takes 2 double and returns int, but here it works fine because int can be
	// converted (AND IT IS CONVERTED) to double, but not otherwise.
	public void question12(){

		DoubleBinaryOperator dbo = Double::compare;
		System.out.println(dbo.applyAsDouble(12.9, 12.99));
	}


	// 1. LongStream - This is the long primitive specialization of Stream.
	// 1.1 summaryStatistics() - Returns a LongSummaryStatistics describing various summary data
	// about the elements of this stream.
	// 2. LongSummaryStatistics - A state object for collecting statistics such as count, min, max, sum, and average.
	// 2.1 long getCount() - Returns the count of values recorded.
	// 2.2 void accept(long value) - Adds a ne long value into the summary information
	// 2.3 long getSum() - Returns the sum of values recorded, or zero if no values have been recorded.
	public void question13(){

		LongStream in = LongStream.of(3, 8, 4, 1, 0, 7, 2).sorted();
		LongSummaryStatistics sm = in.skip(2).limit(4).summaryStatistics();
		long count = sm.getCount();
		sm.accept(sm.getCount());
		System.out.println(sm.getSum());
	}


	//1. ZoneId.SHORT_IDS - static final java.util.Map<String, String> SHORT_IDS -
	// A map of zone overrides to enable the short time-zone names to be used. It's FINAL, so don't put values inside.
	//2. abstract class ZoneId - A time-zone ID, such as Europe/Paris.
	// 2.1 static ZoneId of(String zoneId, Map<String, String> aliasMap) Obtains an
	// instance of ZoneId using its ID using a map of aliases to supplement the standard zone IDs.
	//....................
	//3. abstract class ZoneId - A ZoneId is used to identify the rules used to convert between an Instant and a
	// LocalDateTime. This abstract class has two implementations, both of which are IMMUTABLE and THREAD-SAFE.
	// There are two distinct types of ID:
	// 3.1 Fixed offsets - a fully resolved offset from UTC/Greenwich, that uses the same offset for all local
	// date-times
	// 3.2 Geographical regions - an area where a specific set of rules for finding the offset from UTC/Greenwich apply
	public void question14(){

		Map<String, String> zid = ZoneId.SHORT_IDS;
		zid.put("Australia/Sydney", "AET");
		ZoneId z = ZoneId.of("AET", zid);
		System.out.print(z);
	}


	//1. LocalDate:
	//1.1 - public static LocalDate ofYearDay(int year, int dayOfYear) Obtains an instance of LocalDate from a year
	// and day-of-year. In other words - LocalDate.ofYearDay(2015, 1); - means first day of year 2015, or 1st january
	public void question15(){

		LocalDate ld = LocalDate.ofYearDay(2015, 1); //2015-10-27
		System.out.print(ld);
	}


	//1. static final java.io.PrintStream out (System.out) - The "standard" output stream. This stream is
	// already open and ready to accept output data. Typically this stream corresponds to display output or
	// another output destination specified by the host environment or user.
	//2. static final java.io.PrintStream err - The "standard" error output stream. This stream is already open and
	// ready to accept output data.
	//3. static final java.io.InputStream in -  The "standard" input stream. This stream is already open and ready to
	// supply input data. Typically this stream corresponds to keyboard input or another input source
	// specified by the host environment or user.
	public void question16(){

		PrintStream p_stream = System.out;
		PrintStream err_stream = System.err;
		InputStream in_stream = System.in;
	}


	//Interface methods are implicitly public. When you're overriding method from interface, you have to give the
	// method access level which is WIDER OR THE SAME. So in the following overrided print can only be public.
	public void question17(){

	}

	interface A1{
		void print();
	}
	class B1{}
	class C1 extends B1 implements A1{
		@Override
		public void print(){
		}
	}

	//1. Period - A date-based amount of time in the ISO-8601 calendar system, such as '2 years, 3 months and 4 days'.
	//1.1 - Period.ZERO - A constant for a period of zero.
	//1.2 - getUnits() - Gets the set of units supported by this period. The supported units are YEARS, MONTHS and
	// DAYS. They are returned in the order years, months, days.
	public void question18(){

		System.out.println(Period.ZERO.getUnits());
	}


	//1. Wildcard <? extends Number> for List does not mean "list of objects of different types, all of which extend Number".
	// It means "list of objects of a single type which extends Number". Iy is not known at compile time what types
	// will be added to this list so basically you CAN NOT add any valuest to the List<? extends Number>, but you can
	// assign ANY type of list<? extends Number> to it - ArrayList<Long>() or ArrayList<Double>() etc.
	//Practical usage of it is as a parameters to functions, like example below - sum() can take any type of List<?
	// extends Number> without errors.
	public void question19(){

		List<? extends Number> list = new ArrayList<Integer>();
		list = new ArrayList<Long>();
		sum(list);
		list = new ArrayList<Double>();
		sum(list);
		list = new ArrayList<Float>();
		sum(list);
		list = new ArrayList<Number>();
		sum(list);
		list = new ArrayList<BigDecimal>();
		sum(list);

		//list.add(new Integer(10)); //ERROR
	}

	public double sum(List<? extends Number> myList)
	{
		double sum = 0.0;
		for (Number num : myList)
		{
			sum += num.doubleValue();
		}
		return sum;
	}


	//Interface variables are implicitly PUBLIC STATIC FINAL, so you can't change values of it after initialization.
	public void question20(){

	}

	interface I2{
		int j = 1;
		void substract();
	}

	class A20 implements I2{
		public void substract(){
			//System.out.print(j--);// ERROR
		}
	}


	//1. VARIABLES ACCESSED FROM THE INNER CLASS SHOULD BE FINAL OR EFFECTIVELY FINAL. So here variable "u" can not
	// be accessed from inner class because it is not FINAL.
	public static void question21(int u, final int y){

		int x = 10;

		class Cal{
			public void get(){
				int d = x;
				//int d1 = u; //ERROR
				int d2 = y;
			}
		}
		u = x + 1;
	}


	//1. public void forEach(Consumer<? super T> action)
	//2. public interface Consumer<T> - Represents an operation that accepts a single input argument and returns no
	// result. Unlike most other functional interfaces, Consumer is expected to operate via side-effects.
	public void question22(){

		List<Integer> list = new ArrayList<>();
		list.add(1);
		list.add(2);

		list.forEach(x -> x+=x);

	}


	//1. Duration.ofDays(5); - prints PT120H
	//2. Duration.ofHours(5); - prints PT5H
	//3. Duration.ofMinutes(5); - prints PT5M
	//4. Duration.ofSeconds(5); - prints PT5S
	//4.1 Duration.ofSeconds(400); - prints PT6M40S
	//5. Duration.ofMillis(5); - prints PT0.005S
	//6. Duration.ofNanos(5); - prints PT0.000000005S
	//To summarize: it all prints in hours, minutes and seconds. Duration.ofSeconds(400) - is PT6M40S, same when
	// passing other units.
	public void question23(){

		Duration duration = Duration.ofSeconds(400);
		System.out.print(duration);

	}


	//1. char[] readPassword(String fmt, Object... args) Provides a formatted prompt, then reads a password or
	// passphrase from the console with echoing disabled.
	public void question24(){

		Console con = System.console();
		//String s = con.readPassword(); ERROR

	}


	public void question25(){

		Consumer<Integer> con = System.out::print;
		Consumer<Integer> cons = con.andThen(in -> System.out.print(in*2));

		List<Integer> ins = new ArrayList<>();
		ins.add(1);ins.add(2);ins.add(3);
		ins.forEach(cons); //122436
	}


	//1. static LocalDate parse(@NotNull CharSequence text) Obtains an instance of LocalDate from a text string such
	// as 2007-12-03. The string must represent a valid date and is parsed using
	//2. LocalDate public java.time.Month getMonth() - Gets the month-of-year field using the Month enum.
	public void question26(){

		LocalDate date = LocalDate.parse("2014-12-31");
		date = date.plusDays(2);
		System.out.print(date.getYear() + " " + date.getMonth() + " " + date.getDayOfMonth());
	}


	//1. Period ofDays(int days) - Obtains a Period representing a number of days. So this is just days.
	public void question27(){

		Period p = Period.ofDays(3);
		System.out.println(p);
		System.out.println(p.plusDays(403)); //P406D

	}


	//1. IntStream range(int startInclusive, int endExclusive) Returns a sequential ordered IntStream from
	// startInclusive (inclusive) to endExclusive (exclusive) by an incremental step of 1. So IntStream.range(1, 4);
	// returns stream of 1, 2, 3
	//2. IntStream peek(IntConsumer action) Returns a stream consisting of the elements of this
	// stream, additionally performing the provided action on each element as elements are consumed from the
	// resulting stream.
	//3. IntStream.peek() - PEEK is an INTERMEDIATE Operation while FOREACH is a TERMINAL operation, the execution
	// flow is the following:
	// for(int i = startInclusive(1), i < endExclusive(4); i++){
	//      peek(....);
	//      forEach(...);
	// }
	public void question28(){

		IntStream ints = IntStream.range(1, 4);
		ints.peek(s -> System.out.print(s*2)).forEach(System.out::print);
	}


	//1. Optional:
	//1.1 void ifPresent(Consumer<? super T> consumer) - If a value is present, invoke the specified
	// consumer with the value, otherwise do nothing. So in the following case it will print the value if it will
	// more than 11. Because this function is void - you can't invoke any function on it.
	//1.2. public T orElse(T other) - Return the value if present, otherwise return other.
	public void question29(){

		Optional<Integer> ops = Optional.of(12);

		ops.filter(p -> p > 11).ifPresent(System.out::print); //print 12
		int i = ops.filter(p -> p > 11).orElse(100); //return 12
	}


	//1. public class File - An abstract representation of file and directory pathnames. Can read parameters from a
	// file (canRead, canWrite, getPath, getName, exists, length), can set some attributes (setReadable, setReadonly
	// etc.) can do manipulations like delete and rename.
	//2. abstract class FileStore extends Object - Storage for files. A FileStore represents a storage pool, device,
	// partition, volume, concrete file system or other implementation specific means of file storage. You can
	// instantiate it for example like this - FileStore store = Files.getFileStore(currentPath);
	//3. final class Files extends Object - This class consists exclusively of static methods that operate on files,
	// directories, or other types of files. Like copy, delete etc. Remember about IOException and others.
	public void question30(){

		File file = new File("files//new.txt");
		boolean b = file.exists();
		System.out.println(getFileStoreType(file.getPath()));
		//Files.delete(file.toPath().toAbsolutePath());
	}

	public String getFileStoreType(final String path) {
		File diskFile = new File(path);
		if (!diskFile.exists()) {
			diskFile = diskFile.getParentFile();
		}
		Path currentPath = diskFile.toPath().toAbsolutePath();
		if (currentPath.isAbsolute() && Files.exists(currentPath)) {
			try {
				FileStore store = Files.getFileStore(currentPath);
				return store.type();
			} catch (IOException e) {
				return null;
			}
		}
		return null;
	}


	//1. Protected variables can only be accessed through INHERITANCE, and can not be acessed through object
	// reference as below - it causes compilation error.
	public void question31(){

		ClassFromP1 cp1 = new ClassFromP1();
		ClassFromP2 cp2 = new ClassFromP2();

		//cp2.j = 5;
	}


	//To create a singleton class:
	//1. Create a class
	//2. Create an empty PRIVATE/PROTECTED constructor inside
	//3. Create a PRIVATE STATIC field of type from step 1 inside and assign NULL to it
	//4. Create a PUBLIC STATIC getInstance method of type from step 1 inside:
	//5. In getInstance():
	// 5.1 if variable from step 3 is NULL - assign new instance to it
	// 5.2 return variable from step 3
	public void question32(){

		Single single = Single.getInstance();
		Single single2 = Single.getInstance();

		System.out.println(single == single2); //true
	}


	//1. IntStream:
	// 1.1 - IntStream rangeClosed(int startInclusive, int endInclusive) Returns a sequential ordered IntStream from
	// startInclusive (inclusive) to endInclusive (inclusive) by an incremental step of 1.
	//2. public static <T> Collector<T, ?, Map<Boolean, List<T>>> partitioningBy(.Predicate<? super T>
	// predicate) - Returns a Collector which partitions the input elements according to a Predicate, and organizes
	// them into a Map<Boolean, List<T>>. There are no guarantees on the type, mutability, serializability,
	// or thread-safety of the Map returned.
	// So in the example below map will contain 2 lists - one of them will be even and another not even
	public void question33(){

		IntStream ints = IntStream.rangeClosed(3, 6);
		Map<Boolean, List<Integer>> map = ints.boxed().collect(Collectors.partitioningBy(in -> in%2 == 0));
	}


	//1.OptionalInt findAny() - Returns an OptionalInt describing some element of the stream, or an empty OptionalInt
	// if the stream is empty.
	public void question34(){

		IntStream ints = IntStream.of(3,8,4,1,0,7,2).sorted();
		OptionalInt optionalInt = ints.limit(3).findAny();
		int i = optionalInt.getAsInt(); //returns 0
		System.out.println(optionalInt);
	}


	//1. LongStream:
	//1.1 static LongStream range(long startInclusive, long endExclusive) - Returns a sequential ordered
	// LongStream from startInclusive (inclusive) to endExclusive (exclusive) by an incremental step of 1.
	//1.2  LongStream filter(LongPredicate predicate) - Returns a stream consisting of the elements
	// of this stream that match the given predicate. The main thing in this question is that LongPredicate IS NOT
	// THE SAME as Predicate<Long>
	public void question35(){

		LongStream longs = LongStream.range(1,4);
		LongPredicate pre = it -> it % 2 == 0;
		longs.filter(pre).forEach(System.out::print);
	}


	//1. To map String elements to a Stream of Integers we have to use map method:
	// <R> map(java.util.function.Function<? super T, ? extends R> mapper) - Returns a stream consisting of the results
	// of applying the given function to the elements of this stream.
	//2. Integer decode(@NotNull String nm) throws NumberFormatException Decodes a String into an Integer. Accepts
	// decimal, hexadecimal, and octal numbers given by the following grammar:
	//3. int parseInt(@NotNull String s) throws NumberFormatException - Parses the string argument as a signed decimal
	// integer.
	//4. Difference between Integer::decode and Integer::parseInt is in that the Integer::parseInt returns primitive
	// int and Integer::decode returns boxed Integer. Howewer both will work fine with map method because boxing in
	// java is automatic.
	public void question36(){

		Stream<String> strings = Stream.of("1", "2", "3", "4");
		Stream<Integer> integerStream1 = strings.map(Integer::decode);
		Stream<Integer> integerStream2 = strings.map(Integer::parseInt);
	}


	//1. Stream<T> filter(Predicate<? super T> predicate) Returns a stream consisting of the
	// elements of this stream that match the given predicate. This is an intermediate operation.
	// INTERMEDIATE OPERATION RETURN A NEW STREAM. They are always lazy; executing an intermediate operation such as
	// filter() does not actually perform any filtering, but instead creates a new stream that, when traversed,
	// contains the elements of the initial stream that match the given predicate.
	public void question37(){

		Stream<String> strings = Stream.of("OCPJP", "OCAJP", "OCA", "OCP");
		strings.filter(s -> s.endsWith("A")).filter(s -> s.startsWith("OCA")).forEach(System.out::println);

	}


	//1. If you'll create Locale object with just a single string - it will be a language, so getDisplayCountry()
	// will return an empty string in this case
	public void question38(){

		Locale CAN = new Locale.Builder().setLanguage("EN").setRegion("CA").build();
		System.out.println(CAN.getDisplayCountry());

		Locale loc = new Locale("EN");
		System.out.println(loc.getDisplayCountry());
	}


	//1. public class ForkJoinPool extends AbstractExecutorService - An ExecutorService for
	// running ForkJoinTasks. A ForkJoinPool provides the entry point for submissions from non-ForkJoinTask
	//clients, as well as management and monitoring operations.
	//2. public interface ExecutorService extends Executor - An Executor that provides methods to manage termination
	// and methods that can produce a Future for tracking progress of one or more asynchronous tasks.
	//3. public abstract class ForkJoinTask<V>  - Abstract base class for tasks that run within a ForkJoinPool. A
	// ForkJoinTask is a thread-like entity that is much lighter weight than a normal thread. Huge numbers of tasks
	// and subtasks may be hosted by a small number of actual threads in a ForkJoinPool, at the price of some
	// usage limitations.
	//4. public abstract class RecursiveTask<V> extends java.util.concurrent.ForkJoinTask<V> - A recursive
	// result-bearing ForkJoinTask.
	//So basically to use ForkJoinPool you have to create class that extend RecursiveTask<T>, override protected T
	// compute() and pass the object of this class to invoke method of FJP.
	public void question39(){

		ForkJoinPool fPool = new ForkJoinPool();
		Remember rem = new Remember(12, 3);
		int i = fPool.invoke(rem);
		System.out.println(i);
		int j = rem.compute();
		System.out.println(j);
	}

	class Remember extends RecursiveTask<Integer>{

		int num;
		int devi;

		public Remember(int i, int j){
			num = i;
			devi = j;
		}

		@Override
		protected Integer compute() {
			return num%devi;
		}
	}


	//1. interface UnaryOperator<T> extends java.util.function.Function<T, T> - Represents an operation on a single
	// operand that produces a result of the same type as its operand. This is a specialization of Function for the
	// case where the operand and result are of the same type.
	//2. static <T> UnaryOperator<T> identity() - Returns a unary operator that always returns its input argument. So
	// passing the string to its apply method will return same string
	public void question40(){

		UnaryOperator<String> s = UnaryOperator.identity();
		System.out.println(s.apply("1"));

		UnaryOperator<String> i  = UnaryOperator.identity();
		System.out.println(i.apply("java2s.com"));
	}


	//1. In code below Currency.PENNY and Currency.QUARTER are FINAL so you can't assign values there.
	public void question41(){

		//Currency.PENNY = Currency.QUARTER; //ERROR

	}

	public enum Currency{
		PENNY(1), QUARTER(25);
		private int value;

		private Currency(int value){
			this.value = value;
		}
	}


	//1. interface Comparator<T> - A comparison function, which imposes a total ordering on some collection of objects.
	// Comparators can be passed to a sort method (such as Collections.sort or Arrays.sort) to allow precise
	// control over the sort order.
	//1.1 <T> Comparator<T> comparingInt(@NotNull java.util.function.ToIntFunction<? super T> keyExtractor) - Accepts a
	// function that extracts an int sort key from a type T, and returns a Comparator<T> that compares by that sort key.
	//1.2 Comparator<T> thenComparing(@NotNull Comparator<? super T> other) - Returns a lexicographic-order comparator
	// with another comparator. If this Comparator considers two elements equal, i.e. compare(a, b) == 0,
	// other is used to determine the order.
	//1.3 static final java.util.Comparator<String> CASE_INSENSITIVE_ORDER - A Comparator that orders String objects as
	// by compareToIgnoreCase.
	public void question42(){

		List<String> ts = new ArrayList<>();
		ts.add("AA");
		ts.add("AB");
		ts.add("ABC");
		ts.add("B");

		Comparator<String> cmp = Comparator.comparingInt(String::length).thenComparing(String.CASE_INSENSITIVE_ORDER);

		ts.sort(cmp);

		System.out.println(ts.get(0));
		System.out.println(ts.get(1));
		System.out.println(ts.get(2));
		System.out.println(ts.get(3));

	}


	//1. List<? super Number> means the list typed to either Number or superclass of Number which is Object. In this
	// case it is safe to insert subclasses of Number into the list, but to get those elements, we need to cast them
	// into a superclass of Number, so into Object.
	public void question43(){

		List<? super Number> list = new ArrayList<>();

		list.add(new Integer(1));
		list.add(new Double(2));

		System.out.println(new Integer(1) instanceof Number);

		for(Object o : list){
			System.out.println(o);
		}

	}


	//1. In the following example only numbers without fraction part will be printed. Because it is legal to do
	public void question44(){

		DoubleStream dbs = DoubleStream.of(1.0, 2.3, 2);
		System.out.println(new Double(2.3) - new Double(2.3).intValue()); //0.2999999999999998
		dbs.boxed().filter(d -> d - d.intValue() == 0).forEach(System.out::print); //1.02.0

	}


	//1. public static java.sql.Connection getConnection
	// (@NotNull String url,
	// @Nullable String user,
	// @Nullable String password)
	// throws java.sql.SQLException - Attempts to establish a connection to the given
	// database URL. The
	// DriverManager attempts to select an appropriate driver from the set of registered JDBC drivers.
	public void question45(){

		try {
			DriverManager.getConnection("url", "root", "");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}


	//1. IntStream peek(java.util.function.IntConsumer action) - Returns a stream consisting of the elements of this
	// stream, additionally performing the provided action on each element as elements are consumed from the
	// resulting stream.
	//2. IntStream map(java.util.function.IntUnaryOperator mapper) - Returns a stream consisting of the results of
	// applying the given function to the elements of this stream. This is an intermediate operation.
	//3. boolean noneMatch(java.util.function.IntPredicate predicate) - Returns whether no elements of this stream
	// match the provided predicate. May not evaluate the predicate on all elements if not necessary for determining
	// the result. If the stream is empty then true is returned and the predicate is not evaluated.
	//In the following example noneMatch will be true since we've multiplied all elements of the stream into 2, so
	// division on 2 will not leave anything ((x%2 != 0) == false)
	public void question46(){

		IntStream in = IntStream.range(3, 8);
		IntStream ins = in.peek(System.out::print).map(x -> x*2);
		System.out.println(ins.noneMatch(x -> x%2 != 0)); //true

	}


	//1. R collect(java.util.stream.Collector<? super T, A, R> collector) - Performs a mutable reduction operation on
	// the elements of this stream using a Collector. A Collector encapsulates the functions used as arguments to
	// collect(Supplier, BiConsumer, BiConsumer), allowing for reuse of collection strategies and composition of
	// collect operations such as multiple-level grouping or partitioning:
	//1.1  Collector<T, ?, Double> averagingDouble(@Nullable java.util.function.ToDoubleFunction<?
	// super T> mapper) - Returns a Collector that produces the arithmetic mean of a double-valued function applied
	// to the input elements. If no elements are present, the result is 0.
	//1.2 public interface Collector<T, A, R> - A mutable reduction operation that accumulates input elements into a
	// mutable result container, optionally transforming the accumulated result into a final representation after all
	// input elements have been processed. Reduction operations can be performed either sequentially or in parallel.
	public void question47(){

		double d = Stream.of("AA", "AAA", "aAA", "aaa").collect(Collectors.averagingDouble((String s) -> s.length()))
		                 .intValue();
		System.out.println(d);

	}



	//1. public static java.util.stream.Stream<java.nio.file.Path> find(Path start,
	//                                                                  int maxDepth,
	//                                                                  BiPredicate<Path, BasicFileAttributes> matcher,
	//                                                                  FileVisitOption... options) throws java.io.IOException
	// Return a Stream that is lazily populated with Path by searching for files in a file tree rooted at a given
	// starting file. This method walks the file tree in exactly the manner specified by the walk method. For each
	// file encountered, the given BiPredicate is invoked with its Path and BasicFileAttributes.
	public void question48(){

		try {
			Stream<Path> out = Files.find(Paths.get("D:\\prj\\OCJP\\search_test"),
			                              2,
			                              (p, b) -> p.getFileName()
			                                         .toString()
			                                         .startsWith("h"));
			out.forEach(path -> System.out.println(path));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	//1. interface Set<E> extends java.util.Collection<E> - A collection that contains no duplicate elements. More
	// formally, sets contain no pair of elements e1 and e2 such that e1.equals(e2), and at most one null element.
	// As implied by its name, this interface models the mathematical set abstraction.
	//2. public class TreeSet<E> extends AbstractSet<E> implements NavigableSet<E>, Cloneable, java.io.Serializable - A
	// NavigableSet implementation based on a TreeMap. The elements are ordered using their natural ordering, or by a
	// Comparator provided at set creation time, depending on which constructor is used.
	//2.1 public TreeSet() - Constructs a new, empty tree set, sorted according to the natural ordering of its elements
	// . All elements inserted into the set must implement the Comparable interface.
	//--------------
	// To summarize: because Set shouldn't contain duplicate elements, you can't just insert there own classes - you
	// should implement Comparable interface in case you wan't to do that
	public void question49(){

		Set<Person> ts = new TreeSet<>();
		ts.add(new Person("Alex"));
		ts.add(new Person("Sam"));
		ts.add(new Person("Sam"));
		ts.add(new Person("Max"));
		System.out.print(ts.size());
	}

	class Person implements Comparable<Person>{
		public String name;
		public Person(String name){
			this.name = name;
		}


		@Override
		public int compareTo(Person person) {
			return name.compareTo(person.name);
		}
	}

	//1. interface DoubleFunction<R> - Represents a function that accepts a double-valued argument and produces a
	// result. This is the double-consuming primitive specialization for Function.
	//1.1 R apply(double value) - Applies this function to the given argument.
	//2. String valueOf(double d) - Returns the string representation of the double argument.
	public void question50(){

		DoubleFunction<String> df = String::valueOf;
	}


	//1. Locale:
	//1.1 public Locale(@NotNull String language, @NotNull String country) - Construct a locale from language and
	// country. This constructor normalizes the language value to lowercase and the country value to uppercase.
	public void question51(){

		Locale loc = new Locale("fr", "IN");
		Locale loc1 = new Locale.Builder().setLanguage("fr").setRegion("IN").build();

		System.out.println(loc.getDisplayCountry());
		System.out.println(loc.getCountry());
		System.out.println(loc.getDisplayLanguage());
		System.out.println(loc.getLanguage());

		System.out.println(loc1.getDisplayCountry());
		System.out.println(loc1.getCountry());
		System.out.println(loc1.getDisplayLanguage());
		System.out.println(loc1.getLanguage());
	}


	//1. public final class Instant extends Object implements java.time.temporal.Temporal, java.time.temporal
	// .TemporalAdjuster, Comparable<Instant>, java.io.Serializable - An instantaneous point on the time-line. This
	// class models a single instantaneous point on the time-line. This might be used to record event time-stamps in
	// the application.
	//1.1 public static Instant now() - Obtains the current instant from the system clock.
	//1.2 Instant plus(long amountToAdd, @NotNull java.time.temporal.TemporalUnit unit) - Returns a copy of this
	// instant with the specified amount added. This returns an Instant, based on this one, with the amount in terms
	// of the unit added. If it is not possible to add the amount, because the unit is not supported or for some
	// other reason, an exception is thrown.
	//NOTE: ChronoUnit.YEARS - NOT SUPPORTED
	public void question52(){

		Instant instant = Instant.now();
		System.out.println(instant);
		instant = instant.plus(10, ChronoUnit.DAYS);
		System.out.println(instant);
	}


	//1. class BufferedReader extends java.io.Reader - Reads text from a character-input stream, buffering characters
	// so as to provide for the efficient reading of characters, arrays, and lines.
	//2. Files public static BufferedReader newBufferedReader(Path path) throws java.io.IOException - Opens a file for reading,
	// returning a BufferedReader to read text from the file in an efficient manner. Bytes from the file are decoded
	// into characters using the UTF-8 charset.
	public void question53(){

		try {
			BufferedReader reader = Files.newBufferedReader(Paths.get("D:\\prj\\OCJP\\search_test"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	//interface IntFunction<R> - Represents a function that accepts an int-valued argument and produces a result. This
	// is the int-consuming primitive specialization for Function. This is a functional interface whose functional
	// method is apply(int).
	public void question54(){

		IntFunction intFunction1 = Integer::toString;
		IntFunction intFunction2 = in -> in * 2;
	}


	//The following code will produce 7.25 as 29/4 = 7.25
	//1. <T> java.util.stream.Collector<T, ?, Double> averagingInt(@Nullable java.util.function.ToIntFunction<? super
	// T> mapper) - Returns a Collector that produces the arithmetic mean of an integer-valued function applied to the
	// input elements. If no elements are present, the result is 0. In this case it's same as averagingDouble.
	public void question55(){

		Stream<String> stream = Stream.of("12", "13", "3", "1");
		double avg = stream.collect(Collectors.averagingInt(in -> Integer.parseInt(in)));
		System.out.println(avg);
	}


	//1. ConcurrentHashMap
	//public ConcurrentHashMap(int initialCapacity,
	//                         float loadFactor,
	//                         int concurrencyLevel)
	// Creates a new, empty map with an initial table size based on the given number of elements (initialCapacity),
	//table density (loadFactor), and number of concurrently updating threads (concurrencyLevel).
	public void question56(){

		ConcurrentHashMap cMap = new ConcurrentHashMap(100, 0.8f, 15);
	}


	//1. Comparator public static <T, U extends Comparable<? super U>> Comparator<T> comparing(@NotNull java.util
	// .function.Function<? super T, ? extends U> keyExtractor) - Accepts a function that extracts a Comparable sort
	// key from a type T, and returns a Comparator<T> that compares by that sort key.
	public void question57(){

		Comparator<String> c3 = Comparator.comparing(e -> e.length());

		System.out.println(c3.compare("aa", "aaa")); //-1
		System.out.println(c3.compare("aaa", "aa")); //1
		System.out.println(c3.compare("aaa", "aaa")); //0
	}


	// ACCESS MODIFIERS (public, protected, private) ARE NOT ALLOWED INSIDE METHOD, ONLY DEFAULT IS ALLOWED.
	//STATIC ABSTRACT ALSO NOT ALLOWED
	//STATIC IS ALLOWED (IF NOT ABSTRACT)
	public void question58(){

		abstract class A{
			public abstract int calc(int x);
		}

		A a = new A() {
			@Override
			public int calc(int x) {
				return y58 * x;
			}
			public void print(int x){
				System.out.println(calc(x));
			}
		};

		System.out.println(a.calc(2));
	}

	static int y58;


	//1. interface IntUnaryOperator - Represents an operation on a single int-valued operand that produces an
	// int-valued result. This is the primitive type specialization of UnaryOperator for int.
	public void question59(){

		IntUnaryOperator operator = primitive -> primitive * 2;
	}


	//System.out.println if passed arithmetic operation, first does the operation and only after that prints
	public void question60(){

		System.out.println(3 + 5); //8
	}


	//In the following example Set will contain 2 objects of Man, because hashCode returns an age, so 2 object with
	// the same name but different age are different, so it is added to Set fine.
	public void question61(){

		HashSet<Man> set = new HashSet<>();
		set.add(new Man("Sanka", 22));
		set.add(new Man("Sanka", 24));
		System.out.println(set.size());
	}

	class Man{

		private String name;
		private int age;

		public Man(String name, int age) {
			this.name = name;
			this.age = age;
		}

		public String getName() {
			return name;
		}

		public int getAge() {
			return age;
		}

		@Override
		public boolean equals(Object o) {
			return ((Man) o).getName().equals(this.name);
		}

		@Override
		public int hashCode() {
			return age;
		}
	}


	public void question62(){

		List<String> list = new ArrayList<>();
		list.add("A");
		list.add("C");
		list.add("E");
		list.add("D");

		list.add(1, "B");
		list.add(4, "F");

		System.out.println(list);

	}
}
*/
