package rs.games.tetris;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;

import org.apache.log4j.Logger;

import rs.games.tetris.figures.Figure;
import rs.games.tetris.wall.Wall;

/**
 * Implements abstract game mechanism and all specific functionalities related
 * to this concrete game
 * 
 * @author Zeljko
 * 
 */
public class Tetris extends GameMechanismAbs {

	//Properties
	
	private static final long serialVersionUID = 3503306232219229569L;
	private final Logger log = Logger.getLogger(getClass());
	private Figure figure;
	private Figure nextFigure;
	private final Wall wall;
	private final Playground playground;
	private final Statistics stats;

	// Constructor

	public Tetris() {
		stats = new Statistics(this);
		wall = new Wall(stats);
		playground = new Playground(Configurations.PLAYGROUND_WIDTH,
				Configurations.PLAYGROUND_HEIGHT);
		figure = Figure.createRandomFigure(this, getFigureInitPoint());
		nextFigure = Figure.createRandomFigure(this, getNextFigureInitPoint());
	}

	/**
	 * Returns initial position for every figure created
	 * 
	 * @return initial position of figure at the screen
	 */
	private Point getFigureInitPoint() {
		int posX = (playground.width) / 2;
		if (posX % 10 == 5) {
			posX = posX - 5;
		}
		Point figureInitPoint = new Point(posX, 0);
		return figureInitPoint;
	}

	/**
	 * Returns position for next figure showed for preview purpose
	 * 
	 * @return next figure position at the screen
	 */
	private Point getNextFigureInitPoint() {
		int posX = Configurations.PLAYGROUND_WIDTH + 10;
		int posY = Configurations.PLAYGROUND_HEIGHT + 10;
		Point nextFigureInitPoint = new Point(posX, posY);
		return nextFigureInitPoint;
	}

	// Methods

	@Override
	public void updateWorld() {
		figure.moveDown();
	}

	/**
	 * Paints all screen compound objects to screen
	 */
	public void paint(Graphics g) {
		playground.paint(g);
		wall.paint(g);
		figure.paint(g);
		nextFigure.paint(g);
		stats.paint(g);
	}

	/**
	 * When figure reaches the end, actual figure is putted into wall, previous
	 * next figure from preview is set to be the actual figure and new figure is
	 * created and set as next figure for preview purpose
	 */
	public void figureReachEnd() {
		wall.putIntoWall(figure);
		figure = nextFigure.changePosition(getFigureInitPoint());
		nextFigure = Figure.createRandomFigure(this, getNextFigureInitPoint());
		log.debug("Figure reached the end");
	}

	@Override
	public void keyPressed(KeyEvent e) {
		super.keyPressed(e);
		if (!gameOver) {
			figure.keyPressed(e);
		}
	}

	// Getters and setters
	
	public Figure getFigure() {
		return figure;
	}

	public Wall getWall() {
		return wall;
	}

	public Playground getPlayground() {
		return playground;
	}

	public Statistics getStats() {
		return stats;
	}

	// helpers methods

	public static void main(String[] args) {
		Tetris tetris = new Tetris();
		tetris.initSwing();
		tetris.start();
	}
}