package ru.sergal.testtask.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.sergal.testtask.service.dto.BirthdayResult;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BirthdayServiceImpl implements BirthdayService {

    private static final Logger log = LoggerFactory.getLogger(BirthdayServiceImpl.class);

    private final Map<String, BirthdayResult> currentTasks = new ConcurrentHashMap<>();
    private final String filename = "birthday.csv";

    @Override
    public String startBirthdayCalc(Integer month) {
        log.info("Starting create new task for month {}", month);
        //минус единица - для пересчета месяцев с нуля
        month = (month != null) ? month - 1: DateUtils.getMonthFromDate(new Date());
        String uuid = UUID.randomUUID().toString();
        currentTasks.put(uuid,
                new BirthdayResult(BirthdayResult.BirthdayResultStatus.IN_PROGRESS));
        BirthdayWorker worker = new BirthdayWorker(filename, month, uuid, currentTasks);
        Thread thread = new Thread(worker);
        thread.start();
        log.info("Task for month {} was started with UUID {}", month, uuid);
        return uuid;
    }

    @Override
    public BirthdayResult getBirthdayResult(String uuid) {

        BirthdayResult birthdayResult = currentTasks.get(uuid);
        if (birthdayResult == null) {
            return new BirthdayResult(BirthdayResult.BirthdayResultStatus.NONE);
        }
        if ((birthdayResult.getStatus() == BirthdayResult.BirthdayResultStatus.DONE) ||
                (birthdayResult.getStatus() == BirthdayResult.BirthdayResultStatus.ERROR)){
            currentTasks.remove(uuid);
            log.info("Result of task {} was passed to user and was removed", uuid);
        }
        return birthdayResult;
    }
}
