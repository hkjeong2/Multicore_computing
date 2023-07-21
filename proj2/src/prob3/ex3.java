package prob3;

import java.util.concurrent.atomic.AtomicInteger;

public class ex3 {
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(0);

        Producer3 producer = new Producer3(atomicInteger);
        Consumer3 consumer = new Consumer3(atomicInteger);
        producer.start();
        consumer.start();
    }
}

class Producer3 extends Thread{
    AtomicInteger atomicInteger;

    Producer3(AtomicInteger atomicInteger){
        this.atomicInteger = atomicInteger;
    }

    @Override
    public void run() {
        while (true){
            try{
                sleep((int)(Math.random() * 2000));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Setting the value to 100 ...");
            atomicInteger.set(100);
            int n = atomicInteger.get();
            System.out.println("value = " + n);
        }
    }
}

class Consumer3 extends Thread{
    AtomicInteger atomicInteger;

    Consumer3(AtomicInteger atomicInteger){
        this.atomicInteger = atomicInteger;
    }

    @Override
    public void run() {
        while (true){
            try{
                sleep((int)(Math.random() * 1000));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            int n = atomicInteger.getAndAdd(3);
            System.out.println("value before adding 3 = " + n);
            System.out.println("value after adding 3 = " + atomicInteger.get());

            System.out.println("value before adding 2 = " + atomicInteger.get());
            n = atomicInteger.addAndGet(2);
            System.out.println("value after adding 2 = " + n);
        }
    }
}

