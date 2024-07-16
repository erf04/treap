import java.util.*;

public class Treap<T extends Comparable<T>>  {

    private TreapNode<T> root;
    private HashMap<Integer, ArrayList<String>> a = new HashMap<>();
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RED = "\u001B[31m";
    private static final String empty = "  ";
    private static int count =0;
    private TreapNode<T> largest;
    private TreapNode<T> smallest;


    public boolean isEmpty(){
        return this.root==null;
    }

    public T getLargestValue() {
        return largest.value;
    }

    public T getSmallestValue() {
        return smallest.value;
    }

    Treap(){
        this.root=null;
    }

    public TreapNode getRoot() {
        return root;
    }

    int getHeight() {
        return height(this.root);
    }

    // A utility function to compute the height of the tree
    private static int getHeightRec(TreapNode node) {
        if (node == null) {
            return -1;
        } else {
            int leftHeight = getHeightRec(node.leftChild);
            int rightHeight = getHeightRec(node.rightChild);


            return Math.max(leftHeight, rightHeight) + 1;
        }
    }

    private int height(TreapNode<T> node){
        return node==null? 0 : node.height;
    }

    private int size(TreapNode<T> node) {
        return node == null ? 0 : node.size;
    }

    private void setNewHeightAndSize(TreapNode<T> node){
        if (node != null) {
            node.height = Math.max(height(node.leftChild), height(node.rightChild)) + 1;
            node.size = size(node.leftChild) + size(node.rightChild) + 1;
        }
    }

    public void insert(T value){
        this.root=insertRec(this.root,value,null,null);
    }

    // Recursive function to insert a new key in the BST and set pred/succ
    private TreapNode<T> insertRec(TreapNode<T> root, T key, TreapNode<T> pred, TreapNode<T> succ) {
        // If the tree is empty, return a new node
        if (root == null) {
            TreapNode<T> newNode = new TreapNode<T>(key,pred,succ);
//            newNode.predecessor = pred;
//            newNode.successor = succ;
            if (pred != null)
                pred.successor = newNode;
            if (succ != null)
                succ.predecessor = newNode;
            if (pred == null)
                this.smallest = newNode;
            if (succ == null)
                this.largest = newNode;
            return newNode;
        }

        // Otherwise, recur down the tree
        if (key.compareTo(root.value) < 0) {
            root.leftChild = insertRec(root.leftChild, key, pred, root);
            if (root.leftChild != null && root.leftChild.priority > root.priority)
                root = rightRotate(root);

        } else if (key.compareTo(root.value) > 0) {
            root.rightChild = insertRec(root.rightChild, key, root, succ);
            if (root.rightChild != null && root.rightChild.priority > root.priority)
                root = leftRotate(root);
        }

        setNewHeightAndSize(root);

        return root;
    }

    public boolean contains(T value){
        return containsRec(this.root,value);
    }

    private boolean containsRec(TreapNode<T> rootNode,T value){
        if (rootNode==null)
            return false;
        if (rootNode.value.compareTo(value)==0)
            return true;
        return containsRec(rootNode.value.compareTo(value) < 0 ? rootNode.rightChild : rootNode.leftChild,value);

    }

    public TreapNode<T> search(T value){
        return searchRec(this.root,value);
    }

    private TreapNode<T> searchRec(TreapNode<T> rootNode,T value){
        if (root == null) {
            return null;
        }
        if (root.value.compareTo(value)==0)
            return root;

        if (value.compareTo(root.value) < 0 ) {
            return searchRec(root.leftChild, value);
        }

        return searchRec(root.rightChild, value);
    }



