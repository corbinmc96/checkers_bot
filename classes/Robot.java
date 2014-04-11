// AARON WROTE INSTANCE VARIABLES, CONSTRUCTOR, and disconnect, JAMES WROTE EVERYTHING ELSE

import lejos.pc.comm.*;
import lejos.nxt.remote.NXTCommand;
import java.io.IOException;

public class Robot {

	private NXTConnector conn;
	private int[] armLocation;
	private boolean isHoldingPiece;
	public static final int[] DEAD_LOCATION = new int[] {0, -1};

	public Robot() throws NXTCommException {
		this.conn = new NXTConnector();
		this.conn.connectTo(NXTComm.LCP);
		NXTCommandConnector.setNXTCommand(new NXTCommand(this.conn.getNXTComm()));
	}

	public void disconnect() throws IOException {
		this.conn.close();
	}

	public int[] getArmLocation() {
		return null;
	}
	
	public boolean getIsHoldingPiece() {
		return false;
	}

	public void moveToXY(int[] newXY) {

	}
	
	public void resetPosition() {
		//implementation
	}
	
	public String examineLocation(int[] location) {
		return null;
	}

	public void pickUpPiece() {
		Motor.C.rotateTo( 180);
		Motor.C.rotateTo( 270);
		(boolean) isRotating();
		Motor.C.rotateTo( 180);
		(boolean) isRotating();
	}

	public void dropPiece() {
		Motor.C.rotateTo( 90);
		(boolean) isRotating();
		Motor.C.rotateTo( 180);
	}

	public void waitForSensorPress() {
		TouchSensor(SensorPort port);
		boolean isPressed();
		return;
	}
}
