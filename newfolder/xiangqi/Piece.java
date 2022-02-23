package de.tuberlin.sese.swtpp.gameserver.model.xiangqi;

import java.io.Serializable;
import java.util.HashSet;

public abstract class Piece implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 100835396889416726L;
	
	/**
	 * Attributes
	 */
	char fen_value;
	Colour col;
	
	/**
	 * equals method compares fen_value and colour
	 */
	@Override
	public boolean equals (Object o) {
		if (this == o) return true;
		if (!(o instanceof Piece)) return false;
		return ((Piece) o).col == this.col && ((Piece) o).fen_value == this.fen_value;
	}
}

class Empty extends Piece {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7486359481423799653L;

	/**
	 * Default constructor => should only be called in PieceFactory
	 */
	public Empty () {
		this.fen_value = ' ';
		this.col = null;
	}
	
	/**
	 * toString method returns respective fen value as String
	 */
	@Override
	public String toString () {
		return String.valueOf(this.fen_value);
	}
}

class General extends Piece {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -761714844380664996L;
	
	/**
	 * Attributes
	 */
	static HashSet<Position> offsets = initialiseOffsets();
	
	/**
	 * Calculates possible offsets
	 * @return
	 */
	private static HashSet<Position> initialiseOffsets() {
		HashSet<Position> res = new HashSet<>();
		res.add(new Position(0, 1));
		res.add(new Position(0, -1));
		res.add(new Position(1, 0));
		res.add(new Position(-1, 0));
		return res;
	}
	
	/**
	 * Default constructor => should be called in PieceFactory
	 * @param col
	 */
	public General (Colour col) {
		this.col = col;
		this.fen_value = this.col == Colour.RED ? 'G' : 'g';
	}
	
	/**
	 * toString method returns respective fen value as String
	 */
	@Override
	public String toString () {
		return String.valueOf(this.fen_value);
	}
}

class Advisor extends Piece {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2085987570313649776L;

	/**
	 * Attributes
	 */
	static HashSet<Position> offsets = initialiseOffsets();
	
	/**
	 * Default constructor => should be called in PieceFactory
	 * @param col
	 */
	public Advisor (Colour col) {
		this.col = col;
		this.fen_value = this.col == Colour.RED ? 'A' : 'a';
	}
	
	/**
	 * Calculates possible offsets
	 * @return
	 */
	private static HashSet<Position> initialiseOffsets() {
		HashSet<Position> res = new HashSet<>();
		res.add(new Position(1,1));
		res.add(new Position(1,-1));
		res.add(new Position(-1,1));
		res.add(new Position(-1,-1));
		return res;
	}

	/**
	 * toString method returns respective fen value as String
	 */
	@Override
	public String toString () {
		return String.valueOf(this.fen_value);
	}
}

class Elephant extends Piece {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1233478232547635317L;

	/**
	 * Attributes
	 */
	static HashSet<Position> offsets = initialiseOffsets();
	
	/**
	 * Default constructor => should be called in PieceFactory
	 * @param col
	 */
	public Elephant (Colour col) {
		this.col = col;
		this.fen_value = this.col == Colour.RED ? 'E' : 'e';
	}
	
	/**
	 * Calculates possible offsets
	 * @return
	 */
	private static HashSet<Position> initialiseOffsets() {
		HashSet<Position> res = new HashSet<>();
		res.add(new Position(2,2));
		res.add(new Position(2,-2));
		res.add(new Position(-2,2));
		res.add(new Position(-2,-2));
		return res;
	}
	
	/**
	 * toString method returns respective fen value as String
	 */
	@Override
	public String toString () {
		return String.valueOf(this.fen_value);
	}
}

class Horse extends Piece {

	/**
	 * 
	 */
	private static final long serialVersionUID = 756486188411215815L;

	/**
	 * Attributes
	 */
	static HashSet<Position> offsets = initialiseOffsets();
	
	/**
	 * Default constructor => should be called in PieceFactory
	 * @param col
	 */
	public Horse (Colour col) {
		this.col = col;
		this.fen_value = this.col == Colour.RED ? 'H' : 'h';
	}
	
	/**
	 * Calculates possible offsets
	 * @return
	 */
	private static HashSet<Position> initialiseOffsets() {
		HashSet<Position> res = new HashSet<>();
		res.add(new Position(2,1));
		res.add(new Position(2,-1));
		res.add(new Position(1,2));
		res.add(new Position(1,-2));
		res.add(new Position(-1,2));
		res.add(new Position(-1,-2));
		res.add(new Position(-2,1));
		res.add(new Position(-2,-1));
		return res;
	}
	
	/**
	 * toString method returns respective fen value as String
	 */
	@Override
	public String toString () {
		return String.valueOf(this.fen_value);
	}
}

class Rook extends Piece {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6377666749800822320L;

	/**
	 * Attributes
	 */
	static HashSet<Position> offsets = initialiseOffsets();
	
	/**
	 * Default constructor => should be called in PieceFactory
	 * @param col
	 */
	public Rook (Colour col) {
		this.col = col;
		this.fen_value = this.col == Colour.RED ? 'R' : 'r';
	}
	
	/**
	 * Calculates possible offsets
	 * @return
	 */
	private static HashSet<Position> initialiseOffsets() {
		HashSet<Position> res = new HashSet<>();
		// Felder nach oben
		for (int row = 1; row < 10; row++) {
			res.add(new Position(row, 0));
		}
		// Felder nach rechts
		for (int col = 1; col < 9; col++) {
			res.add(new Position(0, col));
		}
		// Felder nach links
		for (int col = -1; col > -9; col--) {
			res.add(new Position(0, col));
		}
		// Felder nach unten
		for (int row = -1; row > -10; row--) {
			res.add(new Position(row, 0));
		}
		return res;
	}
	
	/**
	 * toString method returns respective fen value as String
	 */
	@Override
	public String toString () {
		return String.valueOf(this.fen_value);
	}
}

class Cannon extends Piece {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4937469540300462752L;

	/**
	 * Attributes
	 */
	static HashSet<Position> offsets = initialiseOffsets();
	
	/**
	 * Default constructor => should be called in PieceFactory
	 * @param col
	 */
	public Cannon (Colour col) {
		this.col = col;
		this.fen_value = this.col == Colour.RED ? 'C' : 'c';
	}
	
	
	/**
	 * Calculates possible offsets
	 * @return
	 */
	private static HashSet<Position> initialiseOffsets() {
		HashSet<Position> res = new HashSet<>();
		// Felder nach oben
		for (int row = 1; row < 10; row++) {
			res.add(new Position(row, 0));
		}
		// Felder nach rechts
		for (int col = 1; col < 9; col++) {
			res.add(new Position(0, col));
		}
		// Felder nach links
		for (int col = -1; col > -9; col--) {
			res.add(new Position(0, col));
		}
		// Felder nach unten
		for (int row = -1; row > -10; row--) {
			res.add(new Position(row, 0));
		}
		return res;
	}
	
	
	/**
	 * toString method returns respective fen value as String
	 */
	@Override
	public String toString () {
		return String.valueOf(this.fen_value);
	}
}

class Solider extends Piece {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3667222342693805087L;
	
	/**
	 * Default constructor => should be called in PieceFactory
	 * @param col
	 */
	public Solider (Colour col) {
		this.col = col;
		this.fen_value = this.col == Colour.RED ? 'S' : 's';
	}
	
	/**
	 * toString method returns respective fen value as String
	 */
	@Override
	public String toString () {
		return String.valueOf(this.fen_value);
	}
}
