import sun.font.TrueTypeFont;

/**
 * Created by Arvind on 1/28/2017.
 */
public class ArrayDeque<Item> {
    private Item[] storage;
    private int size;
    private int front_index;
    private int last_index;
    public ArrayDeque() {
        storage = (Item[]) new Object[8];
        size = 0;
        front_index = 0;
        last_index = 0;
    }
    public int next(int index) {
        if (isEmpty()) {
            return 0;
        }
        if (index == storage.length-1) {
            return 0;
        }
        return index + 1;
    }
    public int previous(int index) {
        if (isEmpty()) {
            return 0;
        }
        if (index == 0) {
            return storage.length - 1;
        }
        return index - 1;
    }
    public void resize(String command) {
        if (command == "increase"){
            Item[] a = (Item[]) new Object[storage.length*2];
            int extension = storage.length - front_index;
            System.arraycopy(storage, front_index, a, 0, extension);
            System.arraycopy(storage, 0, a, extension, size-extension);
            storage = a;
        }
        else {
                int ideal = storage.length/2;
                Item[] a = (Item[]) new Object[ideal];
                int extension = storage.length - front_index;
                System.arraycopy(storage, front_index, a, 0, extension);
                System.arraycopy(storage, 0, a, extension, size-extension);
                storage = a;
        }
    }
    public void addFirst(Item x){
        if (size == storage.length){
            resize("increase");
        }
        front_index = previous(front_index);
        storage[front_index] = x;
        size++;
    }
    public void addLast(Item x) {
        if (size == storage.length){
            resize("increase");
        }
        last_index = next(last_index);
        storage[last_index] = x;
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
        if (isEmpty()) {}
        else{
            int pointer = front_index;
            while (pointer != last_index) {
                System.out.print(storage[pointer] + " ");
                pointer = next(pointer);
            }
            System.out.println(storage[pointer] + " ");
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
        if (size < 16) {
            if (storage.length*0.1 > size && storage.length > 5) {resize("decrease");}
        }
        else {
            if (storage.length*0.25 > size) {resize("decrease");}
        }
        return val;
    }
    public Item removeLast(){
        if (size == 0) {
            return null;
        }
        Item val = storage[last_index];
        size--;
        storage[last_index] = null;
        last_index = previous(last_index);
        if (size < 16) {
            if (storage.length*0.1 > size && storage.length > 5) {resize("decrease");}
        }
        else {
            if (storage.length*0.25 > size) {resize("decrease");}
        }
        return val;
    }
    public Item get(int index) {
        if (index >= size) {
            return null;
        }
        int extension = storage.length - front_index;
        if (index >= extension) {
            return storage[index - (extension)];
        }
        else {
            return storage[front_index + index];
        }
    }
}
