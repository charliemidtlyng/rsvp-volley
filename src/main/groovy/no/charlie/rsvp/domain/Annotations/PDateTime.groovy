package no.charlie.rsvp.domain.Annotations

import groovy.transform.AnnotationCollector
import org.hibernate.annotations.Type

import javax.persistence.Column

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
@Column
@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
@AnnotationCollector
@interface PDateTime {

}