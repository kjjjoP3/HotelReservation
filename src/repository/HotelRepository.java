package repository;

import java.util.Collection;
import java.util.Date;

import model.Customer;
import model.IRoom;
import model.Reservation;
import service.CustomerService;
import service.ReservationService;

public class HotelRepository {

	private static final CustomerService customerService = CustomerService.getInstance();
	private static final ReservationService reservationService = ReservationService.getInstance();

	public static Customer getCustomer(String email) {
		return customerService.getCustomer(email);
	}

	public static void createACustomer(String email, String firstName, String lastName) throws Exception {
		try {
			customerService.addCustomer(email, firstName, lastName);
			System.out.println(String.format("Successfully registered for account: %s", email));
		} catch (Exception e) {
			throw new Exception("Error adding customer: " + e.getLocalizedMessage());
		}
	}

	public static IRoom getRoom(String roomNumber) throws Exception {
		IRoom room = reservationService.getRoom(roomNumber);
		if (room == null) {
			throw new Exception("Room number " + roomNumber + " does not exist");
		}
		return room;
	}

	public static Reservation bookARoom(String customerEmail, IRoom room, Date checkInDate, Date checkOutDate) {
		Reservation reservation;
		try {
			reservation = reservationService.reserveARoom(customerService.getCustomer(customerEmail), room, checkInDate,
					checkOutDate);
		} catch (Exception e) {
			System.out.println("Booking failed: " + e.getLocalizedMessage());
			reservation = null;
		}
		return reservation;
	}

	public static Collection<Reservation> getCustomersReservations(String customerEmail) {
		return reservationService.getCustomersReservation(customerService.getCustomer(customerEmail));
	}

	public static boolean findARoom(Date checkIn, Date checkOut, String emailAdress) {
		return reservationService.findRooms(checkIn, checkOut, emailAdress);
	}

	public static Collection<Customer> getAllCustomers() {
		return customerService.getAllCustomers();
	}

	public static boolean emailExistInCustomers(String email) {
		for (Customer customer : getAllCustomers()) {
			if (customer.getEmail().equals(email)) {
				return true;
			}
		}
		return false;
	}
}
