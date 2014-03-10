package com.github.ndija.acrho_client;


public class Result {
    private Integer place;
    private Integer bib;
    private String firstName;
    private String lastName;
    private Long time;
    private String team;
    private Integer points;
    private String category;

    public Integer getPlace() {
        return place;
    }

    public void setPlace(Integer place) {
        this.place = place;
    }

    public Integer getBib() {
        return bib;
    }

    public void setBib(Integer bib) {
        this.bib = bib;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(bib);
        sb.append(" | ");
        sb.append(firstName).append(" ").append(lastName);
        sb.append(" | ");
        sb.append(team);
        sb.append(" | ");
        sb.append(category);
        sb.append(" | ");
        sb.append(place);
        sb.append(" | ");
        sb.append(time);
        sb.append(" | ");
        sb.append(points);
        return sb.toString();
    }
}
