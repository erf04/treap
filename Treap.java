import java.util.*;

public class Treap<T extends Comparable<T>>  {

    private TreapNode<T> root;

    private TreapNode<T> largest;
    private TreapNode<T> smallest;


    Treap(){
        this.root=null;
    }

    public TreapNode<T> getRoot() {
        return root;
    }

    int getHeight() {
        return height(this.root);
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

    private TreapNode<T> insertRec(TreapNode<T> root, T key, TreapNode<T> pred, TreapNode<T> succ) {

        if (root == null) {
            TreapNode<T> newNode = new TreapNode<T>(key,pred,succ);

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

        setNewHeightAndSize(rootNode);

        return rootNode;
    }

    TreapNode<T> rightRotate(TreapNode<T> node) {
        TreapNode<T> left = node.leftChild;
        TreapNode<T> leftRight = left.rightChild;


        left.rightChild = node;
        node.leftChild = leftRight;

        setNewHeightAndSize(node);
        setNewHeightAndSize(left);

        return left;
    }

    public  TreapNode<T> leftRotate(TreapNode<T> x) {
        TreapNode<T> right = x.rightChild;
        TreapNode<T> rightLeft = right.leftChild;
        right.leftChild = x;
        x.rightChild = rightLeft;

        setNewHeightAndSize(x);
        setNewHeightAndSize(right);

        return right;
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
