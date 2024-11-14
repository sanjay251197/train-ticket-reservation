package ticket.reservation.model;

public class Train {
	private int trainNumber;
	private String from;
	private String to;
	private Section sleeperSection;
	private Section seaterSection;

	public Train(int trainNumber, String from, String to, Section sleeperSection, Section seaterSection) {
		this.trainNumber = trainNumber;
		this.from = from;
		this.to = to;
		this.sleeperSection = sleeperSection;
		this.seaterSection = seaterSection;
	}

	public int getTrainNumber() {
		return trainNumber;
	}

	public void setTrainNumber(int trainNumber) {
		this.trainNumber = trainNumber;
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

	public Section getSleeperSection() {
		return sleeperSection;
	}

	public void setSleeperSection(Section sleeperSection) {
		this.sleeperSection = sleeperSection;
	}

	public Section getSeaterSection() {
		return seaterSection;
	}

	public void setSeaterSection(Section seaterSection) {
		this.seaterSection = seaterSection;
	}

}
