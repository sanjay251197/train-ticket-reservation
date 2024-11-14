package ticket.reservation.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ticket.reservation.dto.SeatUserMappingResponse;
import ticket.reservation.dto.TicketPurchaseRequest;
import ticket.reservation.model.Ticket;
import ticket.reservation.service.BookingService;

@RestController
@RequestMapping("/booking")
public class BookingController {

	private final BookingService bookingService;

	public BookingController(BookingService bookingService) {
		this.bookingService = bookingService;
	}

	@PostMapping("/purchase")
	public ResponseEntity<Ticket> purchaseTicket(@RequestBody TicketPurchaseRequest request) {

		if (isBookingDetailsValid(request)) {
			Optional<Ticket> ticket = bookingService.purchaseTicket(request);
			return ResponseEntity.ok(ticket.get());
		}
		return ResponseEntity.badRequest().build();
	}

	private boolean isBookingDetailsValid(TicketPurchaseRequest request) {

		return request.to() != null && !request.to().isEmpty() && request.from() != null && !request.from().isEmpty()
				&& request.user() != null && request.price() > 0.1;
	}

	@GetMapping("view/{receiptId}")
	public ResponseEntity<Ticket> viewTicketDetails(@PathVariable String receiptId) {
		Optional<Ticket> ticket = bookingService.viewTicket(receiptId);
		return ticket.isPresent() ? ResponseEntity.ok(ticket.get()) : ResponseEntity.notFound().build();
	}

	@GetMapping("/{trainNumber}/sections/{section}")
	public ResponseEntity<List<SeatUserMappingResponse>> retrieveUserSeatMappingsBySectionAndTrain(
			@PathVariable int trainNumber, @PathVariable String section) {
		List<SeatUserMappingResponse> userSeatMappingList = bookingService
				.retrieveUserSeatMappingsBySectionAndTrain(trainNumber, section);
		return userSeatMappingList != null && !userSeatMappingList.isEmpty() ? ResponseEntity.ok(userSeatMappingList)
				: ResponseEntity.notFound().build();
	}

	@DeleteMapping("/trains/{trainNumber}/users/{email}")
	public ResponseEntity<String> removeUserFromTrain(@PathVariable int trainNumber, @PathVariable String email) {
		bookingService.deleteUserFromTrain(trainNumber, email);
		return ResponseEntity.ok("Successfully deleted user from train ticket reservation");
	}

	@PutMapping("update/{trainNumber}/email/{email}/newseat/{newseat}")
	public ResponseEntity<String> modifyUserSeat(@PathVariable("trainNumber") int trainNumber,
			@PathVariable("email") String email, @PathVariable("newseat") int newseat) {

		String response = bookingService.modifyUserSeat(trainNumber, email, newseat);
		return ResponseEntity.ok(response);

	}

}
