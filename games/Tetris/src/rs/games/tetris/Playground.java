package rs.games.tetris;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import org.apache.log4j.Logger;

import rs.games.tetris.exceptions.OutOfPlaygroundException;
import rs.games.tetris.figures.Brick;
import rs.games.tetris.figures.Figure;

/**
 * Represents area of the screen where figures can be moved. If moving figures
 * cross over this allowed area, collision is detected and adequate exception is
 * throwed to notify collision detection.
 * 
 * @author Zeljko
 * 
 */
public class Playground extends Rectangle {

	private static final long serialVersionUID = 384921931803874968L;
	protected final Logger log = Logger.getLogger(getClass());

	// Constructor

	public Playground(int width, int height) {
		super(width, height);
	}

	// Methods

	/**
	 * Paints playground on the screen
	 * 
	 * @param g
	 *            is offscreen image graphics
	 */
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);
	}

	/**
	 * Checks collision of figure and playground
	 * 
	 * @param figure
	 *            to be checked for collision
	 * @throws OutOfPlaygroundException
	 *             is thrown if figure is in collision with playground
	 */
	public void checkCollision(Figure figure) throws OutOfPlaygroundException {
		for (Brick brick : figure.getBricks()) {
			boolean overLeft = brick.x < 0;
			boolean overRight = brick.x > (this.width - Configurations.BRICK_SIZE);
			boolean overUp = brick.y < 0;
			boolean overDown = brick.y > (this.height - Configurations.BRICK_SIZE);
			if (overLeft || overRight || overUp || overDown) {
			    log.debug("figure is out of playground... OutOfPlaygroundException");
				throw new OutOfPlaygroundException();
			}
		}
	}
}