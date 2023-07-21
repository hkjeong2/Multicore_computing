package prob1;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class pc_static_cyclic {
    private static int NUM_END = 200000;
    private static int NUM_THREADS = 4;
    private static int TASK_SIZE = 10;

    public static void main(String[] args) {
        AtomicInteger atomic_counter = new AtomicInteger(0);
        ArrayList<Thread> mThread = new ArrayList<>();

        System.out.println("The Program is starting . . .\n");
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < NUM_THREADS; i++){
            mThread.add(new MyThread(atomic_counter));
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
        int num = 1;
        int order;
        AtomicInteger atomic_counter;

        public MyThread(AtomicInteger atomic_counter){
            String[] token = getName().split("-");
            order = Integer.parseInt(token[1]);
            num += TASK_SIZE * order;
            this.atomic_counter = atomic_counter;
        }
        @Override
        public void run() {
            long startTime = System.currentTimeMillis();
            int idx = 1;
            while (num < NUM_END){
                if (isPrime(num)) {
                    counter++;
                    atomic_counter.incrementAndGet();
                }
                if (idx == TASK_SIZE){   //update the range of numbers to check after calculating the numbers of TASK_SIZE
                    num = num - TASK_SIZE + 1 + NUM_THREADS * TASK_SIZE;
                    idx = 1;
                }else{
                    num += 1;
                    idx += 1;
                }
            }
            long endTime = System.currentTimeMillis();
            long timeDiff = endTime - startTime;
            System.out.println(getName() + " Execution Time: " + timeDiff + "ms");
            System.out.println(getName() + " prime# counter=" + counter);
        }
    }

}
