package no.charlie.rsvp.domain.Annotations

import groovy.transform.AnnotationCollector
import groovy.transform.ToString

import javax.persistence.Entity
import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
@Entity
@XmlAccessorType(XmlAccessType.FIELD)
@AnnotationCollector
@interface RsvpEntity {

}