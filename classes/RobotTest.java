import lejos.nxt.*;

public class RobotTest {

	public static void main(String[] args) {
		int currentMotorIndex = 0;
		int currentMotorSpeed = 0;
		int selectionPosition = 0;
		updateDisplay(currentMotorIndex, currentMotorSpeed, selectionPosition);
		while (true) {
			if (Button.ESCAPE.isDown()) {
				if (Motor.getInstance(currentMotorIndex).isMoving()) {
					Motor.getInstance(currentMotorIndex).stop();

					while (Button.ESCAPE.isDown()) {
						Button.waitForAnyEvent(10000000);
					}
				} else {
					break;
				}
			} else if (Button.RIGHT.isDown()) {
				if (selectionPosition==0) {
					currentMotorIndex = (currentMotorIndex+1)%3;
					updateDisplay(currentMotorIndex, currentMotorSpeed, selectionPosition);
					
					while (Button.RIGHT.isDown()) {
						Button.waitForAnyEvent(10000000);
					}

				} else if (selectionPosition==1) {
					currentMotorSpeed = (currentMotorSpeed+1)%1000;
					updateDisplay(currentMotorIndex, currentMotorSpeed, selectionPosition);

					if (Button.waitForAnyEvent(700)==0) {
						while (Button.waitForAnyEvent(50)==0) {
							selectionPosition = (selectionPosition+1)%3;
							updateDisplay(currentMotorIndex, currentMotorSpeed, selectionPosition);
						}
					}

				} else {
					Motor.getInstance(currentMotorIndex).forward();
				}
			} else if (Button.LEFT.isDown()) {
				if (selectionPosition==0) {
					currentMotorIndex = (currentMotorIndex-1)%3;
					updateDisplay(currentMotorIndex, currentMotorSpeed, selectionPosition);

					while (Button.LEFT.isDown()) {
						Button.waitForAnyEvent(10000000);
					}

				} else if (selectionPosition==1) {
					currentMotorSpeed = (currentMotorSpeed-1)%1000;
					updateDisplay(currentMotorIndex, currentMotorSpeed, selectionPosition);

					if (Button.waitForAnyEvent(700)==0) {
						while (Button.waitForAnyEvent(50)==0) {
							selectionPosition = (selectionPosition+1)%3;
							updateDisplay(currentMotorIndex, currentMotorSpeed, selectionPosition);
						}
					}

				} else {
					Motor.getInstance(currentMotorIndex).backward();
				}
			} else if (Button.ENTER.isDown()) {
				selectionPosition = (selectionPosition+1)%3;
				updateDisplay(currentMotorIndex, currentMotorSpeed, selectionPosition);

				if (Button.waitForAnyEvent(700)==0) {
					while (Button.waitForAnyEvent(50)==0) {
						selectionPosition = (selectionPosition+1)%3;
						updateDisplay(currentMotorIndex, currentMotorSpeed, selectionPosition);
					}
				}
			}
		}
	}

	private static void updateDisplay(int motorIndex, int currentMotorSpeed, int selectionPosition) {
		LCD.clear();

		if (motorIndex==0) {
			LCD.drawString("A", 0, 0);
		} else if (motorIndex==1) {
			LCD.drawString("B", 0, 0);
		} else if (motorIndex==2) {
			LCD.drawString("C", 0, 0);
		} else {
			LCD.drawString("INVALID INDEX", 0, 0);
		}

		LCD.drawInt(currentMotorSpeed, 0, 1);
		LCD.drawString("Run", 0, 2);

		LCD.drawString("Actual speed:", 0, 4);
		LCD.drawInt(Motor.getInstance(motorIndex).getRotationSpeed(), 0, 5);

		LCD.drawString("<", 15, selectionPosition);
	}
}