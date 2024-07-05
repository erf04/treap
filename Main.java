public class Main {
    public static void main(String[] args) throws InterruptedException {
        Treap<Integer> treap=new Treap<>();

        treap.insert(4);
        treap.insert(5);
        treap.insert(3);
        treap.insert(6);
//        treap.add(-5);
        treap.draw();
    }
}