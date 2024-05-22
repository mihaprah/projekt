package com.scm.scm.support.export;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ExportContactRequest {
    private String tenantUniqueName;
    private String tenantId;
    private List<String> contactIds = new ArrayList<>();
}
