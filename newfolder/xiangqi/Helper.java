package de.tuberlin.sese.swtpp.gameserver.model.xiangqi;

import java.io.Serializable;
import java.util.HashSet;

public class Helper implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1492523309427704L;

	/**
	 * private constructor so no helper objects will be created
	 */
	private Helper () {}
	
	/**
	 * checks whether a position object is in a HashSet<Position>
	 * @param palace
	 * @param pos
	 * @return
	 */
	public static boolean positionInPalace (HashSet<Position> palace, Position pos) {
		for (Position p : palace) {
			if (p.row == pos.row && p.col == pos.col) return true;
		}
		return false;
	}

}
