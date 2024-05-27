package com.scm.scm.support;

import com.scm.scm.contact.dto.ContactDTO;
import com.scm.scm.contact.services.ContactServices;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Logger;

@Service
public class ImportContactExcel {

    private static final Logger log = Logger.getLogger(ImportContactExcel.class.toString());

    private final ContactServices contactServices;

    @Autowired
    public ImportContactExcel(ContactServices contactServices) {
        this.contactServices = contactServices;
    }

    public void importContactsFromExcel(MultipartFile file, String userToken, String tenantUniqueName) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            List<ContactDTO> contacts = new ArrayList<>();

            // Read header row to identify columns
            Row headerRow = sheet.getRow(0);
            Map<Integer, String> headerMap = new HashMap<>();
            for (Cell cell : headerRow) {
                headerMap.put(cell.getColumnIndex(), cell.getStringCellValue());
            }

            // Read data rows
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // Skip header row
                }

                ContactDTO contact = new ContactDTO();
                contact.setId(UUID.randomUUID().toString());

                // Extract title from name and surname
                String name = getCellValueAsString(row.getCell(getColumnIndex(headerMap, "Ime", false)));
                String surname = getCellValueAsString(row.getCell(getColumnIndex(headerMap, "Priimek", false)));
                contact.setTitle(name + " " + surname);

                contact.setUser(userToken);
                contact.setTenantUniqueName(tenantUniqueName);

                // Extract comments if the column exists
                Integer commentColumnIndex = getColumnIndex(headerMap, "Komentar", true);
                if (commentColumnIndex != null) {
                    contact.setComments(getCellValueAsString(row.getCell(commentColumnIndex)));
                }

                contact.setCreatedAt(LocalDateTime.now().toString());

                Map<String, String> props = new HashMap<>();
                List<String> tags = new ArrayList<>();

                // Process all other columns
                for (Map.Entry<Integer, String> entry : headerMap.entrySet()) {
                    String columnName = entry.getValue();
                    if (columnName.equals("Ime") || columnName.equals("Priimek") || columnName.equals("Komentar")) {
                        continue;
                    }

                    String value = getCellValueAsString(row.getCell(entry.getKey()));
                    if (columnName.equalsIgnoreCase(value)) {
                        tags.add(columnName); // Add as tag if value matches column name
                    } else if (!value.isEmpty()) {
                        props.put(columnName, value);
                    }
                }

                contact.setTags(tags);
                contact.setProps(props);
                contact.setAttributesToString(contact.contactAttributesToString());

                contacts.add(contact);
            }

            contactServices.saveAllContacts(contacts);
        } catch (Exception e) {
            log.severe("Error occurred during import: " + e.getMessage());
            throw new IOException("Error occurred during import", e);
        }
    }

    private Integer getColumnIndex(Map<Integer, String> headerMap, String columnName, boolean returnNullIfNotFound) {
        return headerMap.entrySet().stream()
                .filter(entry -> entry.getValue().equalsIgnoreCase(columnName))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(returnNullIfNotFound ? null : -1);
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "";
        }
    }
}
