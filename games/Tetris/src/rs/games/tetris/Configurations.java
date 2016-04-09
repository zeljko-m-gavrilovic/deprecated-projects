package rs.games.tetris;

/**
 * Keeps configurations at one place. Class can be easily refactored in order
 * to read configurations from file instead of using constants in this way.
 * 
 * @author Zeljko
 * 
 */
public interface Configurations {
	public static final int APPLICATION_WIDTH = 250;
	public static final int APPLICATION_HEIGHT = 600;
	public static final int PLAYGROUND_WIDTH = 200;
	public static final int PLAYGROUND_HEIGHT = 500;
	public static final int ANIMATION_INTERVAL = 300;
	public static final int BRICK_SIZE = 10;
	public static final String TITLE = "Tetris";
	public static final String GAME_OVER = "GAME OVER";
	public static final String PAUSE = "PAUSE";
	public static final String PRESS_C_KEY_TO_CONTINUE = "Press 'c' key to continue";
}