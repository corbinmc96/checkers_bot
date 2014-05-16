// AARON WROTE CONSTRUCTOR, main, connect, and disconnect, JAMES WROTE MOST OF THE REST OF EVERYTHING ELSE

import lejos.pc.comm.*;
import lejos.nxt.*;
import lejos.nxt.remote.*;
import java.io.*;

import java.util.Arrays;

public class Robot {

	// INSTANCE VARIABLES
	private NXTConnector cartConnector;
	private NXTConnector archConnector;

	private RemoteMotor xMotor;
	private RemoteMotor yMotor1;
	private RemoteMotor yMotor2;
	private RemoteMotor magnetMotor;

	private LightSensor lightSensor;
	private TouchSensor touchSensor;
	private TouchSensor xBumper;

	private boolean isHoldingPiece;
	private Thread hook;
	private boolean connected;

	public int middle_cutoff;
	public int light_cutoff;

	// CLASS VARIABLES
	public static String darkColor = "black";
	public static String middleColor = "green";
	public static String lightColor = "gray";
	
	public static final String BOARD_COLOR = "green";

	public static final int[] DEAD_LOCATION = new int[] {0, -1};

	public static final String CART_BRICK_ADDRESS = "001653058875";
	public static final String ARCH_BRICK_ADDRESS = "001653058A82";

	// all lengths are in same units
	private static final double BASELINE_X_DISTANCE = 0;
	private static final double BASELINE_Y_DISTANCE = 16.5;

	private static final double X_SQUARE_SPACING = 15;
	private static final double Y_SQUARE_SPACING = 6;

	private static final double GEAR_CIRCUMFERENCE = 36;
	private static final double WHEEL_CIRCUMFERENCE = 4.4 * Math.PI;

	private static final double SENSOR_OFFSET_X = 15;
	private static final double SENSOR_OFFSET_Y = 0;

	public Robot() {
		this.connected = false;
		this.cartConnector = new NXTConnector();
		this.archConnector = new NXTConnector();

		this.hook = new Thread() {
			public void start() {
				try {
					yMotor1.stop();
					yMotor2.stop();
					xMotor.stop();
					magnetMotor.stop();
					cartConnector.close();
					archConnector.close();
				} catch (IOException e) {
					// do nothing
				}
			}
		};
	}

