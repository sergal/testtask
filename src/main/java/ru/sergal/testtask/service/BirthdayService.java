package ru.sergal.testtask.service;

import ru.sergal.testtask.service.dto.BirthdayResult;

public interface BirthdayService {

    String startBirthdayCalc(Integer month);

    BirthdayResult getBirthdayResult(String uuid);
}
