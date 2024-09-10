package model;

public class Room implements IRoom {

	private Integer roomNumber;
	private Double price;
	private RoomType roomType;

	public Room(Integer roomNumber, Double price, RoomType roomType) {
		this.roomNumber = roomNumber;
		this.price = price;
		this.roomType = roomType;
	}

	public Integer getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(Integer roomNumber) {
		this.roomNumber = roomNumber;
	}

	public Double getRoomPrice() {
		return price;
	}

	@Override
	public int hashCode() {
		int hash = 17;
		hash = 31 * hash + (roomNumber != null ? roomNumber.hashCode() : 0);
		hash = 31 * hash + (price != null ? price.hashCode() : 0);
		hash = 31 * hash + (roomType != null ? roomType.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Room other = (Room) obj;
		return roomNumber.equals(other.roomNumber);
	}

	public void setRoomPrice(Double price) {
		this.price = price;
	}

	public RoomType getRoomType() {
		return roomType;
	}

	public void setRoomType(RoomType roomType) {
		this.roomType = roomType;
	}

	public boolean isFree() {
		return price == 0;
	}

	@Override
	public String toString() {
		return String.format("%s room number %d priced at %.2f", roomType, roomNumber, price);
	}
}
