package bank;

public class SavingsAccount extends BankAccount {
    private double interestRate; // годовая ставка

    public SavingsAccount(String accountNumber, String owner, double balance, double interestRate) {
        super(accountNumber, owner, balance);
        if (interestRate < 0) {
            System.out.println("interestRate < 0");
        }
        this.interestRate = interestRate;
    }

    @Override
    public double calculateInterest() {
        return balance * interestRate;
    }

    public void applyInterest() {
        balance += calculateInterest();
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" (ставка=%.2f%%)", interestRate * 100);
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        if (interestRate < 0) {
            System.out.println("Процентная ставка <= 0");
        } else {
            this.interestRate = interestRate;
        }
    }
}