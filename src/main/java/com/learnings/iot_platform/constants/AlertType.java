package com.learnings.iot_platform.constants;

public enum AlertType {
    HIGH_TEMPERATURE("High temperature");

    private final String displayName;

    AlertType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
