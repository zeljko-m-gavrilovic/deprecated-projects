package rs.games.tetris.exceptions;

/**
 * Exception is used to notify context that figure is crossed over the playground
 * area and figure is probably need at least to undo last move
 * 
 * @author Zeljko
 * 
 */
public class OutOfPlaygroundException extends Exception {

	private static final long serialVersionUID = 7135055123712304455L;
}
