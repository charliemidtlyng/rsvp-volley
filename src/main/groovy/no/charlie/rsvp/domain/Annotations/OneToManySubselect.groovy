package no.charlie.rsvp.domain.Annotations

import com.fasterxml.jackson.annotation.JsonManagedReference
import groovy.transform.AnnotationCollector
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode

import javax.persistence.FetchType
import javax.persistence.OneToMany
import javax.persistence.OrderBy
import javax.persistence.OrderColumn

import static javax.persistence.CascadeType.MERGE
import static javax.persistence.CascadeType.PERSIST
import static javax.persistence.CascadeType.REMOVE

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
@OneToMany(
        fetch = FetchType.LAZY,
        cascade = [PERSIST, REMOVE, MERGE],
        mappedBy = 'event'
)
@Fetch(FetchMode.SUBSELECT)
@JsonManagedReference
@AnnotationCollector
@OrderBy("timestamp ASC")
@interface OneToManySubselect {

}