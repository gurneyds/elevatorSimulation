package org.kuali.careers.elevator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gurneyds on 8/25/16.
 */
public class Elevator {
	// Configuration data
	private String name;
	private int floorTravelTime;
	private int loadTime;
	private int maxTrips;

	// Runtime data
	private ElevatorState state;
	private int onFloorNumber;
	private int numberOfTrips;
	private int numberOfFloors;

	// Requested floors are place in this array
	private List<Integer> travelList = new ArrayList<>();

	public Elevator(String name, int floorTravelTime, int loadTime, int maxTrips) {
		this.name = name;
		this.floorTravelTime = floorTravelTime;
		this.loadTime = loadTime;
		this.maxTrips = maxTrips;
		this.state = ElevatorState.STOPPED_ON_FLOOR;
		this.onFloorNumber = 1;

		// Start the simulation thread
		Thread simulationThread = new Thread(new MovementThread());
		simulationThread.setDaemon(true);	// When the program ends just kill the thread
		simulationThread.run();
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

	private class MovementThread implements Runnable {
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
						System.out.println("Doors closing");

						// Determine if we are going up or down
						state = gotoFloor > onFloorNumber ? ElevatorState.TRAVELING_UP : ElevatorState.TRAVELING_DOWN;

						// Simulate the travel time
						try {
							Thread.sleep(floorTravelTime);
						} catch (InterruptedException e) {
						}

						// Opening the doors
						System.out.println("Doors opening");
						state = ElevatorState.STOPPED_ON_FLOOR;

						// We have arrived at the floor
						onFloorNumber = gotoFloor;

						// Simulate the time it takes for passengers to get on and off
						try {
							Thread.sleep(loadTime);
						} catch (InterruptedException e) {
						}

						// Ready for the next request now
					}
				}

				// Give some time before looking again
				try {
					System.out.println(name + " is waiting for a request...");
					Thread.sleep(500);
				} catch(InterruptedException e) {}
			}
		}
	}
}
