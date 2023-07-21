package prob1;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class pc_static_block {
    private static int NUM_END = 200000;
    private static int NUM_THREADS = 4;
    private static int NUM_TASKS = NUM_END/NUM_THREADS;

    public static void main(String[] args) {
        AtomicInteger atomic_counter = new AtomicInteger(0);   //atomic variable to count the total number of prime numbers
        ArrayList<Thread> mThread = new ArrayList<>();  //ArrayList to store the threads to start and join

        System.out.println("The Program is starting . . .\n");
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < NUM_THREADS; i++){
            mThread.add(new MyThread(atomic_counter));  //create threads
        }
        for (int i = 0; i < NUM_THREADS; i++){
            mThread.get(i).start(); //start the threads 시작
        }
        for (int i = 0; i < NUM_THREADS; i++){
            try{
                mThread.get(i).join();  //to make sure the commands below start after all the threads are done
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
        int counter = 0;    //the number of prime numbers calculated per thread
        int order;
        int start = 0;  //starting point
        int end = 0;    //end point (determined at compile time)
        int addi = 0;
        AtomicInteger atomic_counter;

        public MyThread(AtomicInteger atomic_counter){
            String[] token = getName().split("-");
            order = Integer.parseInt(token[1]);     //decide the starting and ending points depending on the thread order
            if (NUM_TASKS * NUM_THREADS != NUM_END)         //if (NUM_END / NUM_THREADS) doesn't give an integer,
                addi = NUM_END - NUM_TASKS * NUM_THREADS;   //the first thread takes care of the remaining tasks
            end += order * NUM_TASKS + NUM_TASKS + addi;
            if (order == 0){
                start += 1;
                addi = 0;
            }
            start += order * NUM_TASKS + addi;
            this.atomic_counter = atomic_counter;
        }
        @Override
        public void run() {
            long startTime = System.currentTimeMillis();
            for (int i = start; i < end; i++){
                if (isPrime(i)) {
                    counter++;
                    atomic_counter.incrementAndGet();   //if it is a prime number, increase the atomic variable value
                }
            }
            long endTime = System.currentTimeMillis();
            long timeDiff = endTime - startTime;
            System.out.println(getName() + " Execution Time: " + timeDiff + "ms");
            System.out.println(start + "..." + (end-1) + " prime# counter=" + counter);
        }
    }

}
