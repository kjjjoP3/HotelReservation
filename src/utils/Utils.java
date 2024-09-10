package utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Utils {
	public static String getInput(Scanner scanner, String prompt) {
		System.out.println(prompt);
		return scanner.nextLine();
	}

	public static Date getDateInput(Scanner scanner, String prompt) throws Exception {
		System.out.println(prompt);
		return new SimpleDateFormat("MM/dd/yyyy").parse(scanner.nextLine());
	}

	public static boolean isValidEmail(String emailAddress) {
		// Implement email validation logic if needed
		return emailAddress != null && emailAddress.contains("@");
	}

	public static boolean checkValidDates(Date checkIn, Date checkOut) {
		Date now = new Date();
		if (now.compareTo(checkIn) > 0 || now.compareTo(checkOut) > 0) {
			System.out.println("No date can be earlier than now");
			return false;
		}
		if (checkOut.compareTo(checkIn) < 0) {
			System.out.println("Check-out date is earlier than check-in");
			return false;
		}
		return true;
	}
	
	public static boolean isValidEmailCustome(String email) {
	    return email.matches(Constants.EMAIL_PATTERN);
	}
}
