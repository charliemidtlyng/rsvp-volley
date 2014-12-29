package no.charlie.rsvp.domain.Annotations

import groovy.transform.AnnotationCollector
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode

import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.OneToOne

import static javax.persistence.CascadeType.PERSIST
import static javax.persistence.CascadeType.REMOVE

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
@OneToOne(
        fetch = FetchType.LAZY,
        cascade = [PERSIST, REMOVE],
        optional = true
)
@Fetch(FetchMode.JOIN)
@JoinColumn
@AnnotationCollector
@interface OneToOneJoin {

}