package ru.sergal.testtask.service.dto;

public class DaysToBirthday {

    private final String name;
    private final int days;

    public DaysToBirthday(String name, int days) {
        this.name = name;
        this.days = days;
    }

    public String getName() {
        return name;
    }

    public int getDays() {
        return days;
    }
}
