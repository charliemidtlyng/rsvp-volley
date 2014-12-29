package no.charlie.rsvp.domain.Annotations

import groovy.transform.AnnotationCollector

import javax.persistence.EnumType
import javax.persistence.Enumerated

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
@Enumerated(EnumType.STRING)
@AnnotationCollector
@interface PEnum {

}