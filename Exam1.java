package myexamcloud;

import java.text.DateFormat;
import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

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
}
