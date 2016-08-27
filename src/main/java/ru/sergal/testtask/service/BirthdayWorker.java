package ru.sergal.testtask.service;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import ru.sergal.testtask.service.dto.DaysToBirthday;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class BirthdayWorker implements Callable<List<DaysToBirthday>> {

    private static final Logger log = LoggerFactory.getLogger(BirthdayWorker.class);

    private int month;
    private String filename;

    public BirthdayWorker(String filename, int month) {
        this.filename = filename;
        this.month = month;
    }

    @Override
    public List<DaysToBirthday> call() {
        try {
            log.info("Task for month {} is starting", month);
            Thread.sleep(60000L);
            List<Pair<Date, String>> birthdays = readFile();
            List<DaysToBirthday> daysToBirthdays = birthdays.stream()
                    .filter(pair -> DateUtils.getMonthFromDate(pair.getLeft()) == month)
                    .map(pair -> new DaysToBirthday(pair.getRight(),
                            DateUtils.daysToBirthdayCount(new Date(), pair.getLeft())))
                    .collect(Collectors.toList());
            log.info("Task successfully ended");
            return daysToBirthdays;
        } catch (InterruptedException | IOException e){
            log.error("Taskgi ended with error", e);
            throw new RuntimeException(e);
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
        reader.close();

        return result;
    }

    public static BirthdayWorker getInstance(String filename, int month) {
        return new BirthdayWorker(filename, month);
    }
}
