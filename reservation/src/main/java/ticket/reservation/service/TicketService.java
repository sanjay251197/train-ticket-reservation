package ticket.reservation.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import ticket.reservation.dto.TicketPurchaseRequest;
import ticket.reservation.model.Section;
import ticket.reservation.model.Ticket;
import ticket.reservation.model.Train;

@Service
public class TicketService {

	private Section section;
	private static final String SLEEPER = "sleeper";
	private static final String SEATER = "seater";

	public Optional<Ticket> allocateTicket(TicketPurchaseRequest request, Train train) {

		section = request.price() < 5.0 ? train.getSeaterSection() : train.getSleeperSection();

		if (!section.hasAvailableSeats()) {
			return Optional.empty();
		}

		int seatNumber = section.allocateSeat();
		return Optional.of(new Ticket(request.from(), request.to(), request.user(), request.price(),
				section.getSectionType(), seatNumber, train.getTrainNumber()));
	}

	public void cancelTicket(Ticket ticket, Train train) {

		section = getSectionByTicketDetails(ticket, train);

		if (section != null) {
			section.cancelSeat(ticket.getSeat());
		}
	}

	private Section getSectionByTicketDetails(Ticket ticket, Train train) {
		if (ticket.getSection().equalsIgnoreCase(SLEEPER)) {
			section = train.getSleeperSection();

		} else if (ticket.getSection().equalsIgnoreCase(SEATER)) {
			section = train.getSeaterSection();
		}
		return section;
	}

	public boolean updateUserSeat(Train train, Ticket ticket, int newseat) {

		section = getSectionByTicketDetails(ticket, train);

		return section.modifySeat(ticket.getSeat(), newseat);

	}
}
