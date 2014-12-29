package no.charlie.rsvp.domain.Annotations

import groovy.transform.AnnotationCollector

import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

/**
 * @author Charlie Midtlyng (charlie.midtlyng@BEKK.no)
 */
@ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = [])
@JoinColumn
@AnnotationCollector
@interface ManyToOneLazyNoCascade {

}