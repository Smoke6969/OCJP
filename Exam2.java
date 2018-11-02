package myexamcloud;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.function.BiPredicate;
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

}
