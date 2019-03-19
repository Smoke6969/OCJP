package myexamcloud;

import java.time.Period;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Exam4 {

	public void question1() {

		Period p1 = Period.of(1, 15, 0);
		Period p2 = p1.normalized();
		System.out.println(p2); //P2Y3M
		p2 = p2.minusDays(-1);
		System.out.println(p2); //P2Y3M1D
	}

	//public static <T> .Collector<T, ?, .Optional<T>> minBy(Comparator<? super T> comparator)
	//Returns a Collector that produces the minimal element according to a given Comparator, described as an
	// Optional<T>.
	public void question2() {

		IntStream ints = IntStream.rangeClosed(2, 6);
		System.out.println(ints.boxed().collect(Collectors.minBy(Integer::compare))); //Optional[2]
	}


	//TreeMap sorts map by key, that is why there is this ordering
	public void question3() {

		TreeMap<String, Double> tmap = new TreeMap<>();
		tmap.put("one", 1.0);
		tmap.put("two", 2.0);
		tmap.put("three", 3.0);
		tmap.put("four", 4.0);

		System.out.println(tmap);//{four=4.0, one=1.0, three=3.0, two=2.0}
		tmap.pollFirstEntry();
		System.out.println(tmap);//{one=1.0, three=3.0, two=2.0}
		tmap.pollLastEntry();
		System.out.println(tmap);//{one=1.0, three=3.0}

	}


	public void question4() {

		Stream<Integer> st = Stream.of(1, 2, 3, 1);
		Stream<String> str = st.peek(System.out::print).map(i -> Integer.toString(i));
		str.distinct();
	}

	//Collectors.joining(CharSequence delimiter, CharSequence prefix, CharSequence suffix)
	//Returns a Collector that concatenates the input elements, separated by the specified delimiter, with the
	// specified prefix and suffix, in encounter order.
	//So in the following example 1 - is the delimeter, 2 - prefix, 3 - suffix. That's why it joins prefix to first
	// element and suffix to the last.
	public void question5() {

		Stream<String> stream = Stream.of("A", "B", "C", "D");
		String string = stream.collect(Collectors.joining("1", "2", "3")); //2A1B1C1D3
		System.out.println(string); //2A1B3
	}

	//In the following example the result will be 2.1, because the execution flow is the following:
	//1. apply 1
	//2. compose (1 + .05 = 1.05)
	//3. Double.parseDouble("1.05")
	//4. 1.05 * 2 = 2.1
	public void question6() {

		Function<String, Double> fun = s -> Double.parseDouble(s);
		Double d = fun.compose((String s) -> s + ".05").andThen(s -> s*2).apply("1"); //2.1
		System.out.println(d);
	}
}