    public TreapNode<T> remove(T value){
        this.root=deleteRec(this.root,value);
        return root;
    }
    private TreapNode<T> deleteRec(TreapNode<T> rootNode, T key) {
        if (rootNode == null) return rootNode;

        if (key.compareTo(rootNode.value)<0 ) {
            rootNode.leftChild = deleteRec(rootNode.leftChild, key);
        } else if (key.compareTo(rootNode.value) > 0) {
            rootNode.rightChild = deleteRec(rootNode.rightChild, key);
        } else {
            if (rootNode.leftChild == null) {
                TreapNode<T> temp = rootNode.rightChild;
                if (rootNode.predecessor != null)
                    rootNode.predecessor.successor = rootNode.successor;
                if (rootNode.successor != null)
                    rootNode.successor.predecessor = rootNode.predecessor;
                if (rootNode == smallest)
                    smallest = rootNode.successor;
                if (rootNode == largest)
                    largest = rootNode.predecessor;
                rootNode = null;
                return temp;
            } else if (rootNode.rightChild == null) {
                TreapNode<T> temp = rootNode.leftChild;
                if (rootNode.predecessor != null)
                    rootNode.predecessor.successor = rootNode.successor;
                if (rootNode.successor != null)
                    rootNode.successor.predecessor = rootNode.predecessor;
                if (rootNode == smallest)
                    smallest = rootNode.successor;
                if (rootNode == largest)
                    largest = rootNode.predecessor;
                rootNode = null;
                return temp;
            }

            if (rootNode.leftChild.priority < rootNode.rightChild.priority) {
                rootNode = leftRotate(rootNode);
                rootNode.leftChild = deleteRec(rootNode.leftChild, key);
            } else {
                rootNode = rightRotate(rootNode);
                rootNode.rightChild = deleteRec(rootNode.rightChild, key);
            }
        }

        // Update height of the current node
        setNewHeightAndSize(rootNode);

        return rootNode;
    }

    TreapNode<T> rightRotate(TreapNode<T> node) {
        TreapNode<T> left = node.leftChild;
        TreapNode<T> leftRight = left.rightChild;

        // Perform rotation
        left.rightChild = node;
        node.leftChild = leftRight;
//
//        // Update predecessor and successor pointers
//        if (node.predecessor != null)
//            node.predecessor.successor = left;
//        if (left.successor != null)
//            left.successor.predecessor = node;
//
//        left.predecessor = node.predecessor;
//        node.successor = left.successor;
//
//        node.predecessor = left;
//        left.successor = node;
        setNewHeightAndSize(node);
        setNewHeightAndSize(left);

        return left;
    }

    public  TreapNode<T> leftRotate(TreapNode<T> x) {
        TreapNode<T> right = x.rightChild;
        TreapNode<T> rightLeft = right.leftChild;
//
        right.leftChild = x;
        x.rightChild = rightLeft;
//
//        if (x.successor != null) x.successor.predecessor = right;
//        if (right.predecessor != null) right.predecessor.successor = x;
//
//        right.predecessor = x.predecessor;
//        x.successor = right.successor;
//
//        x.predecessor = right;
//        right.successor = x;
        setNewHeightAndSize(x);
        setNewHeightAndSize(right);

        return right;
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
//    public boolean search(T value) throws InterruptedException {
//        return search(root, value, true) != null;
//    }

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
        return this.smallest;
    }



    public TreapNode<T> max(){
        return this.largest;
    }

    public List<T> inOrder(){
        List<T> inorderList=new ArrayList<>();
        TreapNode<T> current=this.smallest;
        while (current!=null){
            inorderList.add(current.value);
            current=current.successor;
        }
        return inorderList;
    }

    public TreapNode<T> kthSmallest(int k){
        return kthSmallest(this.root,k);
    }
    private TreapNode<T> kthSmallest(TreapNode<T> root, int k) {
        if (root == null) return null;

        int leftSize = size(root.leftChild);
        if (k == leftSize + 1) {
            return root;
        } else if (k <= leftSize) {
            return kthSmallest(root.leftChild, k);
        } else {
            return kthSmallest(root.rightChild, k - leftSize - 1);
        }
    }

    public TreapNode<T> kthLargest(int k){
        return kthLargest(this.root,k);
    }
    private TreapNode<T> kthLargest(TreapNode<T> root, int k) {
        if (root == null) return null;

        int rightSize = size(root.rightChild);
        if (k == rightSize + 1) {
            return root;
        } else if (k <= rightSize) {
            return kthLargest(root.rightChild, k);
        } else {
            return kthLargest(root.leftChild, k - rightSize - 1);
        }
    }


}
