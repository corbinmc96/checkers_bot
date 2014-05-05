// AARON WROTE CONSTRUCTOR, main, connect, and disconnect, JAMES WROTE MOST OF THE REST OF EVERYTHING ELSE

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
	private Thread hook;
	private boolean connected;

	public static final int[] DEAD_LOCATION = new int[] {0, -1};

	public Robot() {
		this.connected = false;
		this.conn = new NXTConnector();

		this.hook = new Thread() {
			public void start() {
				try {
					disconnect();
				} catch (IOException e) {
					// do nothing
				}
			}
		};
	}

	public static void main(String[] args) {
		Robot r = new Robot();
		r.connect();

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

				int input = -1;
				while (input == -1) {
					System.out.print("Execute: ");
					try {
						input = Integer.parseInt(br.readLine());
					} catch (IOException e) {
						//do nothing
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

	public void connect() {
		if (!this.connected) {
			this.conn.connectTo(NXTComm.LCP);
			NXTCommandConnector.setNXTCommand(new NXTCommand(this.conn.getNXTComm()));
			Runtime.getRuntime().addShutdownHook(this.hook);
			this.connected = true;
		}
	}

	public void disconnect() throws IOException {
		if (this.connected) {
			Motor.A.flt();
			Motor.B.flt();
			Motor.C.flt();
			this.conn.close();
			Runtime.getRuntime().removeShutdownHook(this.hook);
			this.connected = false;
		}
	}

	public int[] getArmLocation() {
		return new int[] {currentX, currentY};
	}
	
	public boolean getIsHoldingPiece() {
		return holding;
	}

	public void moveToXY(int[] newXY) {
		System.out.println("in moveToXY");
		int diff;
		int newX = newXY[0];
		int newY = newXY[1];
		if (newX < currentX) {
			diff = (currentX-newX);
			Motor.A.rotate(-diff*(150) );
		}
		else if (newX > currentX) {
			diff = (newX-currentX);
			Motor.A.rotate(diff*(150) );
		}

		System.out.println("done moving X");
		
		if (newY < currentY) {
			diff = (currentX-newX);
			Motor.B.rotateTo((int) Math.round(-diff*(163.7022)) );
		}
		else if (newY > currentY) {
			diff = (newY-currentY);
			Motor.B.rotateTo((int) Math.round(diff*(163.7022)) );
		}

		System.out.println("exiting moveToXY");
	}
	
	public void resetPosition() {
		int resetX = currentX;
		Motor.A.rotate(-resetX*(150) );
		int resetY = currentY;
		Motor.B.rotate((int) Math.round(-resetY*(163.7022)) );
	}
	
	public String examineLocation(int[] location) {
		int lightValue;
		String color = "";
		int diff;

		int newX = location[0];
		int newY = location[1];

		if (newX < currentX) {
        	diff = (newX - currentX);
          	Motor.A.rotate(diff*(150)+(50));
    	}
     
    	if (newX > currentX) {
      	    diff = (currentX - newX);
            Motor.A.rotate(-diff*(150)+(50));
     	}
   
     	if (newY < currentY) {
            diff = (newY - currentY);
            Motor.A.rotate((int) Math.round(diff*(163.7022) + (50)) );
     	}
     
     	if (newY > currentY) {
            diff = (currentY - newY);
            Motor.A.rotate((int) Math.round(-diff*(163.7022) + (50)) );
     	}
		
		LightSensor light = new LightSensor(SensorPort.S1);

    			lightValue = light.getLightValue();
     		
     		if (lightValue <= 33) {
     			color = "green";
     		}
     		else if (lightValue <= 66) {
     			color = "gray";
     		}
     		else {
     			color = "black";
     		}
		return color;
	}

	public void pickUpPiece() {
		Motor.C.rotate(240);
		holding = true;
	}

	public void dropPiece() {
		Motor.C.rotate(120);
		holding = false;
	}

	public void waitForSensorPress() {
		TouchSensor touch = new TouchSensor(SensorPort.S1);
		while(!touch.isPressed()){
		}
	}	
}
