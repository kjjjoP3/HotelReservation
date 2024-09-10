import controller.MainController;
import controller.AdminController;
import view.MainMenu;
import java.util.Scanner;

public class HotelApplication {

	public static void main(String[] args) {
		MainController mainController = new MainController();
		AdminController adminController = new AdminController();
		MainMenu mainMenu = new MainMenu();
		boolean running = true;
		Scanner scMain = new Scanner(System.in);

		System.out.println("Hello and welcome to the Hotel Reservation Application");
		while (running) {
			try {
				System.out.println(mainMenu);
				String userInput = scMain.nextLine();
				switch (userInput) {
				case "1" -> mainController.handleFindAndReserveRoom(scMain);
				case "2" -> mainController.handleSeeReservation(scMain);
				case "3" -> mainController.handleCreateAccount(scMain);
				case "4" -> adminController.adminMenuMethod(scMain);
				case "5" -> {
					System.out.println("Exiting program");
					scMain.close();
					running = false;
				}
				default -> System.out.println("Please enter a valid option");
				}
			} catch (Exception ex) {
				System.out.println("Error: " + ex.getLocalizedMessage());
			}
		}
	}
}
