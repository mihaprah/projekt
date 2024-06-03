package com.scm.scm.support.export;

import com.scm.scm.contact.dto.ContactDTO;
import com.scm.scm.contact.services.ContactServices;
import org.apache.poi.ooxml.POIXMLProperties;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFColor;
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
                    .toList();
        }

        if (contacts.isEmpty()) {
            log.info("No contacts found for the provided IDs.");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        XSSFWorkbook  workbook = new XSSFWorkbook();

        POIXMLProperties xmlProps = workbook.getProperties();
        POIXMLProperties.CoreProperties coreProps = xmlProps.getCoreProperties();
        coreProps.setCreator("SCM");

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

        headerRow.createCell(colIdx++).setCellValue("Title");

        for (String prop : sortedProps) {
            headerRow.createCell(colIdx++).setCellValue(prop);
        }

        for (String tag : sortedTags) {
            headerRow.createCell(colIdx++).setCellValue(tag);
        }

        headerRow.createCell(colIdx++).setCellValue("Comments");

        // Set header row style
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);

        XSSFColor blueColor = new XSSFColor(new java.awt.Color(0, 123, 255), null);
        headerStyle.setFillForegroundColor(blueColor);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        for (Cell cell : headerRow) {
            cell.setCellStyle(headerStyle);
        }

        // Create alternating row style with light gray color
        CellStyle evenRowStyle = workbook.createCellStyle();
        XSSFColor lightGrayColor = new XSSFColor(new java.awt.Color(220, 220, 220), null);
        evenRowStyle.setFillForegroundColor(lightGrayColor);
        evenRowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle defaultCellStyle = workbook.createCellStyle();
        defaultCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        defaultCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        for (int i = 0; i < contacts.size(); i++) {
            ContactDTO contact = contacts.get(i);
            Row row = sheet.createRow(i + 1);
            int cellIdx = 0;

            Cell cellTitle = row.createCell(cellIdx++);
            cellTitle.setCellValue(contact.getTitle());
            cellTitle.setCellStyle((i % 2 == 0) ? evenRowStyle : defaultCellStyle);

            for (String prop : sortedProps) {
                String propValue = contact.getProps().getOrDefault(prop, "");
                Cell cell = row.createCell(cellIdx++);
                cell.setCellValue(propValue);
                cell.setCellStyle((i % 2 == 0) ? evenRowStyle : defaultCellStyle);
            }

            for (String tag : sortedTags) {
                Cell cell = row.createCell(cellIdx++);
                cell.setCellValue(contact.getTags().contains(tag) ? tag : "");
                cell.setCellStyle((i % 2 == 0) ? evenRowStyle : defaultCellStyle);
            }

            Cell cell = row.createCell(cellIdx++);
            cell.setCellValue(contact.getComments());
            cell.setCellStyle((i % 2 == 0) ? evenRowStyle : defaultCellStyle);
        }

        // Enable filtering
        sheet.setAutoFilter(new CellRangeAddress(0, 0, 0, headerRow.getPhysicalNumberOfCells() - 1));

        // Auto size columns
        for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
            sheet.autoSizeColumn(i);
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 1024);
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
