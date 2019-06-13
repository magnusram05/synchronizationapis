package javaweekly;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CyclicBarrierPractice {
    /* 1. Signal will wait for 5 vehicles before turning green
    *  2. It will alternate between red and green
    */

    public static void main(String[] args) {
        Signal signal = new Signal();
        CyclicBarrier cb = new CyclicBarrier(5, () -> {
            signal.flip();
            System.out.println("Signal turned: " + signal.state);
        });
        ExecutorService service = Executors.newFixedThreadPool(5);
        for(int i=1; i< 6; i++){
            service.submit(new Vehicle(cb));
        }
    }

    static class Signal {
        private String state = "RED";

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public void flip(){
            this.state = (this.state == "GREEN" ? "RED" : "GREEN");
        }
    }

    static class Vehicle implements Runnable {
        CyclicBarrier cb;
        Vehicle(CyclicBarrier cb){
            this.cb = cb;
        }
        public void run(){
            while(true){
                try {
                    Thread.sleep(1000);
                    cb.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
