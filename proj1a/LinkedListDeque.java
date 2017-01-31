/**
 * Created by Arvind on 1/27/2017.
 */
public class LinkedListDeque<Item> {
    private NakedList sentinel;
    private int size;
    private class NakedList {
        private Item val;
        private NakedList next;
        private NakedList previous;

        public NakedList(Item f, NakedList rest, NakedList before) {
            val =f;
            next =rest;
            previous = before;
        }
    }
    public LinkedListDeque() {
        sentinel = new NakedList(null, null, null);
        size = 0;
        sentinel.next = sentinel;
        sentinel.previous = sentinel;
    }
    public void addFirst(Item x) {
        size += 1;
        sentinel.next.previous = new NakedList(x, sentinel.next, sentinel);
        sentinel.next = sentinel.next.previous;
    }
    public void addLast(Item x) {
        size += 1;
        sentinel.previous.next = new NakedList(x, sentinel, sentinel.previous);
        sentinel.previous = sentinel.previous.next;
    }
    public boolean isEmpty() {
        if (sentinel.next == sentinel) {
            return true;
        }
        return false;
    }
    public int size() {
    return size;
    }
    public void printDeque() {
        NakedList pointer = sentinel.next;
        while (pointer != sentinel) {
                System.out.print(pointer.val + " ");
                pointer = pointer.next;
            }
        System.out.println();
    }
    public Item removeFirst() {
        if (sentinel.next == sentinel) {
            return null;
        }
        size -= 1;
        Item value = sentinel.next.val;
        sentinel.next = sentinel.next.next;
        sentinel.next.previous = sentinel;
        return value;
    }
    public Item removeLast() {
        if (sentinel.previous == sentinel) {
            return null;
        }
        size -= 1;
        Item value = sentinel.previous.val;
        sentinel.previous = sentinel.previous.previous;
        sentinel.previous.next = sentinel;
        return value;
    }
    public Item get(int index) {
        NakedList place = sentinel.next;
        if (index >= size) {
            return null;
        }
        while (index > 0) {
            place = place.next;
            index -= 1;
        }
        return place.val;
    }
    private Item get_helper(int index, NakedList l) {
        if (index == 0) {
            return l.val;
        }
        return get_helper(index-1, l.next);
    }
    public Item getRecursive(int index) {
        if (index >= size) {
            return null;
        }
        NakedList place = sentinel.next;
        return get_helper(index, place);
    }
}
