package rs.games.tetris;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * This class represents universal abstract game mechanism which implements only
 * most abstract and common parts of every game so it can be easily reused for
 * some other game. It takes responsibilities of creating gui for both applet
 * and swing, starting game simulation, reacting on some events from keyboard
 * and etc.
 * 
 * @author Zeljko
 * 
 */
public abstract class GameMechanismAbs extends Applet implements Runnable,
		KeyListener {

	// Properties
    private static final long serialVersionUID = 2423496722317348183L;
    protected boolean gameOver;
	protected Thread runner = null;
	protected int animationInterval = Configurations.ANIMATION_INTERVAL;

	// Abstract methods which need to be implemented in subclasses

	/**
	 * Update all objects in the game
	 */
	abstract public void updateWorld();

	/**
	 * Paint all objects in the game
	 */
	abstract public void paint(Graphics g);

	// Methods

	/**
	 * Initialize swing components
	 */
	public void initSwing() {
		JFrame mainFrame = new JFrame(Configurations.TITLE);
		JPanel panel = (JPanel) mainFrame.getContentPane();

		panel.setBounds(0, 0, Configurations.APPLICATION_WIDTH,
				Configurations.APPLICATION_HEIGHT);
		panel.setPreferredSize(new Dimension(Configurations.APPLICATION_WIDTH,
				Configurations.APPLICATION_HEIGHT));
		panel.setLayout(null);

		mainFrame.setBounds(0, 0, Configurations.APPLICATION_WIDTH,
				Configurations.APPLICATION_HEIGHT);
		mainFrame.setPreferredSize(new Dimension(
				Configurations.APPLICATION_WIDTH,
				Configurations.APPLICATION_HEIGHT));

		mainFrame.setVisible(true);
		mainFrame.setResizable(false);
		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		panel.add(this);
		panel.setIgnoreRepaint(true);
		setBounds(0, 0, Configurations.APPLICATION_WIDTH,
				Configurations.APPLICATION_HEIGHT);
		setPreferredSize(new Dimension(Configurations.APPLICATION_WIDTH,
				Configurations.APPLICATION_HEIGHT));
		setFocusable(true);
		requestFocusInWindow();
		addKeyListener(this);
		mainFrame.pack();
	}

	@Override
	public void init() {
		setBounds(0, 0, Configurations.APPLICATION_WIDTH,
				Configurations.APPLICATION_HEIGHT);
		setPreferredSize(new Dimension(Configurations.APPLICATION_WIDTH,
				Configurations.APPLICATION_HEIGHT));
		addKeyListener(this);
	}

	@Override
	public void start() {
		if (runner == null) {
			runner = new Thread(this);
			runner.start();
		}
	}

	@Override
	public void stop() {
		runner = null;
	}

	public void run() {
		while (runner != null && !gameOver) {
			long startTime = System.currentTimeMillis();
			updateWorld();
			paintWorld();
			do {
				Thread.yield();
			} while (System.currentTimeMillis() - startTime < animationInterval);
		}
		if (gameOver == true) {
			paintGameOver();
		}
		if (runner == null) {
			paintPause();
		}
	}

	/**
	 * Updates all pieces of game
	 */
	public void paintWorld() {
		Graphics graphics = getGraphics();

		Image offscreenImage = createImage(Configurations.APPLICATION_WIDTH,
				Configurations.APPLICATION_HEIGHT);
		Graphics offscreenGraphics = offscreenImage.getGraphics();
		paintBackground(offscreenGraphics);
		paint(offscreenGraphics);

		graphics.drawImage(offscreenImage, 0, 0, this);
	}

	/**
	 * Paints background of the game to the screen
	 * 
	 * @param offscreenGraphics
	 */
	private void paintBackground(Graphics offscreenGraphics) {
		offscreenGraphics.setColor(Color.GRAY);
		offscreenGraphics.fillRect(0, 0, Configurations.APPLICATION_WIDTH,
				Configurations.APPLICATION_HEIGHT);
		offscreenGraphics.setColor(Color.RED);
		offscreenGraphics.drawLine(0, Configurations.PLAYGROUND_HEIGHT + 1,
				Configurations.APPLICATION_WIDTH,
				Configurations.PLAYGROUND_HEIGHT + 1);
	}

	/**
	 * Paint pause message while game is in paused state
	 */
	public void paintPause() {
		Graphics graphics = getGraphics();
		graphics.setColor(Color.WHITE);
		graphics.setFont(new Font("Arial", Font.BOLD, 20));
		graphics.drawString(Configurations.PAUSE,
				(Configurations.APPLICATION_WIDTH / 2) - 80,
				Configurations.APPLICATION_HEIGHT / 2);
		graphics.setFont(new Font("Arial", Font.BOLD, 12));
		graphics.drawString(Configurations.PRESS_C_KEY_TO_CONTINUE,
				(Configurations.APPLICATION_WIDTH / 2) - 80,
				Configurations.APPLICATION_HEIGHT / 2 + 20);
	}

	/**
	 * Paints game over message when game is over
	 */
	public void paintGameOver() {
		Graphics graphics = getGraphics();
		graphics.setColor(Color.WHITE);
		graphics.setFont(new Font("Arial", Font.BOLD, 20));
		graphics.drawString(Configurations.GAME_OVER,
				(Configurations.APPLICATION_WIDTH / 2) - 60,
				Configurations.APPLICATION_HEIGHT / 2);
	}

	/**
	 * Reacts on key events from keyboard. At he moment events like pause/continue
	 * the game is handled
	 */
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_P) {
			stop();
		}
		if (e.getKeyCode() == KeyEvent.VK_C) {
			start();
		}
	}

	/**
	 * Only needed to satisfy keyEventListener implementation
	 */
	public void keyReleased(KeyEvent e) {
	}

	/**
	 * Only needed to satisfy keyEventListener implementation
	 */
	public void keyTyped(KeyEvent e) {
	}

	// Getters and increment/decrement methods

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	public void decreaseAnimationInterval() {
		this.animationInterval = animationInterval - 10;
	}
}