package org.acrho.client.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * An Acrho run description
 */
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
