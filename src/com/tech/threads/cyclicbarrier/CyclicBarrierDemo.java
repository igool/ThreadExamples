package com.tech.threads.cyclicbarrier;

import java.util.concurrent.CyclicBarrier;

/**
 * It is a barrier that all threads must wait at, until all threads reach it,
 * before any of the threads can continue.
 * 
 * You can reuse CyclicBarrier by calling reset() method which resets Barrier to
 * its initial State.
 */
public class CyclicBarrierDemo {

	public static void main(String[] args) {

		Runnable barrier1Action = new Runnable() {
			public void run() {
				System.out.println("BarrierAction 1 executed ");
			}
		};
		Runnable barrier2Action = new Runnable() {
			public void run() {
				System.out.println("BarrierAction 2 executed ");
			}
		};

		CyclicBarrier barrier1 = new CyclicBarrier(2, barrier1Action);
		CyclicBarrier barrier2 = new CyclicBarrier(2, barrier2Action);

		CyclicBarrierRunnable barrierRunnable1 = new CyclicBarrierRunnable(
				barrier1, barrier2);

		CyclicBarrierRunnable barrierRunnable2 = new CyclicBarrierRunnable(
				barrier1, barrier2);

		new Thread(barrierRunnable1).start();
		new Thread(barrierRunnable2).start();
	}

}

/**
 * Output(May change based on thread sequencing):
 * 
 * Thread-0 waiting at barrier 1 
 * Thread-1 waiting at barrier 1 
 * BarrierAction 1 executed 
 * Thread-1 waiting at barrier 2 
 * Thread-0 waiting at barrier 2
 * BarrierAction 2 executed 
 * Thread-0 done! 
 * Thread-1 done! 
 * Thread-0 waiting again at barrier 1 
 * Thread-1 waiting again at barrier 1 
 * BarrierAction 1 executed
 */

