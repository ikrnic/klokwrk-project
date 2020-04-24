package net.croz.cargotracker.infrastructure.project.boundary.api.exceptional.violation

import groovy.transform.CompileStatic
import groovy.transform.Immutable
import groovy.transform.MapConstructor
import groovy.transform.PropertyOptions
import groovy.transform.TupleConstructor
import groovy.transform.VisibilityOptions
import groovy.transform.options.Visibility
import net.croz.cargotracker.lang.groovy.constructor.support.PostMapConstructorCheckable
import net.croz.cargotracker.lang.groovy.transform.options.RelaxedPropertyHandler

@Immutable
@PropertyOptions(propertyHandler = RelaxedPropertyHandler)
@TupleConstructor(visibilityId = "privateVisibility", pre = { throw new IllegalArgumentException("Calling a private constructor is not allowed") })
@VisibilityOptions(id = "privateVisibility", value = Visibility.PRIVATE)
@MapConstructor(post = { postMapConstructorCheckProtocol(args as Map) })
@CompileStatic
class ViolationCode implements PostMapConstructorCheckable {
  static final ViolationCode UNKNOWN = new ViolationCode(code: "500", codeAsText: "internalServerError", codeMessage: "Internal Server Error")
  static final ViolationCode BAD_REQUEST = new ViolationCode(code: "400", codeAsText: "badRequest", codeMessage: "Bad Request")
  static final ViolationCode NOT_FOUND = new ViolationCode(code: "404", codeAsText: "notFound", codeMessage: "Not Found")

  String code
  String codeAsText
  String codeMessage

  @SuppressWarnings("GroovyPointlessBoolean")
  @Override
  void postMapConstructorCheck(Map<String, ?> constructorArguments) {
    assert code
    assert code.isBlank() == false

    assert codeAsText
    assert codeAsText.isBlank() == false

    assert codeMessage
    assert codeMessage.isBlank() == false
  }
}