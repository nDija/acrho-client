package com.github.ndija.acrho_client;

import java.util.Date;
import java.util.List;

public class RunResult {

    private String name;
    private Long distance;
    private Integer runners;
    private Date date;
    private List<ResultDetails> results;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDistance() {
        return distance;
    }

    public void setDistance(Long distance) {
        this.distance = distance;
    }

    public Integer getRunners() {
        return runners;
    }

    public void setRunners(Integer runners) {
        this.runners = runners;
    }

    public List<ResultDetails> getResults() {
        return results;
    }

    public void setResults(List<ResultDetails> results) {
        this.results = results;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
