package ru.sergal.testtask.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.sergal.testtask.service.dto.BirthdayResult;
import ru.sergal.testtask.service.dto.DaysToBirthday;

import javax.annotation.PreDestroy;
import java.util.*;
import java.util.concurrent.*;

@Service
public class BirthdayServiceImpl implements BirthdayService {

    private static final Logger log = LoggerFactory.getLogger(BirthdayServiceImpl.class);

    private final Map<String, Future<List<DaysToBirthday>>> currentTasks = new ConcurrentHashMap<>();
    private final String filename = "birthday.csv";
    private final ExecutorService executor = Executors.newFixedThreadPool(2);;

    @Override
    public String startBirthdayCalc(Integer month) {
        log.info("Starting create new task for month {}", month);
        //минус единица - для пересчета месяцев с нуля
        month = (month != null) ? month - 1: DateUtils.getMonthFromDate(new Date());
        String uuid = UUID.randomUUID().toString();
        currentTasks.put(uuid,
                executor.submit(BirthdayWorker.getInstance(filename, month)));
        log.info("Task for month {} was started with UUID {}", month, uuid);
        return uuid;
    }

    @Override
    public BirthdayResult getBirthdayResult(String uuid) {

        Future<List<DaysToBirthday>> birthdayResult = currentTasks.get(uuid);
        try {
            if (birthdayResult == null) {
                return new BirthdayResult(BirthdayResult.BirthdayResultStatus.NONE);
            }
            if (!birthdayResult.isDone()) {
                return new BirthdayResult(BirthdayResult.BirthdayResultStatus.IN_PROGRESS);
            }
            List<DaysToBirthday> daysToBirthdays = birthdayResult.get();
            currentTasks.remove(uuid);
            log.info("Result of task {} was passed to user and was removed", uuid);
            return new BirthdayResult(BirthdayResult.BirthdayResultStatus.DONE, daysToBirthdays);
        } catch (ExecutionException | InterruptedException e) {
            currentTasks.remove(uuid);
            return new BirthdayResult(BirthdayResult.BirthdayResultStatus.ERROR);
        }
    }

    @PreDestroy
    public void preDestroy() {
        executor.shutdown();
    }
}
