package lab8;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by Arvind on 3/9/2017.
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K,V>{
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
        @Override
        public String toString() {
            if (left == null && right == null) {
                return key.toString();
            } else if (left == null) {
                return key.toString() + " " + right.toString();
            } else if (right == null) {
                return left.toString() + " " + key.toString();
            } else {
                return left.toString() + " " + key.toString() + " " + right.toString();
            }
        }
    }

    public boolean containsKey(K key) {
        Node p = top;
        while (p != null) {
            if(p.key.equals(key)) {
                return true;
            } else if (p.key.compareTo(key) > 0) {
                p = p.left;
            } else {
                p = p.right;
            }
        }
        return false;
    }

    public V get(K key) {
        Node p = top;
        while (p!= null) {
            if(p.key.equals(key)) {
                return p.val;
            } else if (p.key.compareTo(key) > 0) {
                p = p.left;
            } else {
                p = p.right;
            }
        }
        return null;
    }

    public void put(K key, V value) {
        putHelper(key, value, top);
    }

    public void putHelper(K key, V value, Node p) {
        if (p == null) {
            p = new Node(key, value);
            size++;
        }
        if(p.key.equals(key)) {
            p.val = value;
            return;
        } else if (p.key.compareTo(key) > 0) {
            putHelper(key, value, p.left);
        } else {
            putHelper(key, value, p.right);
        }
    }

    public void clear() {
        top = null;
        size = 0;
    }

    public int size() {
        return size;
    }

    public void printInOrder() {
        if (top == null) {
            System.out.println();
        }
        System.out.print(top.toString());
    }
}
