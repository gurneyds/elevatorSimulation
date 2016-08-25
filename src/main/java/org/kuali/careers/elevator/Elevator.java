package org.kuali.careers.elevator;

/**
 * Created by gurneyds on 8/25/16.
 */
public class Elevator {
	private String name;
	private int floorTravelTime;
	private int maxTrips;

	private ElevatorState state;
	int onFloorNumber;
	int numberOfTrips;
	int numberOfFloors;

	public Elevator(String name, int floorTravelTime, int maxTrips) {
		this.name = name;
		this.floorTravelTime = floorTravelTime;
		this.maxTrips = maxTrips;
		this.state = ElevatorState.STOPPED_ON_FLOOR;
		this.onFloorNumber = 1;
	}

	// Move the elevator to the destination floor
	public void pickupPassenger(int requestFloor, int destinationFloor) {
		// TODO
	}

	public boolean isInService() {
		return numberOfTrips >= maxTrips;
	}
	public void performService() {
		numberOfTrips = 0;
	}

	@Override
	public String toString() {
		return "Elevator{" +
				"name='" + name + '\'' +
				", floorTravelTime=" + floorTravelTime +
				", maxTrips=" + maxTrips +
				", state=" + state +
				", onFloorNumber=" + onFloorNumber +
				", numberOfTrips=" + numberOfTrips +
				", numberOfFloors=" + numberOfFloors +
				'}';
	}
}
