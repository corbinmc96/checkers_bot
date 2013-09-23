import lejos.nxt.*;

public class RobotTest {

	public static void main(String[] args) {
		int currentMotorIndex = 0;
		printMotor(currentMotorIndex);
		while (true) {
			if (Button.ESCAPE.isDown()) {
				break;
			} else if (Button.RIGHT.isDown()) {
				Motor.getInstance(currentMotorIndex).forward();
			} else if (Button.LEFT.isDown()) {
				Motor.getInstance(currentMotorIndex).backward();
			} else if (Button.ENTER.isDown()) {
				if (Motor.getInstance(currentMotorIndex).isMoving()) {
					Motor.getInstance(currentMotorIndex).stop();
				} else {
					currentMotorIndex = ++currentMotorIndex%3;
					printMotor(currentMotorIndex);
				}
				
				while (Button.ENTER.isDown()) {
					Button.waitForAnyEvent(1000);
				}
			}
		}
	}

	private static void printMotor(int motorIndex) {
		if (motorIndex==0) {
			System.out.println("A");
		} else if (motorIndex==1) {
			System.out.println("B");
		} else if (motorIndex==2) {
			System.out.println("C");
		} else {
			System.out.println("INVALID MOTOR INDEX");
		}
	}
}