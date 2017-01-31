/**
 * Created by Arvind on 1/28/2017.
 */
public class ArrayDeque<Item> {
    private Item[] storage;
    private int size;
    private int front_index;
    private int back_index;
    public ArrayDeque() {
        storage = (Item[]) new Object[8];
        size = 0;
        front_index = 0;
        back_index = 0;

    }
    private int next(int index) {
        if (isEmpty()) {
            return index;
        }
        else if (index == storage.length - 1) {
            return 0;
        }
        return index + 1;
    }
    private int previous(int index) {
        if (isEmpty()) {
            return index;
        }
        else if (index == 0) {
            return storage.length - 1;
        }
        return index - 1;
    }
    private void resize(String command) {
        int pointer = front_index;
        int counter = 0;
        if (command.equals("increase")) {
            Item[] a = (Item[]) new Object[storage.length * 2];
            while (counter < size) {
                System.arraycopy(storage, pointer, a, counter, 1);
                pointer = next(pointer);
                counter++;
            }
            storage = a;
        }
        else {
            Item[] a = (Item[]) new Object[storage.length / 2];
            while (counter < size) {
                System.arraycopy(storage, pointer, a, counter, 1);
                pointer = next(pointer);
                counter++;
            }
            storage = a;
        }
        front_index = 0;
        back_index = previous(size);
    }
    public void addFirst(Item x) {
        if (size == storage.length) {
            resize("increase");
        }
        front_index = previous(front_index);
        storage[front_index] = x;
        size++;
    }
    public void addLast(Item x) {
        if (size == storage.length) {
            resize("increase");
        }
        back_index = next(back_index);
        storage[back_index] = x;
        size++;
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
        int pointer = front_index;
        while (storage[pointer] != null) {
            System.out.print(storage[pointer] + " ");
            pointer = next(pointer);
        }
        System.out.println();
    }
    public Item removeFirst() {
        if (isEmpty()) {
            return null;
        }
        Item val = storage[front_index];
        size--;
        storage[front_index] = null;
        front_index = next(front_index);
        if (size < 16) {
            if (storage.length * 0.1 > size && storage.length > 5) {
                resize("decrease");
            } }
        else {
            if (storage.length / 4 > size) {
                resize("decrease");
            } }
        return val;
    }
    public Item removeLast() {
        if (isEmpty()) {
            return null;
        }
        Item val = storage[back_index];
        size--;
        storage[back_index] = null;
        back_index = previous(back_index);
        if (size < 16) {
            if (storage.length * 0.1 > size && storage.length > 5) {
                resize("decrease");
            } }
        else {
            if (storage.length / 4 > size) {
                resize("decrease");
            } }
        return val;
    }
    public Item get(int index) {
        int num_to_break = storage.length - front_index;
        if (front_index > back_index && index >= num_to_break) {
            index -= num_to_break;
        }
        else {
            index += front_index;
        }
        return storage[index];
    }
}
