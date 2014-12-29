package no.charlie.rsvp.repository

import no.charlie.rsvp.domain.Event
import org.joda.time.DateTime
import org.springframework.data.repository.CrudRepository

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
interface EventRepository extends CrudRepository<Event, Long>, EventRepositoryCustom {

    List<Event> findByEndTimeGreaterThan(DateTime fromDate)
}