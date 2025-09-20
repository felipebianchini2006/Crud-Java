package org.example.library.reader;

import java.time.LocalDate;

public record ReaderResponse(
        Long id,
        String name,
        String email,
        String phone,
        LocalDate registrationDate
) {
    static ReaderResponse from(Reader reader) {
        return new ReaderResponse(
                reader.getId(),
                reader.getName(),
                reader.getEmail(),
                reader.getPhone(),
                reader.getRegistrationDate()
        );
    }
}