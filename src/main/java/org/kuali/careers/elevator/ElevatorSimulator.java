package org.kuali.careers.elevator;

/**
 * Created by gurneyds on 8/25/16.
 */
public class ElevatorSimulator {
	public static void main(String args[]) {
		System.out.println("Welcome to the elevator simulator");

		Controller controller = new Controller(2, 10);

		controller.showElevatorState();

		controller.request(1, 2);
		controller.request(2, 3);
		controller.request(3, 4);
		controller.request(5, 1);

		controller.showElevatorState();

		try {
			Thread.sleep(7000);
		} catch(InterruptedException e) {
			System.out.println("interrupted");
		}

		System.out.println("--------------------------------");
		controller.showElevatorState();
	}
}
