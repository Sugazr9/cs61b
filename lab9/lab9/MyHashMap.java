package lab9;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Arvind on 3/16/2017.
 */
public class MyHashMap<K, V> implements Map61B<K, V>{
    private int size;
    private double loadfactor;

    private ArrayList<Entry>[] map;

    public MyHashMap() {
        this(8, 0.8);
    }

    public MyHashMap(int initialSize) {
        this(initialSize, 0.8);
    }

    public MyHashMap(int initialSize, double loadFactor) {
        loadfactor = loadFactor;
        size = 0;
        map = (ArrayList<Entry>[]) new ArrayList[initialSize];
        for (int i = 0; i < initialSize; i++) {
            map[i] = new ArrayList<Entry>();
        }
    }
    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V get(K key) {
        int[] pair = find(key);
        if (pair != null) {
            return map[pair[0]].get(pair[1]).value;
        }
        return null;
    }

    @Override
    public void put(K key, V value) {
        int[] pair = find(key);
        if (pair != null) {
            map[pair[0]].get(pair[1]).value = value;
            return;
        }
        if (loadfactor <= (size + 1) / map.length) {
            resize();
        }
        int index = Math.floorMod(key.hashCode(), map.length);
        Entry e = new Entry(key, value);
        map[index].add(e);
        size++;
    }

    private void resize() {
        ArrayList<Entry>[] newMap = (ArrayList<Entry>[]) new ArrayList[map.length*2];
        for (int i = 0; i < newMap.length; i++) {
            newMap[i] = new ArrayList<Entry>();
        }
        for (int i = 0; i < map.length; i++) {
            ArrayList<Entry> curr = map[i];
            for (int j = 0; j < curr.size(); j++) {
                Entry e = curr.get(j);
                int index = Math.floorMod(e.key.hashCode(), newMap.length);
                newMap[index].add(e);
            }
        }
        map = newMap;
    }
    private int[] find(K key) {
        int initial = Math.floorMod(key.hashCode(), map.length);
        ArrayList<Entry> curr = map[initial];
        for (int i = 0; i < curr.size(); i++) {
            if (curr.get(i).key.equals(key)) {
                return new int[]{initial, i};
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        if (find(key) == null) {
            return false;
        }
        return true;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Set<K> keySet() {
        Set<K> yep = new LinkedHashSet<K>();
        for (int i = 0; i < map.length; i++) {
            ArrayList<Entry> curr = map[i];
            for (int j = 0; j < curr.size(); j++) {
                yep.add(curr.get(j).key);
            }
        }
        return yep;
    }

    @Override
    public void clear() {
        map = (ArrayList<Entry>[]) new ArrayList[8];
        for (int i = 0; i < 8; i++) {
            map[i] = new ArrayList<Entry>();
        }
    }

    @Override
    public Iterator iterator() {
        return keySet().iterator();
    }

    private class Entry {
        public K key;
        public V value;

        Entry(K k, V v) {
            key = k;
            value = v;
        }
    }
}
