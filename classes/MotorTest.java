// ALL AARON

import lejos.nxt.*;
import lejos.nxt.remote.*;

public class MotorTest {
	private static int drawnMotorIndex = -1;
	private static int drawnMotorSpeed = -1;
	private static int drawnSelectionPosition = -1;
	private static int drawnRotationSpeed = -1;

	private static RemoteMotor[] motors = new RemoteMotor[] {Motor.A, Motor.B, Motor.C};

	public static void main(String[] args) throws InterruptedException {
		int currentMotorIndex = 0;
		int currentMotorSpeed = 0;
		int selectionPosition = 0;
		while (true) {
			updateDisplay(currentMotorIndex, currentMotorSpeed, selectionPosition);
			if (Button.ESCAPE.isDown()) {
				if (MotorTest.motors[currentMotorIndex].isMoving()) {
					MotorTest.motors[currentMotorIndex].stop();

					while (Button.ESCAPE.isDown()) {
						//pass
					}
				} else {
					break;
				}
			} else if (Button.RIGHT.isDown()) {
				if (selectionPosition==0) {
					currentMotorIndex = (currentMotorIndex+1)%3;
					updateDisplay(currentMotorIndex, currentMotorSpeed, selectionPosition);
					
					while (Button.RIGHT.isDown()) {
						//pass
					}

				} else if (selectionPosition==1) {
					currentMotorSpeed = (currentMotorSpeed+1)%1000;
					Motor.A.setSpeed(currentMotorSpeed);
					Motor.B.setSpeed(currentMotorSpeed);
					Motor.C.setSpeed(currentMotorSpeed);
					updateDisplay(currentMotorIndex, currentMotorSpeed, selectionPosition);

					Thread.sleep(700);
					while (Button.RIGHT.isDown()) {
						currentMotorSpeed = (currentMotorSpeed+1)%1000;
						Motor.A.setSpeed(currentMotorSpeed);
						Motor.B.setSpeed(currentMotorSpeed);
						Motor.C.setSpeed(currentMotorSpeed);
						updateDisplay(currentMotorIndex, currentMotorSpeed, selectionPosition);
						Thread.sleep(50);
					}

				} else {
					MotorTest.motors[currentMotorIndex].forward();
				}
			} else if (Button.LEFT.isDown()) {
				if (selectionPosition==0) {
					currentMotorIndex = (currentMotorIndex+2)%3;
					updateDisplay(currentMotorIndex, currentMotorSpeed, selectionPosition);

					while (Button.LEFT.isDown()) {
						//pass
					}

				} else if (selectionPosition==1) {
					currentMotorSpeed = (currentMotorSpeed+999)%1000;
					Motor.A.setSpeed(currentMotorSpeed);
					Motor.B.setSpeed(currentMotorSpeed);
					Motor.C.setSpeed(currentMotorSpeed);
					updateDisplay(currentMotorIndex, currentMotorSpeed, selectionPosition);

					Thread.sleep(700);
					while (Button.RIGHT.isDown()) {
						currentMotorSpeed = (currentMotorSpeed+999)%1000;
						Motor.A.setSpeed(currentMotorSpeed);
						Motor.B.setSpeed(currentMotorSpeed);
						Motor.C.setSpeed(currentMotorSpeed);
						updateDisplay(currentMotorIndex, currentMotorSpeed, selectionPosition);
						Thread.sleep(50);
					}

				} else {
					MotorTest.motors[currentMotorIndex].backward();
				}
			} else if (Button.ENTER.isDown()) {
				selectionPosition = (selectionPosition+1)%3;
				updateDisplay(currentMotorIndex, currentMotorSpeed, selectionPosition);

				while (Button.ENTER.isDown()) {
					//pass
				}
			}
		}
	}

	private static void updateDisplay(int motorIndex, int currentMotorSpeed, int selectionPosition) {
		if (MotorTest.drawnMotorIndex!=motorIndex) {
			if (motorIndex==0) {
				LCD.drawString("A            ", 0, 0);
			} else if (motorIndex==1) {
				LCD.drawString("B            ", 0, 0);
			} else if (motorIndex==2) {
				LCD.drawString("C            ", 0, 0);
			} else {
				LCD.drawString("INVALID INDEX", 0, 0);
			}

			MotorTest.drawnMotorIndex = motorIndex;
		}

		if (MotorTest.drawnMotorSpeed!=currentMotorSpeed) {
			LCD.drawString("     ", 0, 1);
			LCD.drawInt(currentMotorSpeed, 0, 1);

			MotorTest.drawnMotorSpeed = currentMotorSpeed;
		}

		LCD.drawString("Run", 0, 2);

		if (MotorTest.drawnRotationSpeed!=MotorTest.motors[motorIndex].getRotationSpeed()) {
			LCD.drawString("Actual speed:", 0, 4);
			LCD.drawString("      ", 0, 5);
			LCD.drawInt(MotorTest.motors[motorIndex].getRotationSpeed(), 0, 5);

			MotorTest.drawnRotationSpeed = MotorTest.motors[motorIndex].getRotationSpeed();
		}

		if (MotorTest.drawnSelectionPosition!=selectionPosition) {
			LCD.drawString(" ", 15, 0);
			LCD.drawString(" ", 15, 1);
			LCD.drawString(" ", 15, 2);
			LCD.drawString("<", 15, selectionPosition);

			MotorTest.drawnSelectionPosition = selectionPosition;
		}
	}

}