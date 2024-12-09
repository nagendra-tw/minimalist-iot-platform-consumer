package com.learnings.iot_platform.constants;

public enum AlertTypes {
    HIGH_TEMPERATURE("HIGH TEMPERATURE");

    private final String displayName;

    AlertTypes(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
