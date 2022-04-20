package org.acrho.client.util;

import org.acrho.client.model.AcrhoResult;
import org.acrho.client.model.AcrhoRun;
import org.acrho.client.model.AcrhoRunner;
import org.acrho.client.model.property.QueryProperties;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static javax.swing.text.html.HTML.Attribute.*;
import static javax.swing.text.html.HTML.Tag.A;
import static javax.swing.text.html.HTML.Tag.H1;
import static org.acrho.client.AcrhoConstant.*;

public final class AcrhoUtil {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(PATTERN_DATE);
    private static final Pattern patternSelectRun = Pattern.compile(PATTERN_SELECT_RUN);
    private static final Pattern patternSelectIdRunner = Pattern.compile(PATTERN_SELECT_ID_RUNNER);

    private static final int ELEMENT_1 = 1;
    private static final int ELEMENT_2 = 2;
    private static final int ELEMENT_3 = 3;
    private static final int ELEMENT_4 = 4;
    private static final int ELEMENT_5 = 5;
    private static final int ELEMENT_6 = 6;
    private static final int ELEMENT_7 = 7;

    private static final int LENGTH_1 = 1;
    private static final int I_60 = 60;
    private static final int I_1000 = 1000;

    private AcrhoUtil(){}

    public static AcrhoRun getRun(Node option) {
        var idRun = Long.parseLong(option.attr(VALUE.toString()));

        if (idRun == 0) {
            return null;
        }
        Long id = idRun;
        String label = option.childNode(0).outerHtml().trim();
        var m = patternSelectRun.matcher(label);

        String name = null;
        LocalDate localDate = null;
        BigDecimal distance = null;
        if (m.matches()) {
            name = m.group(ELEMENT_1).trim();
            localDate = LocalDate.parse(m.group(ELEMENT_2), dateTimeFormatter);
            distance = new BigDecimal(m.group(ELEMENT_3));
        }
        return new AcrhoRun(id, name, null, localDate, distance);
    }

    public static AcrhoResult getResult(Elements tds) {
        var position = Integer.parseInt(tds.get(0).text());
        var name = NBSP_PATTERN.matcher(tds.get(ELEMENT_1).text()).replaceAll(StringUtils.EMPTY);
        var urlProfil = tds.get(ELEMENT_1).getElementsByTag(A.toString()).get(0).attr(HREF.toString());
        var team = tds.get(ELEMENT_2).text();
        var time = getMillis(tds.get(ELEMENT_3).text());
        var avg = getMillis(tds.get(ELEMENT_4).text());
        var speed = new BigDecimal(
                COMA_PATTERN.matcher(tds.get(ELEMENT_5).text()).replaceAll(POINT));
        var points = Integer.valueOf(tds.get(ELEMENT_6).text());
        var category = tds.get(ELEMENT_7).text();
        var m = patternSelectIdRunner.matcher(urlProfil);
        Long idRunner = null;
        if (m.matches()) {
            idRunner = Long.valueOf(m.group(ELEMENT_1));
        }
        return new AcrhoResult(position, name, urlProfil, team, time, avg, speed, points, category, idRunner);
    }

    public static AcrhoRunner getRunner(Element doc) {
        String name = doc.getElementsByAttributeValue(CLASS.toString(), PAR1DESCR).get(0)
                .getElementsByTag(H1.toString()).text();
        var details = doc.getElementsByAttributeValue(CLASS.toString(), PAR1DESCR).get(0)
                .getElementsByAttributeValue(CLASS.toString(), SPEVALUE);
        var runner = new AcrhoRunner();
        runner.setName(name);
        runner.setBib(Integer.parseInt(details.get(ELEMENT_3).text()));
        runner.setBirthDate(LocalDate.parse(details.get(0).text(), dateTimeFormatter));
        runner.setCategory(details.get(ELEMENT_1).text());
        runner.setTeam(details.get(ELEMENT_2).text());
        return runner;
    }

    public static String getType(Element doc, String runId) {
        return doc.getElementsByAttributeValue("data-reveal-id", "myModal" + runId)
                .parents().get(ELEMENT_1)
                .childNodes().get(ELEMENT_5)
                .childNode(0).outerHtml();
    }

    private static Long getMillis(String time) {
        String[] h = time.split(H);
        String[] m = time.split(M);

        var timeInMillis = 0L;
        if (h.length != LENGTH_1) {
            timeInMillis = Long.parseLong(h[0]) * I_60 * I_60 * I_1000;
            m = h[ELEMENT_1].split(M);
        }

        timeInMillis += Long.parseLong(m[0]) * I_60 * I_1000;
        timeInMillis += Long.parseLong(
                S_PATTERN.matcher(m[ELEMENT_1]).replaceAll(StringUtils.EMPTY)) * I_1000;
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
