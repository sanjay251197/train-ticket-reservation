package ticket.reservation.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import ticket.reservation.dto.SeatUserMappingResponse;
import ticket.reservation.dto.TicketPurchaseRequest;
import ticket.reservation.model.Ticket;
import ticket.reservation.model.Train;

@Service
public class BookingService {

	private final TicketService ticketService;
	private final TrainService trainService;
	private List<Ticket> ticketsList;
	private Map<Train, List<Ticket>> ticketByTrainMap;

	@PostConstruct
	public void init() {
		this.ticketsList = new ArrayList<>();
		this.ticketByTrainMap = new HashMap<>();
	}

	public BookingService(TicketService ticketService, TrainService trainService) {
		this.ticketService = ticketService;
		this.trainService = trainService;
	}

	public Optional<Ticket> purchaseTicket(TicketPurchaseRequest request) {

		Optional<Train> train = trainService.getTrainDetails(request.from(), request.to());
		if (train.isEmpty()) {
			return Optional.empty();
		}
		Optional<Ticket> ticket = ticketService.allocateTicket(request, train.get());
		updateTicketList(ticket.get(), train.get());
		return ticket;

	}

	private void updateTicketList(Ticket ticket, Train train) {

		ticketsList.add(ticket);
		List<Ticket> trainBookings = ticketByTrainMap.getOrDefault(train, new ArrayList<>());
		trainBookings.add(ticket);
		ticketByTrainMap.put(train, trainBookings);

	}

	public Optional<Ticket> viewTicket(String receiptId) {
		return ticketsList.stream().filter(r -> r.getReceiptId().equals(receiptId)).findFirst();
	}

	public List<SeatUserMappingResponse> retrieveUserSeatMappingsBySectionAndTrain(int trainNumber, String section) {

		Optional<Train> train = trainService.getTrainDetails(trainNumber);
		List<SeatUserMappingResponse> userWithSeatList = new ArrayList<>();
		if (train.isEmpty() || section.isEmpty() || section == null) {
			return new ArrayList<>();
		}

		List<Ticket> trainBookings = ticketByTrainMap.get(train.get());

		trainBookings.stream().filter(s -> s.getSection().equalsIgnoreCase(section)).forEach(r -> userWithSeatList
				.add(new SeatUserMappingResponse(r.getUser().firstName() + " " + r.getUser().lastName(), r.getSeat())));

		return userWithSeatList;
	}

	public String deleteUserFromTrain(int trainNumber, String email) {

		Optional<Train> train = trainService.getTrainDetails(trainNumber);

		List<Ticket> totalTrainTickets = ticketByTrainMap.get(train.get());

		List<Ticket> trainTicketsToDelete = totalTrainTickets.stream()
				.filter(ticket -> ticket.getUser().email().equals(email)).toList();

		ticketsList.removeAll(trainTicketsToDelete);
		updateBookingListAfterDeletion(train.get(), trainTicketsToDelete);

		return null;
	}

	private void updateBookingListAfterDeletion(Train train, List<Ticket> ticketsToDelete) {
		List<Ticket> ticketListByTrain = ticketByTrainMap.get(train);
		ticketListByTrain.removeAll(ticketsToDelete);
		ticketByTrainMap.put(train, ticketListByTrain);
		ticketsToDelete.forEach(r -> ticketService.cancelTicket(r, train));
	}

	public String modifyUserSeat(int trainNumber, String email, int newseat) {

		Optional<Train> train = trainService.getTrainDetails(trainNumber);
		List<Ticket> totalTrainTickets = ticketByTrainMap.get(train.get());

		Optional<Ticket> OldUserTicket = totalTrainTickets.stream()
				.filter(ticket -> ticket.getUser().email().equals(email)).findFirst();

		if (ticketService.updateUserSeat(train.get(), OldUserTicket.get(), newseat)) {

			// update ticket details in entire tickets list
			int oldSeat = OldUserTicket.get().getSeat();
			Optional<Ticket> updatedTicket = OldUserTicket;
			updatedTicket.get().setSeat(newseat);
			ticketsList.remove(OldUserTicket.get());
			ticketsList.add(updatedTicket.get());

			// update ticket details in map based on train
			List<Ticket> trainBookings = ticketByTrainMap.get(train.get());
			trainBookings.remove(OldUserTicket.get());
			trainBookings.add(updatedTicket.get());
			ticketByTrainMap.put(train.get(), trainBookings);

			return "Successfully updated User seat from " + oldSeat + " to " + newseat;
		}

		return " New seat is already taken, Kindly change the seat and try again";
	}

}
