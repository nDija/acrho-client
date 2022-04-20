package io.hullaert.acrho.client.model.property;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcrhoProperties {

    private String baseUrl;
    private QueryProperties results;
    private QueryProperties runs;
    private QueryProperties runner;
    private QueryProperties years;
    private ProxyProperties proxy;
    private HeadersProperties getRequest;
    private HeadersProperties postRequest;
}
