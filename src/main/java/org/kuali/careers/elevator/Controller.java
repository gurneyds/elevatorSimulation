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

	public void showElevatorState() {
		for(Elevator e : elevatorList) {
			System.out.println(e);
		}
	}
}
