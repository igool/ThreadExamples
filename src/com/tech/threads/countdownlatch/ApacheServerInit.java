package com.tech.threads.countdownlatch;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * CountDownLatch (alternative to wait and notify mechanism) in Java is a kind
 * of synchronizer which allows one Thread to wait for one or more Threads
 * before starts processing. Introduced on Java 5 along with other concurrent
 * utilities like CyclicBarrier, Semaphore, ConcurrentHashMap and BlockingQueue
 * in java. A disadvantage of CountDownLatch is that its not reusable once count
 * reaches to zero you can not use CountDownLatch any more. The alternative is
 * another concurrent utility called CyclicBarrier.
 *
 * Program:
 * The Apache server should not start processing any thread until all services
 * are up and ready to do there job. Countdown latch is ideal choice here as
 * main thread will start with count 2 and wait until count reaches zero. Each
 * thread once up will perform a count down. This will ensure that main thread
 * does not started processing until all services is up.
 * 
 * @author salil
 * 
 */
public class ApacheServerInit {
	
	public static void main(String args[]) {
		final CountDownLatch latch = new CountDownLatch(2);
		
		System.out.println("Apache services initialization - In progress");

		Thread tomcat01Service = new Thread(new Service("Tomcat_01_Service", 5000, latch));
		Thread tomcat02Service = new Thread(new Service("Tomcat_02_Service", 5000, latch));
		
		/**
		 * Initiate each service in it's own separate/independent thread
		 */
		tomcat01Service.start();
		tomcat02Service.start();
		
		try 
		{
			/**
			 * The main thread is waiting on CountDownLatch to finish before
			 * further processing.
			 */
			latch.await(); 
			System.out.println("Apache services initialization - Completed");
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
	}
}

/**
 * Class which will be executed by Thread using CountDownLatch synchronizer.
 */
class Service implements Runnable {
	private final String name;
	private final int timeToStart;
	private final CountDownLatch latch;

	public Service(String name, int timeToStart, CountDownLatch latch) {
		this.name = name;
		this.timeToStart = timeToStart;
		this.latch = latch;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(timeToStart);
		} catch (InterruptedException ex) {
			Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		System.out.println(name + " is Up");
		latch.countDown(); /* Reduce count of CountDownLatch by 1 */
	}
}

/*************** OUTPUT [Here services can be up in any order due to threads] *****************************
Apache services initialization - In progress
Tomcat_02_Service is Up
Tomcat_01_Service is Up
Apache services initialization - Completed
***********************************************************************************************************/