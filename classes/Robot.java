// AARON WROTE INSTANCE VARIABLES, CONSTRUCTOR, and disconnect, JAMES WROTE MOST OF THE REST OF EVERYTHING ELSE

import lejos.pc.comm.*;
import lejos.nxt.*;
import lejos.nxt.remote.*;
import java.io.IOException;

public class Robot {

	private int currentX;
	private int currentY;
	private boolean holding;
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
		return new int[] {currentX, currentY};
	}
	
	public boolean getIsHoldingPiece() {
		return holding;
	}

	public void moveToXY(int[] newXY) {
	int diff;
	int newX = newXY[0];
	int newY = newXY[1];
	if (newX < currentX) {
		diff = (currentX-newX);
		Motor.A.rotateTo(-diff*() );
	} 
	else if (newX > currentX) {
		diff = (newX-currentX);
		Motor.A.rotateTo(diff*() );
	}
	
	if (newY < currentY) {
		diff = (currentX-newX);
		Motor.B.rotateTo(-diff*() );
	}
	else if (newY > currentY) {
		diff = (newY-currentY);
		Motor.B.rotateTo(diff*() );
	
	}
	}
	
	public void resetPosition() {
		int resetX = currentX;
		Motor.A.rotateTo(-resetX*() );
		int resetY = currentY;
		Motor.B.rotateTo(-resetY*() );
	}
	
	public String examineLocation(int[] location) {
		int lightValue;
		String color; 

		if (newX < currentX) {
        	diff = (newX - currentX);
          	Motor.A.rotate(diff*()+());
    	}
     
    	if (newX > currentX) {
      	    diff = (currentX - newX);
            Motor.A.rotate(-diff*()+());
     	}
   
     	if (newY < currentY) {
            diff = (newY - currentY);
            Motor.A.rotate(diff*()+());
     	}
     
     	if (newY > currentY) {
            diff = (currentY - newY);
            Motor.A.rotate(-diff*()+());
     	}
		
		LightSensor light = new LightSensor(SensorPort.S1);

    			lightValue = light.getLightValue();
     		
     		if (lightValue > 0 && lightValue <= 100) {
     			color = "green";
     		}
     		if (lightValue > 100 && lightValue <= 200) {
     			color = "gray";
     		}
     		if (lightValue > 200 && lightValue <= 300) {
     			color = "black";
     		}
		return color;
	}

	public void pickUpPiece() {
		Motor.C.rotate( + 180);
		Motor.C.rotate( + 270);
		Motor.C.rotate( + 180);
		holding = true;
	}

	public void dropPiece() {
		Motor.C.rotate( + 90);
		Motor.C.rotate( + 180);
		holding = false;
	}

	public void waitForSensorPress() {
		TouchSensor touch = new TouchSensor(SensorPort.S1);
		while(!touch.isPressed()){
		}
	}	
}
