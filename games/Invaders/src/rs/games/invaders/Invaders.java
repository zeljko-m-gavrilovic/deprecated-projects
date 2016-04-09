package rs.games.invaders;

/**
 * Curso B�sico de desarrollo de Juegos en Java - Invaders
 * 
 * (c) 2004 Planetalia S.L. - Todos los derechos reservados. Prohibida su reproducci�n
 * 
 * http://www.planetalia.com
 * 
 */

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Invaders extends Canvas implements Stage, KeyListener {

	private BufferStrategy strategy;
	private long usedTime;

	private SpriteCache spriteCache;
	private SoundCache soundCache;
	private ArrayList actors;
	private Player player;
	private BufferedImage background, backgroundTile;
	private int backgroundY;

	private boolean gameEnded = false;

	public Invaders() {
		spriteCache = new SpriteCache();
		soundCache = new SoundCache();

		JFrame ventana = new JFrame("Invaders");
		JPanel panel = (JPanel) ventana.getContentPane();
		setBounds(0, 0, Stage.WIDTH, Stage.HEIGHT);
		panel.setPreferredSize(new Dimension(Stage.WIDTH, Stage.HEIGHT));
		panel.setLayout(null);
		panel.add(this);
		ventana.setBounds(0, 0, Stage.WIDTH, Stage.HEIGHT);
		ventana.setVisible(true);
		ventana.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		ventana.setResizable(false);
		createBufferStrategy(2);
		strategy = getBufferStrategy();
		requestFocus();
		addKeyListener(this);

		setIgnoreRepaint(true);

		BufferedImage cursor = spriteCache.createCompatible(10, 10,
				Transparency.BITMASK);
		Toolkit t = Toolkit.getDefaultToolkit();
		Cursor c = t.createCustomCursor(cursor, new Point(5, 5), "null");
		setCursor(c);
	}

	public void gameOver() {
		gameEnded = true;
	}

	public void initWorld() {
		actors = new ArrayList();
		for (int i = 0; i < 10; i++) {
			Monster m = new Monster(this);
			m.setX((int) (Math.random() * Stage.WIDTH));
			m.setY(i * 20);
			m.setVx((int) (Math.random() * 20 - 10));

			actors.add(m);
		}

		player = new Player(this);
		player.setX(Stage.WIDTH / 2);
		player.setY(Stage.PLAY_HEIGHT - 2 * player.getHeight());

		soundCache.loopSound("musica.wav");

		backgroundTile = spriteCache.getSprite("oceano.gif");
		background = spriteCache.createCompatible(Stage.WIDTH, Stage.HEIGHT
				+ backgroundTile.getHeight(), Transparency.OPAQUE);
		Graphics2D g = (Graphics2D) background.getGraphics();
		g.setPaint(new TexturePaint(backgroundTile, new Rectangle(0, 0,
				backgroundTile.getWidth(), backgroundTile.getHeight())));
		g.fillRect(0, 0, background.getWidth(), background.getHeight());
		backgroundY = backgroundTile.getHeight();

	}

	public void addActor(Actor a) {
		actors.add(a);
	}

	public Player getPlayer() {
		return player;
	}

	public void updateWorld() {
		int i = 0;
		while (i < actors.size()) {
			Actor m = (Actor) actors.get(i);
			if (m.isMarkedForRemoval()) {
				actors.remove(i);
			} else {
				m.act();
				i++;
			}
		}
		player.act();
	}

	public void checkCollisions() {
		Rectangle playerBounds = player.getBounds();
		for (int i = 0; i < actors.size(); i++) {
			Actor a1 = (Actor) actors.get(i);
			Rectangle r1 = a1.getBounds();
			if (r1.intersects(playerBounds)) {
				player.collision(a1);
				a1.collision(player);
			}
			for (int j = i + 1; j < actors.size(); j++) {
				Actor a2 = (Actor) actors.get(j);
				Rectangle r2 = a2.getBounds();
				if (r1.intersects(r2)) {
					a1.collision(a2);
					a2.collision(a1);
				}
			}
		}
	}

	public void paintShields(Graphics2D g) {
		g.setPaint(Color.red);
		g.fillRect(280, Stage.PLAY_HEIGHT, Player.MAX_SHIELDS, 30);
		g.setPaint(Color.blue);
		g.fillRect(280 + Player.MAX_SHIELDS - player.getShields(),
				Stage.PLAY_HEIGHT, player.getShields(), 30);
		g.setFont(new Font("Arial", Font.BOLD, 20));
		g.setPaint(Color.green);
		g.drawString("Shields", 170, Stage.PLAY_HEIGHT + 20);

	}

	public void paintScore(Graphics2D g) {
		g.setFont(new Font("Arial", Font.BOLD, 20));
		g.setPaint(Color.green);
		g.drawString("Score:", 20, Stage.PLAY_HEIGHT + 20);
		g.setPaint(Color.red);
		g.drawString(player.getScore() + "", 100, Stage.PLAY_HEIGHT + 20);
	}

	public void paintAmmo(Graphics2D g) {
		int xBase = 280 + Player.MAX_SHIELDS + 10;
		for (int i = 0; i < player.getClusterBombs(); i++) {
			BufferedImage bomb = spriteCache.getSprite("bombUL.gif");
			g.drawImage(bomb, xBase + i * bomb.getWidth(), Stage.PLAY_HEIGHT,
					this);
		}
	}

	public void paintfps(Graphics2D g) {
		g.setFont(new Font("Arial", Font.BOLD, 12));
		g.setColor(Color.white);
		if (usedTime > 0)
			g.drawString(String.valueOf(1000 / usedTime) + " fps",
					Stage.WIDTH - 50, Stage.PLAY_HEIGHT);
		else
			g.drawString("--- fps", Stage.WIDTH - 50, Stage.PLAY_HEIGHT);
	}

	public void paintStatus(Graphics2D g) {
		paintScore(g);
		paintShields(g);
		paintAmmo(g);
		paintfps(g);
	}

	public void paintWorld() {
		Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
		g.drawImage(background, 0, 0, Stage.WIDTH, Stage.HEIGHT, 0,
				backgroundY, Stage.WIDTH, backgroundY + Stage.HEIGHT, this);
		for (int i = 0; i < actors.size(); i++) {
			Actor m = (Actor) actors.get(i);
			m.paint(g);
		}
		player.paint(g);

		paintStatus(g);
		strategy.show();
	}

	public void paintGameOver() {
		Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
		g.setColor(Color.white);
		g.setFont(new Font("Arial", Font.BOLD, 20));
		g.drawString("GAME OVER", Stage.WIDTH / 2 - 50, Stage.HEIGHT / 2);
		strategy.show();
	}

	public SpriteCache getSpriteCache() {
		return spriteCache;
	}

	public SoundCache getSoundCache() {
		return soundCache;
	}

	public void keyPressed(KeyEvent e) {
		player.keyPressed(e);
	}

	public void keyReleased(KeyEvent e) {
		player.keyReleased(e);
	}

	public void keyTyped(KeyEvent e) {
	}

	public void game() {
		usedTime = 1000;
		initWorld();
		while (isVisible() && !gameEnded) {
			long startTime = System.currentTimeMillis();
			backgroundY--;
			if (backgroundY < 0)
				backgroundY = backgroundTile.getHeight();
			updateWorld();
			checkCollisions();
			paintWorld();
			usedTime = System.currentTimeMillis() - startTime;
			do {
				Thread.yield();
			} while (System.currentTimeMillis() - startTime < 17);
		}
		paintGameOver();
	}

	public static void main(String[] args) {
		Invaders inv = new Invaders();
		inv.game();
	}
}
