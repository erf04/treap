import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

class TreapNode<T>{
    T value;
    TreapNode leftChild;
    TreapNode<T> rightChild;
    int priority;

    public TreapNode(T value,int priority){
        this.value=value;
        this.priority=priority;
        this.leftChild=null;
        this.rightChild=null;
    }

    public TreapNode(){
        this.value=null;
        this.priority=Integer.MIN_VALUE;
        this.leftChild=null;
        this.rightChild=null;
    }



}

public class Treap<T extends Comparable<T>>  {

    private TreapNode<T> root;
    private HashMap<Integer, ArrayList<String>> a = new HashMap<>();
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RED = "\u001B[31m";
    private static final String empty = "  ";
    private static int count =0;

    Treap(){
        this.root=null;
    }

    public TreapNode getRoot() {
        return root;
    }

    int getHeight() {
        return getHeightRec(this.root);
    }

    // A utility function to compute the height of the tree
    static int getHeightRec(TreapNode node) {
        if (node == null) {
            return -1; // return -1 to count edges, or 0 to count nodes
        } else {
            // compute the height of each subtree
            int leftHeight = getHeightRec(node.leftChild);
            int rightHeight = getHeightRec(node.rightChild);

            // use the larger one
            return Math.max(leftHeight, rightHeight) + 1;
        }
    }

    public int getPriority(){
        return (int) (Math.random() * 100);
    }

    public void insert(T value){
        this.root=insertNode(this.root,value);
    }
    private TreapNode insertNode(TreapNode<T> root,T value){
        if (root == null) {
            return new TreapNode<T>(value,this.getPriority());
        }

        // If key is smaller than root
        if (value.compareTo(root.value)<=0) {
            // Insert in left subtree
            root.leftChild = insertNode(root.leftChild, value);

            // Fix Heap property if it is violated
            if (root.leftChild.priority > root.priority) {
                root = rightRotate(root);
            }
        } else { // If key is greater
            // Insert in right subtree
            root.rightChild = insertNode(root.rightChild, value);

            // Fix Heap property if it is violated
            if (root.rightChild.priority > root.priority) {
                root = leftRotate(root);
            }
        }
        return root;
    }

    public TreapNode<T> remove(T value) throws InterruptedException {
//        draw(root, getHeightRec(root), true, value, true);
        this.root=deleteNode(this.root,value);
        System.out.println("after remove");
//        draw();
//        System.out.println(temp.value.toString());
        return root;
    }
    private TreapNode<T> deleteNode(TreapNode<T> root, T key)
    {
        // Base case
        if (root == null) {
            return root;
        }

        // IF KEY IS NOT AT ROOT
        if (key.compareTo(root.value)<0) {
            root.leftChild = deleteNode(root.leftChild, key);
        }
        else if (key.compareTo(root.value)>0 ) {
            root.rightChild = deleteNode(root.rightChild, key);
        }

        // IF KEY IS AT ROOT
        // If left is null
        else if (root.leftChild == null) {
            TreapNode temp = root.rightChild;
            root = null;
            root = temp; // Make right child as root
        }

        // If right is null
        else if (root.rightChild == null) {
            TreapNode temp = root.leftChild;
            root = null;
            root = temp; // Make left child as root
        }

        // If key is at root and both left and right are not
        // null
        else if (root.leftChild.priority < root.rightChild.priority) {
            root = leftRotate(root);
            root.leftChild = deleteNode(root.leftChild, key);
        }
        else {
            root = rightRotate(root);
            root.rightChild = deleteNode(root.rightChild, key);
        }

        return root;
    }

    private static TreapNode rightRotate(TreapNode y) {
        TreapNode x = y.leftChild;
        TreapNode T2 = x.rightChild;

        // Perform rotation
        x.rightChild = y;
        y.leftChild = T2;

        // Return new root
        return x;
    }

    // A utility function to left rotate subtree rooted with x
// See the diagram given above.
    public static TreapNode leftRotate(TreapNode x) {
        TreapNode y = x.rightChild;
        TreapNode T2 = y.leftChild;

        // Perform rotation
        y.leftChild = x;
        x.rightChild = T2;

        // Return new root
        return y;
    }



