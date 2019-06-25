// AARON WROTE EVERYTHING

import lejos.pc.comm.*;
import lejos.nxt.*;
import lejos.nxt.remote.NXTCommand;
import java.io.IOException;

public class Robot {

	// INSTANCE VARIABLES
	private NXTConnector conn;
	private LightSensor lightSensor;
	private TouchSensor touchSensor;
	private int[] armLocation;
	private boolean isHoldingPiece;

	// CLASS VARIABLES
	public static final int[] DEAD_LOCATION = new int[] {0, -1};

	public static String darkColor = "black";
	public static String middleColor = "gray";
	public static String lightColor = "green";
	public static int lowerCutoff = 35;
	public static int upperCutoff = 75;

	// all lengths are in same units
	private static final double BASELINE_Y_DISTANCE = 3;
	private static final double BASELINE_X_DISTANCE = 1.5;
	private static final double SQUARE_SPACING = 2;
	private static final double WHEEL_RADIUS = 0.75;
	private static final double GEAR_RADIUS = 0.8;
	private static final double SENSOR_OFFSET_X = 0;
	private static final double SENSOR_OFFSET_Y = 1.2;

	// all angles are in degrees
	private static final int DOWN_POSITION = 180;
	private static final int HOLD_POSITION = 45;
	private static final int DROP_POSITION = 0;

	public Robot() {
		this.conn = new NXTConnector();
		this.conn.connectTo(NXTComm.LCP);
		NXTCommandConnector.setNXTCommand(new NXTCommand(this.conn.getNXTComm()));

		this.lightSensor = new LightSensor(SensorPort.S1);
		this.touchSensor = new TouchSensor(SensorPort.S2);
	}

	public void disconnect() {
		try {
			this.conn.close();
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	public int[] getArmLocation() {
		return new int[] {this.armLocation[0], this.armLocation[1]};
	}
	
	public boolean getIsHoldingPiece() {
		return this.isHoldingPiece;
	}

	public void moveMagnetToXY(int[] newXY) {
		Motor.A.rotateTo((int) Math.round(180 / Math.PI / Robot.WHEEL_RADIUS * (newXY[1]*Robot.SQUARE_SPACING + Robot.BASELINE_Y_DISTANCE)), true);
		Motor.B.rotateTo((int) Math.round(180 / Math.PI / Robot.GEAR_RADIUS * (newXY[0]*Robot.SQUARE_SPACING + Robot.BASELINE_X_DISTANCE)));
		while (Motor.A.isMoving());
			//do nothing
	}

	private void moveSensorToXY(int[] newXY) {
		Motor.A.rotateTo((int) Math.round(180 / Math.PI / Robot.WHEEL_RADIUS * (newXY[1]*Robot.SQUARE_SPACING + Robot.BASELINE_Y_DISTANCE + Robot.SENSOR_OFFSET_Y)), true);
		Motor.B.rotateTo((int) Math.round(180 / Math.PI / Robot.GEAR_RADIUS * (newXY[0]*Robot.SQUARE_SPACING + Robot.BASELINE_X_DISTANCE + Robot.SENSOR_OFFSET_X)));
		while (Motor.A.isMoving());
			//do nothing
	}
	
	public void resetPosition() {
		Motor.A.rotateTo(-10, true);
		Motor.B.rotateTo(-10);
		while (Motor.A.isMoving());
			//do nothing
		Motor.A.resetTachoCount();
		Motor.B.resetTachoCount();
	}
	
	public String examineLocation(int[] location) {
		this.moveSensorToXY(location);
		int value = this.lightSensor.readValue();
		if (value < Robot.lowerCutoff) {
			return Robot.darkColor;
		} else if (value < Robot.upperCutoff) {
			return Robot.middleColor;
		} else {
			return Robot.lightColor;
		}
	}

	public void pickUpPiece() {
		Motor.C.rotateTo(Robot.DOWN_POSITION);
		Motor.C.rotateTo(Robot.HOLD_POSITION);
	}

	public void dropPiece() {
		Motor.C.rotateTo(Robot.DROP_POSITION);
	}

	public void waitForSensorPress() {
		while (!touchSensor.isPressed());
			//wait for sensor to be pressed
		while (touchSensor.isPressed());
			//wait for sensor to be released
	}
}
