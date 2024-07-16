public class Main {
    public static void main(String[] args) throws InterruptedException {

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