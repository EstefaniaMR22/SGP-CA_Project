package utils;

import org.junit.Assert;
import org.junit.Test;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;

public class DateFormatterTest {
    @Test
    public void getActualDateTest() {
        LocalDate actualDate = LocalDate.from(ZonedDateTime.now());
        String expected = actualDate.toString().replaceAll("-", "/");
        String actual = DateFormatter.getActualDate();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getDateTimeByStringTest() {
        String date = "2022-03-25 00:00:00";
        String expected = "Fri Mar 25 00:00:00 CST 2022";
        String actual = DateFormatter.getDateTimeByString(date).toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getSQLParseDateDateTest() {
        String date = "2020-03-25";
        java.sql.Date actual = DateFormatter.getSQLDateFromString(date);
        Assert.assertNotNull(actual);
    }

    @Test
    public void getTimeStampByStringTest() {
        String date = "2020-03-25 00:00:00";
        java.sql.Timestamp actual = DateFormatter.getTimeStampByString(date);
        Assert.assertNotNull(actual);
    }

    @Test
    public void convertSQLDateToUtilDateTest() {
        java.sql.Date sqlDate = new Date(Date.from(Instant.now()).getTime());
        java.util.Date utilDate = DateFormatter.convertSQLDateToUtilDate(sqlDate);
        Assert.assertNotNull(utilDate);
    }

    @Test
    public void convertUtilDateToSQLDateTest() {
        java.util.Date utildate = new Date(Date.from(Instant.now()).getTime());
        java.sql.Date sqlDate = DateFormatter.convertUtilDateToSQLDate(utildate);
        Assert.assertNotNull(sqlDate);
    }

    @Test
    public void getDateFromDatePickerValueTest() {
        LocalDate localDate = LocalDate.now();
        java.util.Date utilDate = DateFormatter.getDateFromDatepickerValue(localDate);
        Assert.assertNotNull(localDate);
    }

    @Test
    public void getLocalDateFromUtilDateTest() {
        java.util.Date utilDate = Date.from(Instant.now());
        LocalDate localDate = DateFormatter.getLocalDateFromUtilDate(utilDate);
        Assert.assertNotNull(localDate);
    }

    @Test
    public void compareActualDateToGreaterLocalDate() {
        LocalDate dateGreater = LocalDate.of(2023, 11, 25);
        int expected = -1;
        int actual = DateFormatter.compareActualDateToLocalDate(dateGreater);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void compareActualDateToLesserLocalDate() {
        LocalDate dateLesser = LocalDate.of(1999, 9, 25);
        int equalDate = 0;
        int actual = DateFormatter.compareActualDateToLocalDate(dateLesser);
        Assert.assertTrue(actual > equalDate);
    }

    @Test
    public void compareActualDateToActualLocalDate() {
        LocalDate localDate = LocalDate.from(ZonedDateTime.now());
        int equalDate = 0;
        int actual = DateFormatter.compareActualDateToLocalDate(localDate);
        Assert.assertEquals(equalDate, actual);
    }






}
