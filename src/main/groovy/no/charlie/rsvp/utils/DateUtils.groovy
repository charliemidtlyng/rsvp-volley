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

    static String toDate(DateTime dateTime) {
        dateTime?.withZone(DateTimeZone.forID('Europe/Oslo'))?.toLocalDateTime()?.toString('yyyy-MM-dd')
    }
}
