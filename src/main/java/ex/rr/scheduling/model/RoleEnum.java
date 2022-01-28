package ex.rr.scheduling.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum RoleEnum {
    HOST("Host"),
    ADMIN("Admin"),
    USER("User");

    private String role;

    RoleEnum(String role) {
        this.role = role;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(role);
    }
}
