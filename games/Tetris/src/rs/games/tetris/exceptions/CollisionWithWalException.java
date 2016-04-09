package rs.games.tetris.exceptions;

/**
 * Exception is used to notify context that figure is in collision with wall and
 * probably figure is need at least to undo the last move
 * 
 * @author Zeljko
 * 
 */
public class CollisionWithWalException extends Exception {

	private static final long serialVersionUID = 806761597560016118L;
}
