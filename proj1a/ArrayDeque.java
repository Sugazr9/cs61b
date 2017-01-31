/**
 * Created by Arvind on 1/28/2017.
 */
public class ArrayDeque<Item> {
    private Item[] storage;
    private int size;
    private int front_index;
    private int back_index;
    private int extension;
    public ArrayDeque() {
        storage = (Item[]) new Object[8];
        size = 0;
        front_index = 0;
        back_index = 0;
        extension = 0;

    }
    private int next(int index) {
        if (isEmpty()) {
            return 0;
        }
        if (index == storage.length - 1) {
            return 0;
        }
        return index + 1;
    }
    private int previous(int index) {
        if (isEmpty()) {
            return 0;
        }
        if (index == 0) {
            return storage.length - 1;
        }
        return index - 1;
    }
    private void resize(String command) {
        if (command.equals("increase")) {
            Item[] a = (Item[]) new Object[storage.length * 2];
            System.arraycopy(storage, front_index, a, 0, extension);
            System.arraycopy(storage, 0, a, extension, size - extension);
            storage = a;
            front_index = 0;
            back_index = previous(size);
        }
        else {
            Item[] a = (Item[]) new Object[storage.length / 2];
            System.arraycopy(storage, front_index, a, 0, extension);
            System.arraycopy(storage, 0, a, extension, size - extension);
            storage = a;
            front_index = 0;
            back_index = previous(size);
        }
    }
    public void addFirst(Item x){
        if (size == storage.length) {
            resize("increase");
        }
        front_index = previous(front_index);
        storage[front_index] = x;
        size++;
        if (front_index > back_index) {
            extension++;
        }
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
    public int size(){
        return size;
    }
    public void printDeque(){
        int pointer = front_index;
        while (storage[pointer] != null) {
            System.out.print(storage[pointer] + " ");
            pointer = next(pointer);
        }
    }
    public Item removeFirst(){
        if (size == 0) {
            return null;
        }
        Item val = storage[front_index];
        size--;
        storage[front_index] = null;
        front_index = next(front_index);
        extension--;
        if (front_index <= back_index) {
            extension = 0;
        }
        if (size < 16){
            if(storage.length * 0.1 > size && storage.length > 5) {
                resize("decrease");
            }
        }
        else {
            if(storage.length / 4 > size) {
                resize("decrease");
            }
        }
        return val;
    }
    public Item removeLast(){
        if (size == 0) {
            return null;
        }
        Item val = storage[back_index];
        size--;
        storage[back_index] = null;
        if (back_index == 0) {
            extension = 0;
        }
        back_index = previous(back_index);
        if (size < 16){
            if(storage.length * 0.1 > size && storage.length > 5) {
                resize("decrease");
            }
        }
        else {
            if(storage.length / 4 > size) {
                resize("decrease");
            }
        }
        return val;
    }
    public Item get(int index) {
        if (index >= size) {
            return null;
        }
        if (index >= extension) {
            return storage[index - extension];
        }
        else {
            return storage[front_index + index];
        }
    }
}
