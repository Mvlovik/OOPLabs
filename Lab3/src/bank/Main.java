package bank;

public class Main {
    public static void main(String[] args) {
        SavingsAccount savings = new SavingsAccount("1", "User1", 10_000, 0.05);
        CreditAccount credit = new CreditAccount("2", "User2", 0, 50_000, 0.20);

        savings.deposit(5_000);
        credit.withdraw(20_000); // минус

        System.out.println("Проценты");
        System.out.println(savings);
        System.out.printf(" проценты: %.2f%n%n", savings.calculateInterest());
        System.out.println(credit);
        System.out.printf(" проценты: %.2f%n%n", credit.calculateInterest());

        savings.applyInterest();
        System.out.println("После: " + savings);

        // Лимиты
        System.out.println();
        credit.withdraw(40_000);

        System.out.println("\nИТОГОО");
        System.out.println(savings);
        System.out.printf(" проценты: %.2f%n%n", savings.calculateInterest());
        System.out.println(credit);
        System.out.printf(" проценты: %.2f%n%n", credit.calculateInterest());
    }
}