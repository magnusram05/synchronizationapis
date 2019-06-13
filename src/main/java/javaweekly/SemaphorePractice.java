package javaweekly;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class SemaphorePractice {
    /**
     * 1) No. of slots is limited
     * 2) Vehicles will have to wait till slots free up
     */

    /*
     * 4 tasks, 3 permits
     * acquire, sleep, release
     * */
    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(3);
        List<Callable<String>> callables = new ArrayList<>();
        Semaphore semaphore = new Semaphore(3);
        for (int i = 1; i < 5; i++) {
            callables.add(new Vehicles(String.valueOf(i), semaphore));
        }
        try {
            service.invokeAll(callables);
        } catch (InterruptedException ex) {
        }
        service.shutdown();
    }

    static class Vehicles implements Callable<String> {
        Semaphore semaphore;
        ReentrantLock reentrantLock;
        String name;

        Vehicles(String name, Semaphore semaphore) {
            this.name = name;
            this.semaphore = semaphore;
        }

        public String call() {
            try {
                semaphore.acquire();
                System.out.println("Vehicle " + name + ": acquired permit");
                Thread.sleep(3000);
                semaphore.release();
                System.out.println("Vehicle " + name + ": released permit");
            } catch (InterruptedException ex) {}
            return "Vehicle:"+name;
        }
    }
}
