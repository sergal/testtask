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

    private List<DaysToBirthday> birthdayList;

    public BirthdayResult(BirthdayResultStatus status) {
        this.status = status;
    }

    public BirthdayResult(BirthdayResultStatus status, List<DaysToBirthday> birthdayList) {
        this.status = status;
        this.birthdayList = birthdayList;
    }

    public BirthdayResultStatus getStatus() {
        return status;
    }

    public List<DaysToBirthday> getBirthdayList() {
        return birthdayList;
    }
}
