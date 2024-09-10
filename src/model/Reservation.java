package model;

import java.util.Date;

public class Reservation {

	private Customer customer;
	private IRoom room;
	private Date checkInDate;
	private Date checkOutDate;

	/**
	 * @param customer
	 * @param room
	 * @param checkInDate
	 * @param checkOutDate
	 */
	public Reservation(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
		this.customer = customer;
		this.room = room;
		this.checkInDate = checkInDate;
		this.checkOutDate = checkOutDate;
	}

	/**
	 * @return the customer
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * @param customer the customer to set
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	/**
	 * @return the room
	 */
	public IRoom getRoom() {
		return room;
	}

	/**
	 * @param room the room to set
	 */
	public void setRoom(IRoom room) {
		this.room = room;
	}

	/**
	 * @return the checkInDate
	 */
	public Date getCheckInDate() {
		return checkInDate;
	}

	/**
	 * @param checkInDate the checkInDate to set
	 */
	public void setCheckInDate(Date checkInDate) {
		this.checkInDate = checkInDate;
	}

	/**
	 * @return the checkOutDate
	 */
	public Date getCheckOutDate() {
		return checkOutDate;
	}

	/**
	 * @param checkOutDate the checkOutDate to set
	 */
	public void setCheckOutDate(Date checkOutDate) {
		this.checkOutDate = checkOutDate;
	}

	public boolean checkDates(Date checkIn, Date checkOut) {
		boolean isOverlapping = (this.checkInDate.before(checkOut) && this.checkOutDate.after(checkIn));
		return !isOverlapping;
	}

	@Override
	public String toString() {
		return String.format("%s has booked room %s from %s to %s", 
			customer, room, checkInDate, checkOutDate);
	}

	@Override
	public int hashCode() {
		int hash = 17;
		hash = 31 * hash + (checkInDate == null ? 0 : checkInDate.hashCode());
		hash = 31 * hash + (checkOutDate == null ? 0 : checkOutDate.hashCode());
		hash = 31 * hash + (room == null ? 0 : room.hashCode());
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Reservation other = (Reservation) obj;
		return room.equals(other.room) &&
			checkInDate.equals(other.checkInDate) &&
			checkOutDate.equals(other.checkOutDate);
	}
}
