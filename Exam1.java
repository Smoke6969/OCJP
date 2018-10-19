package myexamcloud;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.DoubleBinaryOperator;
import java.util.function.Supplier;
import java.util.function.ToLongBiFunction;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

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
}
