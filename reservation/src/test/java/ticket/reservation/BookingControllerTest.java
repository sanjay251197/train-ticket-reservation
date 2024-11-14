package ticket.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import ticket.reservation.controller.BookingController;
import ticket.reservation.dto.SeatUserMappingResponse;
import ticket.reservation.dto.TicketPurchaseRequest;
import ticket.reservation.model.Ticket;
import ticket.reservation.model.User;
import ticket.reservation.service.BookingService;
import ticket.reservation.service.TicketService;
import ticket.reservation.service.TrainService;

class BookingControllerTest {

	private BookingController bookingController;
	private BookingService bookingService;
	private User user;

	@BeforeEach
	void setUp() {
		TrainService trainService = new TrainService();
		trainService.generateTrains();
		TicketService ticketService = new TicketService();
		bookingService = new BookingService(ticketService, trainService);
		bookingService.init();
		bookingController = new BookingController(bookingService);
		user = new User("sanjay", "sivalingam", "sw@outlook.com");
	}

	@Test
	void givenTicketPurchaseRequestIsInvalidTest() {

		TicketPurchaseRequest request = new TicketPurchaseRequest("", "bangalore", user, 8.0);
		assertThat(bookingController.purchaseTicket(request).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

	}

	@Test
	void givenTicketPurchaseRequestIsValidTest() {

		Ticket ticket = purchaseTicket();
		assertNotNull(ticket.getReceiptId());
		assertNotNull(ticket.getSeat());

	}

	@Test
	void givenTicketReceiptIsInvalidTest() {

		assertThat(bookingController.viewTicketDetails("test").getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

	}

	@Test
	void givenTicketReceiptIsValidTest() {

		Ticket ticket = purchaseTicket();
		Ticket bookedTicket = bookingController.viewTicketDetails(ticket.getReceiptId()).getBody();
		assertNotNull(bookedTicket.getReceiptId());
		assertNotNull(bookedTicket.getSeat());
		assertNotNull(bookedTicket.getSection());

	}

	private Ticket purchaseTicket() {
		TicketPurchaseRequest request = new TicketPurchaseRequest("chennai", "bangalore", user, 8.0);
		Ticket ticket = bookingController.purchaseTicket(request).getBody();
		return ticket;
	}

	@Test
	void givenInvalidTrainNumberToFetchTicketDetails() {
		Ticket ticket = purchaseTicket();
		assertThat(
				bookingController.retrieveUserSeatMappingsBySectionAndTrain(120, ticket.getSection()).getStatusCode())
				.isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	void givenInvalidSectionToFetchTicketDetails() {
		Ticket ticket = purchaseTicket();
		assertThat(bookingController.retrieveUserSeatMappingsBySectionAndTrain(ticket.getTrainNumber(), "")
				.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	void givenValidTrainNumberAndSectionToFetchTicketDetails() {
		Ticket ticket = purchaseTicket();
		List<SeatUserMappingResponse> userSeatMapList = bookingController
				.retrieveUserSeatMappingsBySectionAndTrain(ticket.getTrainNumber(), ticket.getSection()).getBody();
		assertNotNull(userSeatMapList);
		assertEquals(userSeatMapList.get(0).seatNumber(), ticket.getSeat());

	}

	@Test
	void givenValidUserToDelete() {
		Ticket ticket = purchaseTicket();
		assertThat(bookingController.removeUserFromTrain(ticket.getTrainNumber(), ticket.getUser().email())
				.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	void givenValidUserAndSeatToUpdate() {
		Ticket ticket = purchaseTicket();
		assertThat(
				bookingController.modifyUserSeat(ticket.getTrainNumber(), ticket.getUser().email(), 3).getStatusCode())
				.isEqualTo(HttpStatus.OK);
	}

}
