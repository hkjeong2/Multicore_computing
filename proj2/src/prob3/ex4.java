package prob3;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class ex4 {
    public static void main(String[] args) {
        CyclicBarrier[] barriers = new CyclicBarrier[5];
        for (int i = 0; i < barriers.length; i++){
            barriers[i] = new CyclicBarrier(3, barrierAction);
        }

        for(int i = 0; i < 3; i++){
            CyclicBarrierThread cyclicBarrierThread = new CyclicBarrierThread(barriers, i);
            cyclicBarrierThread.start();
        }
    }
    static Runnable barrierAction = new Runnable() {
        @Override
        public void run() {
            System.out.println("All players have succeeded");
        }
    };
}

class CyclicBarrierThread extends Thread{
    CyclicBarrier[] barriers;
    int name;

    CyclicBarrierThread(CyclicBarrier[] barriers, int name){
        this.barriers = barriers;
        this.name = name+1;
    }

    @Override
    public void run() {
        try{
            for (int i = 0; i < barriers.length; i++){
                Thread.sleep(1000);
                System.out.println("P" + name + " with step " + (i+1) + " succeeded");
                barriers[i].await();
            }
            System.out.println("P" + name + " has finished");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
    }
}