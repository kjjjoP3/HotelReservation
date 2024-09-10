package controller;

import java.util.Collection;
import java.util.Date;
import java.util.Scanner;

import model.Customer;
import repository.HotelRepository;
import utils.Constants;
import utils.Utils;

public class MainController {

	public void handleFindAndReserveRoom(Scanner scMain) throws Exception {
		if (HotelRepository.getAllCustomers().isEmpty()) {
			System.out.println("You cannot get a reservation if there are no customers registered.");
			return;
		}
		System.out.println(Constants.ACCOUNT_REGISTERED_MESSAGE);
		for (Customer c : HotelRepository.getAllCustomers()) {
			System.out.println(c);
		}
		String emailAddress;
		// Loop until a valid email address is entered
		while (true) {
			String inputEmail = Utils.getInput(scMain, Constants.EMAIL_PROMPT);
			// Check if the email format is valid and if it exists in the list of customers
			if (Utils.isValidEmail(inputEmail) && HotelRepository.getAllCustomers() != null
					&& HotelRepository.getAllCustomers().stream().anyMatch(c -> c.getEmail().equals(inputEmail))) {
				emailAddress = inputEmail;
				break; // Exit loop if email is valid and exists
			} else {
				System.out.println("Invalid email address. Please enter a valid registered email.");
			}
		}

		Date checkInDate;
		Date checkOutDate;

		// Loop until valid check-in and check-out dates are provided
		while (true) {
			checkInDate = Utils.getDateInput(scMain, Constants.CHECK_IN_DATE_PROMPT);
			checkOutDate = Utils.getDateInput(scMain, Constants.CHECK_OUT_DATE_PROMPT);

			if (Utils.checkValidDates(checkInDate, checkOutDate)) {
				break;
			} else {
				System.out.println("Please enter valid check-in and check-out dates.");
			}
		}

		if (HotelRepository.findARoom(checkInDate, checkOutDate, emailAddress)) {
			System.out.println("Room booked");
		} else {
			System.out.println("Nothing booked");
		}
	}

	public void handleSeeReservation(Scanner scMain) {
		Collection<Customer> customers = HotelRepository.getAllCustomers();

		if (customers.isEmpty()) {
			System.out.println("No email set up.");
		} else {
			System.out.println(Constants.ACCOUNT_REGISTERED_MESSAGE);
			for (Customer c : customers) {
				System.out.println(c);
			}

			String emailAddress = Utils.getInput(scMain, "Enter email address to see reservations: ");
			if (!Utils.isValidEmail(emailAddress)) {
				return;
			}
			// Print the reservations for the specified email address
			System.out.println(HotelRepository.getCustomersReservations(emailAddress));
		}
	}

	public void handleCreateAccount(Scanner scMain) {
		String email;
		while (true) {
			email = Utils.getInput(scMain, "Enter Email: ");
			if (Utils.isValidEmailCustome(email)) {
				break; // Exit loop if the email is valid
			}
			System.out.println("Invalid email format. Please enter a valid email (name@domain.com).");
		}
		String firstName = Utils.getInput(scMain, "Enter First Name: ");
		String lastName = Utils.getInput(scMain, "Enter Last Name: ");

		try {
			HotelRepository.createACustomer(email, firstName, lastName);
		} catch (Exception e) {
			System.out.println("Error: " + e.getLocalizedMessage());
		}
	}
}
