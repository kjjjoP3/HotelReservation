package model;

public class FreeRoom extends Room {

	FreeRoom(Integer roomNumber, Double price, RoomType enumeration) {
		super(roomNumber, 0.0, enumeration);
	}

	@Override
	public String toString() {
		return this.getRoomPrice() + " room number " + this.getRoomNumber() + ", Free";
	}

}
