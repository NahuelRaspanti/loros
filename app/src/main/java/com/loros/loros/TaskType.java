package com.loros.loros;

public enum TaskType {
    HIGIENE(0),
    TRABAJO(1),
    SOCIAL(2),
    HOGAR(3);

    private int code;

    TaskType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
