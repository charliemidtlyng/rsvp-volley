package no.charlie.rsvp.api

import no.charlie.rsvp.domain.Event
import no.charlie.rsvp.domain.Participant
import org.joda.time.DateTime
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

    List<Event> events = createEvents()

    List<Event> createEvents() {
        [new Event(
                id: 1L,
                start: new DateTime(),
                end: new DateTime(),
                creator: new Participant(name: "Charlie", email: "charlie.midtlyng@gmail.com"),
                location: "Vallhall",
                subject: "Trening",
                description: "Oppmøte 19:45!",
                limit: 15

        )]
    }

    @GET
    Response events() {
        return Response.ok().entity(events).build()
    }

    @POST
    @Path("/{id}/attend")
    Response attend(@PathParam("id") Long eventId, Map valueMap) {
        addToEvents(valueMap);
        return Response.ok().entity(events).build()
    }

    def addToEvents(Map map) {
        if (map && map.name) {
            def participant = new Participant(name: map.name, email: map.email)
            events.first().participants.add(participant)
        } else {
            throw new BadRequestException("Name is missing!")
        }
    }
}
