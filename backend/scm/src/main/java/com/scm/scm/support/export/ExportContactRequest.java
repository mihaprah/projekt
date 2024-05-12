package com.scm.scm.support.export;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExportContactRequest {
    private String user;
    private String tenantUniqueName;
    private String tenantId;
}
