public class Main {
    public static void main(String[] args) throws InterruptedException {
        Treap<Long> treap=new Treap<>();

        for (long i=0;i<10;i++){
            treap.insert(i);
        }

        System.out.println(treap.max().value);
//        treap.draw();
//        treap.search((long)6);
    }
}