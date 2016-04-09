package rs.os.checklist.service;

import java.io.IOException;

import rs.os.checklist.service.db.impl.CategoryPersitentManagerDb;
import rs.os.checklist.service.db.impl.CategorySqlManager;
import rs.os.checklist.service.db.impl.ChecklistPersistentManagerDb;
import rs.os.checklist.service.db.impl.ChecklistSqlManager;
import rs.os.checklist.service.json.CategoryJsonManager;
import rs.os.checklist.service.json.ChecklistJsonManager;
import rs.os.checklist.utility.FileLogger;
import rs.os.checklist.utility.ImportExportUtility;
import android.content.Context;

/**
 * Central place where we can get all services from this application. 
 * Implementation of the service registry uses singleton pattern for all of the listed services 
 * as well as lazy creation of the services. This should reprepresent service factory.
 * 
 * @author zgavrilovic
 *
 */
public class ServiceRegistry {
	
	private static ChecklistPersistentManagerDb checklistDbManager;
	private static CategoryPersitentManagerDb categoryDbManager;
	private static ImportExportUtility importExportService;
	private static ChecklistJsonManager checklistJsonManager;
	private static CategoryJsonManager categoryJsonManager;
	private static ChecklistSqlManager checklistSqlManager;
	private static CategorySqlManager categorySqlManager;
	private static FileLogger fileLogger;
	
	
	public static ChecklistPersistentManager getChecklistDbManager(Context context){
		if (checklistDbManager == null) {
			checklistDbManager = new ChecklistPersistentManagerDb(context);
		}
		return checklistDbManager;
	}
	
	public static CategoryPersistentManager getCategoryDbManager(Context context){
		if (categoryDbManager == null) {
			categoryDbManager = new CategoryPersitentManagerDb(context);
		}
		return categoryDbManager;
	}
	
	public static ImportExportUtility getImportExportService(Context context) {
		if (importExportService == null) {
			importExportService = new ImportExportUtility(context);
		}
		return importExportService;
	}
	
	public static ChecklistJsonManager getChecklistJsonManager(){
		if (checklistJsonManager == null) {
			checklistJsonManager = new ChecklistJsonManager();
		}
		return checklistJsonManager;
	}
	
	public static CategoryJsonManager getCategoryJsonManager(){
		if (categoryJsonManager == null) {
			categoryJsonManager = new CategoryJsonManager();
		}
		return categoryJsonManager;
	}

	public static ChecklistSqlManager getChecklistSqlManager() {
		if (checklistSqlManager == null) {
			checklistSqlManager = new ChecklistSqlManager();
		}
		return checklistSqlManager;
	}

	public static CategorySqlManager getCategorySqlManager() {
		if (categorySqlManager == null) {
			categorySqlManager = new CategorySqlManager();
		}
		return categorySqlManager;
	}
	
	public static FileLogger getFileLogger() {
		if (fileLogger == null) {
			try {
				fileLogger = FileLogger.getInstance();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fileLogger;
	}
	
}