package service;

import model.Customer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CustomerService {

	private static CustomerService INSTANCE;
	private final Map<String, Customer> customers = new HashMap<>();

	private CustomerService() {
	}

	public static CustomerService getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CustomerService();
		}
		return INSTANCE;
	}

	public void addCustomer(String email, String firstName, String lastName) throws Exception {
		if (customers.containsKey(email)) {
			throw new Exception("The customer with email " + email + " already exists.");
		}
		customers.put(email, new Customer(firstName, lastName, email));
	}

	public Customer getCustomer(String customerEmail) {
		return customers.get(customerEmail);
	}

	public Collection<Customer> getAllCustomers() {
		return customers.values();
	}
}
