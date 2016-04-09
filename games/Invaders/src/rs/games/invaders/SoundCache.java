
/**
 * Curso B�sico de desarrollo de Juegos en Java - Invaders
 * 
 * (c) 2004 Planetalia S.L. - Todos los derechos reservados. Prohibida su reproducci�n
 * 
 * http://www.planetalia.com
 * 
 */
package rs.games.invaders;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

public class SoundCache extends ResourceCache {
    protected Object loadResource(URL url) {
        return Applet.newAudioClip(url);

    }

    public AudioClip getAudioClip(String name) {
        return (AudioClip) getResource(name);
    }

    public void playSound(final String name) {
        new Thread(new Runnable() {
            public void run() {
                getAudioClip(name).play();
            }
        }).start();
    }

    public void loopSound(final String name) {
        new Thread(new Runnable() {
            public void run() {
                getAudioClip(name).loop();
            }
        }).start();
    }

}
