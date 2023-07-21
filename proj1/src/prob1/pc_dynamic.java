package prob1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class pc_dynamic {
    private static int NUM_END = 200000;
    private static int NUM_THREADS = 32;
    private static int TASK_SIZE = 10;

    public static void main(String[] args) {
        AtomicInteger atomic_counter = new AtomicInteger(0);
        AtomicInteger atomic_number = new AtomicInteger(0);
        ArrayList<Thread> mThread = new ArrayList<>();

        System.out.println("The Program is starting . . .\n");
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < NUM_THREADS; i++){
            mThread.add(new MyThread(atomic_counter, atomic_number));
        }
        for (int i = 0; i < NUM_THREADS; i++){
            mThread.get(i).start();
        }
        for (int i = 0; i < NUM_THREADS; i++){
            try{
                mThread.get(i).join();
            }catch (Exception e){
                System.out.println("error");
            }
        }

        long endTime = System.currentTimeMillis();
        long timeDiff = endTime - startTime;

        System.out.println("\nProgram Execution Time: " + timeDiff + "ms");
        System.out.println("1..." + (NUM_END-1) + " prime# counter=" + atomic_counter);
    }

    private static boolean isPrime(int x){
        int i;
        if (x<=1) return false;
        for (i=2; i<x; i++){
            if (x%i == 0) return false;
        }
        return true;
    }

    static class MyThread extends Thread{
        int counter = 0;
        AtomicInteger atomic_counter;
        AtomicInteger atomic_number;

        public MyThread(AtomicInteger atomic_counter, AtomicInteger atomic_number){
            this.atomic_counter = atomic_counter;
            this.atomic_number = atomic_number;
        }
        @Override
        public void run() {
            long startTime = System.currentTimeMillis();

            while(atomic_number.get() < NUM_END){
                int n = atomic_number.addAndGet(TASK_SIZE);
                for(int i = n-10; i < n; i++){
                    if (isPrime(i)){
                        atomic_counter.incrementAndGet();
                        counter += 1;
                    }
                }
            }

            long endTime = System.currentTimeMillis();
            long timeDiff = endTime - startTime;
            System.out.println(getName() + " Execution Time: " + timeDiff + "ms");
            System.out.println(getName() + " prime# counter=" + counter);
        }
    }

}
