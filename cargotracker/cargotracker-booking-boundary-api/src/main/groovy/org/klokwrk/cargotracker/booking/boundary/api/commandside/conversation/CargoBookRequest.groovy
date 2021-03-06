package org.klokwrk.cargotracker.booking.boundary.api.commandside.conversation

import groovy.transform.CompileStatic
import groovy.transform.MapConstructor
import groovy.transform.PropertyOptions
import org.klokwrk.lang.groovy.transform.options.RelaxedPropertyHandler

@PropertyOptions(propertyHandler = RelaxedPropertyHandler)
@MapConstructor(noArg = true)
@CompileStatic
class CargoBookRequest {
  String aggregateIdentifier

  String originLocation
  String destinationLocation
}
