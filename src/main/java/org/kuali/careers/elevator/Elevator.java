package org.kuali.careers.elevator;

/**
 * Created by gurneyds on 8/25/16.
 */
public class Elevator {
	private String name;
	private int floorTravelTime;
	private int maxCycles;

	private ElevatorState state;
	int onFloorNumber;
	int numberOfTrips;
	int cycles;

	public Elevator(String name, int floorTravelTime, int maxCycles) {
		this.name = name;
		this.floorTravelTime = floorTravelTime;
		this.maxCycles = maxCycles;
	}

	// Move the elevator to the destination floor
	public void pickupPassenger(int destinationFloor) {
		// TODO
	}

	public boolean isInService() {
		return cycles >= maxCycles;
	}

	@Override
	public String toString() {
		return "Elevator{" +
				"name='" + name + '\'' +
				", state=" + state +
				", onFloorNumber=" + onFloorNumber +
				", numberOfTrips=" + numberOfTrips +
				'}';
	}
}
