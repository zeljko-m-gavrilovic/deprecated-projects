/**
 * 
 */
package rs.games.tetris;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/**
 * 
 * Collect and keep statistics of interest in the game. Some of them are number
 * of figures passed, score etc. This class uses Tetris object to sped up the
 * game when number of passed figures achieves some points.
 * 
 * @author Zeljko
 * 
 */
public class Statistics {

	// Properties

	private long score;
	private long figuresPassed;
	private long scoreIncrement;
	private Tetris tetris;

	// Constructor

	public Statistics(Tetris tetris) {
		this.tetris = tetris;
		this.score = 0;
		this.figuresPassed = 0;
		this.scoreIncrement = 100;
	}

	/**
	 * Paints score to the screen
	 * 
	 * @param g
	 *            is offscreen image graphics
	 */
	public void paint(Graphics g) {
		g.setFont(new Font("Courier", Font.BOLD, 12));
		g.setColor(Color.GREEN);
		g.drawString("Figures:", 10, Configurations.PLAYGROUND_HEIGHT + 20);
		g.setColor(Color.RED);
		g.drawString(String.valueOf(figuresPassed), 140,
				Configurations.PLAYGROUND_HEIGHT + 20);

		g.setColor(Color.GREEN);
		g.drawString("Score:", 10, Configurations.PLAYGROUND_HEIGHT + 30);
		g.setColor(Color.RED);
		g.drawString(score + "", 140, Configurations.PLAYGROUND_HEIGHT + 30);

		g.setFont(new Font("Courier", Font.BOLD, 12));
		g.setColor(Color.GREEN);
		g.drawString("Pause - 'p' key ", 10,
				Configurations.PLAYGROUND_HEIGHT + 55);
		g.drawString("Continue - 'c' key", 10,
				Configurations.PLAYGROUND_HEIGHT + 65);
	}

	// Getters and increment methods

	public long getScore() {
		return score;
	}

	/**
	 * Increase score
	 */
	public void increaseScore() {
		score = score + scoreIncrement;
	}

	/**
	 * Get number of passed figures
	 * 
	 * @return
	 */
	public long getFiguresPassed() {
		return figuresPassed;
	}

	/**
	 * Increase number of passed figures and trigger decreasing the animation
	 * interval and gain the score increment
	 */
	public void increaseFiguresPassed() {
		figuresPassed++;
		if (((figuresPassed % 20) == 0)) {
			scoreIncrement = scoreIncrement + 50;
			if (tetris != null) {
				tetris.decreaseAnimationInterval();
			}
		}
	}
}