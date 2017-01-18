package no.charlie.rsvp.service

import no.charlie.rsvp.domain.Event

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
interface ImportEventService {

    List<Event> createEventsByTournamentAndTeamId(Long tournamentId, Long teamId, String subject, int maxNumber)

}