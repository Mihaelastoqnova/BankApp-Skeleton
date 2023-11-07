package bank.entities.loan;

public class StudentLoan extends BaseLoan{
    private static final int INTEREST = 1;
    private static final double AMOUNT = 10000;
    public StudentLoan() {
        super(INTEREST, AMOUNT);
    }

    @Override
    public int getInterestRate() {
        return INTEREST;
    }

    @Override
    public double getAmount() {
        return AMOUNT;
    }
}
