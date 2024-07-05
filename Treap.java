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

    public static TreapNode rightRotate(TreapNode y) {
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



    void printTree() {
        if (root == null) {
            return;
        }

        Queue<TreapNode> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            StringBuilder levelStr = new StringBuilder();

            for (int i = 0; i < levelSize; i++) {
                TreapNode currentNode = queue.poll();
                System.out.println("height :" + getHeightRec(currentNode));
                String rightPadding=" ".repeat(getHeightRec(currentNode)*2);
                levelStr.append(rightPadding)
                        .append(currentNode.value).append(" ".repeat(3));
//                System.out.println(levelStr);

                if (currentNode.leftChild != null) {
                    queue.add(currentNode.leftChild);
                } else if (currentNode.rightChild != null) {
                    queue.add(new TreapNode(-1,this.getPriority())); // Placeholder for formatting
                }

                if (currentNode.rightChild != null) {
                    queue.add(currentNode.rightChild);
                } else if (currentNode.leftChild != null) {
                    queue.add(new TreapNode(-1,this.getPriority())); // Placeholder for formatting
                }
            }
            System.out.println(levelStr.toString());
        }
    }

    private void draw(TreapNode<T> node, int count, Boolean pause, T coloredvalue, Boolean isDel) throws InterruptedException {

        go(node, 0);
        for (int i = 0; i < count + 1; i++) {
            for (int j = 0; j < (2 * (1 << (count - i - 1)) - 1); j++)
                System.out.print(" ");

            for (int j = 0; j < this.a.get(i).size(); j++) {
                String col = ANSI_CYAN;
                if (coloredvalue != null)
                    if (isDel)
                        col = (a.get(i).get(j).equals(coloredvalue.toString())) ? ANSI_RED : ANSI_CYAN;
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
        draw(root, getHeightRec(root), true, null, true);
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

}
