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
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class ExportContactExcel {

    private static final Logger log = Logger.getLogger(ExportContactExcel.class.toString());

    private final ContactServices contactServices;

    @Autowired
    public ExportContactExcel(ContactServices contactServices) {
        this.contactServices = contactServices;
    }

    public ResponseEntity<byte[]> exportContacts(String tenantUniqueName, List<String> contactIds) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormat.format(new Date());

        List<ContactDTO> contacts = contactServices.findAllContacts(tenantUniqueName, false);

        if (!contactIds.isEmpty()) {
            contacts = contacts.stream()
                    .filter(contact -> contactIds.contains(contact.getId()))
                    .collect(Collectors.toList());
        }

        if (contacts.isEmpty()) {
            log.info("No contacts found for the provided IDs.");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Contacts");

        Set<String> allTags = new HashSet<>();
        Set<String> allProps = new HashSet<>();

        for (ContactDTO contact : contacts) {
            allTags.addAll(contact.getTags());
            allProps.addAll(contact.getProps().keySet());
        }

        List<String> sortedTags = new ArrayList<>(allTags);
        List<String> sortedProps = new ArrayList<>(allProps);
        Collections.sort(sortedTags);
        Collections.sort(sortedProps);

        Row headerRow = sheet.createRow(0);
        int colIdx = 0;
        headerRow.createCell(colIdx++).setCellValue("Id");
        headerRow.createCell(colIdx++).setCellValue("Title");
        headerRow.createCell(colIdx++).setCellValue("User");
        headerRow.createCell(colIdx++).setCellValue("TenantUniqueName");
        headerRow.createCell(colIdx++).setCellValue("Comments");
        headerRow.createCell(colIdx++).setCellValue("CreatedAt");

        for (String tag : sortedTags) {
            headerRow.createCell(colIdx++).setCellValue(tag);
        }

        for (String prop : sortedProps) {
            headerRow.createCell(colIdx++).setCellValue(prop);
        }

        for (int i = 0; i < contacts.size(); i++) {
            ContactDTO contact = contacts.get(i);
            Row row = sheet.createRow(i + 1);
            int cellIdx = 0;
            row.createCell(cellIdx++).setCellValue(contact.getId());
            row.createCell(cellIdx++).setCellValue(contact.getTitle());
            row.createCell(cellIdx++).setCellValue(contact.getUser());
            row.createCell(cellIdx++).setCellValue(contact.getTenantUniqueName());
            row.createCell(cellIdx++).setCellValue(contact.getComments());
            row.createCell(cellIdx++).setCellValue(contact.getCreatedAt().toString());

            for (String tag : sortedTags) {
                row.createCell(cellIdx++).setCellValue(contact.getTags().contains(tag) ? "true" : "false");
            }

            for (String prop : sortedProps) {
                String propValue = contact.getProps().getOrDefault(prop, "/");
                row.createCell(cellIdx++).setCellValue(propValue);
            }
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
