//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import Shop.Smartphone;

public class Main {
    public static void main(String[] args) {
        Smartphone smart1 = new Smartphone("123456789012345", "x300", "Vivo", 1000000, 4000);
        Smartphone smart2 = new Smartphone("123456789012345", "x300", "Vivo", 1000000, 4000);
        System.out.println(Smartphone.count);
    }
}