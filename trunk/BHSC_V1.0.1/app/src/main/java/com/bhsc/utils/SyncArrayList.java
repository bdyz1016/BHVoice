package com.bhsc.utils;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class SyncArrayList<E> {

    private final List<E> elements;
    
    /**
     * 读写锁
     */
    private ReentrantReadWriteLock mRwLock;

    public SyncArrayList(List<E> lists){
        elements = lists;
        this.mRwLock = new ReentrantReadWriteLock();
    }
    
    public boolean addAll(Collection<? extends E> arg0){
        boolean result;
        mRwLock.writeLock().lock();
        result = elements.addAll(arg0);
        mRwLock.writeLock().unlock();
        return result;
    }
    
    public E get(int location){
        mRwLock.readLock().lock();
        E object = elements.get(location);
        mRwLock.readLock().unlock();
        return object;
    }
    
    public boolean add(E arg0){
        boolean result = false;
        mRwLock.writeLock().lock();
        result = elements.add(arg0);
        mRwLock.writeLock().unlock();
        return result;
    }

    public E remove(int position){
        mRwLock.writeLock().lock();
        E e = elements.remove(position);
        mRwLock.writeLock().unlock();
        return e;
    }

    public void clear(){
        mRwLock.writeLock().lock();
        elements.clear();
        mRwLock.writeLock().unlock();
    }
    
    public int size(){
        int size = 0;
        mRwLock.readLock().lock();
        size = elements.size();
        mRwLock.readLock().unlock();
        return size;
    }
}
