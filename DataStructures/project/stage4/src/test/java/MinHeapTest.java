package edu.yu.cs.com1320.project.impl;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import edu.yu.cs.com1320.project.MinHeap;
import edu.yu.cs.com1320.project.impl.MinHeapImpl;
import org.junit.Assert;
import org.junit.Test;
import java.net.URI;
import java.net.URISyntaxException;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.util.Comparator;

public class MinHeapTest {

    @Test
    public void getArrayIndexTest(){
        MinHeapImpl<Integer> heap = new MinHeapImpl<>();
        for(int i = 5; i < 10; i++){
            heap.insert(i);
        }
        heap.insert(4);
        heap.insert(1);
        heap.insert(21);
        assertTrue(heap.getArrayIndex(21) == 8);
        assertTrue(heap.getArrayIndex(4) == 3);
        assertTrue(heap.getArrayIndex(1) == 1);
    }

    @Test
    public void doubleArrayTest(){
        MinHeapImpl<Integer> heap = new MinHeapImpl<>();
        for(int i = 1; i < 11; i++){
            heap.insert(i);
        }
        assertTrue(heap.getArrayIndex(5) == 5);
        assertTrue(heap.getArrayIndex(4) == 4);
        assertTrue(heap.getArrayIndex(10) == 10);
        assertThrows(NoSuchElementException.class, () -> { heap.getArrayIndex(18);});
        for(int i = 11; i < 31; i++){
            heap.insert(i);
        }
        assertTrue(heap.getArrayIndex(5) == 5);
        assertTrue(heap.getArrayIndex(4) == 4);
        assertTrue(heap.getArrayIndex(10) == 10);
        assertTrue(heap.getArrayIndex(18) == 18);
        assertTrue(heap.getArrayIndex(27) == 27);
    }

    @Test
    public void NoSuchElementExceptionTest(){
        MinHeapImpl<Integer> heap = new MinHeapImpl<>();
        for(int i = 1; i < 11; i++){
            heap.insert(i);
        }
        assertThrows(NoSuchElementException.class, () -> { heap.getArrayIndex(18);});
        assertThrows(NoSuchElementException.class, () -> { heap.getArrayIndex(null);});
        assertThrows(NoSuchElementException.class, () -> { heap.reHeapify(18);});
        assertThrows(NoSuchElementException.class, () -> { heap.reHeapify(null);});
    }

    @Test
    public void reHeapifyTest(){
        MinHeapImpl<Integer> heap = new MinHeapImpl<>();
        int check = 12;
        for(int i = 1; i < 11; i++){
            heap.insert(i);
        }
        heap.insert(check);
    }
}
