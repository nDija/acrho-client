package io.hullaert.acrho.client.model.property;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeadersProperties {

    private Map<String, String> headers;
}
