package com.sri.play;

import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CollectionTheadSafetyOptions {
	
	public final static int THREAD_POOL_SIZE = 5;
	public static Map<String, Integer> hashTableMap =  null;
	public static Map<String, Integer> syncronizedHashMap = null;
	public static Map<String, Integer> concurrrentHashMap = null;
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try {
			hashTableMap = new Hashtable<String, Integer>();
			doPerformanceTest(hashTableMap);
		
			syncronizedHashMap = Collections.synchronizedMap(new HashMap<String, Integer>());
			doPerformanceTest(syncronizedHashMap);
			
			concurrrentHashMap = new ConcurrentHashMap<String, Integer>();
			doPerformanceTest(concurrrentHashMap);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * SPAWN 5 different threads to process 100k read/writes parallels
	 * Shows the conccurrent HashMap performs better as compared to Collections.synchronizedMap 
	 * 
	 * @param map
	 * @throws InterruptedException
	 */
	private static void doPerformanceTest(final Map<String, Integer> map) throws InterruptedException{
		
		System.out.println("Test started for: " + map.getClass());
		long averageTime = 0;
		for(int i = 0; i < 10; i++){
			long startTime = System.nanoTime();
			ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
 
			for (int j = 0; j < THREAD_POOL_SIZE; j++) {
				executorService.execute(new Runnable() {
					@SuppressWarnings("unused")
					public void run() {
 
						for (int i = 0; i < 100000; i++) {
							Integer crunchifyRandomNumber = (int) Math.ceil(Math.random() * 550000);
 
							// Retrieve value. We are not using it anywhere
							Integer crunchifyValue =  map.get(String.valueOf(crunchifyRandomNumber));
 
							// Put value
							 map.put(String.valueOf(crunchifyRandomNumber), crunchifyRandomNumber);
						}
					}
				});
			}
 
			// Make sure executor stops
			executorService.shutdown();
 
			// Blocks until all tasks have completed execution after a shutdown request
			executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
 
			long entTime = System.nanoTime();
			long totalTime = (entTime - startTime) / 1000000L;
			averageTime += totalTime;
			System.out.println("5000K entried added/retrieved in " + totalTime + " ms");
		}
			
		
	}

}
