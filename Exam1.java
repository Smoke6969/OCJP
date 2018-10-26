package myexamcloud;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.DoubleBinaryOperator;
import java.util.function.LongPredicate;
import java.util.function.ToLongBiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import myexamcloud.supportclasses.Single;
import myexamcloud.supportclasses.package1.ClassFromP1;
import myexamcloud.supportclasses.package2.ClassFromP2;


public class Exam1 {

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
		long c = ChronoUnit.YEARS.between(ld, ld.minusDays(3));
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
			Date date2 = df.parse("13/11/89"); //1/11/90
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
			//j--; ERROR
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

}
