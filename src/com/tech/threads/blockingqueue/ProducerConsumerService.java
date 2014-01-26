package com.tech.threads.blockingqueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * java.util.concurrent.BlockingQueue is a Queue that supports operations that
 * wait for the queue to become non-empty when retrieving and removing an
 * element, and wait for space to become available in the queue when adding an
 * element.
 * 
 * BlockingQueue doesn’t accept null values and throw NullPointerException if
 * you try to store null value in the queue.
 * 
 * BlockingQueue implementations are thread-safe. All queuing methods are atomic
 * in nature and use internal locks or other forms of concurrency control.
 * 
 * http://www.journaldev.com/1034/java-blockingqueue-example-implementing-producer-consumer-problem
 */
public class ProducerConsumerService {
	public static void main(String[] args) {
		// Creating BlockingQueue of size 10
		BlockingQueue<Message> queue = new ArrayBlockingQueue<>(10);
		Producer producer = new Producer(queue);
		Consumer consumer = new Consumer(queue);
		// starting producer to produce messages in queue
		new Thread(producer).start();
		// starting consumer to consume messages from queue
		new Thread(consumer).start();
		System.out.println("Producer and Consumer has been started");
	}
}

/**
 * Output(May change based on thread sequencing):
 * 
 * Producer and Consumer has been started 
 * Produced 1 
 * Produced 2 
 * Consumed 1
 * Produced 3 
 * Produced 4 
 * Consumed 2 
 * Produced 5 
 * Consumed 3 
 * Consumed 4 
 * Consumed 5 
 */
