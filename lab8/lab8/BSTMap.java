package lab8;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by Arvind on 3/9/2017.
 */
public class BSTMap<K extends Comparable<K> , V> implements Map61B<K, V> {
    @Override
    public Iterator iterator() {
        throw new UnsupportedOperationException();
    }
    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }
    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }
    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    Node top;
    int size;

    public BSTMap() {
        top = null;
        size = 0;
    }

    private class Node {
        Node left;
        Node right;
        V val;
        K key;


        Node(K k, V v) {
            left  = null;
            right = null;
            key = k;
            val = v;
        }
        private String helper(Node p) {
            if (p == null) {
                return "";
            }
            return helper(p.left) + " " + p.key + " " + helper(p.right);
        }
        @Override
        public String toString() {
            return helper(this);
        }
    }

    public boolean containsKey(K key) {
        Node p = find(key, top);
        if (p == null) {
            return false;
        }
        return true;
    }
    private Node find(K key, Node top) {
        if (top == null) {
            return null;
        }
        if (top.key.equals(key)) {
            return top;
        } else if (top.key.compareTo(key) > 0) {
            return top.left;
        } else {
            return top.right;
        }
    }
    public V get(K key) {
        Node p = find(key, top);
        if (p == null) {
            return null;
        }
        return p.val;
    }

    public void put(K key, V value) {
        top = putHelper(key, value, top);
    }

    public Node putHelper(K key, V value, Node p) {
        if (p == null) {
            size ++;
            return new Node(key, value);
        }
        if (p.key.equals(key)) {
            p.val = value;
        } else if (p.key.compareTo(key) > 0) {
            p.left = putHelper(key, value, p.left);
        } else {
            p.right = putHelper(key, value, p.right);
        }
        return p;
    }

    public void clear() {
        top = null;
        size = 0;
    }

    public int size() {
        return size;
    }

    public void printInOrder() {
        System.out.print(top.toString());
    }
}
