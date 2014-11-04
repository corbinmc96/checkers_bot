//ALL AARON

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.Arrays;

public class PseudoRobot extends Robot{

	// INSTANCE VARIABLES
	private boolean isHoldingPiece;
	private boolean connected;

	public int middle_cutoff;
	public int light_cutoff;

	public int[] deadLocation;

	// CLASS VARIABLES
	public static String darkColor = "black";
	public static String middleColor = "green";
	public static String lightColor = "gray";
	
	public static final String BOARD_COLOR = "green";


	public static void main(String[] args) {
		PseudoRobot r = new PseudoRobot();
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
			try {
				r.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void connect() {
		this.connected = true;
		this.deadLocation = new int[] {0, -1};
	}

	public int[] getDeadLocation() {
		if (this.deadLocation[0]>7) {
			this.deadLocation[0] = 0;
			this.deadLocation[1]--;
		}
		if (this.deadLocation[1]<-3) {
			this.deadLocation[1] = -1;
		}
		return new int[] {this.deadLocation[0]++, this.deadLocation[1]};
	}


	public void disconnect() throws IOException {
		this.connected = false;
	}

	public boolean getIsHoldingPiece() {
		return this.isHoldingPiece;
	}
	
	public void moveToXY(int[] newXY) {
	}
	
	public void resetPosition() {
		System.out.println("entering resetPosition");
	}
	
	public String examineLocation(int[] location) {
		Scanner sc = new Scanner(System.in);
		int lightValue = 0;
		lightValue = sc.nextInt();
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
		this.isHoldingPiece = true;
	}

	public void dropPiece() {
		this.isHoldingPiece = false;
	}

	public void waitForSensorPress() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Waiting for enter to be pressed...");
		sc.next();
	}

	public void calibrate() {
		int[] check_values = new int[] {0,0,0};
		Scanner sc = new Scanner(System.in);
		for (int[] check_location : new int[][] {new int[] {0,0},new int[] {0,4},new int[] {0,6}}) {
			this.examineLocation(check_location);

			for (int i = 0; i<3; i++) {
				if (check_values[i]==0) {
					check_values[i]=sc.nextInt();
					break;
				}
			}
		}
		Arrays.sort(check_values);
		middle_cutoff = (check_values[0]+check_values[1])/2;
		light_cutoff = (check_values[1]+check_values[2])/2;
	}
}
