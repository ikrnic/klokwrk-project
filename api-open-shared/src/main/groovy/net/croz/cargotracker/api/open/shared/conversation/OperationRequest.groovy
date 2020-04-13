package net.croz.cargotracker.api.open.shared.conversation

import groovy.transform.CompileStatic
import groovy.transform.MapConstructor
import groovy.transform.PropertyOptions
import net.croz.cargotracker.lang.groovy.constructor.support.PostMapConstructorCheckable
import net.croz.cargotracker.lang.groovy.transform.options.RelaxedPropertyHandler

@PropertyOptions(propertyHandler = RelaxedPropertyHandler)
@MapConstructor(post = { postMapConstructorCheckProtocol(args as Map) })
@CompileStatic
class OperationRequest<P> implements OperationMessage<P, Map<String, ?>>, PostMapConstructorCheckable {
  P payload
  Map<String, ?> metaData = Collections.emptyMap()

  @Override
  void postMapConstructorCheck(Map<String, ?> constructorArguments) {
    assert metaData != null
    assert payload != null
  }
}
