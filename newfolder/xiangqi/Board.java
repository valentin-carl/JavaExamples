package de.tuberlin.sese.swtpp.gameserver.model.xiangqi;

import java.io.Serializable;
import java.util.HashSet;

public class Board implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9065843496232252955L;
	
	/**
	 * Class values
	 */
	private static final String INITIAL_STATE = "rheagaehr/9/1c5c1/s1s1s1s1s/9/9/S1S1S1S1S/1C5C1/9/RHEAGAEHR"; 
	public static final int NROWS = 10;
	public static final int NCOLS = 9;
	private HashSet<Position> redPalace = this.createRedPalace();
	private HashSet<Position> blackPalace = this.createBlackPalace();
	
	/**
	 * Attributes
	 */
	private Piece[][] board;
	private String fen;
	
	/**
	 * default constructor creates board with default starting position
	 */
	public Board () {
		this (INITIAL_STATE);
	}
	
	/**
	 * Constructor generates Piece array from String in fen notation
	 * @param fen -- state of the board in FEN notation
	 */
	public Board (String fen) {
		
		// test input first! => throw Exception
		if (!Validator.isValidFEN(fen)) System.out.println("Board :: constructor | this input cannot create a new board");
		
		// set fen attribute
		this.fen = fen;
		
		// generate board attribute
		this.board = new Piece[10][9];
		int row = 9, col = 0;
		
		// iterate over input String
		for (int index = 0; index < fen.length(); index++) {
			char current = fen.charAt(index);
			
			// test if current char is '/' => start with next row
			if (current == '/') {
				row--; col = 0;
			} else {
				
				// if current is a number => insert empty spots accordingly
				if (Character.isDigit(current)) {
					int numSpaces = Integer.parseInt(String.valueOf(current));
					while (numSpaces > 0) {
						this.board[row][col] = PieceFactory.createPiece(' ');
						col++; numSpaces--;
					}
				// else: create new piece at the current spot 
				} else {
					this.board[row][col] = PieceFactory.createPiece(current);
					col++;
				} 
			}
		}
	}
	
	/**
	 * creates HashSet with coordinates of the spots in the red palace
	 * @return
	 */
	private HashSet<Position> createRedPalace () {
		HashSet<Position> res = new HashSet<>();
		for (int row = 0; row < 3; row++) {
			for (int col = 3; col < 6; col++) {
				res.add(new Position(row, col));
			}
		}
		return res;
	}
	
	/**
	 * creates HashSet with coordinates of the spots in the black palace
	 * @return
	 */
	private HashSet<Position> createBlackPalace () {
		HashSet<Position> res = new HashSet<>();
		for (int row = 7; row < 10; row++) {
			for (int col = 3; col < 6; col++) {
				res.add(new Position(row, col));
			}
		}
		return res;
	}
	
	/**
	 * returns piece on board with position row, col
	 * @param row
	 * @param col
	 * @return
	 */
	private Piece get (int row, int col) {
		return this.board[row][col];
	}
	
	/**
	 * looks for piece with fen value c on this board, returns null if not found
	 * CAREFUL: RETURN ONLY THE POSITION OF THE PIECE FOUND FIRST -- MIGHT CAUSE ERRORS IF MULTIPLE PIECES OF SAME FEN VALUE ARE ON THE BOARD
	 * @param piece -- piece to find
	 * @return -- Position of object with fen value c
	 */
	private Position find (char c) {
		
		// iterate over board and look for piece with fen value c
		for (int row = 0; row < 10; row++) {
			for (int col = 0; col < 9; col++) {
				if (this.board[row][col].fen_value == c) return new Position(row, col);
			}
		}
		
		// if not found, return null
		return null;
	}
	
	/**
	 * ONLY CALL IN MOVE METHOD!
	 * puts piece on the board with position row, col
	 * @param piece
	 * @param row
	 * @param col
	 */
	private void put (Piece piece, int row, int col) {
		this.board[row][col] = piece;
	}
	
	/**
	 * This method moves the piece on (fromRow|fromCol) to (toRow|toCol)
	 * Careful: this method can be used to move an empty spot onto a piece, which makes it disappear!
	 * @param fromRow
	 * @param fromCol
	 * @param toRow
	 * @param toCol
	 */
	private void move (int fromRow, int fromCol, int toRow, int toCol) {
		Piece movingPiece = this.get(fromRow, fromCol);
		this.put(PieceFactory.createPiece(' '), fromRow, fromCol);
		this.put(movingPiece, toRow, toCol);
		this.updateFen();
	}
	
	/**
	 * Updates the fen attribute
	 */
	private void updateFen () {
		this.fen = this.generateFen();
	}
	
	// TODO test
	private String generateFen () {
		
		// to return later
        String res = "";

        // iterate over board and build string
        for (int row = 9; row >= 0; row--) {
            for (int col = 0; col < 9; col++) {
            	
                // if current spot is empty -- count empty spots and add count to res
                if (this.get(row, col) instanceof Empty) {
                    int numEmptySpots = 0;
                    countEmptySpots:
                    while (col < 9) {
                        if (!(this.get(row, col) instanceof Empty)) {
                            col--; // to continue normally with the for loops
                            break countEmptySpots;
                        }
                        numEmptySpots++;
                        col++;
                    }
                    res += String.valueOf(numEmptySpots);
                } else {
                    // not an empty spot => just use respective toString methods
                    res += this.get(row, col);
                }
            }
            if (row != 0) res += "/";
        }
        
        // return value
        return res;
	}
	
	/**
	 * Getter method for fen attribute
	 */
	public String getFen () {
		return this.fen;
	}
	
	/**
	 * tests if a move is possible and allowed
	 * @param move -- move, notation e.g. "a1-c4";
	 * @param playerColour -- colour of player trying to move
	 * @return true if move is possible and allowed, else false
	 */
	public boolean isMovePossible (String move, Colour playerColour) {
		
		// Is the input String valid?
		if (!Validator.isValidMoveFormat(move)) return false;
		
		// is the colour valid?
		if (playerColour == null) {
			//System.out.println("Board :: isMovePossible | input playerColour is null => invalid move");
			// TODO fehlt hier ein return statement?
		}
		
		// get move attributes
		int fromRow = Character.getNumericValue(move.charAt(1));
		int fromCol = ((int) move.toCharArray()[0])-97;
		int toRow = Character.getNumericValue(move.charAt(4));
		int toCol = ((int) move.toCharArray()[3])-97;
		Piece movingPiece = this.get(fromRow, fromCol);
		
		
		Object[] stuff = {fromRow, fromCol, toRow, toCol, movingPiece, playerColour};
		if (!this.mcCabeReducer1(stuff)) return false;
		
		/*
		// Is (fromRow | fromCol) != (toRow | toCol)?
		if (fromRow == toRow && fromCol == toCol) return false;
		
		// Are (fromRow | fromCol) and (toRow | toCol) on the board?
		if (!(Validator.isValidPosition(fromRow, fromCol) && Validator.isValidPosition(toRow, toCol))) return false;
		
		// Is there a piece of the same colour on (fromRow | fromCol)?
		if (this.get(fromRow, fromCol) instanceof Empty) return false;
		
		// Does movingPiece have the same colour as playerColour?
		if (this.get(fromRow, fromCol).col != playerColour) return false;
			
		// Is there a piece of the same colour on (toRow | toCol)?
		if (this.get(toRow, toCol).col == movingPiece.col) return false;
		
		// Is the piece allowed to move there?
		if (!this.canPieceMoveThere(fromRow, fromCol, toRow, toCol)) return false;
		*/
		
		// Does the move create todesblick?
		Board boardAfter = this.deepCopy();
		boardAfter.move(fromRow, fromCol, toRow, toCol);
		if (boardAfter.containsTodesblick()) return false;
		
		return boardAfter.isInCheck(playerColour);
}
	
	private boolean mcCabeReducer1 (Object[] stuff) {
		
		// Object[] stuff = {fromRow, fromCol, toRow, toCol, movingPiece, playerColour};
		int fromRow = ((int) stuff[0]);
		int fromCol = (int) stuff[1];
		int toRow = (int) stuff[2];
		int toCol = (int) stuff[3];
		Piece movingPiece = (Piece) stuff[4];
		Colour playerColour = (Colour) stuff[5];
		
		// Is (fromRow | fromCol) != (toRow | toCol)?
		if (fromRow == toRow && fromCol == toCol) return false;
		
		// Are (fromRow | fromCol) and (toRow | toCol) on the board?
		if (!(Validator.isValidPosition(fromRow, fromCol) && Validator.isValidPosition(toRow, toCol))) return false;
		
		// Is there a piece of the same colour on (fromRow | fromCol)?
		if (this.get(fromRow, fromCol) instanceof Empty) return false;
		
		// Does movingPiece have the same colour as playerColour?
		if (this.get(fromRow, fromCol).col != playerColour) return false;
			
		// Is there a piece of the same colour on (toRow | toCol)?
		if (this.get(toRow, toCol).col == movingPiece.col) return false;
		
		// Is the piece allowed to move there?
		if (!this.canPieceMoveThere(fromRow, fromCol, toRow, toCol)) return false;
		
		return true;
	}
	
	
	
	/**
	 * Performs move -- no checks for correctness!
	 * @param move
	 */
	public void doMove (String move) {
		
		// get move attributes
		int fromRow = Character.getNumericValue(move.charAt(1));
		int fromCol = ((int) move.toCharArray()[0])-97;
		int toRow = Character.getNumericValue(move.charAt(4));
		int toCol = ((int) move.toCharArray()[3])-97;
		
		// performs the move
		this.move(fromRow, fromCol, toRow, toCol);
	}
	
	/**
	 * creates a deep copy of this board
	 * @return
	 */
	private Board deepCopy () {
		Board b = new Board();
		b.fen = this.fen;
		b.board = new Piece[10][9];
		for (int row = 0; row < 10; row++) {
			for (int col = 0; col < 9; col++) {
				b.board[row][col] = PieceFactory.createPiece(this.get(row, col).fen_value);
			}
		}
		return b;
	}
	
	/**
	 * Method finds piece on (fromRow | fromCol) and calls method for type of piece on that spot
	 * @param fromRow
	 * @param fromCol
	 * @param toRow
	 * @param toCol
	 * @return
	 */
	protected boolean canPieceMoveThere (int fromRow, int fromCol, int toRow, int toCol) {
		
		// get piece on (fromRow | fromCol)
		Piece movingPiece = this.get(fromRow, fromCol);
		
		// call method to check if piece can move to (toRow | toCol)
		if (movingPiece instanceof General) return canGeneralMoveThere(fromRow, fromCol, toRow, toCol);
		if (movingPiece instanceof Advisor) return canAdvisorMoveThere(fromRow, fromCol, toRow, toCol);
		if (movingPiece instanceof Elephant) return canElephantMoveThere(fromRow, fromCol, toRow, toCol);
		if (movingPiece instanceof Horse) return canHorseMoveThere(fromRow, fromCol, toRow, toCol);
		if (movingPiece instanceof Rook) return canRookMoveThere(fromRow, fromCol, toRow, toCol);
		if (movingPiece instanceof Cannon) return canCannonMoveThere(fromRow, fromCol, toRow, toCol);
		if (movingPiece instanceof Solider) return canSoliderMoveThere(fromRow, fromCol, toRow, toCol);
		
		// if no other method was called, there is no piece on (fromRow | fromCol) => invalid move
		System.out.println("Board :: canPieceMoveThere | reached false");
		return false;
	}
	
	/**
	 * checks whether the general on (fromRow | fromCol) can move to (toRow | toCol)
	 * @param fromRow
	 * @param fromCol
	 * @param toRow
	 * @param toCol
	 * @return
	 */
	private boolean canGeneralMoveThere (int fromRow, int fromCol, int toRow, int toCol) {
		
		// move attributes
		Position from = new Position(fromRow, fromCol);
		Position to = new Position(toRow, toCol);
		System.out.println("Board :: canGeneralMoveThere | trying to move from " + from + " to " + to);
		
		// check if to is in the respective palace
		if (this.get(fromRow, fromCol).col == Colour.RED) {
			if (!Helper.positionInPalace(this.redPalace, to)) {
				System.out.println("Board :: canGeneralMoveThere | position to is not in the red palace => invalid move");
				return false;
			}
		} else {
			if (!Helper.positionInPalace(this.blackPalace, to)) {
				System.out.println("Board :: canGeneralMoveThere | position to is not in the black palace => invalid move");
				return false;
			}
		}
		
		// ist das Feld eine Position weit weg? (nicht diagonal!)
		boolean b = false;
		for (Position p : General.offsets) {
			if (fromRow + p.row == toRow && fromCol + p.col == toCol) b = true;
		}
		if (!b) {
			System.out.println("Board :: canGeneralMoveThere | general.position + offset cannot reach (toRow | toCol) => invalid move");
			return false;
		}
			
		// if reached => should be fine to move there
		return true;
	}
	
	/**
	 * checks if this board contains a todesblick
	 * @return -- true if it does, else false
	 */
	private boolean containsTodesblick () {
		
		// sind beide Generale in derselben Spalte?
		if (this.find('G').col != this.find('g').col) {
			System.out.println("Board :: isInTodesBlick | Generale sind nicht in derselben Spalte");
			return false;
		}
		
		// steht zwischen den Generalen etwas?
		for (int rowIndex = this.find('G').row+1; rowIndex < this.find('g').row; rowIndex++) {
			if (!(this.get(rowIndex, this.find('G').col) instanceof Empty)) {
				System.out.println("Board :: isInTodesBlick | Zwischen den Generalen steht eine Figur");
				return false; 
			}
		}
		
		// if reached: contains todesblick
		return true;
	}

	/**
	 * checks if general of Colour colour is in check
	 * @param colour -- of general to check
	 * @return -- true if is in check, else false
	 */
	private boolean isInCheck (Colour colour) {
		
		// to look for enemies
		Colour otherColour = colour == Colour.RED ? Colour.BLACK : Colour.RED;
		
		// get hashset of all enemies
		HashSet<Position> enemies = new HashSet<>();
		for (int row = 0; row < 10; row++) {
			for (int col = 0; col < 9; col++) {
				if (this.get(row, col).col == otherColour) enemies.add(new Position(row, col));
			}
		}
		
		// for all enemies, check if they can reach the player of colour colour's general
		Position generalPos = colour == Colour.RED ? this.find('G') : this.find('g');
		for (Position p : enemies) {
			if (this.canPieceMoveThere(p.row, p.col, generalPos.row, generalPos.col)) return true;
		}
		
		// if reached, no hostile piece can reach the player of colour colour's general
		return false;
	}	
	
	/**
	 * checks whether the advisor on (fromRow | fromCol) can move to (toRow | toCol)
	 * @param fromRow
	 * @param fromCol
	 * @param toRow
	 * @param toCol
	 * @return
	 */
	private boolean canAdvisorMoveThere (int fromRow, int fromCol, int toRow, int toCol) {
		
		// move attributes
		Position from = new Position(fromRow, fromCol);
		Position to = new Position(toRow, toCol);
		System.out.println("Board :: canAdvisorMoveThere | trying to move from " + from + " to " + to);
		
		// ist die Position im eigenen Palast?
		if (this.get(fromRow, fromCol).col == Colour.RED) {
			if (!Helper.positionInPalace(this.redPalace, to)) {
				System.out.println("Board :: canAdvisorMoveThere | position to is not in the red palace => invalid move");
				return false;
			}
		} else {
			if (!Helper.positionInPalace(this.blackPalace, to)) {
				System.out.println("Board :: canAdvisorMoveThere | position to is not in the black palace => invalid move");
				return false;
			}
		}
		
		// Passen die Offsets?
		boolean b = false;
		for (Position p : Advisor.offsets) {
			if (fromRow + p.row == toRow && fromCol + p.col == toCol) b = true;
		}
		if (!b) {
			System.out.println("Board :: canAdvisorMoveThere | adivisor.position + offset cannot reach (toRow | toCol) => invalid move");
			return false;
		}
		
		// if reached: should be fine
		return true;
	}
	
	/**
	 * checks whether the elephant on (fromRow | fromCol) can move to (toRow | toCol)
	 * @param fromRow
	 * @param fromCol
	 * @param toRow
	 * @param toCol
	 * @return
	 */
	private boolean canElephantMoveThere (int fromRow, int fromCol, int toRow, int toCol) {
		
		// move attributes
		Colour col = this.get(fromRow, fromCol).col;
		Position from = new Position(fromRow, fromCol);
		Position to = new Position(toRow, toCol);
		System.out.println("Board :: canElephantMoveThere | trying to move from " + from + " to " + to);
		
		// Ist die Position noch auf der eigenen Seite? => Elefanten dürfen die Fluss nicht überqueren
		if (col == Colour.RED && toRow > 4) {
			System.out.println("Board :: canElephantMoveThere | tried to move elephant over the river => invalid move");
			return false;
		}
		if (col == Colour.BLACK && toRow < 5) {
			System.out.println("Board :: canElephantMoveThere | tried to move elephant over the river => invalid move");
			return false;
		}
		
		// Passen die Offsets?
		boolean b = false;
		for (Position p : Elephant.offsets) {
			if (fromRow + p.row == toRow && fromCol + p.col == toCol) b = true;
		}
		if (!b) {
			System.out.println("Board :: canElephantMoveThere | elephant.position + offset cannot reach (toRow | toCol) => invalid move");
			return false;
		}
		
		// if reached: should be fine
		return true;
	}

	/**
	 * checks whether the horse on (fromRow | fromCol) can move to (toRow | toCol)
	 * @param fromRow
	 * @param fromCol
	 * @param toRow
	 * @param toCol
	 * @return
	 */
	private boolean canHorseMoveThere (int fromRow, int fromCol, int toRow, int toCol) {
		
		// move attributes
		Position from = new Position(fromRow, fromCol);
		Position to = new Position(toRow, toCol);
		
		
		// Passen die Offsets?
		boolean b = false;
		for (Position p : Horse.offsets) {
			if (fromRow + p.row == toRow && fromCol + p.col == toCol) b = true;
		}
		if (!b) return false;

		
		// Steht nichts im Weg?
		int diffRow = toRow - fromRow;
		int diffCol = toCol - fromCol;
		if (diffRow == 2) {
			// Feld oben
			if (!(this.get(fromRow+1, fromCol) instanceof Empty)) {
				
				return false;
			}
		} else if (diffCol == 2) {
			// Feld rechts
			if (!(this.get(fromRow, fromCol+1) instanceof Empty)) {
				
				return false;
			}
		} else if (diffRow == -2) {
			// Feld unten
			if (!(this.get(fromRow-1, fromCol) instanceof Empty)) return false;
		} else if (diffCol == -2) {
			// Feld links
			if (!(this.get(fromRow, fromCol-1) instanceof Empty)) return false;
		}
		
		// if reached: should be fine
		return true;
	}

	/**
	 * checks whether the rook on (fromRow | fromCol) can move to (toRow | toCol)
	 * @param fromRow
	 * @param fromCol
	 * @param toRow
	 * @param toCol
	 * @return
	 */
	private boolean canRookMoveThere (int fromRow, int fromCol, int toRow, int toCol) {
		
		// move attributes
		Position from = new Position(fromRow, fromCol), to = new Position(toRow, toCol);
		
		// Passen die offsets?
		boolean b = false;
		for (Position p : Rook.offsets) if (fromRow + p.row == toRow && fromCol + p.col == toCol) b = true;
		if (!b) return false;
	
		
		// steht nichts im Weg?
		int diffRow = toRow - fromRow, diffCol = toCol - fromCol;
		if (diffRow > 0) {
			// nach oben
			for (int row = fromRow+1; row < toRow; row++) if (!(this.get(row, fromCol) instanceof Empty)) return false;
		} else if (diffCol > 0) {
			// nach rechts
			for (int col = fromCol+1; col < toCol; col++) if (!(this.get(fromRow, col) instanceof Empty))return false;
		} else if (diffCol < 0) {
			// nach links
			for (int col = fromCol-1; col > toCol; col--) if (!(this.get(fromRow, col) instanceof Empty)) return false;
		} else if (diffRow < 0) {
			// nach unten
			for (int row = fromRow-1; row > toRow; row--) if (!(this.get(row, fromCol) instanceof Empty)) return false;
		}
		
		// if reached: should be fine
		return true;
	}
	
	/**
	 * checks whether the rook on (fromRow | fromCol) can move to (toRow | toCol)
	 * @param fromRow
	 * @param fromCol
	 * @param toRow
	 * @param toCol
	 * @return
	 */
	private boolean canCannonMoveThere (int fromRow, int fromCol, int toRow, int toCol) {
		
		// move attributes
		Position from = new Position(fromRow, fromCol), to = new Position(toRow, toCol);
		
		
		// Wenn die Kanone sich nur bewegt, tut sie dies wie der Rook
		if (this.get(toRow, toCol) instanceof Empty) {
			
			return this.canRookMoveThere(fromRow, fromCol, toRow, toCol);
		}
		
		// Wenn die Kanone schlägt, muss zwischen (fromRow | fromCol) und (toRow | toCol) genau eine andere Figur sein
			// Passen die offsets?
		boolean b = false;
		for (Position p : Cannon.offsets) {
			if (fromRow + p.row == toRow && fromCol + p.col == toCol) b = true;
		}
		if (!b) return false;
		
			// Wieviele Felder sind im weg?
		int numPieces = 0;
				// in Welche Richtung wird sich bewegt? N/S oder W/E
		boolean movesNorthSouth = fromCol == toCol;
		if (movesNorthSouth) {
			// bewegt sich vertikal
			int oben = fromRow < toRow ? toRow : fromRow;
			int unten = fromRow < toRow ? fromRow : toRow;
			for (int i = unten+1; i < oben; i++) {
				if (!(this.get(i, fromCol) instanceof Empty)) numPieces++;
			}
		} else {
			// bewegt sich horizontal
			int links = fromCol < toCol ? fromCol : toCol;
			int rechts = fromCol < toCol ? toCol : fromCol;
			for (int i = links+1; i < rechts; i++) {
				if (!(this.get(fromRow, i) instanceof Empty)) numPieces++;
			}
		}
		
		return (numPieces != 1);
		/*
		// if reached: should be fine
		return true;
		*/
	}

	/**
	 * checks whether the rook on (fromRow | fromCol) can move to (toRow | toCol)
	 * @param fromRow
	 * @param fromCol
	 * @param toRow
	 * @param toCol
	 * @return
	 */
	private boolean canSoliderMoveThere (int fromRow, int fromCol, int toRow, int toCol) {
		
		// move attributes
		Colour col = this.get(fromRow, fromCol).col;
		Position from = new Position(fromRow, fromCol), to = new Position(toRow, toCol);
		
		// Hat der Solider den Fluss überquert?
		boolean passedRiver = this.get(fromRow, fromCol).col == Colour.RED ? fromRow > 4 : fromRow < 5;
		
		// Hat der Solider das ende des Boards erreicht?
		boolean reachedEndOfBoard = this.get(fromRow, fromCol).col == Colour.RED ? fromRow == 9 : fromRow == 0;
		
		// Falls der Solider noch auf der eigenen Seite ist, kann er nur nach vorne gehen
		if (!passedRiver) { if (col == Colour.RED) { if (!(fromCol == toCol && toRow-fromRow == 1)) return false; } else {
				if (!(fromCol == toCol && fromRow-toRow == 1)) return false;
			}
		}
		
		// Falls der Solider auf der gegnerischen Seite und nicht am Ende ist, kann er nach vorne, links und rechts
		if (passedRiver && !reachedEndOfBoard) {
			boolean validMove = false; if (col == Colour.RED) {
				// check vorne 				// check links oder rechts ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse ich hhasse exclipse 
				if ((fromCol == toCol && toRow-fromRow == 1) || (fromRow == toRow && Math.abs(toCol-fromCol) == 1)) validMove = true;
			} else { if ((fromCol == toCol && fromRow-toRow == 1) || (fromRow == toRow && Math.abs(toCol-fromCol) == 1)) validMove = true; }
			if (!validMove) return false;
		}
		
		// Falls der Solider am Ende des Boards ist, kann er nur nach links und rechts
		if (reachedEndOfBoard) {
			boolean validMove = false;
			if (col == Colour.RED) {
				if (toRow == 9 && Math.abs(toCol-fromCol) == 1) validMove = true;
			} else {
				if (toRow == 0 && Math.abs(toCol-fromCol) == 1) validMove = true;
			}
			if (!validMove) return false;
		}
		
		// if reached: should be fine
		return true;
	}

	/**
	 * checks if player colour has any moves left
	 * @param colour
	 * @return
	 */
	public boolean hasAnyMovesLeft (Colour colour) {
		
		// get HashSet of all pieces of that Colour
		HashSet<Position> pieces = new HashSet<>();
		for (int row = 0; row < 10; row++) {
			for (int col = 0; col < 9; col++) {
				if (this.get(row, col).col == colour) pieces.add(new Position(row, col));
			}
		}
		
		// For each peace, iterate over entire board and see if it can go there => if yes return true
		for (Position p : pieces) {
			for (int row = 0; row < 10; row++) {
				for (int col = 0; col < 9; col++) {
					if (this.canPieceMoveThere(p.row, p.col, row, col)) return true;
				}
			}
		}
		
		// if reached, there are no more possible moves
		return false;
	}
}
