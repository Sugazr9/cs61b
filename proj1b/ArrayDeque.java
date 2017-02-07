/**
 * Created by Arvind on 1/28/2017.
 */
public class ArrayDeque<Item> implements Deque<Item>{
    private Item[] storage;
    private int size;
    private int front;
    private int back;

    public ArrayDeque() {
        storage = (Item[]) new Object[8];
        size = 0;
        front = 0;
        back = 0;
    }
    private int next(int index) {
        if (isEmpty()) {
            return index;
        } else if (index == storage.length - 1) {
            return 0;
        }
        return index + 1;
    }
    private int previous(int index) {
        if (isEmpty()) {
            return index;
        } else if (index == 0) {
            return storage.length - 1;
        }
        return index - 1;
    }
    private void copy(Item[] blank) {
        int counter = 0;
        while (counter < size) {
            blank[counter] = get(counter);
            counter++;
        }
        storage = blank;
    }

    private void resize() {
        if (size == storage.length) {
            Item[] a = (Item[]) new Object[storage.length * 2];
            copy(a);
        } else if (size < 16 && storage.length * 0.1 > size && storage.length > 5) {
            Item[] a = (Item[]) new Object[storage.length / 2];
            copy(a);
        } else if (size > 16 && storage.length / 4 > size) {
            Item[] a = (Item[]) new Object[storage.length / 2];
            copy(a);
        } else {
            return;
        }
        front = 0;
        back = previous(size);
    }
    @Override
    public void addFirst(Item x) {
        resize();
        front = previous(front);
        storage[front] = x;
        size++;
    }
    @Override
    public void addLast(Item x) {
        resize();
        back = next(back);
        storage[back] = x;
        size++;
    }
    @Override
    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        return false;
    }
    @Override
    public int size() {
        return size;
    }
    @Override
    public void printDeque() {
        int pointer = front;
        while (storage[pointer] != null) {
            System.out.print(storage[pointer] + " ");
            pointer = next(pointer);
        }
        System.out.println();
    }
    @Override
    public Item removeFirst() {
        if (isEmpty()) {
            return null;
        }
        Item val = storage[front];
        size--;
        storage[front] = null;
        front = next(front);
        resize();
        return val;
    }
    @Override
    public Item removeLast() {
        if (isEmpty()) {
            return null;
        }
        Item val = storage[back];
        size--;
        storage[back] = null;
        back = previous(back);
        resize();
        return val;
    }
    @Override
    public Item get(int index) {
        if (index >  size - 1) {
            return null;
        }
        int distance = storage.length - front;
        if (front > back && index >= distance) {
            index -= distance;
        } else {
            index += front;
        }
        return storage[index];
    }
}
