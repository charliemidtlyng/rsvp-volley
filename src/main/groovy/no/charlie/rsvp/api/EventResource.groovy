package no.charlie.rsvp.api

import no.charlie.rsvp.domain.Event
import no.charlie.rsvp.domain.Participant
import no.charlie.rsvp.exception.RsvpBadRequestException
import no.charlie.rsvp.service.EventService
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
@Component
@Produces(MediaType.APPLICATION_JSON)
@Path("/events")
class EventResource {

    @Autowired EventService eventService


    @GET
    Response events() {
        return Response.ok().entity(eventService.findAllEvents()).build()
    }

    @GET
    @Path("/upcoming")
    Response upcomingEvents() {
        return Response.ok().entity(eventService.findUpcomingEvents()).build()
    }

    @GET
    @Path("/{eventId}")
    Response eventById(@PathParam('eventId') Long eventId) {
        return Response.ok().entity(eventService.findEventById(eventId)).build()
    }

    @POST
    Response createEvent(Map valueMap) {
        Event event = parseEvent(valueMap)
        return Response.accepted().entity(eventService.createEvent(event)).build()
    }


    @DELETE
    @Path("/{eventId}")
    Response deleteEvent(@PathParam('eventId') Long eventId) {
        eventService.deleteEvent(eventId)
        return Response.accepted().build()
    }

    @POST
    @Path("/{id}/register")
    Response register(@PathParam('id') Long eventId, Map valueMap) {
        Participant p = parseParticipant(valueMap)
        return Response.accepted().entity(eventService.addParticipantToEvent(eventId, p)).build()
    }

    @DELETE
    @Path("/{eventId}/register/{participantId}")
    Response unregister(@PathParam('eventId') Long eventId, @PathParam('participantId') Long participantId) {
        return Response.accepted().entity(eventService.removeParticipantFromEvent(eventId, participantId)).build()
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
                maxNumber: map.maxNumber ? map.maxNumber : Integer.MAX_VALUE)

    }

    static Participant parseParticipant(Map map) {
        validateProperties(map, 'name')
        new Participant(name: map.name, email: map.email)
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


}
