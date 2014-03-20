package com.tech.threads.executors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ExecutorCallableSubmit {

	public void runApp() throws InterruptedException, ExecutionException {

		ExecutorService executorService = Executors.newFixedThreadPool(10);

		List<Future<Integer>> futureList = new ArrayList<Future<Integer>>();
		
		for (int count = 1; count <= 5; count++) 
		{
			/**
			 * Make Thread 4 sleep for 9 seconds 
			 */
			futureList.add(executorService.submit(getInstanceOfCallable(count, count == 4 ? true : false)));
			System.out.println("Started: " + count);
		}

		System.out.println("Printing List:");
		for (int count = 1; count <= 5; count++) 
		{
			/**
			 * Since get() is not called for Thread 4, all other tasks will be
			 * completed except that. 
			 * 
			 * Method get() when called on Future, waits
			 * for the output and blocks next thread from executing. So we skip
			 * calling get() on Thread 4 and call get() on other threads instead.
			 */
			if(count != 4)
			{
				System.out.println(futureList.get(count - 1).get());
			}
		}
		
		executorService.shutdown();

		/**
		 * This thread (Main) will wait for 4 secs for all threads to exit
		 * before exiting itself.
		 */
		if (executorService.awaitTermination(4, TimeUnit.SECONDS)) {
			System.out.println("All threads done with their jobs");
		}
		System.out.println("Main Thread Exit");

	}

	private Callable<Integer> getInstanceOfCallable(final Integer count,
			final boolean infinite) {
		Callable<Integer> i = new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {

				if (infinite) {
					try {
						System.out.println("Thread Sleeping - START: " + count);
						Thread.sleep(9000);
						System.out.println("Thread Sleeping - END: " + count);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				return count;
			}
		};
		return i;
	}

	public static void main(String[] args) throws ExecutionException {

		try {
			new ExecutorCallableSubmit().runApp();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

/**************************** SAMPLE OUTPUT **********************
* Started: 1
* Started: 2
* Started: 3
* Started: 4
* Started: 5
* Printing List:
* 1
* 2
* Thread Sleeping - START: 4
* 3
* 5
* Main Thread Exit
* Thread Sleeping - END: 4
*	
/**************************** OUTPUT ***********************/
}
