import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.remote.NXTCommand;
import lejos.nxt.remote.RemoteMotor;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTCommandConnector;
import lejos.pc.comm.NXTConnector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author James Dingwall
 * @author Aaron Miller
 */

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

	private int[] deadLocation;

	// CLASS VARIABLES
	public static final String DARK_COLOR = "black";
	public static final String MIDDLE_COLOR = "green";
	public static final String LIGHT_COLOR = "gray";
	
	public static final String BOARD_COLOR = Robot.MIDDLE_COLOR;

	// brick connection addresses
	public static final String CART_BRICK_ADDRESS = "001653058875";
	public static final String ARCH_BRICK_ADDRESS = "001653058A82";

	// all lengths are in same units
	private static final double BASELINE_X_DISTANCE = 0;
	private static final double BASELINE_Y_DISTANCE = 16.5;

	private static final double X_SQUARE_SPACING = 15;
	private static final double Y_SQUARE_SPACING = 6;

	private static final double GEAR_CIRCUMFERENCE = 36;
	private static final double WHEEL_CIRCUMFERENCE = 4.4 * Math.PI;

	private static final double SENSOR_OFFSET_X = 17;
	private static final double SENSOR_OFFSET_Y = 0;

	public Robot() {
		this.connected = false;
		this.cartConnector = new NXTConnector();
		this.archConnector = new NXTConnector();
	}

	public static void main(String[] args) throws InterruptedException {
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
						e.printStackTrace();
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
								e.printStackTrace();
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
								e.printStackTrace();
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
			r.disconnect();
		}
	}

	public void connect() throws InterruptedException {
		if (!this.connected) {
			this.hook = new Thread() {
				public void run() {
					(new Thread() {
						public void run() {
							if (xMotor != null) {
								xMotor.stop();
							}
							if (yMotor2 != null) {
								yMotor2.stop();
							}
							if (magnetMotor != null) {
								magnetMotor.stop();
							}
						}
					}).start();

					(new Thread() {
						public void run() {
							if (yMotor1 != null) {
								yMotor1.stop();
							}
						}
					}).start();
					
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						//do nothing, thread is finishing anyway
					} finally {
						try {
							cartConnector.close();
						} catch (IOException e) {
							System.err.println("Exception disconnecting cart");
							e.printStackTrace();
						}
						try {
							archConnector.close();
						} catch (IOException e) {
							System.err.println("Exception disconnecting arch");
							e.printStackTrace();
						}
					}
				}
			};

			Runtime.getRuntime().addShutdownHook(this.hook);

			System.err.println("Connecting to robot...");

			boolean successful = false;

			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

			do {
				while (!this.cartConnector.connectTo(null, Robot.CART_BRICK_ADDRESS, NXTCommFactory.ALL_PROTOCOLS, NXTComm.LCP)) {
					System.out.println("Unable to connect to the NXT brick mounted on the cart.  Please make sure all cables are plugged in correctly and the brick is turned on.  Press enter to try again.");
					try {
						br.readLine();
					} catch (InterruptedIOException e) {
						throw new InterruptedException("Interrupted waiting to connect");
					} catch (IOException e) {
						System.err.println("IOException reading line while connecting");
						e.printStackTrace();
					}
					if (Thread.interrupted()) {
						throw new InterruptedException("Interrupted trying to connect to robot");
					}
				}
				while (!this.archConnector.connectTo(null, Robot.ARCH_BRICK_ADDRESS, NXTCommFactory.ALL_PROTOCOLS, NXTComm.LCP)) {
					System.out.println("Unable to connect to the NXT brick mounted on the arch.  Please make sure all cables are plugged in correctly and the brick is turned on.  Press enter to try again.");
					try {
						br.readLine();
					} catch (InterruptedIOException e) {
						throw new InterruptedException("Interrupted waiting to connect");
					} catch (IOException e) {
						System.err.println("IOException reading line while connecting");
						e.printStackTrace();
					}
					if (Thread.interrupted()) {
						throw new InterruptedException("Interrupted trying to connect to robot");
					}
				}
				successful = true;
				NXTCommandConnector.setNXTCommand(new NXTCommand(this.cartConnector.getNXTComm()));

				try {
					Motor.B.resetTachoCount();
					Motor.B.rotate(53);
				} catch (Exception e) {
					successful = false;
				}
				if ((new LightSensor(SensorPort.S1)).getNormalizedLightValue()<2 || Math.abs(Motor.B.getTachoCount()-53)>15 || !successful) {
					successful = false;
					System.out.println("Bad connection. Unplug both cables connecting the robot to the computer, and plug them in again.  Press enter to try again when finished.");
					try {
						this.cartConnector.close();
						this.archConnector.close();
						br.readLine();
					} catch (InterruptedIOException e) {
						throw new InterruptedException("Interrupted waiting to connect");
					} catch (IOException e) {
						System.err.println("IOException reading line while connecting");
						e.printStackTrace();
					}
					if (Thread.interrupted()) {
						throw new InterruptedException("Interrupted trying to connect to robot");
					}
				}
			} while (!successful);

			NXTCommand comm1 = new NXTCommand(this.cartConnector.getNXTComm());
			NXTCommand comm2 = new NXTCommand(this.archConnector.getNXTComm());

			this.xMotor = new RemoteMotor(comm1, 0);
			this.yMotor1 = new RemoteMotor(comm2, 1);
			this.yMotor2 = new RemoteMotor(comm1, 1);
			this.magnetMotor = new RemoteMotor(comm1, 2);

			this.lightSensor = new LightSensor(SensorPort.S1);
			this.touchSensor = new TouchSensor(SensorPort.S2);
			this.xBumper = new TouchSensor(SensorPort.S3);

			this.yMotor1.setSpeed(200);
			this.yMotor2.setSpeed(200);
			this.xMotor.setSpeed(200);
			this.magnetMotor.setSpeed(150);

			this.yMotor1.resetTachoCount();
			this.yMotor2.resetTachoCount();
			this.xMotor.resetTachoCount();
			this.magnetMotor.resetTachoCount();

			this.deadLocation = new int[] {0, -1};

			this.connected = true;
		}
	}

	public void disconnect() throws InterruptedException {
		this.hook.start();
		Runtime.getRuntime().removeShutdownHook(this.hook);

		this.hook.join();

		this.xMotor = null;
		this.yMotor1 = null;
		this.yMotor2 = null;
		this.magnetMotor = null;

		this.lightSensor = null;
		this.touchSensor = null;
		this.xBumper = null;

		this.isHoldingPiece = false;

		this.deadLocation = null;
		
		this.hook = null;
		
		this.connected = false;
	}

	public int[] getDeadLocation() {
		if (this.deadLocation[0]>7) {
			this.deadLocation[0] = 0;
			this.deadLocation[1]--;
		}
		if (this.deadLocation[1]<-2) {
			this.deadLocation[1] = -1;
		}
		return new int[] {this.deadLocation[0]++, this.deadLocation[1]};
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

		while (this.yMotor1.isMoving());
		while (this.yMotor2.isMoving());
			//do nothing

		this.yMotor1.resetTachoCount();
		this.yMotor2.resetTachoCount();
		this.xMotor.resetTachoCount();
	}
	
	public String examineLocation(int[] location) throws InterruptedException {
		this.yMotor1.rotateTo((int) -Math.round(360 / Robot.WHEEL_CIRCUMFERENCE * (location[1]*Robot.Y_SQUARE_SPACING + Robot.BASELINE_Y_DISTANCE + Robot.SENSOR_OFFSET_Y)), true);
		this.yMotor2.rotateTo((int) -Math.round(360 / Robot.WHEEL_CIRCUMFERENCE * (location[1]*Robot.Y_SQUARE_SPACING + Robot.BASELINE_Y_DISTANCE + Robot.SENSOR_OFFSET_Y)), true);
		this.xMotor.rotateTo((int) Math.round(360 / Robot.GEAR_CIRCUMFERENCE * (location[0]*Robot.X_SQUARE_SPACING + Robot.BASELINE_X_DISTANCE + Robot.SENSOR_OFFSET_X)));
		while (this.yMotor1.isMoving());
		while (this.yMotor2.isMoving());
			//do nothing

		Thread.sleep(200);

		int lightValue = this.lightSensor.getNormalizedLightValue();
		System.out.println(lightValue);

		if (lightValue==0) {
			System.out.println("zero returned from sensor");
			return null;
		}

		if (lightValue <= this.middle_cutoff) {
			return Robot.DARK_COLOR;
		} else if (lightValue <= this.light_cutoff) {
			return Robot.MIDDLE_COLOR;
		} else {
			return Robot.LIGHT_COLOR;
		}
	}

	public void pickUpPiece() {
		if (!this.isHoldingPiece) {
			this.magnetMotor.rotateTo(-225);
			this.isHoldingPiece = true;
		}
	}

	public void dropPiece() {
		if (this.isHoldingPiece) {
			this.magnetMotor.rotateTo(0);
			this.isHoldingPiece = false;
		}
	}

	public void waitForSensorPress() throws InterruptedException {
		while (!this.touchSensor.isPressed()) {
			if (Thread.interrupted()) {
				throw new InterruptedException();
			}
		}
		while (this.touchSensor.isPressed()) {
			if (Thread.interrupted()) {
				throw new InterruptedException();
			}
		}
	}

	public void calibrate() throws InterruptedException {
		int[] light_values = new int[3];
		int[] board_values = new int[3];
		int[] dark_values = new int[3];
		int[][] light_locations = new int [][] {new int[]{0,0}, new int[]{3,1}, new int[]{6,2}};
		int[][] board_locations = new int[][] {new int[]{5,3}, new int[]{3,3}, new int[]{0,4}};
		int[][] dark_locations = new int[][] {new int[]{1,5}, new int[]{4,6}, new int[]{7,7}};

		for (int i=0; i<3; i++) {
			this.examineLocation(light_locations[i]);
			light_values[i]= this.lightSensor.getNormalizedLightValue();
		}

		for (int i=0; i<3; i++) {
			this.examineLocation(board_locations[i]);
			board_values[i]= this.lightSensor.getNormalizedLightValue();
		}
		for (int i=0; i<3; i++) {
			this.examineLocation(dark_locations[i]);
			dark_values[i]= this.lightSensor.getNormalizedLightValue();
		}
		
		int average_light = 0;
		for (int i=0; i<light_values.length;i++) {
			average_light+=light_values[i];
		}
		average_light/=3;

		int average_board = 0;
		for (int i=0; i<board_values.length;i++) {
			average_board+=board_values[i];
		}
		average_board/=3;

		int average_dark = 0; 
		for (int i=0; i<dark_values.length;i++) {
			average_dark+=dark_values[i];
		}
		average_dark /= 3;

		middle_cutoff = (average_dark + average_board)/2;
		light_cutoff = (average_board + average_light)/2;
		System.out.println(middle_cutoff);
		System.out.println(light_cutoff);
	}

	public void calibrate(int[][] check_locations) throws InterruptedException {
		int[] check_values = new int[] {0,0,0};
		for (int[] check_location : check_locations) {
			this.examineLocation(check_location);

			for (int i = 0; i<3; i++) {
				if (check_values[i]==0) {
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						this.resetPosition();
						throw e;
					}
					check_values[i]=this.lightSensor.getNormalizedLightValue();
					break;
				}
			}
		}
		Arrays.sort(check_values);
		middle_cutoff = (check_values[0]+check_values[1])/2;
		light_cutoff = (check_values[1]+check_values[2])/2;
		System.out.println(middle_cutoff);
		System.out.println(light_cutoff);
	}

	public Move detectLastMove(Game g, Player p) throws InterruptedException {
		//wait until player signifies they are done with their move
		waitForSensorPress();

		ArrayList<Move> possibleMoves = new ArrayList<Move>(Arrays.asList(p.getAllMoves(g)));
		ArrayList<int[]> unscannedLocations = new ArrayList<int[]>();
		for (int i = 0; i<8; i++) {
			int j = i%2;
			for (;j<8; j+=2) {
				unscannedLocations.add(new int[] {i,j});
			}
		}
		int[] currentLocation = new int[] {0,0};

		//keep eliminating possible moves until only one is left
		while (possibleMoves.size()>1) {
			double[] ranks = new double[unscannedLocations.size()];
			for (int locationNum = 0; locationNum < unscannedLocations.size(); locationNum++) {
				int[] location = unscannedLocations.get(locationNum);
				int[] playerOccurences = new int[] {0,0,0};
				for (Move m : possibleMoves) {
					Piece pieceAtLocation = (new Game(g, m)).getGameBoard().getPieceAtLocation(location);
					if (pieceAtLocation == null) {
						playerOccurences[0] += 1;
					}
					else if (pieceAtLocation.getPlayer() == p) {
						playerOccurences[1] += 1;
					}
					else {
						playerOccurences[2] += 1;
					}
				}
				double average = (playerOccurences[0] + playerOccurences[1] + playerOccurences[2])/3.0;
				double stdev = Math.pow((Math.pow(playerOccurences[0]-average, 2) +
				                        Math.pow(playerOccurences[1]-average, 2) +
				                        Math.pow(playerOccurences[2]-average, 2))/3.0, 0.5);
				ranks[locationNum] = stdev;
			}

			int minRank = 0;
			for (int i = 1; i < ranks.length; i++) {
				if (ranks[i] < ranks[minRank]) {
					minRank = i;
				}
			}
		
			String location_color = this.examineLocation(unscannedLocations.get(minRank));
			System.out.println(location_color);
			currentLocation = unscannedLocations.get(minRank);

			ArrayList<Move> newPossibleMoves = new ArrayList<Move>();

			System.out.println("----------");
			for (int i = possibleMoves.size()-1; i>=0; i--) {
				Move m = possibleMoves.get(i);
				System.out.println(m);
				System.out.println(location_color);
				Piece piece = (new Game(g, m)).getGameBoard().getPieceAtLocation(currentLocation);
				if (piece == null) {
					System.out.println("null");
					if (location_color.equals(Robot.MIDDLE_COLOR)) {

						newPossibleMoves.add(m);
					}
					continue;
				}
				System.out.println(piece.getPlayer().getColor());
				if (piece.getPlayer().getColor().equals(location_color)) {
					newPossibleMoves.add(m);
				}
				// if (location_color != Robot.MIDDLE_COLOR) {
				// 	if ((new Game(g, m)).getGameBoard().getPieceAtLocation(currentLocation) == null) {
				// 		possibleMoves.remove(m);
				// 	}
				// 	continue;
				// }
				// Piece piece = (new Game(g, m)).getGameBoard().getPieceAtLocation(currentLocation);
				// if (piece == null) {
				// 	System.out.println("Piece is null");
				// }
				// if (piece.getPlayer().getColor() != location_color) {
				// 	possibleMoves.remove(m);
				// }
			}
			possibleMoves = newPossibleMoves;
		}
		return possibleMoves.get(0);
	}
}
