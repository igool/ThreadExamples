package com.tech.threads.cyclicbarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierRunnable implements Runnable {

	CyclicBarrier barrier1 = null;
	CyclicBarrier barrier2 = null;

	public CyclicBarrierRunnable(CyclicBarrier barrier1, CyclicBarrier barrier2) {

		this.barrier1 = barrier1;
		this.barrier2 = barrier2;
	}

	public void run() {
		try {
			Thread.sleep(1000);
			System.out.println(Thread.currentThread().getName()
					+ " waiting at barrier 1");
			barrier1.await();

			Thread.sleep(1000);
			System.out.println(Thread.currentThread().getName()
					+ " waiting at barrier 2");
			barrier2.await();

			System.out.println(Thread.currentThread().getName() + " done!");
			
			barrier1.reset();
			
			Thread.sleep(1000);
			System.out.println(Thread.currentThread().getName()
					+ " waiting again at barrier 1");
			barrier1.await();
		
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			e.printStackTrace();
		}
	}
}
