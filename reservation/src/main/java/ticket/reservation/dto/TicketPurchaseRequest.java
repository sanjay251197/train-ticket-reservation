package ticket.reservation.dto;

import ticket.reservation.model.User;


public record TicketPurchaseRequest(String from,String to,User user,double price) {
	
}
