// ALL AARON EXCEPT CORBIN WROTE locationIsInBounds, printBoard, and getDistanceBetweenPieces
// CORBIN AND AARON COLLABORATED ON calculateValue

import java.util.ArrayList;
import java.util.Arrays;

public class Board {

	private ArrayList<int[]> p1Pieces;
	private ArrayList<int[]> p2Pieces;
	private ArrayList<int[]> p1Kings;
	private ArrayList<int[]> p2Kings;

	private Player[] players;

	private static final int[][] SINGLE_DISPLACEMENTS = new int[][] {new int[] {1,1}, new int[] {1,-1}, new int[] {-1,1}, new int[] {-1,-1}};
	private static final int[][] SINGLE_FORWARD_DISPLACEMENTS = new int[][] {new int[] {1,1}, new int[] {-1,1}};
	private static final int[][] SINGLE_BACKWARD_DISPLACEMENTS = new int[][] {new int[] {1,-1}, new int[] {-1,-1}};
	private static final int[][] DOUBLE_DISPLACEMENTS = new int[][] {new int[] {2,2}, new int[] {2,-2}, new int[] {-2,2}, new int[] {-2,-2}};
	private static final int[][] DOUBLE_FORWARD_DISPLACEMENTS = new int[][] {new int[] {2,2}, new int[] {-2,2}};
	private static final int[][] DOUBLE_BACKWARD_DISPLACEMENTS = new int[][] {new int[] {2,-2}, new int[] {-2,-2}};

	public static final String COLOR = "black";
	public static final double MAX_BOARD_VALUE = 36;

	public Board (Player[] players) {
		this.players = players;

		this.p1Pieces = new ArrayList<int[]>();
		this.p2Pieces = new ArrayList<int[]>();
		this.p1Kings = new ArrayList<int[]>();
		this.p2Kings = new ArrayList<int[]>();

		for (int i=0; i<3; i++) {
			for (int j=0; j<4; j++) {
				this.p1Pieces.add(new int[] {(2*j+(i%2)), i});
				this.p2Pieces.add(new int[] {7 - 2*j-(i%2), 7-i});
			}
		}
	}

	public Board (Board previousBoard, Move newMove) {
		this.players = previousBoard.getPlayers();

		this.p1Pieces = new ArrayList<int[]>(previousBoard.getP1Pieces());
		this.p2Pieces = new ArrayList<int[]>(previousBoard.getP2Pieces());
		this.p1Kings = new ArrayList<int[]>(previousBoard.getP1Kings());
		this.p2Kings = new ArrayList<int[]>(previousBoard.getP2Kings());

		Player.performMove(new Move(newMove.getSource(), newMove.getWaypoints()), this);
	}

	public Board(Player[] players, int[][] p1Locations, int[][] p2Locations, int[][] p1KingLocations, int[][] p2KingLocations) {
		this.players = players;

		this.p1Pieces = new ArrayList<int[]>();
		this.p2Pieces = new ArrayList<int[]>();
		this.p1Kings = new ArrayList<int[]>();
		this.p2Kings = new ArrayList<int[]>();

		for (int[] location : p1Locations) {
			this.p1Pieces.add(location);
		}
		for (int[] location : p2Locations) {
			this.p2Pieces.add(location);
		}
		for (int[] location : p1KingLocations) {
			this.p1Kings.add(location);
		}
		for (int[] location : p2KingLocations) {
			this.p2Kings.add(location);
		}
	}

	public boolean pieceExistsAtLocation(int[] location) {
		for (int[] piece : this.p1Pieces) {
			if (piece[0]==location[0] && piece[1]==location[1]) {
				return true;
			}
		}
		for (int[] piece : this.p2Pieces) {
			if (piece[0]==location[0] && piece[1]==location[1]) {
				return true;
			}
		}
		for (int[] piece : this.p1Kings) {
			if (piece[0]==location[0] && piece[1]==location[1]) {
				return true;
			}
		}
		for (int[] piece : this.p2Kings) {
			if (piece[0]==location[0] && piece[1]==location[1]) {
				return true;
			}
		}
		return false;
	}

	public boolean kingExistsAtLocation(int[] location) {
		for (int[] piece : this.p1Kings) {
			if (piece[0]==location[0] && piece[1]==location[1]) {
				return true;
			}
		}
		for (int[] piece : this.p2Kings) {
			if (piece[0]==location[0] && piece[1]==location[1]) {
				return true;
			}
		}
		return false;
	}

