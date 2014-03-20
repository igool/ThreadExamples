package com.tech.threads.executors;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * Sample use case of multiple threads.
 *
 */
public class FutureTaskDemo {

	public static final int MAX_NUMBER = 2000000000;

	public static int amountOfDivisibleBy(int first, int last, int divisor) {

		int amount = 0;
		for (int i = first; i <= last; i++) {
			if (i % divisor == 0) {
				amount++;
			}
		}
		return amount;
	}

	public static int amountOfDivisibleByFuture(final int first,
			final int last, final int divisor) throws InterruptedException,
			ExecutionException {

		int amount = 0;

		// Prepare to execute and store the Futures
		int threadNum = 2;
		ExecutorService executor = Executors.newFixedThreadPool(threadNum);
		List<FutureTask<Integer>> taskList = new ArrayList<FutureTask<Integer>>();

		// Start thread for the first half of the numbers
		FutureTask<Integer> futureTask_1 = new FutureTask<Integer>(
				new Callable<Integer>() {
					@Override
					public Integer call() {
						return FutureTaskDemo.amountOfDivisibleBy(first,
								last / 2, divisor);
					}
				});
		taskList.add(futureTask_1);
		executor.execute(futureTask_1);

		// Start thread for the second half of the numbers
		FutureTask<Integer> futureTask_2 = new FutureTask<Integer>(
				new Callable<Integer>() {
					@Override
					public Integer call() {
						return FutureTaskDemo.amountOfDivisibleBy(last / 2 + 1,
								last, divisor);
					}
				});
		taskList.add(futureTask_2);
		executor.execute(futureTask_2);

		/**
		 * Wait until all results are available and combine them at the same
		 * time
		 * 
		 * We go for the “blocking call” using “get” on my Future-objects in a
		 * loop. This will only return once the processing is finished, thus in
		 * this example the first call will probably wait longer and when I
		 * reach the second object processing will be done already and the
		 * result is returned.
		 */
		for (int j = 0; j < threadNum; j++) {
			FutureTask<Integer> futureTask = taskList.get(j);
			amount += futureTask.get();
		}
		executor.shutdown();

		return amount;
	}

	public static void main(String[] args) throws InterruptedException,
			ExecutionException {

		// Sequential execution
		long timeStart = Calendar.getInstance().getTimeInMillis();
		int result = FutureTaskDemo.amountOfDivisibleBy(0, MAX_NUMBER, 3);
		long timeEnd = Calendar.getInstance().getTimeInMillis();
		long timeNeeded = timeEnd - timeStart;
		System.out.println("Result         : " + result + " calculated in "
				+ timeNeeded + " ms");

		// Parallel execution
		long timeStartFuture = Calendar.getInstance().getTimeInMillis();
		int resultFuture = FutureTaskDemo.amountOfDivisibleByFuture(0,
				MAX_NUMBER, 3);
		long timeEndFuture = Calendar.getInstance().getTimeInMillis();
		long timeNeededFuture = timeEndFuture - timeStartFuture;
		System.out.println("Result (Future): " + resultFuture
				+ " calculated in " + timeNeededFuture + " ms");
	}
	
	/******************************** OUTPUT *********************************** 
	 * Result : 666666667 calculated in 12455 ms 
	 * Result (Future): 666666667 calculated in 8140 ms
	 * 
	 * Note: The second is faster because we split work in 2 sets and execute in
	 * parallel.
	 * 
	 **************************************************************************/
}