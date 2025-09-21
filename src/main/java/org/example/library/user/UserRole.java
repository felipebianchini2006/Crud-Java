package org.example.library.user;

public enum UserRole {
    READER("Leitor"),
    AUTHOR("Autor"),
    ADMIN("Administrador");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
