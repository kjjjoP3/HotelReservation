package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

	// Method to display all customers
	private void displayAllCustomers() {
		System.out.println(AdminRepository.getAllCustomers().toString());
	}

	// Method to display all rooms
	private void displayAllRooms() {
		System.out.println(AdminRepository.getAllRooms().toString());
	}

	// Method for adding rooms
	public void addRooms(Scanner scAdmin) {
		List<IRoom> newRooms = new ArrayList<>();
		boolean addingRooms = true;

		while (addingRooms) {
			try {
				int roomNumber = Integer.parseInt(Utils.getInput(scAdmin, Constants.ADD_ROOM_PROMPT));
				double roomPrice = Double.parseDouble(Utils.getInput(scAdmin, Constants.ROOM_PRICE_PROMPT));
				int roomEnumeration = Integer.parseInt(Utils.getInput(scAdmin, Constants.ROOM_TYPE_PROMPT)) - 1;

				newRooms.add(AdminRepository.createRoom(roomNumber, roomPrice, roomEnumeration));

				String addMore = Utils.getInput(scAdmin, Constants.ADD_MORE_ROOMS_PROMPT);
				addingRooms = addMore.equalsIgnoreCase("Y");

			} catch (Exception ex) {
				System.out.println("Error: " + ex.getLocalizedMessage());
			}
		}

		try {
			AdminRepository.addRoom(newRooms);
		} catch (Exception ex) {
			System.out.println("Error adding rooms: " + ex.getLocalizedMessage());
		}
	}
}
