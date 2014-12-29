package no.charlie.rsvp.repository

import no.charlie.rsvp.domain.Event
import org.springframework.beans.factory.annotation.Autowired

import javax.persistence.EntityManager
import javax.persistence.NoResultException

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
class EventRepositoryImpl implements EventRepositoryCustom {
    @Autowired private EntityManager em

    @Override
    Event findWithPreFetch(Long eventId) {
        resultOrNull {
            return em.createQuery('''
                      from Event e
                        left outer join fetch e.participants
                        left outer join fetch e.history
                      where e.id = :eventId''', Event)
                    .setParameter('eventId', eventId)
                    .getSingleResult()
                    .fetchMainCollections()
        }

    }

    @Override
    List<Event> findAllWithPreFetch() {
        em.createQuery('''
                      from Event e
                        left outer join fetch e.participants
                        left outer join fetch e.history
                      ''', Event)
                .getResultList()
    }

    private resultOrNull = { Closure query ->
        try {
            query.call()
        } catch (NoResultException ignored) {
            return null
        }

    }
}
