package org.klokwrk.cargotracker.booking.boundary.api.commandside.conversation

import groovy.transform.CompileStatic
import groovy.transform.MapConstructor
import groovy.transform.PropertyOptions
import org.klokwrk.lang.groovy.transform.options.RelaxedPropertyHandler

@PropertyOptions(propertyHandler = RelaxedPropertyHandler)
@MapConstructor
@CompileStatic
class CargoBookResponse {
  String aggregateIdentifier

  Map<String, ?> originLocation
  Map<String, ?> destinationLocation
}
