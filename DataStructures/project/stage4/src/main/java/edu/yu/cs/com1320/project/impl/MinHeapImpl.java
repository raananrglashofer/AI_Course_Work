package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.MinHeap;
import java.util.NoSuchElementException;

public class MinHeapImpl<E extends Comparable<E>> extends MinHeap<E> {

    public MinHeapImpl(){
        elements = (E[]) new Comparable[20];
    }

    @Override
    public void reHeapify(E element) {
        if(element == null){
            throw new NoSuchElementException();
        }
        int index = getArrayIndex(element);// just checking if element is in the array - if not then will throw NoSuchElementException
        //MinHeapImpl heaped = new MinHeapImpl<>();
        if(isGreater(index/2, index)){ // is child smaller than parent
            upHeap(index);
        }
        else{
            downHeap(index);
        }
    }

    @Override
    protected int getArrayIndex(E element) {
        if(element == null){
            throw new NoSuchElementException();
        }
        for(int i = 1; i < this.elements.length; i++){
            if(elements[i] == null){
                throw new NoSuchElementException();
            }
            if(elements[i].equals(element)){
                return i;
            }
        }
        throw new NoSuchElementException(); // if i was never returned then it means the element is not in the array/heap
    }

    @Override
    protected void doubleArraySize() {
        E[] doubleArray = (E[]) new Comparable[this.elements.length * 2];
        E[] temp = this.elements;
        this.elements = doubleArray; // double check with HashTable doubleArray where I had to put
        for(int i = 0; i < (this.elements.length/2); i++){
            doubleArray[i] = temp[i];
            }
    }
}
