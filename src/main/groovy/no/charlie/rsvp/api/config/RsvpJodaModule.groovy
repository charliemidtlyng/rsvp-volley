package no.charlie.rsvp.api.config

import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.datatype.joda.ser.JacksonJodaFormat
import com.fasterxml.jackson.datatype.joda.ser.LocalDateSerializer
import org.joda.time.LocalDate
import org.joda.time.format.ISODateTimeFormat

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
class RsvpJodaModule extends JodaModule {
    RsvpJodaModule() {
        super()

        JacksonJodaFormat baseFormat = new JacksonJodaFormat(ISODateTimeFormat.date().withZoneUTC())
        JacksonJodaFormat format = new JacksonJodaFormat(baseFormat, false)
        addSerializer(LocalDate.class, new LocalDateSerializer().withFormat(format))
    }
}