	public Player ownerOfPieceAtLocation(int[] location) {
		for (int[] piece : this.p1Pieces) {
			if (piece[0]==location[0] && piece[1]==location[1]) {
				return this.players[0];
			}
		}
		for (int[] piece : this.p2Pieces) {
			if (piece[0]==location[0] && piece[1]==location[1]) {
				return this.players[1];
			}
		}
		for (int[] piece : this.p1Kings) {
			if (piece[0]==location[0] && piece[1]==location[1]) {
				return this.players[0];
			}
		}
		for (int[] piece : this.p2Kings) {
			if (piece[0]==location[0] && piece[1]==location[1]) {
				return this.players[1];
			}
		}
		return null;
	}

	public ArrayList<int[]> getPlayerPieces (Player p) {
		ArrayList<int[]> result = new ArrayList<int[]>(this.totalPiecesLeft(p));
		if (this.players[0]==p) {
			result.addAll(this.p1Pieces);
			result.addAll(this.p1Kings);
		} else {
			result.addAll(this.p2Pieces);
			result.addAll(this.p2Kings);
		}
		return result;
	}

	public ArrayList<int[]> getP1Pieces() {
		return new ArrayList<int[]>(this.p1Pieces);
	}

	public ArrayList<int[]> getP1Kings() {
		return new ArrayList<int[]>(this.p1Kings);
	}

	public ArrayList<int[]> getP2Pieces() {
		return new ArrayList<int[]>(this.p2Pieces);
	}

	public ArrayList<int[]> getP2Kings() {
		return new ArrayList<int[]>(this.p2Kings);
	}

	public Player[] getPlayers() {
		return this.players;
	}

	public void removePiece (int[] pieceToRemove) {
		for (int[] p : this.p1Pieces) {
			if (p[0]==pieceToRemove[0] && p[1]==pieceToRemove[1]) {
				this.p1Pieces.remove(p);
				return;
			}
		}
		for (int[] p : this.p2Pieces) {
			if (p[0]==pieceToRemove[0] && p[1]==pieceToRemove[1]) {
				this.p2Pieces.remove(p);
				return;
			}
		}
		for (int[] p : this.p1Kings) {
			if (p[0]==pieceToRemove[0] && p[1]==pieceToRemove[1]) {
				this.p1Kings.remove(p);
				return;
			}
		}
		for (int[] p : this.p2Kings) {
			if (p[0]==pieceToRemove[0] && p[1]==pieceToRemove[1]) {
				this.p2Kings.remove(p);
				return;
			}
		}
		System.out.println("Removal failed");
	}

	public void movePiece(int[] startLocation, int[] endLocation) {
		if (this.p1Pieces.remove(startLocation)) {
			this.p1Pieces.add(endLocation);
		} else if (this.p2Pieces.remove(startLocation)) {
			this.p2Pieces.add(endLocation);
		} else if (this.p1Kings.remove(startLocation)) {
			this.p1Kings.add(endLocation);
		} else if (this.p2Kings.remove(startLocation)) {
			this.p2Kings.add(endLocation);
		} else {
			System.out.println("Move failed");
		}
	}

	public void kingPiece(int[] location) {
		for (int[] p : this.p1Pieces) {
			if (p[0]==location[0] && p[1]==location[1]) {
				this.p1Pieces.remove(p);
				this.p1Kings.add(p);
				return;
			}
		}
		for (int[] p : this.p2Pieces) {
			if (p[0]==location[0] && p[1]==location[1]) {
				this.p2Pieces.remove(p);
				this.p2Kings.add(p);
				return;
			}
		}
	}

	public int totalPiecesLeft(Player p) {
		if (this.players[0]==p) {
			return this.p1Pieces.size() + this.p1Kings.size();
		}
		return this.p2Pieces.size() + this.p2Kings.size();
	}

	public int normalPiecesLeft(Player p) {
		if (this.players[0]==p) {
			return this.p1Pieces.size();
		} else {
			return this.p2Pieces.size();
		}
	}

	public int kingsLeft(Player p) {
		if (this.players[0]==p) {
			return this.p1Kings.size();
		} else {
			return this.p2Kings.size();
		}
	}

	public static boolean locationIsInBounds (int[] testLocation) {
		if (testLocation[0] >= 0 && testLocation[0] <= 7 && testLocation[1] >= 0 && testLocation[1] <= 7) {
			return true;
		}
		else {
			return false;
		}
	}

