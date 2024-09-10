package model;

public interface IRoom {

	public Integer getRoomNumber();

	public Double getRoomPrice();

	public RoomType getRoomType();

	public boolean isFree();

	@Override
	boolean equals(Object obj);

	@Override
	int hashCode();

	@Override
	String toString();

}
