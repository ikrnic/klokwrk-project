package net.croz.cargotracker.infrastructure.project.boundary.api.conversation.metadata

import groovy.transform.CompileStatic

/**
 * Contains constants to be used as keys when constructing metadata for inbound/outbound messages.
 * <p/>
 * For example, inbound channel (like the web) can send into domain various metadata, but domain must somehow find and minimally interpret them. Using shared constants for metadata's key names is a
 * starting point.
 */
@CompileStatic
class MetaDataConstant {
  /**
   * Key (as in Map key) to use for storing the name of the inbound channel from which request came in.
   * <p/>
   * The values might be domain related, for example, something like "booking", "handling", etc.
   */
  static final String INBOUND_CHANNEL_NAME_KEY = "INBOUND_CHANNEL_NAME"

  /**
   * Key (as in Map key) to use for storing the type of the inbound channel from which request came in.
   * <p/>
   * The values might be something like "web"/"rest", "messaging" etc. or more specific like "guiFrontend", "webApiFrontend", "queuingMessaging", "kafkaMessaging" etc.
   */
  static final String INBOUND_CHANNEL_TYPE_KEY = "INBOUND_CHANNEL_TYPE"

  /**
   * Key (as in Map key) to use for storing some identifier of the inbound channel.
   * <p/>
   * The values might be something like the IP for web/rest channel, originating server IP and queue for message queueing channel, a topic for Kafka channel, etc.
   */
  static final String INBOUND_CHANNEL_REQUEST_IDENTIFIER_KEY = "INBOUND_CHANNEL_REQUEST_IDENTIFIER"

  /**
   * Key (as in Map key) to use for storing request's locale detected by the inbound channel.
   * <p/>
   * Stored locale comes from and is detected by the inbound channel's specific means. For example, in the Spring MVC environment, request's <code>Accept-Language</code> header will be translated
   * into the locale of the current request.
   */
  static final String INBOUND_CHANNEL_REQUEST_LOCALE_KEY = "INBOUND_CHANNEL_REQUEST_LOCALE"
}
