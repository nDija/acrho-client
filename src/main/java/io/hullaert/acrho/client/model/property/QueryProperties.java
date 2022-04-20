package io.hullaert.acrho.client.model.property;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryProperties {

    private String uri;
    private String type;
    private Map<String, String> parameters;
}
