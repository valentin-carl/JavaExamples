package de.tuberlin.sese.swtpp.gameserver.model.xiangqi;

import java.io.Serializable;

public class PieceFactory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -575945640901166411L;

	/**
	 *  private constructor so no Validator objects can be created accidentally
	 */
	private PieceFactory () {}
	
	/**
	 * This method creates Piece
	 * @param c -- if no piece matches c, it will create an Empty spot
	 * @return
	 */
	public static Piece createPiece (char c) {
		if (c == 'G') return new General(Colour.RED);
		if (c == 'g') return new General(Colour.BLACK);
		if (c == 'A') return new Advisor(Colour.RED);
		if (c == 'a') return new Advisor(Colour.BLACK);
		if (c == 'E') return new Elephant(Colour.RED);
		if (c == 'e') return new Elephant(Colour.BLACK);
		if (c == 'H') return new Horse(Colour.RED);
		if (c == 'h') return new Horse(Colour.BLACK);
		if (c == 'R') return new Rook(Colour.RED);
		if (c == 'r') return new Rook(Colour.BLACK);
		if (c == 'C') return new Cannon(Colour.RED);
		if (c == 'c') return new Cannon(Colour.BLACK);
		if (c == 'S') return new Solider(Colour.RED);
		if (c == 's') return new Solider(Colour.BLACK);
		return new Empty();
	}
}