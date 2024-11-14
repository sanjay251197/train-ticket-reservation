package ticket.reservation.model;

import java.util.UUID;

public class Ticket {

	private String receiptId;
	private String from;
	private String to;
	private User user;
	private double price;
	private String section;
	private int seat;
	private int trainNumber;

	public Ticket(String from, String to, User user, double price, String section, int seat, int trainNumber) {
		this.from = from;
		this.to = to;
		this.user = user;
		this.price = price;
		this.section = section;
		this.seat = seat;
		this.trainNumber = trainNumber;
		this.receiptId = UUID.randomUUID().toString();
	}

	public String getReceiptId() {
		return receiptId;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public int getSeat() {
		return seat;
	}

	public void setSeat(int seat) {
		this.seat = seat;
	}

	public int getTrainNumber() {
		return trainNumber;
	}

	public void setTrainNumber(int trainNumber) {
		this.trainNumber = trainNumber;
	}

}
