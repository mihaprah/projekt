package com.scm.scm.support.export;

import com.scm.scm.contact.dto.ContactDTO;
import com.scm.scm.contact.services.ContactServices;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Service
public class ExportContactExcel {

    private static final Logger log = Logger.getLogger(ExportContactExcel.class.toString());

    @Autowired
    private ContactServices contactServices;

    public ResponseEntity<byte[]> exportContacts(String tenantUniqueName, List<String> contactIds) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormat.format(new Date());

        List<ContactDTO> contacts = contactServices.findAllContacts(tenantUniqueName);

        if (!contactIds.isEmpty()) {
            contacts = contacts.stream()
                    .filter(contact -> contactIds.contains(contact.getId()))
                    .toList();
        }

        if (contacts.isEmpty()) {
            log.info("No contacts found for the provided IDs.");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Contacts");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Id");
        headerRow.createCell(1).setCellValue("Title");
        headerRow.createCell(2).setCellValue("User");
        headerRow.createCell(3).setCellValue("TenantUniqueName");
        headerRow.createCell(4).setCellValue("Comments");
        headerRow.createCell(5).setCellValue("CreatedAt");
        headerRow.createCell(6).setCellValue("Tags");
        headerRow.createCell(7).setCellValue("Props");
        headerRow.createCell(8).setCellValue("AttributesToString");

        for (int i = 0; i < contacts.size(); i++) {
            ContactDTO contact = contacts.get(i);
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(contact.getId());
            row.createCell(1).setCellValue(contact.getTitle());
            row.createCell(2).setCellValue(contact.getUser());
            row.createCell(3).setCellValue(contact.getTenantUniqueName());
            row.createCell(4).setCellValue(contact.getComments());
            row.createCell(5).setCellValue(contact.getCreatedAt());
            row.createCell(6).setCellValue(contact.getTags());
            row.createCell(7).setCellValue(contact.getProps());
            row.createCell(8).setCellValue(contact.getAttributesToString());
        }

        byte[] bytes;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            workbook.write(bos);
            bytes = bos.toByteArray();
        } catch (IOException e) {
            log.severe("Error while creating Excel file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                log.severe("Error while closing workbook: " + e.getMessage());
            }
        }

        String filename = tenantUniqueName + "_contacts_" + currentDateTime + ".xlsx";
        String encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFilename);

        return ResponseEntity.ok()
                .headers(headers)
                .body(bytes);
    }
}