package com.scm.scm.support.export;

import lombok.Data;

@Data
public class ExportContactRequest {
    private String user;
    private String tenantUniqueName;
}
