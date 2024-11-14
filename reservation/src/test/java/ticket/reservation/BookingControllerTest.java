package ticket.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import ticket.reservation.controller.BookingController;
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

		assertThat(bookingController.viewTicketDetails("test").getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

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
				.isEqualTo(HttpStatus.BAD_REQUEST);
	}
	
	@Test
	void givenInvalidSectionToFetchTicketDetails() {
		Ticket ticket = purchaseTicket();
		assertThat(
				bookingController.retrieveUserSeatMappingsBySectionAndTrain(ticket.getTrainNumber(), "").getStatusCode())
				.isEqualTo(HttpStatus.BAD_REQUEST);
	}

}
