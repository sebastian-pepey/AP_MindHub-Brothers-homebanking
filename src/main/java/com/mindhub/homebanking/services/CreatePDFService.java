package com.mindhub.homebanking.services;

import com.itextpdf.text.DocumentException;
import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;

public interface CreatePDFService {
    void export(HttpServletResponse response, LocalDateTime minSearchDate, LocalDateTime maxSearchDate, Account account, List<TransactionDTO> transactionDTOList) throws IOException, DocumentException, URISyntaxException;
}
