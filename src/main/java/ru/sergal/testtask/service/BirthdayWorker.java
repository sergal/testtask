package ru.sergal.testtask.service;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import ru.sergal.testtask.service.dto.BirthdayResult;
import ru.sergal.testtask.service.dto.DaysToBirthday;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class BirthdayWorker implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(BirthdayWorker.class);

    private int month;
    private String filename;
    private String uuid;
    private Map<String, BirthdayResult> currentTasks;

    public BirthdayWorker(String filename, int month,
                          String uuid,
                          Map<String, BirthdayResult> currentTasks) {
        this.filename = filename;
        this.month = month;
        this.uuid = uuid;
        this.currentTasks = currentTasks;
    }

    @Override
    public void run() {
        try {
            log.info("Task {} is starting", uuid);
            Thread.sleep(60000L);
            List<Pair<Date, String>> birthdays = readFile();
            List<DaysToBirthday> daysToBirthdays = birthdays.stream()
                    .filter(pair -> DateUtils.getMonthFromDate(pair.getLeft()) == month)
                    .map(pair -> new DaysToBirthday(pair.getRight(),
                            DateUtils.daysToBirthdayCount(new Date(), pair.getLeft())))
                    .collect(Collectors.toList());
            BirthdayResult result = new BirthdayResult(BirthdayResult.BirthdayResultStatus.DONE);
            result.getBirthdayList().addAll(daysToBirthdays);
            currentTasks.put(uuid, result);
            log.info("Task {} successfully ended", uuid);
        } catch (InterruptedException | IOException e){
            currentTasks.put(uuid, new BirthdayResult(BirthdayResult.BirthdayResultStatus.ERROR));
            log.error("Task {} ended with error", e);
        }
    }

    private List<Pair<Date, String>> readFile() throws IOException {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Resource resource = new ClassPathResource(filename);
        File file = resource.getFile();

        List<Pair<Date, String>> result = new ArrayList<>();
        Reader reader = new InputStreamReader(new FileInputStream(file), "UTF-8");
        CSVParser parser = CSVFormat.DEFAULT
                .withDelimiter(',').withQuote('"').parse(reader);

        for (CSVRecord record : parser.getRecords()) {
            try {
                result.add(Pair.of(format.parse(record.get(0)),
                        record.get(1)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return result;
    }
}
