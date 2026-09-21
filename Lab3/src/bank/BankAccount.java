package bank;

public class BankAccount {
    protected String accountNumber;
    protected String owner;
    protected double balance;

    public BankAccount(String accountNumber, String owner, double balance) {
        this.accountNumber = accountNumber;
        this.owner = owner;
        this.balance = balance;

    }


    public void deposit(double amount) {
        if (amount <= 0) {
            System.out.println("Сумма <= 0");
            return;
        }
        balance += amount;
    }

    public void withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Сумма <= 0");
            return;
        }
        if (amount > balance) {
            System.out.println("Не хватает денег");
            return;
        }
        balance -= amount;
    }

    public double calculateInterest() { return 0.0; }

    @Override
    public String toString() {
        return String.format("%s[№%s, владелец=%s, баланс=%.2f]",
                getOwner(), accountNumber, owner, balance);
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwner() {
        return owner;
    }

    public double getBalance() {
        return balance;
    }
}