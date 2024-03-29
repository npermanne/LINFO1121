import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

public class BST<Key extends Comparable<Key>, Value> implements Iterable<Key> {
    private Node root;             // root of BST

    private class Node {
        private final Key key;       // sorted by key
        private Value val;           // associated data
        private Node left, right;    // left and right subtrees
        private int size;            // number of nodes in subtree

        public Node(Key key, Value val, int size) {
            this.key = key;
            this.val = val;
            this.size = size;
        }

        public int getSize() {
            return size;
        }
    }

    /**
     * Initializes an empty symbol table.
     */
    public BST() {}

    /**
     * Returns true if this symbol table is empty.
     * @return {@code true} if this symbol table is empty; {@code false} otherwise
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns the number of key-value pairs in this symbol table.
     * @return the number of key-value pairs in this symbol table
     */
    public int size() {
        return size(root);
    }

    // return number of key-value pairs in BST rooted at x
    private int size(Node x) {
        if (x == null) return 0;
        else return x.size;
    }

    /**
     * Returns the value associated with the given key.
     *
     * @param  key the key
     * @return the value associated with the given key if the key is in the symbol table
     *         and {@code null} if the key is not in the symbol table
     */
    public Value get(Key key) {
        return get(root, key);
    }

    private Value get(Node x, Key key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if      (cmp < 0) return get(x.left, key);
        else if (cmp > 0) return get(x.right, key);
        else              return x.val;
    }

    /**
     * Search for key, update value if key is found. Grow table if key is new.
     *
     * @param  key the key
     * @param  val the value
     */
    public void put(Key key, Value val) {
        root = put(root, key, val);
    }
    private Node put(Node x, Key key, Value val) {
        if (x == null) return new Node(key, val, 1);
        int cmp = key.compareTo(x.key);
        if      (cmp < 0) x.left  = put(x.left,  key, val);
        else if (cmp > 0) x.right = put(x.right, key, val);
        else              x.val   = val;
        x.size = 1 + size(x.left) + size(x.right);
        return x;
    }

    /**
     * Returns an iterator that iterates through the keys in Incresing order
     * (In-Order transversal).
     * @return an iterator that iterates through the items in FIFO order.
     */
    @Override
    public Iterator<Key> iterator() {
        return new BSTIterator();
    }

    // TODO STUDDENT: Implement the BSTIterator class

    // an iterator, doesn't implement remove() since it's optional
    /**/
    private class BSTIterator implements Iterator<Key> {

        private final int size;
        private Node current;
        private boolean lastChecker;

        Stack<Node> stack;

        public BSTIterator() {
            size = size(root);
            if (size==0){
                lastChecker = false;
                return;
            }
            current = root;
            stack = new Stack<>();
            lastChecker = true;
            while (current.left != null){
                stack.push(current);
                current = current.left;
            }
        }

        public boolean hasNext() {
            if (size==0){
                return false;
            }
            if (size != size(root)){
                throw new ConcurrentModificationException();
            }
            if (lastChecker && stack.empty() && current.right == null){
                lastChecker = false;
                return true;
            }
            return lastChecker;
        }

        public Key next() {
            if (size==0){
                throw new NoSuchElementException();
            }
            if (!hasNext() && lastChecker){
                throw new NoSuchElementException();
            }
            Key returned = current.key;
            if (!lastChecker){
                return returned;
            }
            if (current.right != null){
                current = current.right;
                while (current.left!=null){
                    stack.push(current);
                    current = current.left;
                }
            }
            else{
                current = stack.pop();
            }
            return returned;
        }

    }

    public static void main(String[] args) {
        BST<Integer,Integer> bst = new BST<>();
        for (int a:new int[]{1,2,3,4,5,6,7}) {
            bst.put(a,a);
        }
        for (Integer a:bst) {
            System.out.println(a);
        }
    }
}