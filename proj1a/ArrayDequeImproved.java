import java.lang.reflect.Array;

public class ArrayDequeImproved<Item> {
    private Item[] memory_array;
    private int i_first;
    private int i_last;
    private int size;
    private final int smallest_array = 8;

    public ArrayDequeImproved() {
        size = 0;
        i_first = 0;
        i_last = 0;
        memory_array = (Item[]) new Object[smallest_array];
    }

    public void addFirst(Item entity) {
        if (size == 0) {
            memory_array[i_first] = entity;
        }
        else {
            int next_i = movingBackward(i_first);
            memory_array[next_i] = entity;
            i_first = next_i;
        }
        size += 1;
        resize();
    }

    public void addLast(Item entity) {
        if (size == 0) {
            memory_array[i_last] = entity;
        }
        else {
            int next_i = movingForward(i_last);
            memory_array[next_i] = entity;
            i_last = next_i;
        }
        size += 1;
        resize();
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public Item removeFirst() {
        if (isEmpty()) {
            return null;
        }
        else {
            Item x = memory_array[i_first];
            memory_array[i_first] = null;
            size -= 1;
            if (size != 0) {
                i_first = movingForward(i_first);
            }
            resize();
            return x;
        }
    }

    public Item removeLast() {
        if (isEmpty()) {
            return null;
        }
        else {
            Item x = memory_array[i_last];
            memory_array[i_last] = null;
            size -= 1;
            if (size != 0) {
                i_last = movingBackward(i_last);
            }
            resize();
            return x;
        }
    }

    private int movingForward(int index) {
        if (index == memory_array.length - 1) {
            return 0;
        }
        else {
            return index + 1;
        }
    }

    private int movingBackward(int index) {
        if (index == 0) {
            return memory_array.length - 1;
        }
        else {
            return index - 1;
        }
    }

    public Item get(int index) {
        if (index - 1 > size) {
            return null;
        }
        int i_real = index + i_first;
        if (i_real >= memory_array.length) {
            i_real -= memory_array.length;
        }
        return memory_array[i_real];
    }

    public void printDeque() {
        int pt = i_first;
        for(int i = 0; i < size; i++) {
            System.out.print(memory_array[pt] + " ");
            pt = movingForward(pt);
        }
        System.out.println();
    }

    private void resize() {
        int array_length = memory_array.length;
        int new_array_length;
        if (size == array_length) {
            new_array_length = size * 4;
        }
        else if ( size < array_length / 4 && array_length != smallest_array) {
            new_array_length = array_length / 4;
            if (new_array_length <= smallest_array) {
                new_array_length = smallest_array;
            }
        }
        else {
            return;
        }
        int pt = i_first;
        Item[] new_array = (Item[]) new Object[new_array_length];
        for(int i = 0; i < size; i++) {
            new_array[i] = memory_array[pt];
            pt = movingForward(pt);
        }
        memory_array = new_array;
        i_first = 0;
        i_last = size - 1;
    }
}
