package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.MinHeap;

public class MinHeapImpl<E extends Comparable<E>> extends MinHeap<E> { // is it extends or implements and should it be an abstract class

    @Override
    public void reHeapify(E element) {

    }

    @Override
    protected int getArrayIndex(E element) {
        return 0;
    }

    @Override
    protected void doubleArraySize() {

    }
}
