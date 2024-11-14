package ticket.reservation.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import ticket.reservation.model.SeaterSection;
import ticket.reservation.model.SleeperSection;
import ticket.reservation.model.Train;

@Service
public class TrainService {

	private List<Train> trainsList;

	@PostConstruct
	public void generateTrains() {
		trainsList = new ArrayList<>();
		Train chennaiMail = new Train(124, "chennai", "bangalore", new SleeperSection(), new SeaterSection());
		Train bangaloreMail = new Train(128, "bangalore", "chennai", new SleeperSection(), new SeaterSection());
		Train trivandrumMail = new Train(132, "trivandrum", "chennai", new SleeperSection(), new SeaterSection());
		Train vandeBharat = new Train(141, "chennai", "delhi", new SleeperSection(), new SeaterSection());
		trainsList.add(chennaiMail);
		trainsList.add(bangaloreMail);
		trainsList.add(trivandrumMail);
		trainsList.add(vandeBharat);
	}

	Optional<Train> getTrainDetails(String from, String to) {
		return trainsList.stream().filter(r -> r.getFrom().equalsIgnoreCase(from) && r.getTo().equalsIgnoreCase(to))
				.findFirst();
	}

	Optional<Train> getTrainDetails(int trainNumber) {
		return trainsList.stream().filter(r -> r.getTrainNumber() == trainNumber).findFirst();
	}

}
