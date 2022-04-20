package io.hullaert.acrho.client.test.util;

import org.mockserver.model.Parameter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class TestUtil {

    public static List<Parameter> toMockServerParameters(Map<String, String> parameters) {
        return parameters.entrySet().stream()
                .map(p -> new Parameter(p.getKey(), p.getValue()))
                .collect(Collectors.toList());
    }
}