	public double calculateValue(Player p) {
		double p1Value = 0;
		double p2Value = 0;

		boolean p1IsOnZeroSide = this.players[0].getIsOnZeroSide();
		//calculates point value of p's pieces
		for (int[] piece : this.p1Pieces) {
			//determines which side of the board the player is on
			if (p1IsOnZeroSide) {
				//adds value based on distance down the board
				p1Value += 3 + 0.125*piece[1];
			//the player is on the side of the board with index 7
			} else {
				//adds value based on distance down the board
				p1Value += 3 + 0.125*(7 - piece[1]);
			}
		}

		p1Value += this.p1Kings.size() * 5;

		boolean p2IsOnZeroSide = this.players[1].getIsOnZeroSide();
		//calculates point value of p's opponent's pieces
		for (int[] piece : p2Pieces) {
			//determines which side of the board the player is on
			if (p2IsOnZeroSide) {
				//adds value based on distance down the board
				p2Value += 3 + 0.125*piece[1];
			//the player is on the side of the board with index 7
			} else {
				//adds value based on distance down the board
				p2Value += 3 + 0.125*(7 - piece[1]);
			}
		}
		
		p2Value += this.p2Kings.size() * 5;

		//discovers which player is winning by the current calculations above
		ArrayList<int[]> winningPlayerKings;
		ArrayList<int[]> loserPlayerPieces;
		if (p1Value > p2Value) {
			winningPlayerKings = this.p1Kings;
			loserPlayerPieces = this.getPlayerPieces(this.players[1]);
		} 
		else {
			winningPlayerKings = this.p2Kings;
			loserPlayerPieces = this.getPlayerPieces(this.players[0]);
		}

		//performs extra calculations to encourage winner's kings to attack opponent's pieces
		for (int[] piece1 : winningPlayerKings) {
			double distance = 20;
			for (int[] piece2 : loserPlayerPieces) {
				double testDistance = this.getDistanceBetweenPieces(piece1, piece2);
				if (testDistance < distance) {
					distance = testDistance;
				}
			}
			if (p1Value > p2Value) {
				p1Value -= .05*distance;
			} 
			else {
				p2Value -= .05*distance;
			}
		}

		//returns final calculated score
		if (this.players[0]==p) {
			return p1Value-p2Value;
		} else {
			return p2Value-p1Value;
		}
	}

	public void printBoard() {
		System.out.println();
		for (int y : new int[] {7,6,5,4,3,2,1,0}) {
			String[] theLine = new String[8];
			for (int x : new int[] {0,1,2,3,4,5,6,7}) {
				if (this.pieceExistsAtLocation(new int[] {x,y})) {
					theLine[x] = this.ownerOfPieceAtLocation(new int[] {x,y}).getXO();
					if (this.kingExistsAtLocation(new int[] {x,y})) {
						theLine[x] = theLine[x].toUpperCase();
					}
				} else {
					theLine[x] = "-";
				}
			}
			for (String s : theLine) {
				System.out.print(s+" ");
			}
			System.out.println();
		}
		System.out.println();
	}

	public double getDistanceBetweenPieces(int[] p1, int[] p2) {
		double dif1 = p1[0] - p2[0];
		double dif2 = p1[1] - p2[1];
		return Math.pow(Math.pow(dif1,2)+Math.pow(dif2,2),0.5);
	}

	public Move[] getMovesOfPiece (int[] piece) {
		//creates return ArrayList
		ArrayList<Move> result = new ArrayList<Move>();
		//gets array of all sets of waypoints
		int[][][] allWaypoints = this.getMovesFromLocation(piece, this, false);
		//iterates through each ser
		for (int[][] theWaypoints : allWaypoints) {
			//creates move and adds to return string
			result.add(new Move(piece, theWaypoints));
		}
		//returns final result
		return result.toArray(new Move[result.size()]);
	}

