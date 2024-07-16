public class TreapNode<T extends Comparable<T>> {
    T value;
    TreapNode<T> leftChild;
    TreapNode<T> rightChild;
    int priority;

    TreapNode<T> successor;
    TreapNode<T> predecessor;

    int height;
    int size;


    public TreapNode(T value,TreapNode<T> predecessor,TreapNode<T> successor) {
        this.value = value;
        this.priority = (int) (Math.random() * 100);
        this.leftChild = null;
        this.rightChild = null;
        this.successor=successor;
        this.predecessor=predecessor;
        this.height=1;
        this.size=1;
    }

    public TreapNode() {
        this.value = null;
        this.priority = Integer.MIN_VALUE;
        this.leftChild = null;
        this.rightChild = null;
    }

    @Override
    public String toString() {
        return this.value.toString() + ":" + this.priority;
    }
}
