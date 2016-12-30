package no.charlie.rsvp.utils

import org.joda.time.DateTime
import org.joda.time.DateTimeZone

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
class DateUtils {

    static String toDateWithTimeStamp(DateTime dateTime) {
        dateTime?.withZone(DateTimeZone.forID('Europe/Oslo'))?.toLocalDateTime()?.toString('yyyy-MM-dd HH:mm')
    }

    static String toDateWithNameOfDayTimeStamp(DateTime dateTime) {
        dateTime?.withZone(DateTimeZone.forID('Europe/Oslo'))?.toLocalDateTime()?.toString('EEEE yyyy-MM-dd HH:mm', new Locale('nb', 'no'))
    }

    static String toDate(DateTime dateTime) {
        dateTime?.withZone(DateTimeZone.forID('Europe/Oslo'))?.toLocalDateTime()?.toString('yyyy-MM-dd')
    }

    static String toDayOfWeek(DateTime dateTime) {
        dateTime?.withZone(DateTimeZone.forID('Europe/Oslo'))?.toLocalDateTime()?.toString('EEEE', new Locale('nb', 'no'))
    }

    static String toTime(DateTime dateTime) {
        dateTime?.withZone(DateTimeZone.forID('Europe/Oslo'))?.toLocalDateTime()?.toString('HH:mm')
    }
}
