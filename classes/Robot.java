// AARON WROTE INSTANCE VARIABLES, CONSTRUCTOR, and disconnect, JAMES WROTE MOST OF THE REST OF EVERYTHING ELSE

import lejos.pc.comm.*;
import lejos.nxt.*;
import lejos.nxt.remote.*;
import java.io.*;

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

	public static void main(String[] args) throws NXTCommException {
		Robot r = new Robot();

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

			boolean done = false;
			String locationString;

			while (!done) {
				System.out.println("");
				System.out.println("0\tgetArmLocation");
				System.out.println("1\tgetIsHoldingPiece");
				System.out.println("2\tmoveToXY");
				System.out.println("3\tresetPosition");
				System.out.println("4\texamineLocation");
				System.out.println("5\tpickUpPiece");
				System.out.println("6\tdropPiece");
				System.out.println("7\twaitForSensorPress");
				System.out.println("8\tQUIT");

				System.out.print("Execute: ");

				int input = -1;
				while (input == -1) {
					try {
						input = Integer.parseInt(br.readLine());
					} catch (IOException e) {
						System.err.println(e);
					}
					if (input<0 || input>8) {
						input = -1;
					}
				}

				switch (input) {
					case 0:
						System.out.println("[" + r.getArmLocation()[0] + ", " + r.getArmLocation()[1] + "]");
						break;
					case 1:
						System.out.println(r.getIsHoldingPiece());
						break;
					case 2:
						locationString = null;
						while (locationString==null) {
							System.out.print("Enter the location(two integers separated by spaces): ");
							try {
								locationString = br.readLine();
							} catch (IOException e) {
								System.err.println(e);
							}
							if (!locationString.matches("[0-7] [0-7]")) {
								locationString = null;
							}
						}
						r.moveToXY(new int[] {Integer.parseInt(locationString.substring(0, 1)), Integer.parseInt(locationString.substring(2))});
						break;
					case 3:
						r.resetPosition();
						break;
					case 4:
						locationString = null;
						while (locationString==null) {
							System.out.print("Enter the location(two integers separated by spaces): ");
							try {
								locationString = br.readLine();
							} catch (IOException e) {
								System.err.println(e);
							}
							if (!locationString.matches("[0-7] [0-7]")) {
								locationString = null;
							}
						}
						System.out.println(r.examineLocation(new int[] {Integer.parseInt(locationString.substring(0, 1)), Integer.parseInt(locationString.substring(2))}));
						break;
					case 5:
						r.pickUpPiece();
						break;
					case 6:
						r.dropPiece();
						break;
					case 7:
						r.waitForSensorPress();
						break;
					case 8:
						done = true;
						break;
					default:
						System.err.println("Something went wrong!");
						break;
				}
			}
		} finally {
			try {
				r.disconnect();
			} catch (IOException e) {
				System.err.println(e);
			}
		}
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
