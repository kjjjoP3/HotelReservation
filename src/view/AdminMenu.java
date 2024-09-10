package view;

import java.util.ArrayList;
import java.util.List;

public class AdminMenu {

	List<String> menuItems = new ArrayList<String>();

	public AdminMenu() {
		menuItems.add("Admin Menu:");
		menuItems.add("1. See all Customers");
		menuItems.add("2. See all Rooms");
		menuItems.add("3. See all Reservations");
		menuItems.add("4. Add a Room");
		menuItems.add("5. Back to Main Menu");
		menuItems.add("Please select and option");
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (String each : menuItems) {
			sb.append(each).append("\n");
		}
		return sb.toString();
	}
}
