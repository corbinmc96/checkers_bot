// AARON WROTE INSTANCE VARIABLES, CONSTRUCTOR, and disconnect, JAMES WROTE EVERYTHING ELSE

import lejos.pc.comm.*;
import lejos.nxt.remote.NXTCommand;
import java.io.IOException;

public class Robot {

	private NXTConnector conn;
	private int[] armLocation;
	private boolean isHoldingPiece;
<<<<<<< HEAD
	private final int[] getDeadLocation; // need to add location here
	private int[] location
=======
	public static final int[] DEAD_LOCATION = new int[] {0, -1};
>>>>>>> 09f96dc8dc44dea2ecfd063d420a977eda6f82e1

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

	public int[] location
		write float v;

	public void moveToXY(int[] newXY) {

	}
	
	public void resetPosition() {
		Motor.C.rotateTo (DeadLocation-x)
	}
	
	public String examineLocation(int[] location) {
		while(SensorValue(lightSensor)>40)
			motor[MotorA] = 0
			motor[MotorB] = 0
		return null;
	}

	public void pickUpPiece() {
		Motor.C.rotateTo( + 180);
		Motor.C.rotateTo( + 270);
		(boolean) isRotating();
		Motor.C.rotateTo( + 180);
		(boolean) isRotating();
	}

	public void dropPiece() {
		Motor.C.rotateTo( + 90);
		(boolean) isRotating();
		Motor.C.rotateTo( + 180);
	}

	public void waitForSensorPress() {
		
   public class TouchTest {
	public static void main (String[] args) Throws Exception{
		TouchSensor touch - new TouchSensor( SensorPort.S1)
		while (!touch.isPressed()) {
			return True
	}	
}
	}
<<<<<<< HEAD

	
}


=======
}
>>>>>>> 09f96dc8dc44dea2ecfd063d420a977eda6f82e1
