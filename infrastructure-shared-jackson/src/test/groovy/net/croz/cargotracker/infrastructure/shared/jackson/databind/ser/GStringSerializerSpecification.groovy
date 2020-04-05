package net.croz.cargotracker.infrastructure.shared.jackson.databind.ser

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import spock.lang.Specification

class GStringSerializerSpecification extends Specification {
  ObjectMapper objectMapper

  def setup() {
    SimpleModule simpleModule = new SimpleModule()
    simpleModule.addSerializer(GString, new GStringSerializer())

    ObjectMapper objectMapper = new ObjectMapper()
    objectMapper.registerModule(simpleModule)

    this.objectMapper = objectMapper
  }

  def "should serialize GString as a String"() {
    given:
    Closure closure = {
      return "${123} 456"
    }

    Map mapToSerialize = [
        "bla": closure()
    ]

    when:
    String serializedString = objectMapper.writeValueAsString(mapToSerialize)

    then:
    serializedString.contains("123 456")
  }
}
