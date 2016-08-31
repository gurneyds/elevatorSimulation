package org.kuali.careers.elevator;

import org.kuali.careers.elevator.exceptions.ElevatorException;
import org.kuali.careers.elevator.exceptions.InvalidFloorRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by gurneyds on 8/25/16.
 */
public class Controller {
	// Time is in Milliseconds
	private static final int FLOOR_TRAVEL_TIME = 10000;
	private static final int LOAD_TIME = 2000;

	private static final int MAX_CYCLES = 100;

	private List<Elevator> elevatorList = new ArrayList<>();
	private int numFloors;

	public Controller(int numElevators, int numFloors) {
		if(numElevators > 0 && numFloors > 0) {
			this.numFloors = numFloors;

			for (int i = 0; i < numElevators; i++) {
				Elevator elevator = new Elevator("Elevator" + i, FLOOR_TRAVEL_TIME, LOAD_TIME, MAX_CYCLES);
				elevatorList.add(elevator);
				elevator.putIntoService();
			}
		} else {
			throw new RuntimeException("Invalid number of elevators or number of floors");
		}
	}

	// Alternate way to create the controller. User can provide elevator data
	public Controller(List<Elevator> elevatorList, int numFloors) throws ElevatorException {
		if(elevatorList == null || (elevatorList != null && elevatorList.size() <= 0)) {
			throw new ElevatorException("No elevators provided");
		}

		elevatorList.stream().forEach( e -> e.putIntoService());

		this.elevatorList = elevatorList;
		this.numFloors = numFloors;
	}

	// This is the main api for a caller to request an elevator
	// The elevator that will pickup the user is returned. This is done to facilitate testing, but may be useful
	public String request(int pickupFloor, int destinationFloor) throws InvalidFloorRequest, ElevatorException {
		Elevator elevator = null;

		if(pickupFloor < 1 || destinationFloor < 1) {
			throw new InvalidFloorRequest();
		} else if(pickupFloor > numFloors || destinationFloor > numFloors) {
			throw new InvalidFloorRequest();
		} else {
			// Find a suitable elevator to be used
			elevator = findElevator(pickupFloor, destinationFloor);

			if (elevator != null) {
				// Dispatch the elevator
				elevator.pickupPassenger(pickupFloor, destinationFloor);
			} else {
				System.out.println("Sorry - no elevators are in service at this time, please use the stairs!");
				throw new ElevatorException("All elevators require service");
			}
		}
		return elevator.getName();
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
		Elevator bestElevator = null;

		List<Elevator> movingUpElevators = elevatorList
				.stream()
				.filter(e -> e.getState() == ElevatorState.TRAVELING_UP)
				.collect(Collectors.toList());

		List<Elevator> movingDownElevators = elevatorList
				.stream()
				.filter(e -> e.getState() == ElevatorState.TRAVELING_DOWN)
				.collect(Collectors.toList());

		List<Elevator> stoppedElevators = elevatorList
				.stream()
				.filter(e -> e.getState() == ElevatorState.STOPPED_ON_FLOOR)
				.collect(Collectors.toList());

		// Rules in priority order:
		// 1-) Moving elevator that will pass by floor
		// 2-) Unoccupied elevator closest to request will answer

		boolean movingUp = destinationFloor > pickupFloor ? true :false;

		if(movingUp) {
			for(Elevator e : movingUpElevators) {
				// Subtract 1 from the current floor because the elevator will need a little notice to decelerate
				if(e.getCurrentFloor()-1 < pickupFloor) {
					bestElevator = e;
					break;
				}
			}
		} else {
			for(Elevator e : movingDownElevators) {
				// Add 1 to the current floor because the elevator will need a little notice to decelerate
				if(e.getCurrentFloor()+1 > pickupFloor) {
					bestElevator = e;
					break;
				}
			}
		}

		if(bestElevator == null) {
			// Look for a stationary elevator
			if(stoppedElevators.size() > 0) {
				bestElevator = stoppedElevators.get(0);
			}
		}

		// If there still isn't an elevator, then find the elevator that will be closest to the request floor when
		// it is done with it's last request

		if(bestElevator == null) {
			// Worst case is that the request is at one end of the building and all the elevators are at the other end
			int floorsAway = numFloors;
			for (Elevator e : elevatorList) {
				if(e.getState() != ElevatorState.OUT_OF_SERVICE) {
					int floorNum = e.getLastRequestedFloor();
					int diff = floorNum - pickupFloor;
					if (diff < 0) {
						diff = -diff;
					}
					if (diff < floorsAway) {
						bestElevator = e;
						floorsAway = diff;
					}
				}
			}
		}

		return bestElevator;
	}
}
