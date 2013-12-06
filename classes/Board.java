import java.util.ArrayList;

public class Board {

	private ArrayList<Piece> piecesOnBoard;

	public static final String color = "black";
	public static final double maxBoardValue = 36;

	public Board (Player[] players) {
		this.piecesOnBoard = new ArrayList<Piece>();
		for (int i=0; i<3; i++) {
			for (int j=0; j<4; j++) {
				this.piecesOnBoard.add(new Piece(new int[] {(2*j+(i%2)), i}, players[0], this));
				this.piecesOnBoard.add(new Piece(new int[] {7 - 2*j-(i%2), 7-i}, players[1], this));
			}
		}
	}

	public Board (Board previousBoard, Move newMove) {
		this.piecesOnBoard = new ArrayList<Piece>();
		for (Piece p : previousBoard.getPiecesOnBoard()) {
			this.piecesOnBoard.add(p.copyToBoard(this));
		}
		Player.performMove(new Move(this.getPieceAtLocation(newMove.getSource()), newMove.getWaypoints()), this);
	}

	public Board(Player[] players, int[][] p1Locations, int[][] p2Locations, int[][] p1kings, int[][] p2kings) {
		this.piecesOnBoard = new ArrayList<Piece>();
		for (int[] location : p1Locations) {
			this.piecesOnBoard.add(new Piece(location, players[0], this));
		}
		for (int[] location : p2Locations) {
			this.piecesOnBoard.add(new Piece(location, players[1], this));
		}
		for (int[] location : p1kings) {
			this.piecesOnBoard.add(new Piece(location, players[0], this));
			this.piecesOnBoard.get(piecesOnBoard.size()-1).setIsKing(true);
		}
		for (int[] location : p2kings) {
			this.piecesOnBoard.add(new Piece(location, players[1], this));
			this.piecesOnBoard.get(piecesOnBoard.size()-1).setIsKing(true);
		}
	}

	public Piece[] getPiecesOnBoard () {
		return this.piecesOnBoard.toArray(new Piece[this.piecesOnBoard.size()]);
	}

	public Piece getPieceAtLocation(int[] location) {
		for (Piece piece : this.piecesOnBoard) {
			if (piece.getLocation()[0] == location[0] && piece.getLocation()[1] == location[1]) {
				return piece;
			}
		}
		return null;
	}

	public void removePiece (Piece pieceToRemove) {
		this.piecesOnBoard.remove(pieceToRemove);
	}

	public int totalPiecesLeft(Player p) {
		int count = 0;
		for (Piece piece : this.piecesOnBoard) {
			if (piece.getPlayer()==p) {
				count++;
			}
		}
		return count;
	}

	public int normalPiecesLeft(Player p) {
		int count = 0;
		for (Piece piece : this.piecesOnBoard) {
			if (piece.getPlayer()==p && !piece.getIsKing()) {
				count++;
			}
		}
		return count;
	}

	public int kingsLeft(Player p) {
		int count = 0;
		for (Piece piece : this.piecesOnBoard) {
			if (piece.getPlayer()==p && piece.getIsKing()) {
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
		double p1Value = 0;
		double p2Value = 0;

		//iterates over every piece on the board
		for (Piece piece : this.piecesOnBoard) {
			//determines if the piece is owned by player 1
			if (piece.getPlayer()==p) {
				//determines if the piece is a king
				if (piece.getIsKing()) {
					//adds 3 to the player's total value for the board
					p1Value += 3;
				//the piece is not a king
				} else {
					//determines which side of the board the player is on
					if (p.getIsOnZeroSide()) {
						//adds value based on distance down the board
						p1Value += 1 + 0.125*piece.getLocation()[1];
					//the player is on the side of the board with index 7
					} else {
						//adds value based on distance down the board
						p1Value += 1 + 0.125*(7 - piece.getLocation()[1]);
					}
				}
			//the piece is owned by player 2
			} else {
				//determines if the piece is a king
				if (piece.getIsKing()) {
					//adds 3 to the player's total value for the board
					p2Value += 3;
				//the piece is not a king
				} else {
					//determines which side of the board the player is on
					if (piece.getPlayer().getIsOnZeroSide()) {
						//adds value based on distance down the board
						p2Value += 1 + 0.125*piece.getLocation()[1];
					//the player is on the side of the board with index 7
					} else {
						//adds value based on distance down the board
						p2Value += 1 + 0.125*(7 - piece.getLocation()[1]);
					}
				}
			}
		}

		return p1Value-p2Value;
	}

	public double calculateValue2(Player p) {
		double p1Value = 0;
		double p2Value = 0;
		int p1Moves = p.getAllMoves(this).length;
		int p2Moves = 0;

		//iterates over every piece on the board
		for (Piece piece : this.piecesOnBoard) {
			//determines if the piece is owned by player 1
			if (piece.getPlayer()==p) {
				//determines if the piece is a king
				if (piece.getIsKing()) {
					//adds 3 to the player's total value for the board
					p1Value += 3;
				//the piece is not a king
				} else {
					//determines which side of the board the player is on
					if (p.getIsOnZeroSide()) {
						//adds value based on distance down the board
						p1Value += 1 + 0.125*piece.getLocation()[1];
					//the player is on the side of the board with index 7
					} else {
						//adds value based on distance down the board
						p1Value += 1 + 0.125*(7 - piece.getLocation()[1]);
					}
				}
			//the piece is owned by player 2
			} else {
				if (p2Moves==0) {
					p2Moves = piece.getPlayer().getAllMoves(this).length;
				}
				//determines if the piece is a king
				if (piece.getIsKing()) {
					//adds 3 to the player's total value for the board
					p2Value += 3;
				//the piece is not a king
				} else {
					//determines which side of the board the player is on
					if (piece.getPlayer().getIsOnZeroSide()) {
						//adds value based on distance down the board
						p2Value += 1 + 0.125*piece.getLocation()[1];
					//the player is on the side of the board with index 7
					} else {
						//adds value based on distance down the board
						p2Value += 1 + 0.125*(7 - piece.getLocation()[1]);
					}
				}
			}
		}

		return p1Value - p2Value + 0.0001*p1Moves - 0.0001*p2Moves;
	}

	public void printBoard() {
		System.out.println();
		for (int y : new int[] {7,6,5,4,3,2,1,0}) {
			String[] theLine = new String[8];
			for (int x : new int[] {0,1,2,3,4,5,6,7}) {
				if (this.getPieceAtLocation(new int[] {x,y}) != null) {
					theLine[x] = this.getPieceAtLocation(new int[] {x,y}).getPlayer().getXO();
					if (this.getPieceAtLocation(new int[] {x,y}).getIsKing()) {
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
}