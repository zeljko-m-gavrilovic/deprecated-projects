package rs.os.checklist.service.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * Defines callback interface. Functionalities wich implement this interface could be sent to other methods 
 * which can wrap functionality done in this callback  
 * @author Gavrilovici
 *
 * @param <T> is return type of executed DB statement. It can be object, list of objects or whatever...
 */
public interface DbCallback<T> {

	/**
	 * 
	 * @param db is database
	 * @return can be whatever is appropriate, i.e object or list of objects...
	 */
	T execute(SQLiteDatabase db);
	
}