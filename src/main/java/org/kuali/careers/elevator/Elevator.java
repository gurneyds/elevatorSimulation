package org.kuali.careers.elevator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gurneyds on 8/25/16.
 */
public class Elevator implements Runnable {
	private static final int DEFAULT_FLOOR_TRAVEL_TIME = 10000;	// Milliseconds
	private static final int DEFAULT_MAX_TRIPS = 100;
	private static final int DEFAULT_LOAD_TIME = 5000;	// Milliseconds

	// Configuration data
	private String name;
	private int floorTravelTime = DEFAULT_FLOOR_TRAVEL_TIME;
	private int loadTime = DEFAULT_LOAD_TIME;
	private int maxTrips = DEFAULT_MAX_TRIPS;

	// Runtime data
	private ElevatorState state = ElevatorState.STOPPED_ON_FLOOR;
	private int onFloorNumber = 1;
	private int numberOfTrips;
	private int numberOfFloors;

	// Requested floors are place in this array
	private List<Integer> travelList = new ArrayList<>();

	public Elevator(String name, int floorTravelTime, int loadTime, int maxTrips) {
		this.name = name;
		this.floorTravelTime = floorTravelTime;
		this.loadTime = loadTime;
		this.maxTrips = maxTrips;
		this.onFloorNumber = 1;
	}

	public Elevator(String name) {
		this.name = name;
	}

	public void putIntoService() {
		// Start the simulation thread
		Thread simulationThread = new Thread(this);
		simulationThread.start();
	}

	public String getName() {
		return name;
	}


	// Move the elevator to the destination floor
	public void pickupPassenger(int requestFloor, int destinationFloor) {
		// Apply some logic so that these are place in the correct order
		travelList.add(requestFloor);
		travelList.add(destinationFloor);
	}

	public boolean isInService() {
		return numberOfTrips >= maxTrips;
	}
	public void performService() {
		numberOfTrips = 0;
	}

	public ElevatorState getState() {
		return state;
	}
	public void setElevatorState(ElevatorState state) {
		this.state = state;
	}

	public int getCurrentFloor() {
		return onFloorNumber;
	}
	public void setCurrentFloor(int onFloorNumber) {
		this.onFloorNumber = onFloorNumber;
	}

	// This is the last request floor where the elevator will stop moving (end of a trip)
	public int getLastRequestedFloor() {
		int lastRequestedFloor = 1;
		synchronized (travelList) {
			if(travelList.size() > 0) {
				lastRequestedFloor = travelList.get(travelList.size() - 1);
			} else {
				// Return the floor that the elevator is on right now
				lastRequestedFloor = onFloorNumber;
			}
		}
		return lastRequestedFloor;
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

	@Override
	public void run() {
		while(true) {
			// This is where we will be going
			Integer gotoFloor = null;

			// Watch the travelList and move the elevator between floors
			synchronized (travelList) {
				if(travelList.size() > 0) {
					// Pull the first floor off the list and travel to it
					gotoFloor = travelList.get(0);
					travelList.remove(0);
				}
			}

			synchronized (state) {
				if (gotoFloor != null) {
					// Close the doors - in a real system we would emit or publish an event
					System.out.println(name + " - doors closing");

					// Determine if we are going up or down
					state = gotoFloor > onFloorNumber ? ElevatorState.TRAVELING_UP : ElevatorState.TRAVELING_DOWN;

					System.out.println(name + " - traveling to floor #" + gotoFloor);
					// Simulate the travel time
					try {
						Thread.sleep(floorTravelTime);
					} catch (InterruptedException e) {
					}

					// Opening the doors
					System.out.println(name + " - doors opening");
					state = ElevatorState.STOPPED_ON_FLOOR;

					// We have arrived at the floor
					onFloorNumber = gotoFloor;

					// Simulate the time it takes for passengers to get on and off
					try {
						Thread.sleep(loadTime);
					} catch (InterruptedException e) {
					}

					// Ready for the next request now
				} else {
					System.out.println(name + " is waiting on floor#" + getCurrentFloor());
				}
			}

			// Give some time before looking again
			try {
				Thread.sleep(1000);
			} catch(InterruptedException e) {}
		}
	}
}
