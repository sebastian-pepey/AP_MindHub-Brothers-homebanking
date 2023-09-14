package com.mindhub.homebanking.controllers;
import com.itextpdf.text.*;
import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.CreatePDFService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/api")
public class ExtrasController {

    @Autowired
    private CreatePDFService createPDFService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @GetMapping("/report")
    public ResponseEntity<String> convertPdf(
        HttpServletResponse response,
        @RequestParam(value = "id") Long id,
        @RequestParam(value = "minSearchDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime minSearchDate,
        @RequestParam(value = "maxSearchDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime maxSearchDate,
        Authentication authentication) throws IOException, DocumentException, URISyntaxException {

        try {
            if (accountService.getAccountById(id).getClient() != clientService.findByEmail(authentication.getName())) {
                return new ResponseEntity<>("The client Authenticated does not have the requested account", HttpStatus.FORBIDDEN);
            }

            if (minSearchDate == null || maxSearchDate == null) {
                return new ResponseEntity<>("At least one of the Dates is null", HttpStatus.FORBIDDEN);
            }
            if (minSearchDate.isAfter(maxSearchDate)) {
                return new ResponseEntity<>("The dates placed are wrong - Final Date is before Initial Date", HttpStatus.FORBIDDEN);
            }

            Account account = accountService.findByIdAndClient(id, clientService.findByEmail(authentication.getName()));
            List<TransactionDTO> transactionDTOList = transactionService.findByDateBetweenAndAccount(minSearchDate,maxSearchDate,account);

            response.setContentType("application/pdf");
            DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");

            String currentDateTime = dateFormatter.format(new Date());

            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=transactions_report_" + currentDateTime + ".pdf";
            response.setHeader(headerKey, headerValue);

            createPDFService.export(response, minSearchDate, maxSearchDate, account, transactionDTOList);

            return new ResponseEntity<>("Document Created", HttpStatus.OK);

        } catch(NullPointerException e) {
            return new ResponseEntity<>("The id Account does not exist", HttpStatus.FORBIDDEN);
        }
    }
}
