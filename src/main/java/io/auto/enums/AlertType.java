package io.auto.enums;

public enum AlertType {
    REGISTRATION_EXPIRY("Registration Expiry"),
    HIGH_VALUE_VEHICLE("High Value Vehicle"),
    MAINTENANCE_OVERDUE("Maintenance Overdue");

    private final String displayName;
    AlertType(String displayName) {
        this.displayName = displayName;
    }
    public String getDisplayName() {
        return displayName;
    }

}