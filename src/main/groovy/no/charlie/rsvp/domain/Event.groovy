package no.charlie.rsvp.domain

import no.charlie.rsvp.domain.Annotations.*
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

import javax.persistence.Column

import static no.charlie.rsvp.domain.Event.EventSubType.Match
import static no.charlie.rsvp.domain.Event.EventSubType.Training
import static no.charlie.rsvp.domain.Event.EventType.Football

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
@RsvpEntity
class Event {
    @GeneratedId Long id
    @PDateTime DateTime startTime
    @PDateTime DateTime endTime
    @PDateTime DateTime regStart
    @PDateTime DateTime regEnd

    @PEnum EventType eventType = Football
    @PEnum EventSubType eventSubType = Training

    String creator
    String location
    String subject
    @Column(length = 2048) String description
    Integer maxNumber = Integer.MAX_VALUE

    @OneToManySubselect Set<Participant> participants = []
    @OneToManySubselect Set<History> history = []

    Event fetchMainCollections() {
        participants.size()
        history.size()
        this
    }

    Event updateParticipants() {
        if (hasManualLineUp()) {
            return this
        }
        participants.eachWithIndex { Participant entry, int index ->
            entry.reserve = index >= maxNumber
        }
        this
    }

    boolean hasManualLineUp() {
        eventSubType && eventSubType == Match
    }

    String asICalendarEvent() {
        DateTimeFormatter dt = DateTimeFormat.forPattern("yyyyMMdd'T'HHmmss")
        return "BEGIN:VCALENDAR\n" +
                "VERSION:2.0\n" +
                "PRODID:-//BEKK//BEKK Fotball//NO\n" +
                "BEGIN:VEVENT\n" +
                "UID:$id@rsvp-app\n" +
                "DTSTAMP;TZID=Europe/Oslo:${dt.print(DateTime.now())}\n" +
                "DTSTART;TZID=Europe/Oslo:${dt.print(startTime)}\n" +
                "DTEND;TZID=Europe/Oslo:${dt.print(endTime)}\n" +
                "SUMMARY:$subject\n" +
                "LOCATION:$location\n" +
                "END:VEVENT\n" +
                "END:VCALENDAR"
    }

    public static enum EventType {
        Football
    }

    public static enum EventSubType {
        Training,
        Match
    }

}