	public static void main(String[] args) {
		Robot r = new Robot();
		System.out.println("constructor finished");
		r.connect();
		System.out.println("connected");

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
					} catch (NumberFormatException e) {
						input = -1;
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
			System.out.println(this.cartConnector.connectTo(null, Robot.CART_BRICK_ADDRESS, NXTCommFactory.USB, NXTComm.LCP));
			System.out.println(this.archConnector.connectTo(null, Robot.ARCH_BRICK_ADDRESS, NXTCommFactory.USB, NXTComm.LCP));
			
			Runtime.getRuntime().addShutdownHook(this.hook);

			NXTCommand comm1 = new NXTCommand(this.cartConnector.getNXTComm());
			NXTCommand comm2 = new NXTCommand(this.archConnector.getNXTComm());

			this.xMotor = new RemoteMotor(comm1, 0);
			this.yMotor1 = new RemoteMotor(comm2, 1);
			this.yMotor2 = new RemoteMotor(comm1, 1);
			this.magnetMotor = new RemoteMotor(comm1, 2);

			this.lightSensor = new LightSensor(SensorPort.S4);
			this.touchSensor = new TouchSensor(SensorPort.S2);
			this.xBumper = new TouchSensor(SensorPort.S3);

			this.yMotor1.setSpeed(100);
			this.yMotor2.setSpeed(100);
			this.xMotor.setSpeed(150);
			this.magnetMotor.setSpeed(150);

			this.yMotor1.setAcceleration(100);
			this.yMotor2.setAcceleration(100);
			this.xMotor.setAcceleration(100);

			this.yMotor1.resetTachoCount();
			this.yMotor2.resetTachoCount();
			this.xMotor.resetTachoCount();
			this.magnetMotor.resetTachoCount();

			this.resetPosition();
			this.calibrate();
			this.resetPosition();

			this.connected = true;
		}
	}

	public void disconnect() throws IOException {
		if (this.connected) {

			// MotorPort.A.controlMotor(0, 4);
			// MotorPort.B.controlMotor(0, 4);
			// MotorPort.C.controlMotor(0, 4);

			this.cartConnector.close();
			this.archConnector.close();

			Runtime.getRuntime().removeShutdownHook(this.hook);

			this.xMotor = null;
			this.yMotor1 = null;
			this.yMotor2 = null;
			this.magnetMotor = null;

			this.lightSensor = null;
			this.touchSensor = null;
			this.xBumper = null;

			this.isHoldingPiece = false;

			this.connected = false;
		}
	}

	public boolean getIsHoldingPiece() {
		return this.isHoldingPiece;
	}
	
	public void moveToXY(int[] newXY) {
		this.yMotor1.rotateTo((int) -Math.round(360 / Robot.WHEEL_CIRCUMFERENCE * (newXY[1]*Robot.Y_SQUARE_SPACING + Robot.BASELINE_Y_DISTANCE)), true);
		this.yMotor2.rotateTo((int) -Math.round(360 / Robot.WHEEL_CIRCUMFERENCE * (newXY[1]*Robot.Y_SQUARE_SPACING + Robot.BASELINE_Y_DISTANCE)), true);
		this.xMotor.rotateTo((int) Math.round(360 / Robot.GEAR_CIRCUMFERENCE * (newXY[0]*Robot.X_SQUARE_SPACING + Robot.BASELINE_X_DISTANCE)));
		while (this.yMotor1.isMoving());
		while (this.yMotor2.isMoving());
			//do nothing
	}
	
	public void resetPosition() {
		System.out.println("entering resetPosition");
		this.yMotor1.rotateTo(180, true);
		this.yMotor2.rotateTo(180, true);
		this.xMotor.rotateTo(0);
		// this.xMotor.backward();
		// while (!this.xBumper.isPressed());
		// 	//do nothing
		// try {
		// 	Thread.sleep(50);
		// } catch (InterruptedException e) {
		// 	e.printStackTrace();
		// }
		// this.xMotor.stop();
		while (this.yMotor1.isMoving());
		while (this.yMotor2.isMoving());
			//do nothing

		this.yMotor1.resetTachoCount();
		this.yMotor2.resetTachoCount();
		this.xMotor.resetTachoCount();
	}
	
	public String examineLocation(int[] location) {
		this.yMotor1.rotateTo((int) -Math.round(360 / Robot.WHEEL_CIRCUMFERENCE * (location[1]*Robot.Y_SQUARE_SPACING + Robot.BASELINE_Y_DISTANCE + Robot.SENSOR_OFFSET_Y)), true);
		this.yMotor2.rotateTo((int) -Math.round(360 / Robot.WHEEL_CIRCUMFERENCE * (location[1]*Robot.Y_SQUARE_SPACING + Robot.BASELINE_Y_DISTANCE + Robot.SENSOR_OFFSET_Y)), true);
		this.xMotor.rotateTo((int) Math.round(360 / Robot.GEAR_CIRCUMFERENCE * (location[0]*Robot.X_SQUARE_SPACING + Robot.BASELINE_X_DISTANCE + Robot.SENSOR_OFFSET_X)));
		while (this.yMotor1.isMoving());
		while (this.yMotor2.isMoving());
			//do nothing

		int lightValue = this.lightSensor.getNormalizedLightValue();
		System.out.println(lightValue);

		if (lightValue==0) {
			System.out.println("zero returned from sensor");
			return null;
		}

		if (lightValue <= this.middle_cutoff) {
			return Robot.darkColor;
		} else if (lightValue <= this.light_cutoff) {
			return Robot.middleColor;
		} else {
			return Robot.lightColor;
		}
	}

	public void pickUpPiece() {
		if (!this.isHoldingPiece) {
			this.magnetMotor.rotateTo(-215);
			this.isHoldingPiece = true;
		}
	}

	public void dropPiece() {
		if (this.isHoldingPiece) {
			this.magnetMotor.rotateTo(-360);
			this.magnetMotor.resetTachoCount();
			this.isHoldingPiece = false;
		}
	}

	public void waitForSensorPress() {
		while (!this.touchSensor.isPressed());
			//wait for sensor to be pressed
		while (this.touchSensor.isPressed());
			//wait for sensor to be released
	}

	public void calibrate() {
		int[] check_values = new int[] {0,0,0};
		for (int[] check_location : new int[][] {new int[] {0,0},new int[] {0,4},new int[] {0,6}}) {
			this.examineLocation(check_location);

			for (int i = 0; i<3; i++) {
				if (check_values[i]==0) {
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					check_values[i]=this.lightSensor.getNormalizedLightValue();
					break;
				}
			}
		}
		Arrays.sort(check_values);
		middle_cutoff = (check_values[0]+check_values[1])/2;
		light_cutoff = (check_values[1]+check_values[2])/2;
	}
}
