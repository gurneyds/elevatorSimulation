package org.kuali.careers.elevator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gurneyds on 8/25/16.
 */
public class Controller {
	private static final int FLOOR_TRAVEL_TIME = 10;
	private static final int MAX_CYCLES = 100;

	private List<Elevator> elevatorList = new ArrayList<>();
	private int numFloors;

	public Controller(int numElevators, int numFloors) {
		// TODO - validate incoming numbers

		this.numFloors = numFloors;

		for(int i=0; i < numElevators; i++) {
			elevatorList.add(new Elevator("Elevator" + i, FLOOR_TRAVEL_TIME, MAX_CYCLES));
		}
	}

	// This is the main api for a caller to request an elevator
	public void request(int pickupFloor, int destinationFloor) {
		// Find a suitable elevator to be used
		Elevator elevator = findElevator(pickupFloor, destinationFloor);

		if(elevator != null) {
			// Dispatch the elevator
			elevator.pickupPassenger(pickupFloor, destinationFloor);
		} else {
			System.out.println("Sorry - no elevators are in service at this time, please use the stairs!");
		}
	}

	// Service just the elevators that have exceeded their cycles
	public void serviceSomeElevators() {
		for(Elevator elevator : elevatorList) {
			if(!elevator.isInService()) {
				elevator.performService();
			}
		}
	}

	//Service all elevators
	public void serviceAllElevators() {
		for(Elevator elevator : elevatorList) {
			elevator.performService();
		}
	}

	public void showElevatorState() {
		for(Elevator e : elevatorList) {
			System.out.println(e);
		}
	}

	// Algorithm to find the best elevator to dispatch
	private Elevator findElevator(int pickupFloor, int destinationFloor) {
		// Rules in priority order:
		// 1-) Moving elevator that will pass by floor
		// 2-) Unoccupied elevator closest to request will answer
		//
		// TODO - implement
		return elevatorList.get(0);
	}
}
