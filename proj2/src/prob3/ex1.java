package prob3;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ex1 {
    public static void main(String[] args) {
        BlockingQueue queue = new ArrayBlockingQueue<String>(5);

        Producer producer = new Producer(queue);
        Consumer consumer = new Consumer(queue);

        producer.start();
        consumer.start();

    }
}

class Producer extends Thread{

    private BlockingQueue<String> queue;

    Producer(BlockingQueue blockingQueue){
        this.queue = blockingQueue;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++){
            try{
                sleep((int)(Math.random() * 100));
                System.out.println("Food " + i + " has been cooked");
                queue.put("Food " + i);
            }catch (InterruptedException e){}
        }
    }
}

class Consumer extends Thread{

    private BlockingQueue<String> queue;

    Consumer(BlockingQueue blockingQueue){
        this.queue = blockingQueue;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++){
            try{
                sleep((int)(Math.random() * 150));
                String n = queue.take();
                System.out.println("\t" + n + " has been eaten");
            }catch (InterruptedException e){}
        }
    }
}