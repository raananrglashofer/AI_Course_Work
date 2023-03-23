package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.HashTable;

public class HashTableImpl<Key, Value> implements HashTable<Key, Value> {
    private Object[][] hashTable = new Object[5][10];
    private class Entry<Key,Value>{
        Key key;
        Value value;
        private Entry(Key k, Value v){
            if(k == null){
                throw new IllegalArgumentException();
            }
            key = k;
            value = v;
        }
        private Key getKey(){
            return (Key) this.key;
        }
    }
    public HashTableImpl(){
    }
    // get the index of the key for the 2d array
    // loop through the array to find the key i am looking for
    // return that value which is paired with the key put in
    @Override
    public Object get(Object k){
        int index = this.hashFunction(k);
        int keyInt = this.findKey(index,k);
        Entry current = (Entry) this.hashTable[index][keyInt];
        if(current == null){
            return null;
        }
        return current.value;
    }
    // get the index for the first dimension of the 2d array (i.e. the column of the 2d array)
    // create an entry to hold the previous value at that key/index
    // check if the key's value is null or not - if not null return the value, if null then create a new entry and store it at that index
    // if new key/value added then add one to the counter and return null
    @Override
    public Object put(Object k, Object v) {
        int index = this.hashFunction(k);
        int keyInt = this.findKey(index,k);
        Entry old = (Entry) this.hashTable[index][keyInt];
        this.hashTable[index][keyInt] = new Entry<Object, Object>( k, v);
        if(old != null){
            return old.value;
        }
        if(hashTable[0].length / 2 <= keyInt){
            hashTable = doubleMyArray(hashTable);
        }
        return null;
    }

     // @param key the key whose presence in the hashtable we are inquiring about
     // @return true if the given key is present in the hashtable as a key, false if not
     // @throws NullPointerException if the specified key is null - done

    @Override
    public boolean containsKey(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        int index = this.hashFunction(key);
        for (int i = 0; i < hashTable[0].length; i++) {
            Entry check = (Entry) this.hashTable[index][i];
            if (check != null && check.getKey().equals(key)) {
                return true;
            }
            if (check == null) {
                return false;
            }
        }
        return true;
    }

    // need to double check if i need to do this
    // also this value should be % table.length
    private int hashFunction(Object k){
        return (k.hashCode() & 0x7fffffff) % this.hashTable.length;
    }

    private Object[][] doubleMyArray(Object[][] hashTable){
        Object [][] doubleArray = new Object [hashTable.length][hashTable[0].length];
        for(int i = 0; i < hashTable.length; i++){
            for(int j = 0; j < hashTable[i].length; j++){
                doubleArray[i][j] = hashTable[i][j];
            }
        }
        Object [][] doubledArray = new Object[doubleArray.length][doubleArray[0].length * 2];
        for(int z = 0; z < hashTable.length; z++){
            for(int y = 0; y < hashTable[z].length; y++){
                doubledArray[z][y] = doubleArray[z][y];
            }
        }
        return doubledArray;
    }
    // if i have to initizalize place as 0 then it will cause problems when v is actually in 0, i need to find a way
    // to figure out what to do when v is not found and how to return either counter or hashtable[0].length
    private int findKey(int index, Object k) {
        for (int i = 0; i < hashTable[0].length; i++) {
            Entry check = (Entry) this.hashTable[index][i];
            if(check != null && check.getKey().equals(k)) {
                return i;
            }
            if (check == null) {
                return i;
            }
        }
        return 1;
    }
}
