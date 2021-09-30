package org.acrho.client.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * An Acrho run result description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcrhoResult {
    private Integer position;
    private String name;
    private String urlProfil;
    private String team;
    private Long time;
    private Long avg;
    private BigDecimal speed;
    private Integer points;
    private String category;
    @NotNull
    private Long idRunner;
}
