package rs.os.checklist.service.db;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TransactionalDbTemplate {
	
	protected SQLiteOpenHelper dbSQLHelper;
	
	public TransactionalDbTemplate(SQLiteOpenHelper dbSQLHelper) {
		this.dbSQLHelper = dbSQLHelper;
	}

	public <T extends Object> T doInTransaction(DbCallback<T> callback) throws SQLException {
		SQLiteDatabase db = dbSQLHelper.getWritableDatabase();
		db.beginTransaction();
		try {
			T result = callback.execute(db);
			db.setTransactionSuccessful();
			return result;
		} catch (SQLException e) {
			Log.e(getClass().getName(), "Exception executing SQL command in transaction", e);
			throw e;
		} finally {
			db.endTransaction();
			db.close();
		}
	}
}