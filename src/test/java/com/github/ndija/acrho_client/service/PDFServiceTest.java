package com.github.ndija.acrho_client.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import com.github.ndija.acrho_client.Result;
import com.github.ndija.acrho_client.RunResult;
import com.github.ndija.acrho_client.exception.AcrhoConnectionException;
import com.github.ndija.acrho_client.exception.AcrhoPDFException;

public class PDFServiceTest {

    private final static Logger log = Logger.getLogger(PDFServiceTest.class);
    @Test
    public void extractTest() throws AcrhoConnectionException, AcrhoPDFException {
        // Given
        String filename = "/Resultats_Bury_220214.pdf";
        String filename2 ="/Resultats_Dottignies_16022014.pdf";
        String filename3 = "/Resultats_Blicquy_080314.pdf";
        // When
        RunResult result = PdfService.extract(filename3);
        List<Result> results = result.getResults();

        // Then
        Assert.assertNotNull(results);
        Assert.assertNotEquals(results.size(), 0);

        for(Result r : results) {
            log.info(r.toString());
        }

    }
}
