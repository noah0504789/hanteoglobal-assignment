package com.hanteo.board;

public enum CategoryType {
    ROOT, SUB, GENERAL, NOTICE, ANONYMOUS;

    private static final Category ANONYMOUS_INSTANCE;

    static {
        ANONYMOUS_INSTANCE = Category.createAnonymousCategory(Long.MAX_VALUE);
    }

    public static Category getAnonymous() {
        return ANONYMOUS_INSTANCE;
    }

    public Integer getOrder() {
        return switch (this) {
            case ROOT -> 1;
            case SUB -> 2;
            case NOTICE -> 3;
            case ANONYMOUS -> 4;
            case GENERAL -> 5;
            default -> throw new IllegalArgumentException("Unknown type : " + this);
        };
    }
}
