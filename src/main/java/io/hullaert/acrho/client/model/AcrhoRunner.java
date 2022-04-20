package io.hullaert.acrho.client.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * An Acrho runner description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcrhoRunner {
    private String name;
    private LocalDate birthDate;
    private String category;
    private String team;
    private Integer bib;
    private Long id;
}
