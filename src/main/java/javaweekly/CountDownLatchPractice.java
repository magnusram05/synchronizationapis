package javaweekly;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
Cache A depends on Cache B and C.
Mark Cache A as completed only after Cache B and C have been loaded.

Synchronization aid

Employee
 -name

 Spawn: Thread1, 2 and 3 and each one is responsible for building a cache

 CacheA depends on CacheB and CacheC

 Thread1 --> CacheA
 Thread2 --> CacheB
 Thread3 --> CacheC
*/


public class CountDownLatchPractice {

    public static void main(String[] args) {

        Map<String, String> cacheA = new ConcurrentHashMap<>();
        Map<String, String> cacheB = new ConcurrentHashMap<>();
        Map<String, String> cacheC = new ConcurrentHashMap<>();

        ExecutorService service = Executors.newFixedThreadPool(2);

        CountDownLatch cl = new CountDownLatch(2);
        service.submit(new CacheLoadingTask(cl, cacheB));
        service.submit(new CacheLoadingTask(cl, cacheC));
        try {
            cl.await(); //blocking operation
            cacheA.put(String.valueOf(cl.getCount()), String.valueOf(cl.getCount()));
        } catch (InterruptedException ex) {
        }
        System.out.println("Get count of latches: " + cl.getCount());
        System.out.println("Built Cache A");

        System.out.println("Size of Cache A: " + cacheA.size());
        System.out.println("Size of Cache B: " + cacheB.size());
        System.out.println("Size of Cache C: " + cacheC.size());

        service.shutdownNow();
    }

    static class CacheLoadingTask implements Runnable {
        CountDownLatch cl;
        Map<String, String> cache;

        CacheLoadingTask(CountDownLatch cl, Map<String, String> cache) {
            this.cl = cl;
            this.cache = cache;
        }

        public void run() {
            try {
                Thread.sleep(1000);
                cache.put(String.valueOf(cl.getCount()), String.valueOf(cl.getCount()));
                System.out.println("Thread Name: " + Thread.currentThread().getName());
                this.cl.countDown();
            } catch (InterruptedException ex) {
            }
        }
    }
}
