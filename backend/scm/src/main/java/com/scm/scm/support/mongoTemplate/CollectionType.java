package com.scm.scm.support.mongoTemplate;

import lombok.Getter;

@Getter
public enum CollectionType {
    MAIN("_main"),
    DELETED("_deleted"),
    ACTIVITY("_activity");

    private final String collectionType;

    CollectionType(String collectionType) {
        this.collectionType = collectionType;
    }

}
