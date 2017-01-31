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
    public void resize(String command) {

}
    public void addFirst(Item x){

    }
    public void addLast(Item x) {

    }
    public boolean isEmpty() {
        return false;
    }
    public int size(){
        return size;
    }
    public void printDeque(){

    }
    public Item removeFirst(){
        return null;
    }
    public Item removeLast(){
        return null;
    }
    public Item get(int index) {
        return null;
    }

}
