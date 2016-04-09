package rs.os.checklist.service.db;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import rs.os.checklist.R;
import rs.os.checklist.model.Category;
import rs.os.checklist.model.Described;
import rs.os.checklist.model.Item;
import rs.os.checklist.model.Subject;
import rs.os.checklist.service.ServiceRegistry;
import rs.os.checklist.service.db.impl.CategorySqlManager;
import android.app.AlertDialog;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Utility class which should easy the work with SQLite database 
 * @author zgavrilovic
 *
 */
public class DbHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "checklist";
	private static final int DATABASE_VERSION = 54;
	private static DbHelper instance;
	
	public static final String TBL_CATEGORY = "tbl_category";
	public static final String TBL_SUBJECT = "tbl_subject";
	
	public static final String TBL_CHECKLIST = "tbl_checklist";
	public static final String TBL_CHECKLIST_ITEM = "tbl_checklist_item";
	
	private final String TABLE_CATEGORY =
    	"CREATE TABLE " + TBL_CATEGORY + " ("
    	+ Described.PROP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
		+ Described.PROP_NAME + " TEXT UNIQUE NOT NULL,"
		+ Described.PROP_DESCR + " TEXT,"
		+ Described.PROP_IMAGE_NAME + " TEXT);";

	private final String TABLE_SUBJECT = 
    	"CREATE TABLE " + TBL_SUBJECT + " ("
    	+ Described.PROP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
		+ Described.PROP_NAME + " TEXT UNIQUE NOT NULL,"
		+ Described.PROP_DESCR + " TEXT,"
		+ Described.PROP_IMAGE_NAME + " TEXT,"
    	+ Subject.PROP_CATEGORY + " INTEGER NOT NULL REFERENCES " + TBL_CATEGORY +"(" + Described.PROP_ID + ")"
    	+ ");";
	
	private final String TABLE_CHECKLIST =
    	"CREATE TABLE " + TBL_CHECKLIST + " ("
    	+ Described.PROP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
		+ Described.PROP_NAME + " TEXT UNIQUE NOT NULL,"
		+ Described.PROP_DESCR + " TEXT,"
		+ Described.PROP_IMAGE_NAME + " TEXT);";
	
	private final String TABLE_CHECKLIST_ITEM =
    	"CREATE TABLE " + TBL_CHECKLIST_ITEM + " ("
    	+ Described.PROP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
		+ Item.PROP_CHECKLIST + " INTEGER NOT NULL,"
		+ Item.PROP_SUBJECT + " INTEGER NOT NULL,"
		+ Item.PROP_CHECKED + " INTEGER);";

	// Foreign key constraint doesn't work so we need triggers
	
	private final String TRIGGER_SUBJECT_CATEGORYFK = 
		"CREATE TRIGGER fk_subject_categoryid " +
	    "BEFORE INSERT ON "+ TBL_SUBJECT +
	    " FOR EACH ROW BEGIN "+
	    "SELECT CASE WHEN (" +
	    "	(SELECT " + Described.PROP_ID + " FROM " + TBL_CATEGORY +
	    "	WHERE "+ Described.PROP_ID + "=new." + Subject.PROP_CATEGORY + " ) IS NULL)"+
	    "THEN RAISE (ABORT,'Foreign Key Violation') END;"+
	    "END;";
	
	private final String TRIGGER_CHECKLIST_ITEM_CHECKLISTFK = 
		"CREATE TRIGGER fk_checklist_item_checklistid " +
	    "BEFORE INSERT ON "+ TBL_CHECKLIST_ITEM +
	    " FOR EACH ROW BEGIN "+
	    "SELECT CASE WHEN (" +
	    "	(SELECT " + Described.PROP_ID + " FROM " + TBL_CHECKLIST +
	    "	WHERE "+ Described.PROP_ID + "=new." + Item.PROP_CHECKLIST + " ) IS NULL)"+
	    "THEN RAISE (ABORT,'Foreign Key Violation') END;"+
	    "END;";
	
	private final String TRIGGER_CHECKLIST_ITEM_SUBJECTFK = 
		"CREATE TRIGGER fk_checklist_item_subjectid " +
	    "BEFORE INSERT ON "+ TBL_CHECKLIST_ITEM +
	    " FOR EACH ROW BEGIN "+
	    "SELECT CASE WHEN (" +
	    "	(SELECT " + Described.PROP_ID + " FROM " + TBL_SUBJECT +
	    "	WHERE "+ Described.PROP_ID + "=new." + Item.PROP_SUBJECT + " ) IS NULL)"+
	    "THEN RAISE (ABORT,'Foreign Key Violation') END;"+
	    "END;";

	public static DbHelper instance(Context context) {
		if (instance == null) {
			instance = new DbHelper(context);
			//ServiceRegistry.getFileLogger().logInfo("db helper created");
		}
		return instance;
		
	}
	
	private Context context;
	
	// prevent instantiation
	private DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
    }

    /*
     * (non-Javadoc)
     * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
    	db.execSQL(TABLE_CATEGORY);
        db.execSQL(TABLE_SUBJECT);
        db.execSQL(TABLE_CHECKLIST);
        db.execSQL(TABLE_CHECKLIST_ITEM);
        db.execSQL(TRIGGER_SUBJECT_CATEGORYFK);
        db.execSQL(TRIGGER_CHECKLIST_ITEM_CHECKLISTFK);
        db.execSQL(TRIGGER_CHECKLIST_ITEM_SUBJECTFK);
        loadPredefinedCategories(db);
    }
    
    // NOTE: This method is not transactional so it can be a problem if something breaks in the 
    // middle of the process of inserting data into DB !!! 
    private void loadPredefinedCategories(SQLiteDatabase db) {
    	CategorySqlManager categoryDbQueries = ServiceRegistry.getCategorySqlManager();
    	List<Category> categories = parseJsonCategories();
    	for(Category category : categories) {
    		try {
    			categoryDbQueries.insertCategory(db, category);
    			categoryDbQueries.insertSubjects(db, category);
    			
    		} catch(SQLException sqle) {
    			Log.d(getClass().getSimpleName(), "Exception loading predefined categories. " +
    					"Cant insert category " + category, sqle);
    		}
    	}
    	
    }
    
    private List<Category> parseJsonCategories() {
		InputStream is = context.getResources().openRawResource(R.raw.categories);
		try {
			byte[] buffer = new byte[is.available()];
			is.read(buffer);
			String jsontext = new String(buffer);
			List<Category> categories = ServiceRegistry.getCategoryJsonManager().importCategoryList(jsontext);
			return categories;
		} catch (Exception e) {
			Log.e(getClass().getName(), context.getString(R.string.exceptionCreatingChecklistTemplates), e);
			new AlertDialog.Builder(context)
	        .setTitle(context.getString(R.string.error)).setMessage(R.string.exportAll)
	        .setPositiveButton(context.getString(R.string.ok), null).create().show();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		 Log.w(this.getClass().getName(), String.format("Upgrading database from version %1s to %2s"
                 + ", which will destroy all old data", oldVersion, newVersion));
		 db.execSQL("DROP TABLE IF EXISTS " + TBL_CATEGORY);
		 db.execSQL("DROP TABLE IF EXISTS " + TBL_SUBJECT);
		 db.execSQL("DROP TABLE IF EXISTS " + TBL_CHECKLIST);
		 db.execSQL("DROP TABLE IF EXISTS " + TBL_CHECKLIST_ITEM);
         onCreate(db);
	}
	
	@Override
	  public void onOpen(SQLiteDatabase db)
	  {
	    super.onOpen(db);
	    if (!db.isReadOnly())
	    {
	      // Enable foreign key constraints
	      db.execSQL("PRAGMA foreign_keys=ON;");
	    }
	  }

}