	private static int[][][] getMovesFromLocation (int[] piece, Board b, boolean mustBeJump) {
		Player ownerOfPiece = b.ownerOfPieceAtLocation(piece);
		//creates return array
		ArrayList<int[][]> result = new ArrayList<int[][]>();
		//tests if this piece is king
		if (b.kingExistsAtLocation(piece)) {
			//tests if this move does not need to be a jump
			if (!mustBeJump) {
				//loops through all displacements
				for (int[] displacement : Board.SINGLE_DISPLACEMENTS) {
					//finds potential destination
					int[] testDestination = new int[] {piece[0]+displacement[0], piece[1]+displacement[1]};
					//if destination empty and inbounds
					if (!b.pieceExistsAtLocation(testDestination) && Board.locationIsInBounds(testDestination)) {
						//add waypoint set to return array
						result.add(new int[][] {piece, testDestination});
					}
				}
			}
			//loops through jump displacements
			for (int[] displacement : Board.DOUBLE_DISPLACEMENTS) {
				//finds potential destinations
				int[] testDestination = new int[] {piece[0]+displacement[0], piece[1]+displacement[1]};
				//finds location being jumped over
				int[] midpoint = new int[] {piece[0]+displacement[0]/2, piece[1]+displacement[1]/2};
				//tests that destination is in bounds, destination is unoccupied, and opponent piece is being jumped over
				if (Board.locationIsInBounds(testDestination) && !b.pieceExistsAtLocation(testDestination) && b.pieceExistsAtLocation(midpoint) && b.ownerOfPieceAtLocation(midpoint) != ownerOfPiece) {
					//adds move to return array
					result.add(new int[][] {piece, testDestination});
					//cycles through possible multi-jump scenarios
					for (int[][] potentialMove : Board.getMovesFromLocation(testDestination, new Board(b, new Move(piece, new int[][]{piece, testDestination})), true)) {
						result.add(ArraysHelper.addTwoArrays(new int[][] {piece}, potentialMove));
					}
				}
			}
		}
		else {
			boolean playerIsOnZeroSide = b.ownerOfPieceAtLocation(piece).getIsOnZeroSide();
			//confirms that move doesnt need to be a jump
			if (!mustBeJump) {
				//declares regularDisplacements
				int[][] regularDisplacements;
				//is on zero side of the board (robot side)
				if (playerIsOnZeroSide) {
					//sets displacement values
					regularDisplacements = Board.SINGLE_FORWARD_DISPLACEMENTS;
				}
				//is not on zero side (human side)
				else {
					//sets different displacement values
					regularDisplacements = Board.SINGLE_BACKWARD_DISPLACEMENTS;
				}
				//iterates through all displacements
				for (int[] displacement : regularDisplacements) {
					//calculates potential destination
					int[] testDestination = new int[] {piece[0]+displacement[0], piece[1]+displacement[1]};
					//tests that location is in bounds and unoccupied
					if (!b.pieceExistsAtLocation(testDestination) && Board.locationIsInBounds(testDestination)) {
						//adds waypoint set to the return array
						result.add(new int[][] {piece,testDestination});
					}
				}
			}
			
			// declares jumpDisplacements
			int[][] jumpDisplacements;
			//tests if player is on the zero side
			if (playerIsOnZeroSide) {
				//sets displacements for jumps
				jumpDisplacements = Board.DOUBLE_FORWARD_DISPLACEMENTS;
			}
			//called if player is on the non-zero side
			else {
				//sets different displacement values for jumps
				jumpDisplacements = Board.DOUBLE_BACKWARD_DISPLACEMENTS;
			}
			//iterates over all displacements
			for (int[] displacement : jumpDisplacements) {
				//calculates potential endpoint
				int[] testDestination = new int[] {piece[0]+displacement[0], piece[1]+displacement[1]};
				//calculates location being jumped over
				int[] midpoint = new int[] {piece[0]+displacement[0]/2, piece[1]+displacement[1]/2};
				//tests if destination is in bounds, unoccupied, and that midpoint is occupied by opponent's piece
				if (Board.locationIsInBounds(testDestination) && !b.pieceExistsAtLocation(testDestination) && b.pieceExistsAtLocation(midpoint) && b.ownerOfPieceAtLocation(midpoint) != ownerOfPiece) {
					//adds waypoint set
					result.add(new int[][] {piece,testDestination});
					//finds all potential multi-jumps
					for (int[][] potentialMove : Board.getMovesFromLocation(testDestination, new Board(b, new Move(piece, new int[][]{piece, testDestination})), true)) {
						//adds multi-jump scenarios
						result.add(ArraysHelper.addTwoArrays(new int[][] {piece}, potentialMove));
					}
				}
			}
		}
		//returns the result
		return result.toArray(new int[result.size()][][]);
	}

}