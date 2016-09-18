package no.charlie.rsvp.api

import net.fortuna.ical4j.model.TimeZoneRegistry
import net.fortuna.ical4j.model.TimeZoneRegistryFactory
import net.fortuna.ical4j.model.component.VEvent
import net.fortuna.ical4j.model.component.VTimeZone
import net.fortuna.ical4j.model.property.*
import net.fortuna.ical4j.util.TimeZones
import net.fortuna.ical4j.util.UidGenerator
import no.charlie.rsvp.domain.*
import no.charlie.rsvp.domain.Event.EventSubType
import no.charlie.rsvp.domain.Event.EventType
import no.charlie.rsvp.exception.RsvpBadRequestException
import no.charlie.rsvp.service.*
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.ws.rs.*
import javax.ws.rs.core.*

import static no.charlie.rsvp.service.ICalGenerator.generateCalendarForEvents

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
@Component
@Produces(MediaType.APPLICATION_JSON)
@Path("/events")
class EventResource {

    @Autowired EventService eventService
    @Autowired SmsService smsService
    @Autowired OtpService otpService
    @Autowired CaptchaService captchaService


    @GET
    Response events(@QueryParam('eventType') EventType eventType,
                    @QueryParam('eventSubType') EventSubType eventSubType) {
        return Response.ok().entity(eventService.findAllEvents(eventType, eventSubType)).build()
    }

    @GET
    @Path("/upcoming")
    Response upcomingEvents(@QueryParam('eventType') EventType eventType,
                            @QueryParam('eventSubType') EventSubType eventSubType) {
        return Response.ok().entity(eventService.findUpcomingEvents(eventType, eventSubType)).build()
    }

    @GET
    @Path("/feed")
    Response feed(@QueryParam('eventType') EventType eventType,
                  @QueryParam('eventSubType') EventSubType eventSubType) {


        List<Event> events = eventService.findAllEvents(eventType, eventSubType)
        return Response.ok().entity(generateCalendarForEvents(events)).build()
    }

    @GET
    @Path("/feed/iCal")
    Response feedAsICal(@QueryParam('eventType') EventType eventType,
                  @QueryParam('eventSubType') EventSubType eventSubType) {


        List<Event> events = eventService.findAllEvents(eventType, eventSubType)
        return Response.ok().entity(generateCalendarForEvents(events).toString()).build()
    }

    @GET
    @Path("/{eventId}")
    Response eventById(@PathParam('eventId') Long eventId) {
        return Response.ok().entity(eventService.findEventById(eventId)).build()
    }

    @GET
    @Path('/{eventId}/icalendar')
    Response eventAsIcalendar(@PathParam('eventId') Long eventId) {
        Event event = eventService.findEventById(eventId)
        if (event == null) {
            throw new NotFoundException()
        }
        return Response.ok(event.asICalendarEvent())
                .header('Content-Disposition', "attachment; filename=event-${event.id}.ics")
                .build()
    }

    @POST
    Response createEvent(Map valueMap) {
        Event event = parseEvent(valueMap)
        return Response.ok().entity(eventService.createEvent(event)).build()
    }


    @DELETE
    @Path("/{eventId}")
    Response deleteEvent(@PathParam('eventId') Long eventId) {
        eventService.deleteEvent(eventId)
        return Response.ok().build()
    }

    @POST
    @Path("/{id}/register")
    Response register(@PathParam('id') Long eventId, Map valueMap) {
        String otp = valueMap.otp
        if (otp) {
            otpService.validateOtp(eventId, otp)
        } else {
            validateCaptcha(valueMap)
        }

        Participant p = parseParticipant(valueMap)
        return Response.ok().entity(eventService.addParticipantToEvent(eventId, p)).build()
    }

    @POST
    @Path("/{id}/otp")
    Response sendOtp(@PathParam('id') Long eventId, Map valueMap) {
        String phoneNumber = valueMap.phoneNumber
        if (!phoneNumber) {
            throw new RsvpBadRequestException('Må fylle ut mobilnummer for å få engangskode på SMS')
        }
        validatePhoneNumber(valueMap)
        Otp otp = otpService.resolveOtpForEventId(eventId)
        return Response.ok().entity(smsService.sendOtpSms(otp.password, phoneNumber)).build()
    }


    @DELETE
    @Path("/{eventId}/register/{participantId}")
    Response unregister(@PathParam('eventId') Long eventId, @PathParam('participantId') Long participantId) {
        return Response.ok().entity(eventService.removeParticipantFromEvent(eventId, participantId)).build()
    }

    @POST
    @Path("/{eventId}/confirmLineup")
    Response confirmLineup(@PathParam('eventId') Long eventId, Map lineupMap) {
        if (!lineupMap) {
            throw new RsvpBadRequestException('Må legge ved uttakslista')
        }
        Response.ok().entity(eventService.confirmLineup(eventId, lineupMap)).build()
    }


    static Event parseEvent(Map map) {
        validateProperties(map, 'subject', 'startTime', 'endTime', 'regStart', 'regEnd', 'creator', 'location')
        new Event(
                startTime: toDateTime(map.startTime),
                endTime: map.endTime ? toDateTime(map.endTime) : null,
                regStart: map.regStart ? toDateTime(map.regStart) : null,
                regEnd: map.regEnd ? toDateTime(map.regEnd) : null,
                creator: map.creator,
                location: map.location,
                subject: map.subject,
                description: map.description,
                eventType: map.eventType ? map.eventType as EventType : EventType.Football,
                eventSubType: map.eventSubType ? map.eventSubType as EventSubType : EventSubType.Training,
                maxNumber: map.maxNumber ? map.maxNumber : Integer.MAX_VALUE)
    }

    static Participant parseParticipant(Map map) {
        validateProperties(map, 'name')
        validatePhoneNumber(map)
        new Participant(name: map.name, email: map.email, phoneNumber: map.phoneNumber)
    }

    static Boolean validatePhoneNumber(Map map) {
        String phoneNumber = map.phoneNumber
        if (phoneNumber) {
            if (phoneNumber.length() == 8 && (phoneNumber.startsWith('4') || phoneNumber.startsWith('9'))) {
                return true
            }
            throw new RsvpBadRequestException("Feltet mobiltlf må være 8 tegn og begynne på 9 eller 4!")
        }
        return true
    }

    private static DateTime toDateTime(String stringValue) {
        DateTime.parse(stringValue)
    }

    static Boolean validateProperties(Map map, String... properties) {
        if (!map) {
            throw new RsvpBadRequestException("Mangelfull utfylling")
        }
        properties.each {
            if (!map.containsKey(it)) {
                throw new RsvpBadRequestException("Feltet $it mangler!")
            }
        }
    }

    String getRemoteIpFromHeroku() {
        String ip = "0.0.0.0"
        try {
            ip = Request.getHeader("X-Forwarded-For").split(",")[0]
        } catch (Exception ignored) {

        }

        return ip
    }

    private void validateCaptcha(Map valueMap) {
        if (!captchaService.isHuman(valueMap.get("g-recaptcha-response"), getRemoteIpFromHeroku())) {
            throw new RsvpBadRequestException("Captcha validerte ikke. ")
        }
    }


}
