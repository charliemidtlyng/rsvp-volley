package no.charlie.rsvp.service

import net.fortuna.ical4j.model.TimeZoneRegistry
import net.fortuna.ical4j.model.TimeZoneRegistryFactory
import net.fortuna.ical4j.model.component.VEvent
import net.fortuna.ical4j.model.component.VTimeZone
import net.fortuna.ical4j.model.property.CalScale
import net.fortuna.ical4j.model.property.Location
import net.fortuna.ical4j.model.property.ProdId
import net.fortuna.ical4j.model.property.Uid
import net.fortuna.ical4j.util.HostInfo
import net.fortuna.ical4j.util.SimpleHostInfo
import net.fortuna.ical4j.util.UidGenerator
import no.charlie.rsvp.domain.Event
import org.apache.commons.lang.time.StopWatch
import org.joda.time.DateTime

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
class ICalGenerator {

    static net.fortuna.ical4j.model.Calendar generateCalendarForEvents(List<Event> events) {
        StopWatch watch = new StopWatch()
        watch.start()
        TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry()
        TimeZone timezone = registry.getTimeZone('Europe/Oslo')
        VTimeZone tz = timezone.getVTimeZone()
        net.fortuna.ical4j.model.Calendar icsCalendar = new net.fortuna.ical4j.model.Calendar()
        icsCalendar.getProperties().add(new ProdId("-//BEKK Fotball//iCal4j 1.0//EN"))
        icsCalendar.getProperties().add(CalScale.GREGORIAN)

        events.each {
            String eventName = "${it.subject} - ${it.location}"
            net.fortuna.ical4j.model.DateTime start = new net.fortuna.ical4j.model.DateTime(new DateTime(it.startTime).getMillis())
            net.fortuna.ical4j.model.DateTime end = new net.fortuna.ical4j.model.DateTime(new DateTime(it.endTime).getMillis())
            VEvent meeting = new VEvent(start, end, eventName)
            meeting.getProperties().add(tz.getTimeZoneId())
            UidGenerator ug = new UidGenerator(new SimpleHostInfo('paamelding.herokuapp.com'),"BekkFotball${it.id}")
            Uid uid = ug.generateUid()
            meeting.getProperties().add(uid)
            icsCalendar.getComponents().add(meeting)
        }
        return icsCalendar
    }
}
