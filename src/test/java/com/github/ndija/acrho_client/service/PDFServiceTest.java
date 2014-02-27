package com.github.ndija.acrho_client.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.github.ndija.acrho_client.Result;
import com.github.ndija.acrho_client.RunResult;
import com.github.ndija.acrho_client.exception.AcrhoConnectionException;
import com.github.ndija.acrho_client.exception.AcrhoPDFException;

public class PDFServiceTest {

    @Test
    public void extractTest() throws AcrhoConnectionException, AcrhoPDFException {
        // Given
        String filename = "/Resultats_Bury_220214.pdf";

        // When
        RunResult result = PdfService.extract(filename);
        List<Result> results = result.getResults();

        // Then
        Assert.assertNotNull(results);
        Assert.assertNotEquals(results.size(), 0);
    }
}
