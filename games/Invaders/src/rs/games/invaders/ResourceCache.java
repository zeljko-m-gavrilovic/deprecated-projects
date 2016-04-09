

/**
 * Curso Básico de desarrollo de Juegos en Java - Invaders
 * 
 * (c) 2004 Planetalia S.L. - Todos los derechos reservados. Prohibida su reproducción
 * 
 * http://www.planetalia.com
 * 
 */

package rs.games.invaders;

import java.net.URL;
import java.util.HashMap;

public abstract class ResourceCache {
	protected HashMap resources;
	
	public ResourceCache() {
	  resources = new HashMap();
	}
	
	protected Object loadResource(String name) {
		URL url=null;
		url = getClass().getClassLoader().getResource(name);
		return loadResource(url);
	}
	
	protected Object getResource(String name) {
		Object res = resources.get(name);
		if (res == null) {
			res = loadResource("res/"+name);
			resources.put(name,res);
		}
		return res;
	}
	
	protected abstract Object loadResource(URL url);

}

