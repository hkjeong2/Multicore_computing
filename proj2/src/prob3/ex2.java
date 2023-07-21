package prob3;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ex2 {
    public static void main(String[] args) {
        ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        Reader[] readers = new Reader[5];
        Writer[] writers = new Writer[5];

        for (int i = 0 ; i < 5; i++){
            readers[i] = new Reader(readWriteLock, i);
            writers[i] = new Writer(readWriteLock, i);
            readers[i].start();
            writers[i].start();
        }

    }
}

class Reader extends Thread{
    ReadWriteLock readWriteLock;
    int name;

    Reader(ReadWriteLock readWriteLock, int name){
        this.readWriteLock = readWriteLock;
        this.name = name;
    }

    @Override
    public void run() {
        while(true){
            try {
                sleep((int)(Math.random() * 2000));
            } catch (InterruptedException e) {}
            System.out.println("ReadLock : " + name + " is accessing...");
            readWriteLock.readLock().lock();
            System.out.println("ReadLock : " + name + " has acquired the lock...");
            try {
                sleep((int)(Math.random() * 2000));
            } catch (InterruptedException e) {}
            System.out.println("\t\tReadLock : " + name + " is about to release the lock...");
            readWriteLock.readLock().unlock();
            System.out.println("\t\tReadLock : " + name + " has released the lock...");
        }
    }
}

class Writer extends Thread{
    ReadWriteLock readWriteLock;
    int name;

    Writer(ReadWriteLock readWriteLock, int name){
        this.readWriteLock = readWriteLock;
        this.name = name;
    }

    @Override
    public void run() {
        while(true){
            try {
                sleep((int)(Math.random() * 10000));
            } catch (InterruptedException e) {}
            System.out.println("WriteLock : " + name + " is accessing...");
            readWriteLock.writeLock().lock();
            System.out.println("WriteLock : " + name + " has acquired the lock...");
            try {
                sleep((int)(Math.random() * 10000));
            } catch (InterruptedException e) {}
            System.out.println("\t\tWriteLock : " + name + " is about to release the lock...");
            readWriteLock.writeLock().unlock();
            System.out.println("\t\tWriteLock : " + name + " has released the lock...");
        }
    }
}