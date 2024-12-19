public class LinkedListDequeImproved<Item> {
    private RealList first;
    private RealList last;
    private int size;

    private class RealList {
        private Item val;
        private RealList parent;
        private RealList child;

        private RealList(Item x, RealList parent, RealList child) {
            val = x;
            this.parent = parent;
            this.child = child;
        }

    }

    public LinkedListDequeImproved() {
        size = 0;
        first = null;
        last = null;
    }

    public void addFirst(Item entity) {
        first = new RealList(entity, null, first);
        if (size == 0) {
            last = first;
        }
        size += 1;
    }

    public void addLast(Item entity) {
        if (size == 0) {
            last = new RealList(entity, null, null);
            first = last;
        }
        else {
            last.child = new RealList(entity, last, null);
            last = last.child;
        }
        size += 1;
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public Item removeFirst() {
        if (first == null) {
            return null;
        }
        else {
            Item x = first.val;
            first = first.child;
            size -= 1;
            if (size == 0) {
                last = null;
            }
            else {
                first.parent = null;
            }
            return x;
        }
    }

    public Item removeLast() {
        Item val;
        if (last == null) {
            val = null;
        } else {
            val = last.val;
            last = last.parent;
            size -= 1;
        }
        if (size == 0) {
            first = null;
        }
        return val;
    }

    public Item get(int index) {
        if (index - 1 > size) {
            return null;
        }
        RealList pt = first;
        for (int i = 0; i < index; i++) {
            pt = pt.child;
        }
        return pt.val;
    }

    public Item getRecursive(int index) {
        if (index - 1 > size) {
            return null;
        }
        else {
            return getRecurHelper(index, first);
        }
    }

    private Item getRecurHelper(int index, RealList l) {
        if (index == 0) {
            return l.val;
        }
        else {
            return getRecurHelper(index - 1, l.child);
        }
    }

    public void printDeque() {
        RealList pt = first;
        while (pt != null) {
            System.out.print(pt.val + " ");
            pt = pt.child;
        }
        System.out.println();
    }
}

