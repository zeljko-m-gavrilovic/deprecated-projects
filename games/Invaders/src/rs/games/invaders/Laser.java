package rs.games.invaders;

/**
 * Curso Básico de desarrollo de Juegos en Java - Invaders
 * 
 * (c) 2004 Planetalia S.L. - Todos los derechos reservados. Prohibida su reproducción
 * 
 * http://www.planetalia.com
 * 
 */


public class Laser extends Actor {
	protected static final int BULLET_SPEED=3;
	
	public Laser(Stage stage) {
		super(stage);
		setSpriteNames( new String[] {"disparo0.gif","disparo1.gif","disparo2.gif"});
		setFrameSpeed(10);
	}
	
	public void act() {
		super.act();
		y+=BULLET_SPEED;
		if (y > Stage.PLAY_HEIGHT)
		  remove();
	}
}