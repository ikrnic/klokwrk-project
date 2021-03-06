package org.klokwrk.lib.jackson.springboot

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.annotation.Nulls
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import groovy.transform.CompileStatic
import org.klokwrk.lib.jackson.databind.deser.StringSanitizingDeserializer
import org.klokwrk.lib.jackson.databind.ser.GStringSerializer
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder

/**
 * Implementation of Spring Boot's {@link Jackson2ObjectMapperBuilderCustomizer} which configures Spring Boot's provided {@link ObjectMapper} with a set of essential options.
 * <p/>
 * So called 'essential' options are completely opinionated, of course, but they proven to be very useful base in many internal projects. If one finds some, or all of them, not appropriate, they can
 * be turned off via {@link EssentialJacksonCustomizerConfigurationProperties} configuration properties.
 * <p/>
 * Beside Jackson configuration options available here, Spring Boot default options from <code>JacksonAutoConfiguration</code> still apply. Namely these are
 * <code>SerializationFeature.WRITE_DATES_AS_TIMESTAMPS = false</code> and <code>SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS = false</code>.
 * <p/>
 * EssentialJacksonCustomizer configures following defaults:
 * <ul>
 *   <li>transient fields and properties are ignored for de/serialization (<code>MapperFeature.PROPAGATE_TRANSIENT_MARKER = true</code>)</li>
 *   <li>serialization - adds {@link GStringSerializer} serializer</li>
 *   <li>serialization - null values are skipped (<code>serializationInclusion = JsonInclude.Include.NON_NULL</code>)</li>
 *   <li>deserialization - adds {@link StringSanitizingDeserializer} deserializer</li>
 *   <li>deserialization - json comments are allowed (<code>JsonParser.Feature.ALLOW_COMMENTS = true</code>)</li>
 *   <li>deserialization - single values are accepted into array (<code>DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY = true</code>)</li>
 *   <li>deserialization - failing on unknown properties is disabled (<code>DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES = false</code>)</li>
 *   <li>deserialization - null values are skipped (<code>defaultSetterInfo = JsonSetter.Value.forValueNulls(Nulls.SKIP)</code>)</li>
 * </ul>
 */
@CompileStatic
class EssentialJacksonCustomizer implements Jackson2ObjectMapperBuilderCustomizer, BeanPostProcessor {
  static private final String DEFAULT_SPRING_BOOT_OBJECT_MAPPER_BEAN_NAME = "jacksonObjectMapper"

  EssentialJacksonCustomizerConfigurationProperties essentialJacksonCustomizerConfigurationProperties

  EssentialJacksonCustomizer(EssentialJacksonCustomizerConfigurationProperties essentialJacksonCustomizerConfigurationProperties) {
    this.essentialJacksonCustomizerConfigurationProperties = essentialJacksonCustomizerConfigurationProperties
  }

  @Override
  void customize(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
    //noinspection GroovyPointlessBoolean
    if (essentialJacksonCustomizerConfigurationProperties.enabled == false) {
      return
    }

    List<JsonDeserializer> myDeserializerList = []
    if (essentialJacksonCustomizerConfigurationProperties.deserialization.stringSanitizingDeserializer.enabled) {
      myDeserializerList << new StringSanitizingDeserializer()
    }

    List<JsonSerializer> mySerializerList = []
    if (essentialJacksonCustomizerConfigurationProperties.serialization.gStringSerializer.enabled) {
      mySerializerList << new GStringSerializer()
    }

    List<Object> myFeatureToEnableList = []

    if (essentialJacksonCustomizerConfigurationProperties.mapper.ignoreTransient) {
      myFeatureToEnableList << MapperFeature.PROPAGATE_TRANSIENT_MARKER
    }

    if (essentialJacksonCustomizerConfigurationProperties.deserialization.allowJsonComments) {
      myFeatureToEnableList << JsonParser.Feature.ALLOW_COMMENTS
    }

    if (essentialJacksonCustomizerConfigurationProperties.deserialization.acceptSingleValueAsArray) {
      myFeatureToEnableList << DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY
    }

    List<Object> myFeatureToDisableList = []
    if (essentialJacksonCustomizerConfigurationProperties.deserialization.failOnUnknownProperties) {
      myFeatureToEnableList << DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
    }
    else {
      myFeatureToDisableList << DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
    }

    jacksonObjectMapperBuilder
        .deserializers(myDeserializerList as JsonDeserializer[])
        .serializers(mySerializerList as JsonSerializer[])
        .featuresToEnable(myFeatureToEnableList as Object[])
        .featuresToDisable(myFeatureToDisableList as Object[])

    if (essentialJacksonCustomizerConfigurationProperties.serialization.skipNullValues) {
      jacksonObjectMapperBuilder.serializationInclusion(JsonInclude.Include.NON_NULL)
    }
  }

  /**
   * For default Spring Boot's {@link ObjectMapper} bean, configures properties that are not exposed via builder customization.
   */
  @SuppressWarnings("Instanceof")
  @Override
  Object postProcessAfterInitialization(Object bean, String beanName) {
    //noinspection GroovyPointlessBoolean
    if (essentialJacksonCustomizerConfigurationProperties.enabled == false) {
      return bean
    }

    if (bean instanceof ObjectMapper && beanName == DEFAULT_SPRING_BOOT_OBJECT_MAPPER_BEAN_NAME) {
      if (essentialJacksonCustomizerConfigurationProperties.deserialization.skipNullValues) {
        bean.defaultSetterInfo = JsonSetter.Value.forValueNulls(Nulls.SKIP)
      }
    }

    return bean
  }
}
