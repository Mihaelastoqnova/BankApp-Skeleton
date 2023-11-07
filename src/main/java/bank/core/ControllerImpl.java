package bank.core;

import bank.common.ConstantMessages;
import bank.common.ExceptionMessages;
import bank.entities.bank.Bank;
import bank.entities.bank.BranchBank;
import bank.entities.bank.CentralBank;
import bank.entities.client.Adult;
import bank.entities.client.Client;
import bank.entities.client.Student;
import bank.entities.loan.Loan;
import bank.entities.loan.MortgageLoan;
import bank.entities.loan.StudentLoan;
import bank.repositories.LoanRepository;

import java.util.LinkedHashMap;
import java.util.Map;

public class ControllerImpl implements Controller {
    private LoanRepository loanRepository;
    private Map<String, Bank> banks;

    public ControllerImpl() {
        this.loanRepository = new LoanRepository();
        this.banks = new LinkedHashMap<>();
    }

    @Override
    public String addBank(String type, String name) {
        Bank bank;
        if (type.equals("CentralBank")) {
            bank = new CentralBank(name);
        } else if (type.equals("BranchBank")) {
            bank = new BranchBank(name);
        } else {
            throw new IllegalArgumentException(ExceptionMessages.INVALID_BANK_TYPE);
        }
        this.banks.put(name, bank);
        return String.format(ConstantMessages.SUCCESSFULLY_ADDED_BANK_OR_LOAN_TYPE, type);
    }

    @Override
    public String addLoan(String type) {
        Loan loan;
        if (type.equals("StudentLoan")) {
            loan = new StudentLoan();
        } else if (type.equals("MortgageLoan")) {
            loan = new MortgageLoan();
        } else {
            throw new IllegalArgumentException(ExceptionMessages.INVALID_LOAN_TYPE);
        }
        this.loanRepository.addLoan(loan);
        return String.format(ConstantMessages.SUCCESSFULLY_ADDED_BANK_OR_LOAN_TYPE, type);
    }

    @Override
    public String returnedLoan(String bankName, String loanType) {
        Loan loan = loanRepository.findFirst(loanType);
        if (loan == null) {
            throw new IllegalArgumentException(String.format(ExceptionMessages.NO_LOAN_FOUND, loanType));
        }
        this.banks.get(bankName).addLoan(loan);
        loanRepository.removeLoan(loan);
        return String.format(ConstantMessages.SUCCESSFULLY_ADDED_CLIENT_OR_LOAN_TO_BANK, loanType, bankName);
    }

    @Override
    public String addClient(String bankName, String clientType, String clientName, String clientID, double income) {
        Client client;
        if (clientType.equals("Student")) {
            client = new Student(clientName, clientID, income);
        } else if (clientType.equals("Adult")) {
            client = new Adult(clientName, clientID, income);
        } else {
            throw new IllegalArgumentException(ExceptionMessages.INVALID_CLIENT_TYPE);
        }
        Bank bank = this.banks.get(bankName);
        bank.getClients().add(client);
        if (clientType.equals("Student") && bank.getClass().getSimpleName().equals("BranchBank")) {
            return String.format(ConstantMessages.SUCCESSFULLY_ADDED_CLIENT_OR_LOAN_TO_BANK, clientType, bankName);
        } else if(clientType.equals("Adult") && bank.getClass().getSimpleName().equals("CentralBank")){
            return String.format(ConstantMessages.SUCCESSFULLY_ADDED_CLIENT_OR_LOAN_TO_BANK, clientType, bankName);
        }
        return String.format(ConstantMessages.UNSUITABLE_BANK);
    }

    @Override
    public String finalCalculation(String bankName) {
        Bank bank = this.banks.get(bankName);
        double sum = 0;
        for (Client client : bank.getClients()) {
            sum += client.getIncome();
        }
        for (Loan loan : bank.getLoans()) {
            sum += loan.getAmount();
        }
        return String.format(ConstantMessages.FUNDS_BANK, bankName, sum);
    }

    @Override
    public String getStatistics() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Bank> entry : this.banks.entrySet()) {
            sb.append(entry.getValue().getStatistics());
        }
        return sb.toString().trim();
    }
}
