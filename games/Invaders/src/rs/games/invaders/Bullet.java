package rs.games.invaders;

/**
 * Curso Básico de desarrollo de Juegos en Java - Invaders
 * 
 * (c) 2004 Planetalia S.L. - Todos los derechos reservados. Prohibida su reproducción
 * 
 * http://www.planetalia.com
 * 
 */


public class Bullet extends Actor {
	protected static final int BULLET_SPEED=10;
	
	public Bullet(Stage stage) {
		super(stage);
		setSpriteNames( new String[] {"misil.gif"});
	}
	
	public void act() {
		super.act();
		y-=BULLET_SPEED;
		if (y < 0)
		  remove();
	}
}
