/**
 * Created by Arvind on 1/27/2017.
 */
public class LinkedListDeque<Item> {
    public NakedList first;
    public int size;
    public NakedList last;
    private class NakedList {
        private Item val;
        private NakedList next;
        private NakedList previous;

        public NakedList(Item f, NakedList rest) {
            val =f;
            next =rest;
        }
    }
    public LinkedListDeque(Item a) {
        first = new NakedList(a, null);
        size = 1;
        last = first;
    }
    public LinkedListDeque() {
        size = 0;
        first = null;
        last = null;
    }
    public void addFirst(Item x) {
        size += 1;
        first = new NakedList(x, first);
        if (size > 1) {
            first.next.previous = first;
        }
        else{
            last = first;
        }
    }
    public void addLast(Item x) {
        if (size == 0) {
            addFirst(x);
        }
        else {
            size += 1;
            last.next = new NakedList(Item, null);
            last.next.previous = last;
            last = last.next;
        }
    }
    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        return false;
    }
    public int size() {
    return size;
    }
    public void printDeque() {
        NakedList pointer = first;
        if (size == 0) {
        }
        else {
            while (pointer.next != null) {
                System.out.print(pointer.val);
                pointer = pointer.next
            }
            System.out.println(pointer.val);
        }
    }
    public Item removeFirst() {
        if (size == 0) {
            return null;
        }
        size -= 1;
        Item value = first.val;
        first = first.next;
        if (size > 0) {
            first.previous = null;
        }
        else{
            last = null;
        }
        return value;
    }
    public Item removeLast() {
        if (size == 0) {
            return null;
        }
        size -= 1;
        Item value = last.val;
        last = last.previous;
        if size > 0){
            last.next = null;
        }
        else {
            first = null;
        }
        return value;
    }
    public Item get(int index) {
        NakedList place = first;
        if (index >= size) {
            return null;
        }
        while (index > 0) {
            place = place.next;
            index -= 1;
        }
        return place.val;
    }
    public Item get_helper(int index, NakedList l) {
        if (index == 0) {
            return l.val;
        }
        return get_helper(index-1, l.next)
    }
    public Item getRecursive(int index) {
        if (index >= size) {
            return null;
        }
        NakedList place = first;
        return get_helper(index, place);
    }
}
