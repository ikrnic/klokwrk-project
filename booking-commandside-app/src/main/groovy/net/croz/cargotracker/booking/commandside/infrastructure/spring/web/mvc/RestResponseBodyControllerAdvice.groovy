package net.croz.cargotracker.booking.commandside.infrastructure.spring.web.mvc

import groovy.transform.CompileStatic
import net.croz.cargotracker.infrastructure.project.web.spring.mvc.RestResponseBodyAdvice
import org.springframework.web.bind.annotation.ControllerAdvice

@ControllerAdvice
@CompileStatic
class RestResponseBodyControllerAdvice extends RestResponseBodyAdvice {
}
