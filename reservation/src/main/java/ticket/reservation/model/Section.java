package ticket.reservation.model;

import java.util.List;

public interface Section {

	String getSectionType();

	boolean hasAvailableSeats();

	int allocateSeat() throws IllegalStateException;

	void cancelSeat(int seatNumber);

	List<Integer> getAvailableSeats();

	boolean modifySeat(int oldSeatNumber, int newSeatNumber);
}
