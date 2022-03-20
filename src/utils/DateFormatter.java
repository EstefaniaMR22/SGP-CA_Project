package utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DateFormatter {
    /***
     * Get datetime in sql by a string.
     * <p>
     * This method it's used when you need to format a string to a type date
     * for example: string -> 2020-06-26 12:30:00
     * This method will return a type Date -> 2020-06-26
     * </p>
     * @return Date representing the date of string.
     */
    public static Date getDateTimeByString(String date) {
        Date dateSQL = null;
        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            dateSQL = new Date( simpleDateFormat.parse(date).getTime() );
        } catch (ParseException e) {
            Logger.getLogger( DateFormatter.class.getName() ).log(Level.WARNING, null, e);
        }
        return dateSQL;
    }

    /***
     * Get Timestamp by a string.
     * <p>
     * This method it's used when you need to format a string to a type timestamp
     * for example: string -> 2020-06-26 12:30:00
     * This method will return a type timestamp -> 2020-06-26 12:30:00
     * </p>
     * @return Timestamp representing the date of string.
     */
    public static Timestamp getTimeStampByString(String date) {
        Timestamp timestamp = null;
        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            timestamp = new Timestamp( simpleDateFormat.parse(date).getTime() );
        } catch (ParseException e) {
            Logger.getLogger( DateFormatter.class.getName() ).log(Level.WARNING, null, e);
        }
        return timestamp;
    }
    /***
     * Convertion SQL DATE to UTIL DATE
     * <p>
     * This method should be used to convert the SQL DATE into UTIL DATE.
     * </p>
     * @param sqlDate the java.sql.Date variable
     * @return Date from java.util.Date
     */
    public static java.util.Date convertSQLDateToUtilDate(java.sql.Date sqlDate) {
        java.util.Date utilDate = null;
        if(sqlDate != null ) {
            utilDate = new java.util.Date(sqlDate.getTime());
        }
        return utilDate;
    }
    /***
     * Convertion UTIL DATE TO SQL DATE.
     * <p>
     * This method should be used to convert the UTIL DATE TO SQL DATE.
     * </p>
     * @param utilDate the java.util.Date variable
     * @return Date from java.sql.Date
     */
    public static java.sql.Date convertUtilDateToSQLDate(java.util.Date utilDate) {
        java.sql.Date dateSQL = null;
        if(utilDate != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String stringDate = dateFormat.format(utilDate);
            dateSQL = java.sql.Date.valueOf(stringDate);
        }
        return dateSQL;
    }
    /***
     * Convertion LocalDate from DatePicker to java.util.Date
     * <p>
     * This method should be used to convert Datepicker's value to java.util.date
     * </p>
     * @param localDate the datepicker value
     * @return Date from java.util.Date
     */
    public static java.util.Date getDateFromDatepickerValue(LocalDate localDate) {
        Date date = null;
        if(localDate != null ) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(localDate.getYear(), localDate.getMonthValue()-1, localDate.getDayOfMonth());
            date = calendar.getTime();
        }
        return date;
    }
    /***
     * Convertion java.util.Date to LocalDate
     * <p>
     * This method should be used to convert java.util.date to LocalDate
     * </p>
     * @param utilDate the java.util.date
     * @return LocalDate from LocalDate
     */
    public static LocalDate getLocalDateFromUtilDate(java.util.Date utilDate) {
        LocalDate localDate = null;
        Instant instant = utilDate.toInstant();
        ZoneId defaultZoneId = ZoneId.systemDefault();
        localDate = instant.atZone(defaultZoneId).toLocalDate();
        return localDate;
    }

    /***
     * Compare the actual Date to Other Date
     * <p>
     * This method compare actual day e.p. (2020-13-09) to any other e.g (2020-15-09).
     * </p>
     * @param chronoLocalDate the date in yyyy-MM-dd
     * @return returns 0 if both dates are equal. returns 1 if actual is greather than chronoLocalDate.
     * returns -1 if actual date is less than chronoLocalDate.
     */
    public static int compareActualDateToLocalDate(ChronoLocalDate chronoLocalDate) {
        LocalDate actual = LocalDate.from(ZonedDateTime.now());
        return actual.compareTo(chronoLocalDate);
    }

}
