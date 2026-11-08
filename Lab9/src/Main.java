import java.util.Random;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Matrix a = Matrix.random(4, 5);
        Matrix b = Matrix.random(4, 5);
        System.out.println("A:");
        a.print();
        System.out.println("B:");
        b.print();
        Matrix c = a.addParallel(b, 4);
        System.out.println("A+B:");
        c.print();
        System.out.println("ok: " + c.equals(a.add(b)));
    }
}
