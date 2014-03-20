package com.tech.threads.executors;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Refer ExecutorCallableSubmit.java for more details. Similar example but with
 * Callable returned.
 * 
 */
public class ExecutorSubmit {

	public void runApp() throws InterruptedException, ExecutionException {

		ExecutorService executorService = Executors.newFixedThreadPool(10);

		for (int count = 1; count <= 5; count++) {
			executorService.submit(new MyThread(count, count == 4 ? true : false));
			System.out.println("Started: " + count);
		}

		executorService.shutdown();

		if (executorService.awaitTermination(4, TimeUnit.SECONDS)) {
			System.out.println("All threads done with their jobs");
		}
		System.out.println("Main Thread Exit");

	}

	public static void main(String[] args) throws ExecutionException {

		try {
			new ExecutorSubmit().runApp();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	
	class MyThread implements Runnable {
		private int count;
		private boolean infinite;

		MyThread(final int count, boolean infinite) {
			this.count = count;
		    this.infinite = infinite;
		}
		@Override
		public void run() {
			if (infinite) {
				try {
					System.out.println("Thread Sleeping - START: " + count);
					Thread.sleep(5000);
					System.out.println("Thread Sleeping - END: " + count);
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("Count:" + count);

			}
		}
	}
}
