public class Main {
    public static void main(String[] args) throws InterruptedException {
//        Treap<Integer> treap=new Treap<>();
//
//        for (int i=0;i<10;i++){
//            treap.insert(i);
//        }
//
//        treap.remove(3);
//        treap.inOrder().forEach(s ->{
//            System.out.println(s.toString());
//        });
//        System.out.println(treap.contains(3));
//        System.out.println(treap.max());
//        System.out.println(treap.kthLargest(5));
////        treap.draw();
////        treap.search((long)6);
        
        
        // kesht main
        int sum=0;
        for (int i = 0; i < 1000; i++) {
            Treap<Integer> treap=new Treap<>();
            for (int j = 1; j <= 100000; j++) {
                treap.insert(j);
            }
            sum+= treap.getHeight();
            System.out.println(sum);
        }
        System.out.println(sum/1000);
    }
}