package com.scm.scm.support.export;

import com.scm.scm.contact.dto.ContactDTO;
import com.scm.scm.contact.services.ContactServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
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

    public ResponseEntity<byte[]> exportContacts(String tenantUniqueName) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormat.format(new Date());

        List<ContactDTO> contacts = contactServices.findAllContacts(tenantUniqueName);

        StringBuilder builder = new StringBuilder();
        builder.append("Id,Title,User,TenantUniqueName,Comments,CreatedAt,Tags,Props,AttributesToString\n");

        for (ContactDTO contact : contacts) {
            builder.append(contact.getId()).append(",");
            builder.append(contact.getTitle()).append(",");
            builder.append(contact.getUser()).append(",");
            builder.append(contact.getTenantUniqueName()).append(",");
            builder.append(contact.getComments()).append(",");
            builder.append(contact.getCreatedAt()).append(",");
            builder.append(contact.getTags()).append(",");
            builder.append(contact.getProps()).append(",");
            builder.append(contact.getAttributesToString()).append("\n");
        }

        byte[] buffer = builder.toString().getBytes(StandardCharsets.UTF_8);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + tenantUniqueName + "_contacts_" + currentDateTime + ".csv");

        return ResponseEntity.ok()
                .headers(headers)
                .body(buffer);
    }
}