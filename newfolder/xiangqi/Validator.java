package de.tuberlin.sese.swtpp.gameserver.model.xiangqi;

import java.io.Serializable;
import java.util.ArrayList;

public class Validator implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4645581801381091773L;

	/**
	 *  private constructor so no Validator objects can be created accidentally
	 */
	private Validator () {	}
	
	/**
     * tests whether input s can be turned into a board
     * IMPORTANT: method does not test whether the state violates the game's rules -- only if the String can be used for a constructor call
     * @param s -- input String that is to be tested for viability
     * @return -- true if allowed, false if not
     */
    public static boolean isValidFEN (String s) {

        // if null input
        if (s == null) return false;

        // create String array with one entry for each row
        String[] array = s.split("/");

        // check if there is one entry for each row
        if (array.length != 10) {
            return false;
        }

        // iterate over String array
        for (int i = 0; i < array.length; i++) {

            // get one list of Integers and one list of characters
            ArrayList<Integer> listInts = new ArrayList<>();
            ArrayList<Character> listChars = new ArrayList<>();
            for (char c : array[i].toCharArray()) {
                try {
                    listInts.add((int) Integer.parseInt(String.valueOf(c)));
                } catch (Exception e) {
                    listChars.add(c);
                }
            }

            // calculate sum of all Integers
            int sum = 0;
            for (Integer j : listInts) sum += j;
            if (listChars.size() + sum != 9) return false;

            // validate characters
            for (Character c : listChars) if (!Validator.isValidChar(c)) return false;
        }

        // if this is reached, input String should be fine
        return true;
    }
	
	/**
     * checks if char is valid part of FEN string
     * @param c -- input char
     * @return -- boolean true or false respectively
     */
    public static boolean isValidChar (char c) {
        char[] valid = {'1', '2', '3', '4', '5', '6', '7', '8', '9', 'G', 'A', 'E', 'H', 'R', 'C', 'S', 'g', 'a', 'e', 'h', 'r', 'c', 's', ' '};
        for (char k : valid) if (c == k) return true;
        System.out.println(c);
        return false;
    }
    
    /**
     * tests whether a move is possible to use -- does not check whether move is legal! just if it is in the right format
     * @param s -- possible move
     * @return -- true if valid else false
     */
    public static boolean isValidMoveFormat (String s) {

        // länge muss 5 sein
        if (s.length() != 5) return false;

        // format muss buchstabe - nummer - bindestrich - buchstabe - nummer sein
        char[] chars = s.toCharArray();

        // erlaubte zahlen 0-9 (inkl.)
        if (!(Validator.charIsNumber(chars[1]) && Validator.charIsNumber(chars[4]))) {
            return false;
        }

        // erlaubte buchstaben a, b, c, d, e, f, g, h, i
        char[] valid_letters = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i'};
        boolean b1 = false;
        for (char c : valid_letters) {
            if (c == chars[0]) b1 = true;
        }
        boolean b2 = false;
        for (char c : valid_letters) {
            if (c == chars[3]) b2 = true;
        }
        if (!(b1 && b2)) return false;

        // check bindestrich
        if (chars[2] != '-') return false;

        // if reached, move is valid format
        return true;
    }
    
    /**
     * Helper method checks whether char c is a number (and castable)
     * @param c -- input char to test
     * @return -- true if number else false
     */
    public static boolean charIsNumber (char c) {
        char[] array = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        for (char k : array) if (k == c) return true;
        return false;
    }

    /**
     * checks if a position is still in the board
     * @return true if in board else false
     */
    public static boolean isValidPosition (int row, int col) {
        return (row >= 0 && col >= 0 && row < Board.NROWS && col < Board.NCOLS);
    }
}
