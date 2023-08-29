package com.mindhub.homebanking.dtos;
import com.mindhub.homebanking.models.ClientLoan;

public class LoanApplicationDTO {

    private Long loanTypeId;
    private double amount;
    private int payments;
    private String destinationAccount;

    public LoanApplicationDTO(ClientLoan clientLoan) {
        this.loanTypeId = clientLoan.getLoan().getId();
        this.amount = clientLoan.getAmount();
        this.payments = clientLoan.getPayments();
        //this.destinationAccount = destinationAccount;
    }

    public Long getLoanTypeId() {
        return loanTypeId;
    }
    public double getAmount() {
        return amount;
    }
    public int getPayments() {
        return payments;
    }
    public String getDestinationAccount() {
        return destinationAccount;
    }
}
