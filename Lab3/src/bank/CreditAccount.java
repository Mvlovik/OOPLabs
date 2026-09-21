package bank;

public class CreditAccount extends BankAccount {
    private final double creditLimit; // максималный минус
    private final double creditInterestRate; // годовая ставка

    public CreditAccount(String accountNumber, String owner, double balance,
                         double creditLimit, double creditInterestRate) {
        super(accountNumber, owner, balance);
        if (creditLimit < 0) {
            System.out.println("Кредитный лимит < 0");
        }
        if (creditInterestRate < 0) {
            System.out.println("Ставка < 0");
        }
        this.creditLimit = creditLimit;
        this.creditInterestRate = creditInterestRate;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public double getCreditInterestRate() {
        return creditInterestRate;
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("amount <= 0");
        }
        if (balance - amount < -creditLimit) {
            System.out.println("кредитный лимит");
        }
        balance -= amount;
    }

    @Override
    public double calculateInterest() {
        if (balance < 0) return -balance * creditInterestRate;

        return 0.0;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" (лимит=%.2f, ставка по кредиту=%.2f%%)",
                creditLimit, creditInterestRate * 100);
    }
}