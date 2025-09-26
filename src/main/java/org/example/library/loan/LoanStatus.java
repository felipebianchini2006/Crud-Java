package org.example.library.loan;

/**
 * Enum que representa os possíveis status de um empréstimo
 */
public enum LoanStatus {
    ACTIVE("Ativo"),
    RETURNED("Devolvido"),
    OVERDUE("Atrasado"),
    CANCELLED("Cancelado");

    private final String description;

    LoanStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
