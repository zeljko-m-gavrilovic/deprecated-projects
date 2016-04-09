package rs.games.tetris.figures;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import rs.games.tetris.Configurations;

/**
 * Represents brick which we use to construct figure and wall
 * 
 * @author Zeljko
 * 
 */
public class Brick extends Rectangle {

	// Properties
	
	private static final long serialVersionUID = -1368289358229102578L;
	private final Color color;
	private final int boxSize;
	
	// Constructors
	
	public Brick(Point initPosition, Color color) {
		this.color = color;
		int boxSize = Configurations.BRICK_SIZE;
		setBounds(initPosition.x, initPosition.y, boxSize, boxSize);
		this.boxSize = boxSize;
	}

	// Methods

	/**
	 * This method paints figure on graphics
	 * 
	 * @param g
	 *            graphics
	 */
	public void paint(Graphics g) {
		g.setColor(color);
		g.fillRect(x, y, width, height);
		g.setColor(Color.BLACK);
		g.drawRect(x, y, width, height);
	}

	/**
	 * Brick is represented as box so this method returns boxsize
	 * @return box size
	 */
	public int getBoxSize() {
		return boxSize;
	}
}