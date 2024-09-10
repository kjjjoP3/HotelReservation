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
		String emailAddress = Utils.getInput(scMain, Constants.EMAIL_PROMPT);
		if (!Utils.isValidEmail(emailAddress))
			return;

		Date checkInDate = Utils.getDateInput(scMain, Constants.CHECK_IN_DATE_PROMPT);
		Date checkOutDate = Utils.getDateInput(scMain, Constants.CHECK_OUT_DATE_PROMPT);
		if (!Utils.checkValidDates(checkInDate, checkOutDate))
			return;

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
