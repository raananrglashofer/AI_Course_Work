package edu.yu.cs.com1320.project;

import edu.yu.cs.com1320.project.impl.HashTableImpl;
import org.junit.Test;

import java.security.Key;

public class HashTest {

    @Test // expected output: 24
    public void putAndGetTest(){
        HashTableImpl hashing = new HashTableImpl();
        hashing.put("A", 24);
        System.out.println(hashing.get("A"));
    }

    @Test // expected output: null
    public void noKeyInTable(){
        HashTableImpl hashing = new HashTableImpl();
        hashing.put("A", 24);
        System.out.println(hashing.get("B"));
    }

    @Test // expected output: 24
    public void previousValueStored(){
        HashTableImpl hashing = new HashTableImpl();
        hashing.put("A", 24);
        System.out.println(hashing.put("A", 12));
    }

    @Test // expected output: null
    public void keyNotAlreadyPresent(){
        HashTableImpl hashing = new HashTableImpl();
        System.out.println(hashing.put("A", 12));
    }

    @Test // expected output: true, false, NullPointerException
    public void containsKeyPresent(){
        HashTableImpl hashing = new HashTableImpl();
        hashing.put("A", 24);
        System.out.println(hashing.containsKey("A"));
        System.out.println(hashing.containsKey("B"));
        System.out.println(hashing.containsKey(null));
    }

    @Test // expected output: 3 (need to make hashFunction public in order to determine if two keys have the same hashcode
    public void sameIndexCheck(){
        HashTableImpl hashing = new HashTableImpl();
       // System.out.println(hashing.hashFunction("A")); - A and F have the same hash number
       // System.out.println(hashing.hashFunction("F"));
        hashing.put("A", 24);
        hashing.put("F", 3);
        System.out.println(hashing.get("F"));
    }
    @Test // expected output: 10,20 (need to make hashTable public in order to run this test)
    public void isArrayDoubling(){
        HashTableImpl hashing = new HashTableImpl();
        //System.out.println(hashing.hashTable[0].length);
        hashing.put("A", 24);
        hashing.put("F", 3);
        hashing.put("K", 24);
        hashing.put("P", 3);
        hashing.put("U", 24);
        hashing.put("Z", 13);
        //System.out.println(hashing.hashTable[0].length);
    }
}
