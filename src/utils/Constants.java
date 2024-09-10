package utils;

public class Constants {
    // Application constants
    public static final String EMAIL_PROMPT = "Enter email address (example@domain.com) to book a room: ";
    public static final String CHECK_IN_DATE_PROMPT = "Enter check-in date (MM/dd/yyyy): ";
    public static final String CHECK_OUT_DATE_PROMPT = "Enter check-out date (MM/dd/yyyy): ";
    public static final String ACCOUNT_REGISTERED_MESSAGE = "These are the accounts registered: ";
    public static final String DATE_ERROR_MESSAGE = "No date can be earlier than now";
    public static final String CHECKOUT_ERROR_MESSAGE = "Check-out date is earlier than check-in";
    public static final String EXIT_MESSAGE = "Exiting program";
    public static final String ADD_ROOM_PROMPT = "Enter Room Number: ";
    public static final String ROOM_PRICE_PROMPT = "Enter Room Price: ";
    public static final String ROOM_TYPE_PROMPT = "Enter Room Type (1-Single, 2-Double): ";
    public static final String ADD_MORE_ROOMS_PROMPT = "Do you want to add more rooms? (Y/N): ";
    
    // Regex pattern for email validation
    public static final String EMAIL_PATTERN = "^(.+)@(.+)\\.com$";
}

