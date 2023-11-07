package bank.entities.loan;

public class MortgageLoan extends BaseLoan{
    private static final int INTEREST = 3;
    private static final double AMOUNT = 50000;
    public MortgageLoan() {
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
