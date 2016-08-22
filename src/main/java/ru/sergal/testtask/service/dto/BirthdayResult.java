package ru.sergal.testtask.service.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sergal on 16.08.2016.
 */
public class BirthdayResult {

    public enum BirthdayResultStatus {
        NONE,
        IN_PROGRESS,
        DONE,
        ERROR
    }

    private BirthdayResultStatus status;

    private final List<DaysToBirthday> birthdayList = new ArrayList<>();

    public BirthdayResult(BirthdayResultStatus status) {
        this.status = status;
    }

    public BirthdayResultStatus getStatus() {
        return status;
    }

    public List<DaysToBirthday> getBirthdayList() {
        return birthdayList;
    }
}
