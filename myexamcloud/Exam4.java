package myexamcloud;

import java.time.Period;
import java.util.Comparator;
import java.util.TreeMap;
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
}
