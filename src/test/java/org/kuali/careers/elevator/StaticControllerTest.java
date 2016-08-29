package org.kuali.careers.elevator;

import org.junit.Test;
import org.kuali.careers.elevator.exceptions.ElevatorException;
import org.kuali.careers.elevator.exceptions.InvalidFloorRequest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by gurneyds on 8/25/16.
 */
public class StaticControllerTest {
	@Test(expected = RuntimeException.class)
	public void invalidNumberOfElevators() {
		new Controller(0, 1);
	}

	@Test(expected = RuntimeException.class)
	public void invalidNumberOfFloors() {
		new Controller(1, 0);
	}

	@Test(expected = RuntimeException.class)
	public void invalidConstructorBothArgs() {
		new Controller(-1, -1);
	}

	@Test
	public void invalidPickupFloor() {
		Controller c = new Controller(1, 1);
		try {
			c.request(-1, 1);
			assertTrue(false);
		} catch(InvalidFloorRequest e) {
			assertTrue(true);
		} catch(ElevatorException e) {
			assertTrue(false);
		}
	}

	@Test
	public void invalidDestinationFloor() {
		Controller c = new Controller(1, 1);
		try {
			c.request(1, -1);
			assertTrue(false);
		} catch(InvalidFloorRequest e) {
			assertTrue(true);
		} catch(ElevatorException e) {
			assertTrue(false);
		}
	}

	@Test
	public void oneElevatorOnRequestedFloor() {
		List<Elevator> elevatorList = new ArrayList<>();
		Elevator e1 = new Elevator("Number1");	e1.setCurrentFloor(1);

		elevatorList.add(new Elevator("Number1"));
		try {
			Controller c = new Controller(elevatorList, 10);
			String elevatorName = c.request(1, 2);
			assertTrue(elevatorName.equals("Number1"));
		} catch(ElevatorException e) {
			assertTrue(false);
		} catch(InvalidFloorRequest e) {
			assertTrue(false);
		}
	}

	@Test
	public void stoppedElevatorTest() {
		List<Elevator> elevatorList = new ArrayList<>();
		Elevator e1 = new Elevator("Number1");	e1.setCurrentFloor(10);

		elevatorList.add(new Elevator("Number1"));
		try {
			Controller c = new Controller(elevatorList, 10);
			String elevatorName = c.request(1, 2);
			assertTrue(elevatorName.equals("Number1"));
		} catch(ElevatorException e) {
			assertTrue(false);
		} catch(InvalidFloorRequest e) {
			assertTrue(false);
		}
	}

	@Test
	public void allMovingNeedToWaitAndFindClosestTest() {
		List<Elevator> elevatorList = new ArrayList<>();
		Elevator e1 = new Elevator("Number1");	e1.setCurrentFloor(2); e1.setElevatorState(ElevatorState.TRAVELING_UP);
		Elevator e2 = new Elevator("Number2");	e2.setCurrentFloor(3); e2.setElevatorState(ElevatorState.TRAVELING_UP);
		Elevator e3 = new Elevator("Number3");	e3.setCurrentFloor(10); e3.setElevatorState(ElevatorState.TRAVELING_UP);

		elevatorList.add(e1);
		elevatorList.add(e2);
		elevatorList.add(e3);
		try {
			Controller c = new Controller(elevatorList, 10);
			String elevatorName = c.request(1, 2);
			System.out.println(elevatorName);
			assertTrue(elevatorName.equals("Number1"));
		} catch(ElevatorException e) {
			assertTrue(false);
		} catch(InvalidFloorRequest e) {
			assertTrue(false);
		}
	}

	@Test
	public void interruptMovingUpElevatorTest() {
		List<Elevator> elevatorList = new ArrayList<>();
		Elevator e1 = new Elevator("Number1");	e1.setCurrentFloor(2); e1.setElevatorState(ElevatorState.STOPPED_ON_FLOOR);
		Elevator e2 = new Elevator("Number2");	e2.setCurrentFloor(5); e2.setElevatorState(ElevatorState.TRAVELING_UP);
		Elevator e3 = new Elevator("Number3");	e3.setCurrentFloor(10); e3.setElevatorState(ElevatorState.TRAVELING_DOWN);

		elevatorList.add(e1);
		elevatorList.add(e2);
		elevatorList.add(e3);
		try {
			Controller c = new Controller(elevatorList, 10);
			String elevatorName = c.request(7, 8);
			System.out.println(elevatorName);
			assertTrue(elevatorName.equals("Number2"));
		} catch(ElevatorException e) {
			assertTrue(false);
		} catch(InvalidFloorRequest e) {
			assertTrue(false);
		}
	}

	@Test
	public void interruptMovingDownElevatorTest() {
		List<Elevator> elevatorList = new ArrayList<>();
		Elevator e1 = new Elevator("Number1");	e1.setCurrentFloor(2); e1.setElevatorState(ElevatorState.STOPPED_ON_FLOOR);
		Elevator e2 = new Elevator("Number2");	e2.setCurrentFloor(5); e2.setElevatorState(ElevatorState.TRAVELING_UP);
		Elevator e3 = new Elevator("Number3");	e3.setCurrentFloor(10); e3.setElevatorState(ElevatorState.TRAVELING_DOWN);

		elevatorList.add(e1);
		elevatorList.add(e2);
		elevatorList.add(e3);
		try {
			Controller c = new Controller(elevatorList, 10);
			String elevatorName = c.request(7, 6);
			System.out.println(elevatorName);
			assertTrue(elevatorName.equals("Number3"));
		} catch(ElevatorException e) {
			assertTrue(false);
		} catch(InvalidFloorRequest e) {
			assertTrue(false);
		}
	}

	@Test
	public void outOfServiceTest() {
		List<Elevator> elevatorList = new ArrayList<>();
		Elevator e1 = new Elevator("Number1");	e1.setCurrentFloor(2); e1.setElevatorState(ElevatorState.OUT_OF_SERVICE);
		Elevator e2 = new Elevator("Number2");	e2.setCurrentFloor(5); e2.setElevatorState(ElevatorState.OUT_OF_SERVICE);
		Elevator e3 = new Elevator("Number3");	e3.setCurrentFloor(10); e3.setElevatorState(ElevatorState.OUT_OF_SERVICE);

		elevatorList.add(e1);
		elevatorList.add(e2);
		elevatorList.add(e3);
		try {
			Controller c = new Controller(elevatorList, 10);
			String elevatorName = c.request(7, 6);
			System.out.println(elevatorName);
			assertTrue(false);	// Shouldn't get here
		} catch(ElevatorException e) {
			assertTrue(true);
		} catch(InvalidFloorRequest e) {
			assertTrue(false);
		}
	}
}