    private void draw(TreapNode<T> node, int count, Boolean pause, T coloredvalue, Boolean isDel) throws InterruptedException {

        go(node, 0);
        for (int i = 0; i < count + 1; i++) {
            for (int j = 0; j < (2 * (1 << (count - i - 1)) - 1); j++)
                System.out.print(" ");

            for (int j = 0; j < this.a.get(i).size(); j++) {
                String col = ANSI_CYAN;
                if (coloredvalue != null)
                    if (isDel){
                        col = (a.get(i).get(j).equals(coloredvalue.toString())) ? ANSI_RED : ANSI_CYAN;

                    }
                    else
                        col = (a.get(i).get(j).equals(coloredvalue.toString())) ? ANSI_YELLOW : ANSI_CYAN;
                System.out.print(col + a.get(i).get(j) + ANSI_RESET);

                if (!a.get(i).get(j).equals("  ") && pause)
                    Thread.sleep(500);

                for (int k = 0; k < (2 * (1 << (count - i - 1)) - 1); k++)
                    System.out.print("  ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private void go(TreapNode<T> node, int level) {
        if (level > getHeightRec(root))
            return;
        if (node.equals(root))
            addToMap(level, node.value.toString());

        if (node.leftChild != null) {
            addToMap(level + 1, node.leftChild.value.toString());
            go(node.leftChild, level + 1);
        } else {
            addToMap(level + 1, empty);
            go(new TreapNode<>(), level + 1);
        }


        if (node.rightChild != null) {
            addToMap(level + 1, node.rightChild.value.toString());
            go(node.rightChild, level + 1);
        } else {
            addToMap(level + 1, empty);
            go(new TreapNode<>(), level + 1);
        }

    }

    private void addToMap(int key, String value) {
        if (a.get(key) == null)
            a.put(key, new ArrayList<>());
        a.get(key).add(value);
    }

    public void draw() throws InterruptedException {
        System.out.println("Tree:");
        draw(root, getHeightRec(root), true, null, false);
    }
    public boolean search(T value) throws InterruptedException {
        return search(root, value, true) != null;
    }

    private TreapNode<T> search(TreapNode<T> node, T value, Boolean print) throws InterruptedException {
        if (value.compareTo(node.value) < 0)
            if (node.leftChild != null)
                if (!print && value.compareTo((T) node.leftChild.value) == 0)
                    return node;
                else
                    return search(node.leftChild, value, print);
            else {
                System.out.println("There is no " + value + " in tree");
                return null;
            }
        else if (value.compareTo(node.value) > 0)
            if (node.rightChild != null)
                if (!print && value.compareTo(node.rightChild.value) == 0)
                    return node;
                else
                    return search(node.rightChild, value, print);
            else {
                System.out.println("There is no " + value + " in tree");
                return null;
            }
        else {
            if (print) {
                System.out.println(value + " was found:");
                draw(root, getHeightRec(root), false, value, false);
            }
            return node;
        }

    }

    public TreapNode<T> findkthSmallest(int k){
        count=0;
        return kthSmallest(this.getRoot(),k);
    }

    private TreapNode<T> kthSmallest(TreapNode<T> root, int k)
    {
        // base case
        if (root == null)
            return null;

        // search in left subtree
        TreapNode<T> left = kthSmallest(root.leftChild, k);

        // if k'th smallest is found in left subtree, return it
        if (left != null)
            return left;

        // if current element is k'th smallest, return it
        count++;
        if (count == k)
            return root;

        // else search in right subtree
        return kthSmallest(root.rightChild, k);
    }

    public TreapNode<T> findkthLargest(int k){
        count=0;
        return KthLargestUsingMorrisTraversal(root,k);
    }


    private TreapNode<T> KthLargestUsingMorrisTraversal(TreapNode<T> root, int k)
    {
        TreapNode<T> curr = root;
        TreapNode<T> Klargest = null;

        // count variable to keep count of visited Nodes
        int count = 0;

        while (curr != null)
        {
            // if right child is NULL
            if (curr.rightChild == null)
            {

                // first increment count and check if count = k
                if (++count == k)
                    Klargest = curr;

                // otherwise move to the left child
                curr = curr.leftChild;
            }

            else
            {

                // find inorder successor of current Node
                TreapNode<T> succ = curr.rightChild;

                while (succ.leftChild != null && succ.leftChild != curr)
                    succ = succ.leftChild;

                if (succ.leftChild == null)
                {

                    // set left child of successor to the
                    // current Node
                    succ.leftChild = curr;

                    // move current to its right
                    curr = curr.rightChild;
                }

                // restoring the tree back to original binary
                // search tree removing threaded links
                else
                {

                    succ.leftChild = null;

                    if (++count == k)
                        Klargest = curr;

                    // move current to its left child
                    curr = curr.leftChild;
                }
            }
        }
        return Klargest;
    }

    public TreapNode<T> min(){
        return getMin(root);
    }


    private TreapNode<T> getMin(TreapNode<T> root){
        if (root.leftChild==null)
            return root;

        return getMin(root.leftChild);
    }

    public TreapNode<T> max(){
        return getMax(root);
    }

    private TreapNode<T> getMax(TreapNode<T> root){
        if (root.rightChild==null)
            return root;

        return getMax(root.rightChild);
    }

}
