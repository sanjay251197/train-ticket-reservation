package ticket.reservation.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class SeaterSection implements Section {

	private static final int CAPACITY = 20;
	private static final String SEATER = "seater";
	private Set<Integer> occupiedSeats = new HashSet<>();
	private Queue<Integer> availableSeats = new LinkedList<>();

	public SeaterSection() {
		// Initialize all seats as available
		for (int i = 1; i <= CAPACITY; i++) {
			availableSeats.add(i);
		}
	}

	@Override
	public String getSectionType() {
		return SEATER;
	}

	@Override
	public boolean hasAvailableSeats() {
		return !availableSeats.isEmpty();
	}

	@Override
	public int allocateSeat() throws IllegalStateException {
		if (!hasAvailableSeats()) {
			throw new IllegalStateException("Sleeper section is full");
		}
		int seatNumber = availableSeats.poll(); // Allocate the next available seat
		occupiedSeats.add(seatNumber);
		return seatNumber;
	}

	@Override
	public void cancelSeat(int seatNumber) {
		if (occupiedSeats.remove(seatNumber)) {
			availableSeats.add(seatNumber); // Mark the seat as available
		}

	}

	@Override
	public List<Integer> getAvailableSeats() {
		return new ArrayList<>(availableSeats);
	}

	@Override
	public boolean modifySeat(int oldSeatNumber, int newSeatNumber) {
		if (availableSeats.contains(newSeatNumber)) {
			// Remove the current seat from occupied and available lists
			occupiedSeats.remove(oldSeatNumber);
			availableSeats.add(oldSeatNumber);

			// Add the new seat to occupied and remove it from available
			occupiedSeats.add(newSeatNumber);
			availableSeats.remove(newSeatNumber);
			return true;
		}
		return false;
	}

}
