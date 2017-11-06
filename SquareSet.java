import java.util.Set;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Represents a set of unique Squares.
 *
 * @author sdelmerico3
 * @version 0.0.1
 */
public class SquareSet implements Set<Square> {
    private int arraySize = 1024;
    private Square[] set;

    /**
     * Creates a SquareSet with an initial pre-allocation for 1024 elements.
     */
    public SquareSet() {
        this.set = new Square[arraySize];
    }

    /**
     * Creates a SquareSet with an initial pre-allocation for 1024 elements, and
     * the set is initialized with all the elements present in the collection.
     *
     * @param c colletion to initialize the set
     */
    public SquareSet(Collection<? extends Square> c) {
        this();

        this.addAll(c);
    }

    @Override
    public void clear() {
        this.set = new Square[this.arraySize];
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean valid = false;
        for (Object i : c) {
            valid = this.remove(i) ? true : valid;
        }

        return valid;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        c.removeIf((Object o) -> !this.contains(o));
        SquareSet newSet = new SquareSet();

        @SuppressWarnings("unchecked")
        boolean ok = newSet.addAll((Collection<? extends Square>) c);
        return ok;
    }

    @Override
    public boolean addAll(Collection<? extends Square> c) {
        boolean changed = false;

        for (Square i : c) {
            changed = this.willAdd(i) ? true : changed;
        }

        for (Square i : c) {
            changed = this.add(i) ? true : changed;
        }

        return changed;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        boolean containsAll = true;
        for (Object i : c) {
            containsAll = this.contains(i) ? containsAll : false;
        }

        return containsAll;
    }

    @Override
    public boolean remove(Object o) {
        int index = this.indexOf(o);
        if (index >= 0) {
            this.set[index] = null;
            return true;
        }

        return false;
    }

    /**
     * Checks to see if the argument passed will be a valid addition to the set.
     *
     * @param s the argument to test for validity
     * @return true if the argument could be added to the set
     */
    protected boolean willAdd(Square s) {
        if (s == null) {
            throw new NullPointerException();
        }

        s = new Square(s.getFile(), s.getRank());

        if (this.contains(s)) {
            return false;
        }

        return true;
    }

    @Override
    public boolean add(Square s) {
        if (!this.willAdd(s)) {
            return false;
        }

        for (int i = 0; i < this.set.length; i++) {
            if (this.set[i] == null) {
                this.set[i] = s;
                return true;
            }
        }

        // if there were no empty spaces in the array
        // then we need to expand it

        this.arraySize *= 2;
        Square[] newArray = new Square[this.arraySize];
        for (int i = 0; i < this.set.length; i++) {
            newArray[i] = this.set[i];
        }

        this.set = newArray;

        this.set[this.arraySize / 2] = s;
        return true;
    }

    @Override
    public boolean contains(Object o) {
        if (this.indexOf(o) >= 0) {
            return true;
        }

        return false;
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < this.set.length; i++) {
            if (this.set[i] != null) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int size() {
        if (this.isEmpty()) {
            return 0;
        }

        int count = 0;
        for (int i = 0; i < this.set.length; i++) {
            if (this.set[i] != null) {
                count += 1;
            }
        }

        return count;
    }

    @Override
    public Object[] toArray() {
        return this.toArray(new Object[0]);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a == null) {
            throw new NullPointerException();
        }

        if (!(a instanceof Square[])) {
            throw new ArrayStoreException();
        }

        int setSize = this.size();
        T[] ret = a;

        if (a.length < setSize) {
            ret = (T[]) new Object[setSize];
        }

        int retIndex = 0;
        for (int i = 0; i < this.set.length; i++) {
            if (this.set[i] != null) {
                ret[retIndex] = (T) this.set[i];
                retIndex += 1;
            }
        }

        for (int i = setSize; i < a.length - setSize; i++) {
            ret[i] = null;
        }

        return ret;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Iterator<Square> iterator() {
        return new SquareIterator(this.set);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (!(o instanceof Set)) {
            return false;
        }

        Set s2 = (Set) o;
        if (s2.size() != this.size()) {
            return false;
        }

        for (Object i : s2.toArray()) {
            if (!this.contains(i)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        return 1;
    }

    /**
     * Finds the index of an element in the set's array, if it exists.
     *
     * @param o The object that you are looking for in the set.
     * @return the index into the set's array of the object
     */
    protected int indexOf(Object o) {
        for (int i = 0; i < this.set.length; i++) {
            if (this.set[i] == null) {
                continue;
            }
            if (this.set[i].equals(o)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * An iterator over the valid elements of a SquareSet.
     *
     * @author sdelmerico3
     */
    class SquareIterator implements Iterator<Square> {
        private int currentIndex;
        private Square[] set;

        /**
         * Creates an Iterator for a SquareSet that is instantiated with an
         * an array of the current state of the set.
         *
         * @param set An array of the current state of the set.
         */
        public SquareIterator(Square[] set) {
            this.set = set;
        }

        @Override
        public boolean hasNext() {
            for (int i = this.currentIndex; i < this.set.length; i++) {
                if (this.set[i] != null) {
                    return true;
                }
            }

            return false;
        }

        @Override
        public Square next() {
            for (; this.currentIndex < this.set.length; this.currentIndex++) {
                if (this.set[this.currentIndex] != null) {
                    return this.set[this.currentIndex++];
                }
            }

            throw new NoSuchElementException();
        }
    }
}
