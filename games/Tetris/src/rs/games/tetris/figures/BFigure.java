package rs.games.tetris.figures;

import java.awt.Color;
import java.awt.Point;

import rs.games.tetris.Tetris;

/*  Positions:
 *
 *  0
 *  +-+-+
 *  +-+-+
 *  +-+-+ 0)
 *
 */

/**
 * One of implementation of figure. It has its own rotations and that makes this
 * game interesting. Also it has it's own color for bricks used in construction
 * of the figure.
 * 
 * @author Zeljko
 * 
 */
public class BFigure extends Figure {

	// Constructor

	public BFigure(Tetris tetris, Point initPosition) {
		super(tetris, Color.RED, 1, initPosition);
	}

	// Methods

	@Override
	public void rotate() {
		bricks.get(0).setLocation(position.x, position.y);
		bricks.get(1).setLocation(position.x + boxSize, position.y);
		bricks.get(2).setLocation(position.x, position.y + boxSize);
		bricks.get(3).setLocation(position.x + boxSize, position.y + boxSize);
	}
}