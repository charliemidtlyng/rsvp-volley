package no.charlie.rsvp.domain.Annotations

import groovy.transform.AnnotationCollector

import javax.persistence.GeneratedValue
import javax.persistence.Id

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
@Id
@GeneratedValue
@AnnotationCollector
@interface GeneratedId {

}