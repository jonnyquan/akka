package com.xkeshi.task.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ruancl@xkeshi.com on 2017/1/17.
 */
public class Test {

    private int writer = 0;

    private int writerRequest = 0;//写优先级高于读 没有写等待的前提才能获取读锁

    private Map<Thread,Integer> readMap = new HashMap<>();//可重入

    private Map<Thread,Integer> writeMap = new HashMap<>();

    public synchronized void writeLock() throws InterruptedException {
        writerRequest++;
        Thread now = Thread.currentThread();
        boolean contain = writeMap.containsKey(now);
        while(writer>0 || writerRequest>0){
            wait();
        }
        writerRequest--;
        writer++;
    }

    public synchronized void writeUnlock(){
        writer --;
        notifyAll();
    }

    public synchronized void readLock() throws InterruptedException {
        Thread now = Thread.currentThread();
        boolean containThread = readMap.containsKey(now);
        int count = 0;
        if(containThread){
            count = readMap.get(now);
        }
        while((writer>0 || writerRequest>0) && !containThread){
            wait();
        }
        readMap.put(now,count++);
    }

    public synchronized void readUnlock(){
        Thread now = Thread.currentThread();
        int count  = readMap.get(now);
        if(count == 1){
            readMap.remove(now);
        }else {
            readMap.put(now,count--);
        }
        notifyAll();
    }



    public static void main(String[] args) {

    }
}
