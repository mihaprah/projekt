package com.scm.scm.support.export;

import com.scm.scm.contact.dto.ContactDTO;
import com.scm.scm.contact.services.ContactServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
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

    public void exportContacts(String tenantUniqueName) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormat.format(new Date());

        List<ContactDTO> contacts = contactServices.findAllContacts(tenantUniqueName);
        try (FileWriter writer = new FileWriter(tenantUniqueName + "_contacts_" + currentDateTime + ".csv")) {
            writer.append("Id,Title,User,TenantUniqueName,Comments,CreatedAt,Tags,Props,AttributesToString\n");

            for (ContactDTO contact : contacts) {
                writer.append(contact.getId()).append(",");
                writer.append(contact.getTitle()).append(",");
                writer.append(contact.getUser()).append(",");
                writer.append(contact.getTenantUniqueName()).append(",");
                writer.append(contact.getComments()).append(",");
                writer.append(contact.getCreatedAt()).append(",");
                writer.append(contact.getTags()).append(",");
                writer.append(contact.getProps()).append(",");
                writer.append(contact.getAttributesToString()).append("\n");
            }

            log.info("Contacts exported successfully");

        } catch (IOException e) {
            log.severe("Error exporting contacts: " + e.getMessage());
        }
    }
}