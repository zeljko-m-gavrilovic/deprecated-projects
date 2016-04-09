package rs.games.tetris.figures;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * This class reacts on key events. We need to track only key events which
 * represents moving left, right, up and down so this class adapts keyListener
 * to our need.
 * 
 * @author Zeljko
 * 
 */
public class Movable4Side extends KeyAdapter {

	// Methods

	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			moveLeft();
			break;
		case KeyEvent.VK_RIGHT:
			moveRight();
			break;
		case KeyEvent.VK_UP:
			moveUp();
			break;
		case KeyEvent.VK_DOWN:
			moveDown();
			break;
		}
	}

	/**
	 * Move left
	 */
	public void moveLeft() {
	}

	/**
	 * Move right
	 */
	public void moveRight() {
	}

	/**
	 * Move up
	 */
	public void moveUp() {
	}

	/**
	 * Move down
	 */
	public void moveDown() {
	}
}