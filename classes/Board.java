import java.util.ArrayList;
import java.util.Arrays;

public class Board {

	private ArrayList<int[]> piecesOnBoard;

	private Player[] players;

	public static final String color = "black";
	public static final double maxBoardValue = 36;

	public Board (Player[] players) {
		this.players = players;

		this.piecesOnBoard = new ArrayList<int[]>();
		for (int i=0; i<3; i++) {
			for (int j=0; j<4; j++) {
				this.piecesOnBoard.add(new int[] {(2*j+(i%2)), i, 0, 0});
				this.piecesOnBoard.add(new int[] {7 - 2*j-(i%2), 7-i, 1, 0});
			}
		}
	}

	public Board (Board previousBoard, Move newMove) {
		this.players = previousBoard.getPlayers();

		this.piecesOnBoard = new ArrayList<int[]>();
		for (int[] p : previousBoard.getPiecesOnBoard()) {
			this.piecesOnBoard.add(Arrays.copyOf(p, 4));
		}
		Player.performMove(new Move(this.getPieceAtLocation(newMove.getSource()), newMove.getWaypoints()), this);
	}

	public Board(Player[] players, int[][] p1Locations, int[][] p2Locations, int[][] p1kings, int[][] p2kings) {
		this.players = players;

		this.piecesOnBoard = new ArrayList<int[]>();
		for (int[] location : p1Locations) {
			this.piecesOnBoard.add(new int[] {location[0], location[1], 0, 0});
		}
		for (int[] location : p2Locations) {
			this.piecesOnBoard.add(new int[] {location[0], location[1], 1, 0});
		}
		for (int[] location : p1kings) {
			this.piecesOnBoard.add(new int[] {location[0], location[1], 0, 1});
		}
		for (int[] location : p2kings) {
			this.piecesOnBoard.add(new int[] {location[0], location[1], 1, 1});
		}
	}

	private int[][] getPiecesOnBoard () {
		return this.piecesOnBoard.toArray(new int[this.piecesOnBoard.size()][]);
	}

	public int[] getPieceAtLocation(int[] location) {
		for (int[] piece : this.piecesOnBoard) {
			if (piece[0] == location[0] && piece[1] == location[1]) {
				return piece;
			}
		}
		return null;
	}

	public int[][] getPlayerPieces (Player p) {
		int[][] result = new int[this.totalPiecesLeft(p)][];
		int i = 0;
		for (int[] piece : this.piecesOnBoard) {
			if (this.players[piece[2]] == p) {
				result[i++] = piece;
			}
		}
		return result;
	}

	public Player[] getPlayers() {
		return this.players;
	}

	public void removePiece (int[] pieceToRemove) {
		this.piecesOnBoard.remove(pieceToRemove);
	}

	public int totalPiecesLeft(Player p) {
		int playerNumber = this.players[0]==p ? 0 : 1;
		int count = 0;
		for (int[] piece : this.piecesOnBoard) {
			if (piece[2]==playerNumber) {
				count++;
			}
		}
		return count;
	}

	public int normalPiecesLeft(Player p) {
		int playerNumber = this.players[0]==p ? 0 : 1;
		int count = 0;
		for (int[] piece : this.piecesOnBoard) {
			if (piece[2]==playerNumber && piece[3]==0) {
				count++;
			}
		}
		return count;
	}

	public int kingsLeft(Player p) {
		int playerNumber = this.players[0]==p ? 0 : 1;
		int count = 0;
		for (int[] piece : this.piecesOnBoard) {
			if (piece[2]==playerNumber && piece[3]==0) {
				count++;
			}
		}
		return count;
	}

	public static boolean locationIsInBounds (int[] testLocation) {
		int[] boardValues = new int[] {0,1,2,3,4,5,6,7};
		if (ArraysHelper.asArrayList(boardValues).contains(testLocation[0]) && ArraysHelper.asArrayList(boardValues).contains(testLocation[1])) {
			return true;
		}
		else {
			return false;
		}
	}

