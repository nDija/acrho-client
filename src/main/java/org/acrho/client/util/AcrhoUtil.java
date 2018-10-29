package org.acrho.client.util;

import org.acrho.client.model.AcrhoResult;
import org.acrho.client.model.AcrhoRun;
import org.acrho.client.model.AcrhoRunner;
import org.acrho.client.model.property.QueryProperties;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.math.BigDecimal;
import org.jsoup.select.Elements;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static javax.swing.text.html.HTML.Attribute.CLASS;
import static javax.swing.text.html.HTML.Attribute.HREF;
import static javax.swing.text.html.HTML.Attribute.VALUE;
import static javax.swing.text.html.HTML.Tag.A;
import static javax.swing.text.html.HTML.Tag.H1;
import static org.acrho.client.AcrhoConstant.*;

public class AcrhoUtil {

    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(PATTERN_DATE);
    private static Pattern patternSelectRun = Pattern.compile(PATTERN_SELECT_RUN);
    private static Pattern patternSelectIdRunner = Pattern.compile(PATTERN_SELECT_ID_RUNNER);

    private AcrhoUtil(){}

    public static AcrhoRun getRun(Element option) {
        long idRun = Long.parseLong(option.attr(VALUE.toString()));
        String name = null;
        LocalDate localDate = null;
        BigDecimal distance = null;

        if (idRun == 0) {
            return null;
        }
        Long id = idRun;
        String label = option.childNode(0).outerHtml().trim();
        Matcher m = patternSelectRun.matcher(label);
        if (m.matches()) {
            name = m.group(1).trim();
            localDate = LocalDate.parse(m.group(2), dateTimeFormatter);
            distance = new BigDecimal(m.group(3));
        }
        return new AcrhoRun(id, name, null, localDate, distance);
    }

    public static AcrhoResult getResult(Elements tds) {
        Integer position = Integer.parseInt(tds.get(0).text());
        String name = tds.get(1).text().replaceAll(NBSP, EMPTY_STRING);
        String urlProfil = tds.get(1).getElementsByTag(A.toString()).get(0).attr(HREF.toString());
        String team = tds.get(2).text();
        Long time = getMillis(tds.get(3).text());
        Long avg = getMillis(tds.get(4).text());
        BigDecimal speed = new BigDecimal(tds.get(5).text().replaceAll(COMA, POINT));
        Integer points = Integer.valueOf(tds.get(6).text());
        String category = tds.get(7).text();
        Matcher m = patternSelectIdRunner.matcher(urlProfil);
        Long idRunner = null;
        if (m.matches()) {
            idRunner = Long.valueOf(m.group(1));
        }
        return new AcrhoResult(position, name, urlProfil, team, time, avg, speed, points, category, idRunner);
    }

    public static AcrhoRunner getRunner(Document doc) {
        String name = doc.getElementsByAttributeValue(CLASS.toString(), PAR1DESCR).get(0)
                .getElementsByTag(H1.toString()).text();
        Elements details = doc.getElementsByAttributeValue(CLASS.toString(), PAR1DESCR).get(0)
                .getElementsByAttributeValue(CLASS.toString(), SPEVALUE);
        AcrhoRunner runner = new AcrhoRunner();
        runner.setName(name);
        runner.setBib(Integer.parseInt(details.get(3).text()));
        runner.setBirthDate(LocalDate.parse(details.get(0).text(), dateTimeFormatter));
        runner.setCategory(details.get(1).text());
        runner.setTeam(details.get(2).text());
        return runner;
    }

    public static String getType(Document doc, String runId) {
        return doc.getElementsByAttributeValue("data-reveal-id", "myModal" + runId).parents().get(1).childNodes().get(5).childNode(0).outerHtml();
    }

    private static Long getMillis(String time) {
        String[] h = time.split(H);
        String[] m = time.split(M);

        long timeInMillis = 0L;
        if (h.length != 1) {
            timeInMillis = Long.valueOf(h[0]) * 60 * 60 * 1000;
            m = h[1].split(M);
        }

        timeInMillis += Long.valueOf(m[0]) * 60 * 1000;
        timeInMillis += Long.valueOf(m[1].replaceAll(S, EMPTY_STRING)) * 1000;
        return timeInMillis;
    }

    /**
     * Clone parameters map to avoid conflict in request
     * @param qp The query properties
     * @return A cloned list of parameters
     */
    public static Map<String, String> getParameters(QueryProperties qp) {
        return qp.getParameters().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
