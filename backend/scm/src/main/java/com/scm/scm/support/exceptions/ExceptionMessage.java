package com.scm.scm.support.exceptions;

import lombok.Getter;

@Getter
public enum ExceptionMessage {
    USER_ACCESS_CONTACT("User does not have access to this contact"),
    USER_ACCESS_TENANT("User does not have access to this tenant"),
    COLLECTION_NOT_EXIST("Collection does not exist"),
    TENANT_NOT_FOUND("Tenant not found"),
    SEARCH_NOT_FOUND("PredefinedSearch not found"),
    TENANT_NULL("Tenant is null"),
    TENANT_NAME_EMPTY("TenantUniqueName name is empty"),
    EVENT_STATE_NOT_VALID("Event state is not valid");

    private final String exceptionMessage;

    ExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }
}