	public double calculateValue(Player p) {
		int playerNumber = this.players[0]==p ? 0 : 1;
		double p1Value = 0;
		double p2Value = 0;
		ArrayList<int[]> p1Pieces = new ArrayList<int[]>();
		ArrayList<int[]> p2Pieces = new ArrayList<int[]>();

		//sorts ever piece as p1Piece or p2Piece
		for (int[] piece : this.piecesOnBoard) {
			if (piece[2] == playerNumber) {
				p1Pieces.add(piece);
			}
			else {
				p2Pieces.add(piece);
			}
		}

		//calculates point value of p's pieces
		for (int[] piece : p1Pieces) {
			//determines if the piece is a king
			if (piece[3]==1) {
				//adds 3 to the player's total value for the board
				p1Value += 5;
			//the piece is not a king
			} else {
				//determines which side of the board the player is on
				if (p.getIsOnZeroSide()) {
					//adds value based on distance down the board
					p1Value += 3 + 0.125*piece[1];
				//the player is on the side of the board with index 7
				} else {
					//adds value based on distance down the board
					p1Value += 3 + 0.125*(7 - piece[1]);
				}
			}
		} 

		//calculates point value of p's opponent's pieces
		for (int[] piece : p2Pieces) {
			//determines if the piece is a king
			if (piece[3]==1) {
				p2Value += 5;
			//the piece is not a king
			} else {
				//determines which side of the board the player is on
				if (this.players[piece[2]].getIsOnZeroSide()) {
					//adds value based on distance down the board
					p2Value += 3 + 0.125*piece[1];
				//the player is on the side of the board with index 7
				} else {
					//adds value based on distance down the board
					p2Value += 3 + 0.125*(7 - piece[1]);
				}
			}
		}
		

		//discovers which player is winning by the current calculations above
		ArrayList<int[]> winningPlayerPieces;
		ArrayList<int[]> loserPlayerPieces;
		if (p1Value > p2Value) {
			winningPlayerPieces = p1Pieces;
			loserPlayerPieces = p2Pieces;
		} 
		else {
			winningPlayerPieces = p2Pieces;
			loserPlayerPieces = p1Pieces;
		}

		//performs extra calculations to encourage winner's kings to attack opponent's pieces
		for (int[] piece1 : winningPlayerPieces) {
			if (piece1[3]==1) {
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
		}

		//returns final calculated score
		return p1Value-p2Value;
	}

	public void printBoard() {
		System.out.println();
		for (int y : new int[] {7,6,5,4,3,2,1,0}) {
			String[] theLine = new String[8];
			for (int x : new int[] {0,1,2,3,4,5,6,7}) {
				if (this.getPieceAtLocation(new int[] {x,y}) != null) {
					theLine[x] = this.players[this.getPieceAtLocation(new int[] {x,y})[2]].getXO();
					if (this.getPieceAtLocation(new int[] {x,y})[3]==1) {
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
		if (dif1<0) {
			dif1=dif1*-1;
		} if (dif2<0) {
			dif2=dif2*-1;
		}
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
		if (piece.length!=4) System.out.println(piece);
		//creates separate location array
		int[] pieceLocation = new int[] {piece[0], piece[1]};
		//creates return array
		ArrayList<int[][]> result = new ArrayList<int[][]>();
		//tests if this piece is king
		if (piece[3]==1) {
			//tests if this move does not need to be a jump
			if (!mustBeJump) {
				//loops through all displacements
				for (int[] displacement : new int[][] {new int[] {1,1}, new int[] {1,-1}, new int[] {-1,1}, new int[] {-1,-1}}) {
					//finds potential destination
					int[] testDestination = new int[] {pieceLocation[0]+displacement[0], pieceLocation[1]+displacement[1]};
					//if destination empty and inbounds
					if (b.getPieceAtLocation(testDestination) == null && Board.locationIsInBounds(testDestination)) {
						//add waypoint set to return array
						result.add(new int[][] {pieceLocation, testDestination});
					}
				}
			}
			//loops through jump displacements
			for (int[] displacement : new int[][] {new int[] {2,2}, new int[] {2,-2}, new int[] {-2,2}, new int[] {-2,-2}}) {
				//finds potential destinations
				int[] testDestination = new int[] {pieceLocation[0]+displacement[0], pieceLocation[1]+displacement[1]};
				//finds location being jumped over
				int[] midpoint = new int[] {pieceLocation[0]+displacement[0]/2, pieceLocation[1]+displacement[1]/2};
				//tests that destination is in bounds, destination is unoccupied, and opponent piece is being jumped over
				if (Board.locationIsInBounds(testDestination) && b.getPieceAtLocation(testDestination) == null && b.getPieceAtLocation(pieceLocation) != null && b.getPieceAtLocation(midpoint) != null && b.getPlayers()[b.getPieceAtLocation(midpoint)[2]] != b.getPlayers()[piece[2]]) {
					//adds move to return array
					result.add(new int[][] {pieceLocation, testDestination});
					//cycles through possible multi-jump scenarios
					for (int[][] potentialMove : Board.getMovesFromLocation(new int[] {testDestination[0], testDestination[1], piece[2], piece[3]}, new Board(b, new Move(piece, new int[][]{pieceLocation, testDestination})), true)) {
						result.add(ArraysHelper.addTwoArrays(new int[][] {pieceLocation, testDestination}, potentialMove));
					}
				}
			}
		}
		else {
			//confirms that move doesnt need to be a jump
			if (!mustBeJump) {
				//declares regularDisplacements
				int[][] regularDisplacements;
				//is on zero side of the board (robot side)
				if (b.getPlayers()[piece[2]].getIsOnZeroSide()) {
					//sets displacement values
					regularDisplacements = new int[][] {new int[] {1,1}, new int[] {-1,1}};
				}
				//is not on zero side (human side)
				else {
					//sets different displacement values
					regularDisplacements = new int[][] {new int[] {1,-1}, new int[] {-1,-1}};
				}
				//iterates through all displacements
				for (int[] displacement : regularDisplacements) {
					//calculates potential destination
					int[] testDestination = new int[] {pieceLocation[0]+displacement[0], pieceLocation[1]+displacement[1]};
					//tests that location is in bounds and unoccupied
					if (b.getPieceAtLocation(testDestination) == null && Board.locationIsInBounds(testDestination)) {
						//adds waypoint set to the return array
						result.add(new int[][] {pieceLocation,testDestination});
					}
				}
			}
			
			// declares jumpDisplacements
			int[][] jumpDisplacements;
			//tests if player is on the zero side
			if (b.getPlayers()[piece[2]].getIsOnZeroSide()) {
				//sets displacements for jumps
				jumpDisplacements = new int[][] {new int[] {2,2}, new int[] {-2,2}};
			}
			//called if player is on the non-zero side
			else {
				//sets different displacement values for jumps
				jumpDisplacements = new int[][] {new int[] {2,-2}, new int[] {-2,-2}};
			}
			//iterates over all displacements
			for (int[] displacement : jumpDisplacements) {
				//calculates potential endpoint
				int[] testDestination = new int[] {pieceLocation[0]+displacement[0], pieceLocation[1]+displacement[1]};
				//calculates location being jumped over
				int[] midpoint = new int[] {pieceLocation[0]+displacement[0]/2, pieceLocation[1]+displacement[1]/2};
				//tests if destination is in bounds, unoccupied, and that midpoint is occupied by opponent's piece
				if (Board.locationIsInBounds(testDestination) && b.getPieceAtLocation(testDestination) == null && b.getPieceAtLocation(pieceLocation) != null && b.getPieceAtLocation(midpoint) != null && b.getPlayers()[b.getPieceAtLocation(midpoint)[2]] != b.getPlayers()[piece[2]]) {
					//adds waypoint set
					result.add(new int[][] {pieceLocation,testDestination});
					//finds all potential multi-jumps
					for (int[][] potentialMove : Board.getMovesFromLocation(new int[] {testDestination[0], testDestination[1], piece[2], piece[3]}, new Board(b, new Move(piece, new int[][]{pieceLocation, testDestination})), true)) {
						//adds multi-jump scenarios
						result.add(ArraysHelper.addTwoArrays(new int[][] {pieceLocation}, potentialMove));
					}
				}
			}
		}
		//returns the result
		return result.toArray(new int[result.size()][][]);
	}

}