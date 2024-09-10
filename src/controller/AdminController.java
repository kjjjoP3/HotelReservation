package controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import model.Customer;
import model.IRoom;
import repository.AdminRepository;
import utils.Constants;
import utils.Utils;
import view.AdminMenu;

public class AdminController {

	public void adminMenuMethod(Scanner scAdmin) {
		AdminMenu adminMenu = new AdminMenu();
		boolean inAdminMenu = true;

		while (inAdminMenu) {
			System.out.println(adminMenu);
			try {
				String userInput = scAdmin.nextLine();
				switch (userInput) {
				case "1" -> displayAllCustomers();
				case "2" -> displayAllRooms();
				case "3" -> AdminRepository.displayAllReservations();
				case "4" -> addRooms(scAdmin);
				case "5" -> {
					inAdminMenu = false;
					System.out.println("Going back to Main Menu");
				}
				default -> System.out.println("Please enter a valid option");
				}
			} catch (Exception ex) {
				System.out.println("Error: " + ex.getLocalizedMessage());
			}
		}
	}

	private void displayAllCustomers() {
		Collection<Customer> customers = AdminRepository.getAllCustomers();
		if (customers.isEmpty()) {
			System.out.println("No customers have been registered.");
		} else {
			customers.forEach(customer -> System.out.println(customer));
		}
	}

	// Method to display all rooms
	private void displayAllRooms() {
		Collection<IRoom> rooms = AdminRepository.getAllRooms();
		if (rooms.isEmpty()) {
			System.out.println("No rooms have been created.");
		} else {
			rooms.forEach(room -> System.out.println(room));
		}
	}

	// Method for adding rooms
	public void addRooms(Scanner scAdmin) {
		boolean addingRooms = true;

		while (addingRooms) {
			List<IRoom> newRooms = new ArrayList<>();
			try {
				int roomNumber = Integer.parseInt(Utils.getInput(scAdmin, Constants.ADD_ROOM_PROMPT));
				// Check for duplicate room number
				if (AdminRepository.getAllRooms().stream().anyMatch(room -> room.getRoomNumber().equals(roomNumber))) {
					System.out.println("Error: Room number " + roomNumber
							+ " already exists. Please enter a different room number.");
					continue;
				}
				double roomPrice = Double.parseDouble(Utils.getInput(scAdmin, Constants.ROOM_PRICE_PROMPT));
				int roomEnumeration = Integer.parseInt(Utils.getInput(scAdmin, Constants.ROOM_TYPE_PROMPT)) - 1;

				newRooms.add(AdminRepository.createRoom(roomNumber, roomPrice, roomEnumeration));
				AdminRepository.addRoom(newRooms);
				String addMore = Utils.getInput(scAdmin, Constants.ADD_MORE_ROOMS_PROMPT);
				addingRooms = addMore.equalsIgnoreCase("Y");

			} catch (Exception ex) {
				System.out.println("Error: " + ex.getLocalizedMessage());
			}
		}
	}
}
