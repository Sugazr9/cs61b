/**
 * Created by Arvind on 1/28/2017.
 */
public class ArrayDeque<Item> {
    private Item[] storage;
    private int size;
    private int FrontIndex;
    private int BackIndex;

    public ArrayDeque() {
        storage = (Item[]) new Object[8];
        size = 0;
        FrontIndex = 0;
        BackIndex = 0;
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
        FrontIndex = 0;
        BackIndex = previous(size);
    }
    public void addFirst(Item x) {
        resize();
        FrontIndex = previous(FrontIndex);
        storage[FrontIndex] = x;
        size++;
    }
    public void addLast(Item x) {
        resize();
        BackIndex = next(BackIndex);
        storage[BackIndex] = x;
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
        int pointer = FrontIndex;
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
        Item val = storage[FrontIndex];
        size--;
        storage[FrontIndex] = null;
        FrontIndex = next(FrontIndex);
        resize();
        return val;
    }
    public Item removeLast() {
        if (isEmpty()) {
            return null;
        }
        Item val = storage[BackIndex];
        size--;
        storage[BackIndex] = null;
        BackIndex = previous(BackIndex);
        resize();
        return val;
    }
    public Item get(int index) {
        if (index >  size - 1) {
            return null;
        }
        int NumbertoEnd = storage.length - FrontIndex;
        if (FrontIndex > BackIndex && index >= NumbertoEnd) {
            index -= NumbertoEnd;
        } else {
            index += FrontIndex;
        }
        return storage[index];
    }
}
