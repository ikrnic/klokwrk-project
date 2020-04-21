package net.croz.cargotracker.infrastructure.web.spring.mvc

import groovy.transform.CompileStatic
import net.croz.cargotracker.api.open.shared.conversation.OperationResponse
import net.croz.cargotracker.api.open.shared.conversation.response.ResponseReportViolationPart
import net.croz.cargotracker.api.open.shared.exceptional.exception.CommandException
import net.croz.cargotracker.api.open.shared.exceptional.exception.DomainException
import net.croz.cargotracker.api.open.shared.exceptional.exception.QueryException
import net.croz.cargotracker.api.open.shared.exceptional.violation.Severity
import net.croz.cargotracker.api.open.shared.exceptional.violation.ViolationInfo
import net.croz.cargotracker.infrastructure.web.conversation.response.HttpResponseReportPart
import net.croz.cargotracker.infrastructure.web.conversation.response.HttpResponseReport
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

import java.time.Instant

// useful references:
//   https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-ann-exceptionhandler
//   https://www.baeldung.com/global-error-handler-in-a-spring-rest-api
//   https://blog.restcase.com/rest-api-error-handling-problem-details-response/
//   https://tools.ietf.org/html/rfc7807
@CompileStatic
class RestExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler
  ResponseEntity handleDomainException(DomainException domainException) {
    throw domainException
  }

  @ExceptionHandler
  ResponseEntity handleDomainQueryException(QueryException queryException) {
    HttpResponseReport httpResponseReport = createHttpResponseReport(queryException)
    HttpStatus httpStatus = mapQueryExceptionToHttpStatus(queryException)
    OperationResponse operationResponse = new OperationResponse(payload: [:], metaData: httpResponseReport.propertiesFiltered)
    ResponseEntity responseEntity = new ResponseEntity(operationResponse, new HttpHeaders(), httpStatus)

    return responseEntity
  }

  @ExceptionHandler
  ResponseEntity handleDomainCommandException(CommandException commandException) {
    // TODO dmurat: implement handling of CommandException
    throw commandException
  }

  protected HttpResponseReport createHttpResponseReport(QueryException queryException) {
    HttpStatus httpStatus = mapQueryExceptionToHttpStatus(queryException)

    // TODO dmurat: resolve responseReport's titleText and titleDetailedText and violationCodeText through resource bundle
    HttpResponseReport httpResponseReport = new HttpResponseReport(
        timestamp: Instant.now(),
        severity: Severity.WARNING,
        locale: LocaleContextHolder.locale,
        violation: createResponseReportViolationPart(queryException),
        http: createHttpResponseReportPart(httpStatus)
    )

    return httpResponseReport
  }

  protected HttpStatus mapQueryExceptionToHttpStatus(QueryException queryException) {
    HttpStatus httpStatus
    switch (queryException.violationInfo) {
      case ViolationInfo.NOT_FOUND:
        httpStatus = HttpStatus.NOT_FOUND
        break
      default:
        httpStatus = HttpStatus.INTERNAL_SERVER_ERROR
        break
    }

    return httpStatus
  }

  protected ResponseReportViolationPart createResponseReportViolationPart(QueryException queryException) {
    ResponseReportViolationPart responseReportViolationPart = new ResponseReportViolationPart(
        code: queryException.violationInfo.violationCode.code,
        codeMessage: queryException.violationInfo.violationCode.codeMessage
    )

    return responseReportViolationPart
  }

  protected HttpResponseReportPart createHttpResponseReportPart(HttpStatus httpStatus) {
    HttpResponseReportPart httpResponseReportPart = new HttpResponseReportPart(
        status: httpStatus.value().toString(),
        message: httpStatus.reasonPhrase
    )

    return httpResponseReportPart
  }
}