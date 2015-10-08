package com.bhsc.mobile.utils;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class SyncArrayList<T> {

    private final List<T> elements;
    
    /**
     * 读写锁
     */
    private ReentrantReadWriteLock mRwLock;

    public SyncArrayList(List<T> lists){
        elements = lists;
        this.mRwLock = new ReentrantReadWriteLock();
    }
    
    public boolean addAll(Collection<? extends T> arg0){
        boolean result = false;
        mRwLock.writeLock().lock();
        elements.clear();
        result = elements.addAll(arg0);
        mRwLock.writeLock().unlock();
        return result;
    }
    
    public T get(int location){
        mRwLock.readLock().lock();
        T object = elements.get(location);
        mRwLock.readLock().unlock();
        return object;
    }
    
    public boolean add(T arg0){
        boolean result = false;
        mRwLock.writeLock().lock();
        result = elements.add(arg0);
        mRwLock.writeLock().unlock();
        return result;
    }
    
    public int size(){
        int size = 0;
        mRwLock.readLock().lock();
        size = elements.size();
        mRwLock.readLock().unlock();
        return size;
    }
}
