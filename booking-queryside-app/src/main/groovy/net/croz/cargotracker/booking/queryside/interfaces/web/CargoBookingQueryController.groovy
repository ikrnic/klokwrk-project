package net.croz.cargotracker.booking.queryside.interfaces.web

import net.croz.cargotracker.booking.queryside.application.CargoBookingQueryApplicationService
import net.croz.cargotracker.booking.queryside.domain.query.CargoSummaryQuery
import net.croz.cargotracker.booking.queryside.domain.query.CargoSummaryResult
import net.croz.cargotracker.booking.queryside.interfaces.web.dto.CargoSummaryWebRequest
import net.croz.cargotracker.api.open.shared.conversation.OperationRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/cargo-booking-query")
class CargoBookingQueryController {
  private CargoBookingQueryApplicationService cargoBookingQueryApplicationService

  CargoBookingQueryController(CargoBookingQueryApplicationService cargoBookingQueryApplicationService) {
    this.cargoBookingQueryApplicationService = cargoBookingQueryApplicationService
  }

  @PostMapping("/cargo-summary-query")
  CargoSummaryResult cargoSummaryQuery(@RequestBody CargoSummaryWebRequest cargoSummaryWebRequest) {
    CargoSummaryResult cargoSummary = cargoBookingQueryApplicationService.queryCargoSummary(cargoSummaryWebRequestToCargoSummaryQueryOperationRequest(cargoSummaryWebRequest))
    return cargoSummary
  }

  static OperationRequest<CargoSummaryQuery> cargoSummaryWebRequestToCargoSummaryQueryOperationRequest(CargoSummaryWebRequest cargoSummaryWebRequest) {
    return new OperationRequest<CargoSummaryQuery>(payload: new CargoSummaryQuery(cargoSummaryWebRequest.properties))
  }
}
