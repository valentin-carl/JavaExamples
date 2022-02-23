package de.tuberlin.sese.swtpp.gameserver.model.xiangqi;

import java.io.Serializable;

public class Position implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8207107606156354539L;
	// attributes
	int row;
	int col;
	
	/**
	 * standard constructor
	 */
	public Position (int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	/**
	 * equals method compares row and col attributes
	 */
	@Override
	public boolean equals (Object o) {
		if (this == o) return true;
		if (!(o instanceof Position)) return false;
		System.out.println(((Position) o).row == this.row && ((Position) o).col == this.col);
		return ((Position) o).row == this.row && ((Position) o).col == this.col;
	}
	
	@Override
	public String toString () {
		return "(" + this.row + ", " + this.col + ")";
	}
}
