package rs.games.tetris.figures;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import rs.games.tetris.Configurations;
import rs.games.tetris.Tetris;
import rs.games.tetris.exceptions.CollisionWithWalException;
import rs.games.tetris.exceptions.OutOfPlaygroundException;

/**
 * Represents abstraction over potential figure implementations. All common
 * methods and properties are extracted at this place. Class also extends
 * movable interface in order to explicitly declare that figure is movable by
 * keyboard.
 * 
 * @author Zeljko
 * 
 */
public abstract class Figure extends Movable4Side {

	// Properties
	
	protected final Logger log = Logger.getLogger(getClass());
	protected final Tetris tetris;
	protected final int numberOfRotation;
	protected final int boxSize;
	protected final Point position;
	protected final List<Brick> bricks;
	private final int dx;
	private final int dy;
	protected int rotation;

	/**
	 * Constructor. Figures knows about game itself because it uses wall,
	 * playground and other objects
	 * 
	 * @param tetris
	 *            represents the game
	 * @param color
	 *            is color of the figure bricks
	 * @param numberOfRotation
	 *            is number of rotation of the figure
	 * @param initPosition
	 *            is initial position on the screen
	 */
	public Figure(Tetris tetris, Color color, int numberOfRotationP,
			Point initPosition) {
		this.tetris = tetris;
		this.numberOfRotation = numberOfRotationP;
		this.position = new Point(initPosition.x, initPosition.y);
		this.boxSize = Configurations.BRICK_SIZE;
		this.dx = Configurations.BRICK_SIZE;
		this.dy = Configurations.BRICK_SIZE;

		bricks = new ArrayList<Brick>(4);
		bricks.add(new Brick(new Point(position), color));
		bricks.add(new Brick(new Point(position), color));
		bricks.add(new Brick(new Point(position), color));
		bricks.add(new Brick(new Point(position), color));
		rotate();
	}

	// Abstract methods

	/**
	 * Rotate figure. Position all bricks in the figure for current rotation
	 */
	abstract public void rotate();

	// Methods

	/**
	 * Factory method for figures. Used for random creating new figure instances
	 * using random number generator
	 */
	public static Figure createRandomFigure(Tetris tetris, Point initPoint) {
		Figure figure = null;
		switch (new Random().nextInt(4)) {
		case 0:
			figure = new BFigure(tetris, new Point(initPoint));
			break;
		case 1:
			figure = new GFigure(tetris, new Point(initPoint));
			break;
		case 2:
			figure = new IFigure(tetris, new Point(initPoint));
			break;
		case 3:
			figure = new TFigure(tetris, new Point(initPoint));
			break;
		default:
            throw new RuntimeException("Unexpected figure number, expected number ={0,1,2,3}");
		}
		tetris.getStats().increaseFiguresPassed();
		return figure;
	}

	/**
	 * Set next rotation figure
	 */
	public void nextRotation() {
		rotation = (rotation + 1) % numberOfRotation;
	}

	/**
	 * Translate figure (figure position and all children bricks) without
	 * checking and handling collisions. It forces moving figure to new place.
	 * 
	 * @param dx
	 *            is distance for x axis in related unit
	 * @param dy
	 *            is distance for y axis in related unit
	 */
	private void translate(int distanceX, int distanceY) {
		position.translate(distanceX, distanceY);
		for (Brick brick : bricks) {
			brick.translate(distanceX, distanceY);
		}
	}

	/**
	 * Public method for translating figure to new position
	 * 
	 * @param newPosition
	 *            is new absolute position for figure
	 * @return this figure set at the new position
	 */
	public Figure changePosition(Point newPosition) {
		translate(newPosition.x - position.x, newPosition.y - position.y);
		return this;
	}

	/**
	 * Move figure and take into consider collision detection. React on this
	 * event by undoing last move and forwarding exception for any further
	 * handling if it is needed.
	 * 
	 * @param distance
	 *            is dx, dy distances in related measuring units
	 * @throws OutOfPlaygroundException
	 *             is thrown when figure is out of allowed area (playground)
	 * @throws CollisionWithWalException
	 *             is thrown when figure colised with wall
	 */
	protected void move(int deltaX, int deltaY)
			throws OutOfPlaygroundException, CollisionWithWalException {
		translate(deltaX, deltaY);
		try {
			tetris.getPlayground().checkCollision(this);
			tetris.getWall().checkCollision(this);
		} catch (OutOfPlaygroundException oope) {
			translate(-deltaX, -deltaY);
			throw oope;
		} catch (CollisionWithWalException cwwe) {
			translate(-deltaX, -deltaY);
			throw cwwe;
		}

	}

	@Override
	public void moveLeft() {
		try {
			move(-dx, 0);
		} catch (Exception e) {
			log.debug("Can't move left");
		}
	}

	@Override
	public void moveRight() {
		try {
			move(dx, 0);
		} catch (Exception e) {
			log.debug("Can't move right");
		}
	}

	@Override
	public void moveDown() {
		try {
			move(0, dy);
		} catch (OutOfPlaygroundException e) {
			tetris.figureReachEnd();
		} catch (CollisionWithWalException e) {
			tetris.figureReachEnd();
			if (position.y == 0) {
				tetris.setGameOver(true);
			}

		}
		log.debug("moved down without any exception...");
	}

	/*
	 * This method adds handling rotation event
	 * 
	 * (non-Javadoc)
	 * 
	 * @see rs.games.tetris.figures.Movable4Side#keyPressed(java.awt.event.KeyEvent)
	 */
	public void keyPressed(KeyEvent e) {
		super.keyPressed(e);
		if (e.getKeyCode() == (KeyEvent.VK_SPACE)) {
			rotate();
		}
	}

	/**
	 * This method paints figure on graphics
	 * 
	 * @param g
	 *            is offscreen graphics
	 */
	public void paint(Graphics g) {
		for (Brick brick : bricks) {
			brick.paint(g);
		}
	}

	// Getters and setters

	public List<Brick> getBricks() {
		return bricks;
	}
}