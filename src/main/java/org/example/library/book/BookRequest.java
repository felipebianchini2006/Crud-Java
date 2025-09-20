package org.example.library.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BookRequest(
        @NotBlank
        @Size(max = 200)
        String title,

        @NotBlank
        @Size(max = 120)
        String author,

        @Size(max = 80)
        String genre,

        Boolean available
) {
}