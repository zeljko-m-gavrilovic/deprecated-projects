package rs.games.tetris.figures;

import java.awt.Color;
import java.awt.Point;

import rs.games.tetris.Tetris;

/*  Positions:
 *
 *  0		   0			0		  0
 *  +-+        +-+          +-+    	
 *  +-+-+    +-+-+-+      +-+-+    	+-+-+-+
 *  +-+-+    +-+-+-+      +-+-+     +-+-+-+
 *  +-+   0) 		 1)  	+-+ 2)    +-+     3)
 *
 */

/**
 * One of implementation of figure. It has its own rotations and that makes this
 * game interesting. Also it has it's own color for bricks used in construction
 * of the figure.
 */
public class TFigure extends Figure {

	// Constructor

	public TFigure(Tetris tetris, Point initPosition) {
		super(tetris, Color.BLUE, 4, initPosition);
	}

	// Methods

	@Override
	public void rotate() {
		switch (rotation) {
		case 0:
			bricks.get(0).setLocation(position.x, position.y);
			bricks.get(1).setLocation(position.x, position.y + boxSize);
			bricks.get(2).setLocation(position.x + boxSize,
					position.y + boxSize);
			bricks.get(3).setLocation(position.x, position.y + (2 * boxSize));
			break;
		case 1:
			bricks.get(0).setLocation(position.x, position.y);
			bricks.get(1).setLocation(position.x - boxSize,
					position.y + boxSize);
			bricks.get(2).setLocation(position.x, position.y + boxSize);
			bricks.get(3).setLocation(position.x + boxSize,
					position.y + boxSize);
			break;
		case 2:
			bricks.get(0).setLocation(position.x, position.y);
			bricks.get(1).setLocation(position.x - boxSize,
					position.y + boxSize);
			bricks.get(2).setLocation(position.x, position.y + boxSize);
			bricks.get(3).setLocation(position.x, position.y + (2 * boxSize));
			break;
		case 3:
			bricks.get(0).setLocation(position.x - boxSize,
					position.y + boxSize);
			bricks.get(1).setLocation(position.x, position.y + boxSize);
			bricks.get(2).setLocation(position.x + boxSize,
					position.y + boxSize);
			bricks.get(3).setLocation(position.x, position.y + (2 * boxSize));
			break;
		default:
            throw new IllegalArgumentException("Number of rotations outsteped");
		}
		nextRotation();
	}
}