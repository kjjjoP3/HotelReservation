package service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import model.Customer;
import model.IRoom;
import model.Reservation;
import model.Room;
import model.RoomType;
import repository.HotelRepository;

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
		boolean reservationMade = false;
		List<IRoom> availableRooms = new ArrayList<>(rooms);

		// First pass: filter out rooms that are already booked within the specified
		// dates
		for (Reservation currentReservation : reservations) {
			if (currentReservation.checkDates(checkInDate, checkOutDate)) {
				if (isDateOverlap(checkInDate, checkOutDate, currentReservation)) {
					availableRooms.remove(currentReservation.getRoom());
				}
			} else {
				availableRooms.remove(currentReservation.getRoom());
			}
		}

		// If no rooms are available in the requested dates
		if (availableRooms.isEmpty()) {
			System.out.println("No rooms available for the selected dates.");
			Date newCheckInDate = addDays(checkInDate, 7);
			Date newCheckOutDate = addDays(checkOutDate, 7);
			availableRooms.addAll(rooms);

			// Second pass: recommend rooms for alternate dates
			for (Reservation currentReservation : reservations) {
				if (currentReservation.checkDates(newCheckInDate, newCheckOutDate)) {
					if (isDateOverlap(newCheckInDate, newCheckOutDate, currentReservation)) {
						availableRooms.remove(currentReservation.getRoom());
					}
				} else {
					availableRooms.remove(currentReservation.getRoom());
				}
			}

			if (!availableRooms.isEmpty()) {
				System.out
						.println("No rooms for the selected dates. You may want to consider these alternative dates:");
				System.out.println("New check-in: " + newCheckInDate);
				System.out.println("New check-out: " + newCheckOutDate);
				checkInDate = newCheckInDate;
				checkOutDate = newCheckOutDate;
			}
		}

		// If there are available rooms, prompt the user to book one
		if (!availableRooms.isEmpty()) {
			List<Integer> roomNumbers = availableRooms.stream().map(IRoom::getRoomNumber).collect(Collectors.toList());

			while (!reservationMade) {
				System.out.println("Select a room to book or press 'N' to cancel:");
				System.out.println(availableRooms);
				Scanner scanner = new Scanner(System.in);
				String input = scanner.nextLine();

				if (roomNumbers.contains(Integer.parseInt(input))) {
					IRoom selectedRoom = availableRooms.stream()
							.filter(room -> room.getRoomNumber().equals(Integer.parseInt(input))).findFirst()
							.orElse(null);

					if (selectedRoom != null) {
						return HotelRepository.bookARoom(emailAddress, selectedRoom, checkInDate, checkOutDate) != null;
					}
				} else if (input.equalsIgnoreCase("N")) {
					return false;
				} else {
					System.out.println("Please enter a valid room number or 'N' to cancel.");
				}
			}
		} else {
			System.out.println("No available rooms on the selected dates or the suggested alternative dates.");
			return false;
		}

		return false;
	}

	// Helper method to check for date overlap
	private boolean isDateOverlap(Date checkInDate, Date checkOutDate, Reservation reservation) {
		return (checkInDate.before(reservation.getCheckInDate()) && checkOutDate.after(reservation.getCheckOutDate()))
				|| (checkInDate.after(reservation.getCheckInDate())
						&& checkOutDate.before(reservation.getCheckOutDate()))
				|| (checkInDate.before(reservation.getCheckInDate())
						&& checkOutDate.after(reservation.getCheckInDate()))
				|| (checkInDate.before(reservation.getCheckOutDate())
						&& checkOutDate.after(reservation.getCheckOutDate()))
				|| (checkInDate.equals(reservation.getCheckInDate())
						|| checkInDate.equals(reservation.getCheckOutDate())
						|| checkOutDate.equals(reservation.getCheckInDate())
						|| checkOutDate.equals(reservation.getCheckOutDate()));
	}

	// Helper method to add days to a date
	private Date addDays(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, days);
		return cal.getTime();
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
			System.out.println(
					"Which room do you want to book? (Enter the 'Room number' to book a room, or press 'N' to cancel):");
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

	public Collection<String> getCustomersReservation(Customer customer) {
		List<String> customerReservations = new ArrayList<>();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy"); // Date formatter

		for (Reservation reservation : reservations) {
			if (reservation.getCustomer().equals(customer)) {
				// Format check-in and check-out dates
				String formattedCheckIn = dateFormatter.format(reservation.getCheckInDate());
				String formattedCheckOut = dateFormatter.format(reservation.getCheckOutDate());

				// Build the reservation string with formatted dates
				String reservationDetails = reservation.getCustomer() + ", Room: "
						+ reservation.getRoom().getRoomNumber() + " Price: " + reservation.getRoom().getRoomPrice()
						+ " RoomType: " + reservation.getRoom().getRoomType() + ", Check-in: " + formattedCheckIn
						+ ", Check-out: " + formattedCheckOut;

				customerReservations.add(reservationDetails); // Add formatted reservation to the list
			}
		}
		return customerReservations; // Return formatted reservations as a collection
	}


	public void printAllReservations() {
		if (reservations.isEmpty()) {
			System.out.println("No reservations found.");
		} else {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");

			reservations.forEach(reservation -> {
				String formattedCheckIn = dateFormatter.format(reservation.getCheckInDate());
				String formattedCheckOut = dateFormatter.format(reservation.getCheckOutDate());

				System.out.println("Reservation: " + reservation.getCustomer() + ", Check-In: " + formattedCheckIn
						+ ", Check-Out: " + formattedCheckOut + ", Room: " + reservation.getRoom().getRoomNumber()
						+ " Price: " + reservation.getRoom().getRoomPrice() + " RoomType: "
						+ reservation.getRoom().getRoomType());
			});
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
