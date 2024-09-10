package service;

import model.Customer;
import model.IRoom;
import model.Reservation;
import model.Room;
import model.RoomType;
import repository.HotelRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class ReservationService {

	private static ReservationService INSTANCE;
	private final Set<Reservation> reservations = new HashSet<>();
	private final Set<IRoom> rooms = new HashSet<>();

	private ReservationService() {
	}

	public static ReservationService getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ReservationService();
		}
		return INSTANCE;
	}

	public void addRoom(IRoom room) throws Exception {
		if (rooms.contains(room)) {
			throw new Exception("The room " + room.getRoomNumber() + " cannot be added. It already exists.");
		}
		rooms.add(room);
	}

	public Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate)
			throws Exception {
		Reservation tempReservation = new Reservation(customer, room, checkInDate, checkOutDate);
		for (Reservation reservation : reservations) {
			if (reservation.getRoom().equals(tempReservation.getRoom())
					&& !reservation.checkDates(tempReservation.getCheckInDate(), tempReservation.getCheckOutDate())) {
				throw new Exception("This reservation is not possible. Check other dates.");
			}
		}
		reservations.add(tempReservation);
		return tempReservation;
	}

	public boolean findRooms(Date checkInDate, Date checkOutDate, String emailAddress) {
		Set<IRoom> availableRooms = new HashSet<>(rooms);
		LocalDate newCheckIn = adjustCheckInOutDates(checkInDate);
		LocalDate newCheckOut = adjustCheckInOutDates(checkOutDate);

		// Remove unavailable rooms based on existing reservations
		for (Reservation reservation : reservations) {
			if (reservation.checkDates(checkInDate, checkOutDate)) {
				availableRooms.remove(reservation.getRoom());
			}
		}

		// If no rooms available, suggest new dates
		if (availableRooms.isEmpty()) {
			System.out.println("No rooms available on the entered dates.");
			System.out.println("Suggested dates: Check-in: " + newCheckIn + ", Check-out: " + newCheckOut);
			availableRooms.addAll(rooms);
			for (Reservation reservation : reservations) {
				if (reservation.checkDates(Date.from(newCheckIn.atStartOfDay(ZoneId.systemDefault()).toInstant()),
						Date.from(newCheckOut.atStartOfDay(ZoneId.systemDefault()).toInstant()))) {
					availableRooms.remove(reservation.getRoom());
				}
			}
			if (availableRooms.isEmpty()) {
				System.out.println("No rooms available even on suggested dates.");
				return false;
			}
		}

		// Book a room from available options
		return selectRoomToBook(availableRooms, emailAddress, newCheckIn, newCheckOut);
	}

	private LocalDate adjustCheckInOutDates(Date date) {
		LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return localDate.plusDays(8); // Adjust check-in/out by adding 8 days
	}

	private boolean selectRoomToBook(Collection<IRoom> availableRooms, String emailAddress, LocalDate checkIn,
			LocalDate checkOut) {
		System.out.println("Available rooms: " + availableRooms);
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);

		while (true) {
			System.out.println("Which room do you want to book? (Enter the 'Room number' to book a room, or press 'N' to cancel):");
			String input = scanner.nextLine();
			if (input.equalsIgnoreCase("N")) {
				return false; // User chose to exit
			}

			try {
				int roomNumber = Integer.parseInt(input);
				for (IRoom room : availableRooms) {
					if (room.getRoomNumber() == roomNumber) {
						return HotelRepository.bookARoom(emailAddress, room,
								Date.from(checkIn.atStartOfDay(ZoneId.systemDefault()).toInstant()),
								Date.from(checkOut.atStartOfDay(ZoneId.systemDefault()).toInstant())) != null;
					}
				}
				System.out.println("Invalid room number. Please try again.");
			} catch (NumberFormatException e) {
				System.out.println("Please enter a valid room number.");
			}
		}
	}

	public Collection<Reservation> getCustomersReservation(Customer customer) {
		List<Reservation> customerReservations = new ArrayList<>();
		for (Reservation reservation : reservations) {
			if (reservation.getCustomer().equals(customer)) {
				customerReservations.add(reservation);
			}
		}
		return customerReservations;
	}

	public void printAllReservations() {
		if (reservations.isEmpty()) {
			System.out.println("No reservations found.");
		} else {
			reservations.forEach(reservation -> System.out.println(reservation));
		}
	}

	public Collection<IRoom> getAllRooms() {
		return rooms;
	}

	public IRoom getRoom(String roomNumber) {
		return rooms.stream().filter(room -> room.getRoomNumber().equals(roomNumber)).findFirst().orElse(null);
	}

	public IRoom createRoom(Integer number, double price, int type) {
		return new Room(number, price, RoomType.values()[type]);
	}
}
