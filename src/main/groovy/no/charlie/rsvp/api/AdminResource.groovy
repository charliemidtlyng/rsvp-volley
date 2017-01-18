package no.charlie.rsvp.api

import no.charlie.rsvp.domain.Event
import no.charlie.rsvp.exception.RsvpBadRequestException
import no.charlie.rsvp.service.EventService
import no.charlie.rsvp.service.ImportEventService
import no.charlie.rsvp.service.MailService
import no.charlie.rsvp.service.SlackService
import no.charlie.rsvp.utils.ValidatorUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

import static no.charlie.rsvp.utils.ValidatorUtils.validateProperties

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
@Component
@Produces(MediaType.APPLICATION_JSON)
@Path("/admin")
class AdminResource {

    @Autowired EventService eventService
    @Autowired MailService mailService
    @Autowired SlackService slackService
    @Autowired ImportEventService importEventService

    @GET
    @Path('/events/{eventId}/sendNotification/slack')
    Response sendSlackNotification(@PathParam('eventId') Long eventId) {

        Event event = findAndValidateEvent(eventId)
        slackService.sendEventNotification(event)
        Response.ok(event).build()
    }

    @GET
    @Path('/events/{eventId}/sendNotification/mail')
    Response sendMailNotification(@PathParam('eventId') Long eventId) {

        Event event = findAndValidateEvent(eventId)
        mailService.sendEventNotification(event)
        Response.ok(event).build()
    }

    @POST
    @Path('/events/generateForImport')
    Response generateEventsForImport(Map map) {
        validateProperties(map, 'tournamentId', 'teamId', 'subject', 'maxNumber')
        List<Event> events = importEventService.createEventsByTournamentAndTeamId(map.tournamentId, map.teamId,map.subject, map.maxNumber)
        Response.ok(events).build()
    }

    private Event findAndValidateEvent(long eventId) {
        Event event = eventService.findEventById(eventId)
        if (event == null) {
            throw new RsvpBadRequestException("Can not find event by id ${eventId}")
        }
        event
    }
}
