package com.company;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class RWSample {
    private final Map<String, String> map = new TreeMap<>();
    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock r = rwl.readLock();
    private final Lock w = rwl.writeLock();
    public String get(String key) {
        r.lock();
        System.out.println("读锁锁定！"+ map.get(key));
        try {
            return map.get(key);
        } finally {
            r.unlock();
        }
    }

    public String put(String key, String entry) {
        w.lock();
        System.out.println("写锁锁定！" + entry);
        try {
            return map.put(key, entry);
        } finally {
            w.unlock();
        }
    }
    public static void main(String[] args) {
        RWSample rwSample = new RWSample();
        rwSample.put("1", "1");
        new Thread(()->{
            for(int i=0; i<20; i++){
                rwSample.get("1");
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(()->{
            for(int i=0; i<5; i++){
                int n = new Random().nextInt(10);
                rwSample.put("1",String.valueOf(n));
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
