package org.acrho.client.model.property;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.Map;

@Log4j2
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeadersProperties {

    private Map<String, String> headers;
}
