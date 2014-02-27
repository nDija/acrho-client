package com.github.ndija.acrho_client.service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import com.github.ndija.acrho_client.Result;
import com.github.ndija.acrho_client.RunResult;
import com.github.ndija.acrho_client.exception.AcrhoConnectionException;
import com.github.ndija.acrho_client.exception.AcrhoPDFException;

public class PdfService {

    private static final Logger log = Logger.getLogger(PdfService.class);
    private static SimpleDateFormat formatterRun = new SimpleDateFormat("h:mm:ss");
    private static SimpleDateFormat formatterDate = new SimpleDateFormat("EEEE dd MMMM yyyy", new Locale("fr", "FR"));

    static {
        formatterRun.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public static RunResult extract(String filename) throws AcrhoConnectionException, AcrhoPDFException {
        String url = AcrhoProperties.get(AcrhoProperties.URL) + filename;
        InputStream is = HttpService.get(url);
        return extract(is);
    }

    public static RunResult extract(InputStream is) throws AcrhoPDFException {
        PDDocument doc = getDocument(is);
        PDFTextStripper stripper = getStripper();
        String s;
        RunResult runResult = new RunResult();
        List<Result> results = new ArrayList<Result>();
        try {
            for (int i = 1; i <= doc.getNumberOfPages(); i++) {
                stripper.setStartPage(i);
                stripper.setEndPage(i);
                s = stripper.getText(doc);
                String[] result = s.split(stripper.getLineSeparator());
                // Fill run info
                if (i == 1) {
                    setResultInfo(runResult, result);
                }
                for (int j = 5; j < result.length; j++) {
                    Result res = getResult(result[j]);
                    results.add(res);
                }
            }

        } catch (IOException e) {
            log.error("error when reading pdf", e);
            throw new AcrhoPDFException("error when reading pdf", e);
        }
        runResult.setResults(results);
        return runResult;
    }

    public static PDDocument getDocument(InputStream is) throws AcrhoPDFException {
        try {
            return PDDocument.load(is);
        } catch (IOException e) {
            log.error("error when reading pdf", e);
            throw new AcrhoPDFException("error when reading pdf", e);
        }
    }

    public static PDFTextStripper getStripper() throws AcrhoPDFException {
        PDFTextStripper stripper;
        try {
            stripper = new PDFTextStripper();
        } catch (IOException e) {
            log.error("error when reading pdf", e);
            throw new AcrhoPDFException("error when reading pdf", e);
        }
        stripper.setSortByPosition(true);
        stripper.setWordSeparator("/");
        return stripper;
    }

    public static Result getResult(String line) throws AcrhoPDFException {
        String[] result = line.split("/");
        Result res = new Result();
        res.setPlace(Integer.parseInt(result[0]));
        res.setBib(Integer.parseInt(result[1]));
        res.setLastName(result[2]);
        res.setFirstName(result[3]);
        

        String team;
        String category;
        String time;
        // The standard
        if(result.length == 9) {
            category = result[5];
            team = result[4];
            time = result[6];
            res.setPoints(Integer.parseInt(result[8]));
        } else if (result.length == 8) {
            // wrong team and category bad parse by pdfbox...
            String[] split = result[4].split(" ");
            category = split[split.length - 1];
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i< split.length - 1 ; i++) {
                sb.append(split[i]);
            }
            team = sb.toString();
            time = result[5];
            res.setPoints(Integer.parseInt(result[7]));
        } else {
            throw new AcrhoPDFException("Can't parse result: " + line);
        }
        res.setTeam(team);
        res.setCategory(category);
        try {
            res.setTime(formatterRun.parse(time).getTime());
        } catch (ParseException e) {
            log.error("error when parsing time", e);
            throw new AcrhoPDFException("error when parsing time", e);
        }

        return res;
    }

    private static void setResultInfo(RunResult runResult, String[] result) throws AcrhoPDFException {
        String[] splitLine3 = result[2].split("/");
        String[] splitLine4 = result[3].split("/");
        String name = splitLine3[1];
        Integer runners = Integer.parseInt(splitLine3[3]);
        Long distance = new BigDecimal(splitLine4[3].replaceAll(",", ".")).multiply(new BigDecimal(1000)).longValue();
        Date date;
        try {
            date = formatterDate.parse(splitLine4[1]);
        } catch (ParseException e) {
            log.error("error when parsing date" + splitLine4[3], e);
            throw new AcrhoPDFException("error when parsing date" + splitLine4[3], e);
        }
        runResult.setDate(date);
        runResult.setDistance(distance);
        runResult.setName(name);
        runResult.setRunners(runners);
    }
}
