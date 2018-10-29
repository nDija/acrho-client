package org.acrho.client.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * An Acrho run description
 */
@Log4j2
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcrhoRun {
    private Long id;
    private String name;
    private String type;
    private LocalDate date;
    private BigDecimal distance;
}
