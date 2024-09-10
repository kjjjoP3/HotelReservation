package repository;

import java.util.Collection;
import java.util.List;

import model.Customer;
import model.IRoom;
import service.CustomerService;
import service.ReservationService;

public class AdminRepository {

	private static final CustomerService customerService = CustomerService.getInstance();
	private static final ReservationService reservationService = ReservationService.getInstance();

	public static void addRoom(List<IRoom> rooms) throws Exception {
		for (IRoom each : rooms) {
			try {
				reservationService.addRoom(each);
			} catch (Exception e) {
				System.err.println("Failed to add room " + each.getRoomNumber() + ": " + e.getMessage());
			}
		}
	}

	public static Collection<IRoom> getAllRooms() {
		return reservationService.getAllRooms();
	}

	public static Collection<Customer> getAllCustomers() {
		return customerService.getAllCustomers();
	}

	public static void displayAllReservations() {
		reservationService.printAllReservations();
	}

	public static IRoom createRoom(Integer number, double price, int type) {
		return reservationService.createRoom(number, price, type);
	}
}
