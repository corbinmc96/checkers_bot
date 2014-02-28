public class Robot {
	
	private int[] armLocation;
	private boolean isHoldingPiece;
	private final int[] getDeadLocation; // need to add location here

	public int[] getDeadLocation() {
		return null;
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