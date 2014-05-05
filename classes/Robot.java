// AARON WROTE CONSTRUCTOR, main, connect, and disconnect, JAMES WROTE MOST OF THE REST OF EVERYTHING ELSE

import lejos.pc.comm.*;
import lejos.nxt.*;
import lejos.nxt.remote.*;
import java.io.*;

public class Robot {

	// INSTANCE VARIABLES
	private NXTConnector conn;
	private LightSensor lightSensor;
	private TouchSensor touchSensor;
	private int[] armLocation;
	private boolean isHoldingPiece;
	private Thread hook;
	private boolean connected;

	// CLASS VARIABLES
	public static final int[] DEAD_LOCATION = new int[] {0, -1};

	public static String darkColor = "black";
	public static String middleColor = "gray";
	public static String lightColor = "green";
	public static int lowerCutoff = 35;
	public static int upperCutoff = 75;

	// all lengths are in same units
	private static final double BASELINE_X_DISTANCE = 50;
	private static final double BASELINE_Y_DISTANCE = 12;
	private static final double X_SQUARE_SPACING = 15;
	private static final double Y_SQUARE_SPACING = 6;
	private static final double GEAR_CIRCUMFERENCE = 36;
	private static final double WHEEL_CIRCUMFERENCE = 4.2 * Math.PI;
	private static final double SENSOR_OFFSET_X = 15;
	private static final double SENSOR_OFFSET_Y = 0;

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

			this.touchSensor = new TouchSensor(SensorPort.S1);
			this.lightSensor = new LightSensor(SensorPort.S2);

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

			this.touchSensor = null;
			this.lightSensor = null;

			this.connected = false;
		}
	}

	public boolean getIsHoldingPiece() {
		return this.isHoldingPiece;
	}
	
	public void moveToXY(int[] newXY) {
		Motor.A.rotateTo((int) Math.round(360 / Robot.WHEEL_CIRCUMFERENCE * (newXY[1]*Robot.Y_SQUARE_SPACING + Robot.BASELINE_Y_DISTANCE)), true);
		Motor.B.rotateTo((int) Math.round(360 / Robot.GEAR_CIRCUMFERENCE * (newXY[0]*Robot.X_SQUARE_SPACING + Robot.BASELINE_X_DISTANCE)));
		while (Motor.A.isMoving());
			//do nothing
	}
	
	public void resetPosition() {
		Motor.A.rotateTo(0, true);
		Motor.B.rotateTo(0);
		while (Motor.A.isMoving());
			//do nothing
	}
	
	public String examineLocation(int[] location) {
		Motor.A.rotateTo((int) Math.round(360 / Robot.WHEEL_CIRCUMFERENCE * (location[1]*Robot.Y_SQUARE_SPACING + Robot.BASELINE_Y_DISTANCE + Robot.SENSOR_OFFSET_Y)), true);
		Motor.B.rotateTo((int) Math.round(360 / Robot.GEAR_CIRCUMFERENCE * (location[0]*Robot.X_SQUARE_SPACING + Robot.BASELINE_X_DISTANCE + Robot.SENSOR_OFFSET_X)));
		while (Motor.A.isMoving());
			//do nothing

		int lightValue = this.lightSensor.readValue();

		if (lightValue <= Robot.lowerCutoff) {
			return Robot.darkColor;
		} else if (lightValue <= Robot.upperCutoff) {
			return Robot.middleColor;
		} else {
			return Robot.lightColor;
		}
	}

	public void pickUpPiece() {
		if (!this.isHoldingPiece) {
			Motor.C.rotate(240);
			this.isHoldingPiece = true;
		}
	}

	public void dropPiece() {
		if (this.isHoldingPiece) {
			Motor.C.rotate(120);
			this.isHoldingPiece = false;
		}
	}

	public void waitForSensorPress() {
		while (!this.touchSensor.isPressed());
			//wait for sensor to be pressed
		while (this.touchSensor.isPressed());
			//wait for sensor to be released
	}
}
