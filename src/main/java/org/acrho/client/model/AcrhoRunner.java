package org.acrho.client.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDate;

/**
 * An Acrho runner description
 */
@Log4j2
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcrhoRunner {
    private String name;
    private LocalDate birthDate;
    private String category;
    private String team;
    private Integer bib;
}
