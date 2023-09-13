package com.mindhub.homebanking.services.implement;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.services.CreatePDFService;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

@Service
public class CreatePDFServiceImplement implements CreatePDFService {

    @Override
    public void export(HttpServletResponse response, LocalDateTime minSearchDate, LocalDateTime maxSearchDate, Account account, List<TransactionDTO> transactionDTOList) throws IOException, DocumentException, URISyntaxException {
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        PdfWriter.getInstance(document,response.getOutputStream());
        document.open();

        // Parámetros del PDF

        Font fontTitle = FontFactory.getFont("Verdana", 18, Font.BOLD);
        Font fontParagraph = FontFactory.getFont("Verdana", 12, Font.NORMAL);
        Font fontParagraphBold = FontFactory.getFont("Verdana", 12, Font.BOLD);

        Paragraph date = new Paragraph(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss")), fontParagraph);
        date.setAlignment(Element.ALIGN_RIGHT);
        document.add(date);

        Image img = Image.getInstance("http://localhost:8080/web/img/Mindhub-logo.png");
        img.scalePercent(25);
        document.add(img);

        // Título del Documento
        Paragraph title = new Paragraph("Resumen de Cuenta " + account.getClient().getFirstName()+" "+account.getClient().getLastName(), fontTitle);
        title.setSpacingBefore(10);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        Paragraph accountNumberParagraph = new Paragraph("Número de cuenta " + account.getAccountNumber(), fontParagraph);
        accountNumberParagraph.setSpacingBefore(10);
        accountNumberParagraph.setAlignment(Element.ALIGN_JUSTIFIED);
        document.add(accountNumberParagraph);

        Paragraph accountBalanceParagraph = new Paragraph("Balance al día de la fecha: " + String.format("$ %.2f",account.getAccountBalance()) , fontParagraph);
        accountBalanceParagraph.setSpacingBefore(10);
        accountBalanceParagraph.setAlignment(Element.ALIGN_JUSTIFIED);
        document.add(accountBalanceParagraph);

        Paragraph transactionParagraph = new Paragraph("Transacciones entre las fechas : " + minSearchDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss")) +" y "+ maxSearchDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss")), fontParagraphBold);
        transactionParagraph.setSpacingBefore(10);
        transactionParagraph.setAlignment(Element.ALIGN_JUSTIFIED);
        document.add(transactionParagraph);

        PdfPTable table = new PdfPTable(new float[]{10,25,35,30});
        table.setSpacingBefore(10);
        addTableHeader(table);
        addRows(table,transactionDTOList);

        document.add(table);
        document.close();

        document.close();
    }

    private void addTableHeader(PdfPTable table) {
        Stream.of("Nº", "Descripción", "Fecha","Monto")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

    private void addRows(PdfPTable table, List<TransactionDTO> transactionDTOList) {
        for (TransactionDTO transaction : transactionDTOList) {
            table.addCell(Long.toString(transaction.getId()));
            table.addCell(transaction.getDescription());
            table.addCell(transaction.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss")));
            table.addCell(String.format("$ %.2f",transaction.getAmount()));
        }
    }
}
