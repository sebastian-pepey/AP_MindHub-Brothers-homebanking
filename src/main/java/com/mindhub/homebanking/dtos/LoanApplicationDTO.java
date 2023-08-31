package com.mindhub.homebanking.dtos;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;

import javax.persistence.ElementCollection;
import java.util.List;

public class LoanApplicationDTO {

    private String loanId;
    private String amount;
    private String payments;
    private String toAccountNumber;

    public LoanApplicationDTO() {
    }

    public String getLoanId() {
        return loanId;
    }

    public String getAmount() {
        return amount;
    }

    public String getPayments() {
        return payments;
    }

    public String getToAccountNumber() {
        return toAccountNumber;
    }
}

