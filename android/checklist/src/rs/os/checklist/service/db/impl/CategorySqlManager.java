package rs.os.checklist.service.db.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rs.os.checklist.model.Category;
import rs.os.checklist.model.Described;
import rs.os.checklist.model.Named;
import rs.os.checklist.model.Subject;
import rs.os.checklist.service.db.DbHelper;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * This class implement functionalities related to Category SQL queries and it is NOT TRANSACTIONAL
 * @author zgavrilovic
 *
 */
public class CategorySqlManager {
	private DBMappings dbMappings;

	public CategorySqlManager() {
		dbMappings = new DBMappings();
	}

	/**
	 * 
	 * @param db connection
	 * @return list of category->subject(without category)
	 */
	public List<Category> getCategories(SQLiteDatabase db) {
		Cursor categoriesCursor = db.query(DbHelper.TBL_CATEGORY, null, null,
				null, null, null, Named.PROP_NAME + " ASC");
		return extractCategoryFull(db, categoriesCursor);
	}

	/**
	 * 
	 * @param categoryName is the name of the category
	 * @param db connection
	 * @return category->subject(without category)
	 */
	public Category getCategory(final String categoryName, SQLiteDatabase db) {
		Cursor categoriesCursor = db.query(DbHelper.TBL_CATEGORY, null,
				Described.PROP_NAME + "=?", new String[] { categoryName }, null, null,
				null);
		List<Category> categories = extractCategoryFull(db, categoriesCursor);
		Category category = null;
		if ((categories != null) && (!categories.isEmpty())) {
			category = categories.get(0);
		}
		//categoriesCursor.close();
		return category;
	}
	
	/**
	 * 
	 * @param categoryId is the id of the category
	 * @param db connection
	 * @return category->subject(without category)
	 */
	public Category getCategory(final Long categoryId, SQLiteDatabase db) {
		Cursor categoriesCursor = db.query(DbHelper.TBL_CATEGORY, null,
				Described.PROP_ID + "=?", new String[] { String.valueOf(categoryId) }, null, null,
				null);
		List<Category> categories = extractCategoryFull(db, categoriesCursor);
		Category category = null;
		if ((categories != null) && (!categories.isEmpty())) {
			category = categories.get(0);
		}
		//curCategory.close();
		return category;
	}

	/**
	 * Insert checklist into db. After return id of the category will be populated
	 * 
	 * @param db connection
	 * @param category to  be inserted
	 * @return id of the inserted category
	 */
	public Long insertCategory(SQLiteDatabase db, Category category) {
		// first insert category
		ContentValues categoryValues = dbMappings.categoryToContentValues(category);
		long categoryId = db.insertOrThrow(DbHelper.TBL_CATEGORY, null, categoryValues);
		category.setId(categoryId);
		//insertSubjects(db, category);
		return categoryId;
	}

	/**
	 * 
	 * @param db connection
	 * @param category which subjects will be added
	 */
	public void insertSubjects(SQLiteDatabase db, Category category) {
		// then insert subjects for the category
		for (Subject subject : category.getSubjects()) {
			// skip inserting subject if already exists in some other category
			/*if(getSubject(subject.getName(), db) != null) {
				break;
			}*/
			ContentValues subjectValues = dbMappings.subjectToContentValues(subject);
			subjectValues.put(Subject.PROP_CATEGORY, category.getId());
			try {
				long subjectId = db.insertOrThrow(DbHelper.TBL_SUBJECT, null, subjectValues);
				subject.setId(subjectId);
			} catch(SQLException sqle) {
				Log.d(getClass().getSimpleName(), "Exception inserting subject " + subject + 
						"from category " + category, sqle);
				throw sqle;
			}
		}
	}

	/**
	 * 
	 * @param category to be updated. Only category will be updated
	 * @param db connection
	 */
	public void updateCategory(final Category category, SQLiteDatabase db) {
		ContentValues categoryValues = dbMappings.categoryToContentValues(category);
		db.update(DbHelper.TBL_CATEGORY, categoryValues, Described.PROP_ID
				+ "=?", new String[] { Long.toString(category.getId()) });
	}

	/**
	 * 
	 * @param category to be deleted
	 * @param db connection
	 */
	public void deleteCategory(final Category category, SQLiteDatabase db) {
		// because we can not relay on sqllite 'on delete' cascade restriction
		for (Subject subject : category.getSubjects()) {
			db.delete(DbHelper.TBL_SUBJECT, Described.PROP_ID
					+ "=?", new String[] { Long.toString(subject.getId()) });
		}
		db.delete(DbHelper.TBL_CATEGORY, Described.PROP_ID + "=?",
				new String[] { Long.toString(category.getId()) });
	}

	/**
	 * 
	 * @param subjectName is the name of the subject
	 * @param db connection
	 * @return subject(without the category)
	 */
	public Subject getSubject(final String subjectName, SQLiteDatabase db) {
		Cursor subjectsCursor = db.query(DbHelper.TBL_SUBJECT, null,
				Described.PROP_NAME + "=?", new String[] { subjectName }, null, null, null);
		subjectsCursor.moveToFirst();
		Subject subject = null;
		if (subjectsCursor.isAfterLast() == false) {
			subject = dbMappings.toSubject(subjectsCursor);
			long categoryId = subjectsCursor.getLong(4);
			subject.setCategory(getCategory(categoryId, db));
		}
		subjectsCursor.close();
		return subject;
	}
	
	/**
	 * Insert subject into db. After return, id of the subject will be populated
	 * 
	 * @param subject to be inserted
	 * @param db connection
	 * @return
	 */
	public Long insertSubject(final Subject subject, SQLiteDatabase db) {
		ContentValues subjectValues = dbMappings.subjectToContentValues(subject);
		subjectValues.put(Subject.PROP_CATEGORY, subject.getCategory().getId());
		long subjectId = db.insertOrThrow(DbHelper.TBL_SUBJECT, null, subjectValues);
		subject.setId(subjectId);
		return subjectId;
	}

	/**
	 * Only subject will be updated
	 * 
	 * @param db connection
	 * @param subject to be updated
	 */
	public void updateSubject(SQLiteDatabase db, Subject subject) {
		ContentValues subjectValues = dbMappings.subjectToContentValues(subject);
		if (subject.getCategory() != null) {
			subjectValues.put(Subject.PROP_CATEGORY, subject.getCategory().getId());
		}
		db.update(DbHelper.TBL_SUBJECT, subjectValues,
				Described.PROP_ID + "=?",
				new String[] { Long.toString(subject.getId()) });
	}

	/**
	 * 
	 * @param subject to be deleted
	 * @param db connection
	 */
	public void deleteSubject(final Subject subject, SQLiteDatabase db) {
		db.delete(DbHelper.TBL_SUBJECT, Described.PROP_ID + "=?",
				new String[] { Long.toString(subject.getId()) });
	}

	/**
	 * 
	 * @param db
	 * @param categoriesCursor
	 * @return list of category->subject(without category)
	 */
	private List<Category> extractCategoryFull(SQLiteDatabase db, Cursor categoriesCursor) {
		// take the Categories from DB
		Map<Long, Category> categoriesMap = new HashMap<Long, Category>();
		categoriesCursor.moveToFirst();
		while (categoriesCursor.isAfterLast() == false) {
			Category category = dbMappings.toCategory(categoriesCursor);
			categoriesMap.put(category.getId(), category);
			categoriesCursor.moveToNext();
		}
		categoriesCursor.close();

		// prepare sql query for quering subjects
		StringBuilder whereClause = new StringBuilder("SELECT * FROM ");
		whereClause.append(DbHelper.TBL_SUBJECT);
		if(!categoriesMap.keySet().isEmpty()) {
			whereClause.append(" WHERE ");
			whereClause.append(Subject.PROP_CATEGORY);
			whereClause.append(" IN (");
			for (Long checklistId : categoriesMap.keySet()) {
				whereClause.append(checklistId);
				whereClause.append(",");
			}
			whereClause.deleteCharAt(whereClause.length() - 1);
			whereClause.append(");");
		}

		// take subjects related to categories we are working with
		Cursor subjectsCursor = db.rawQuery(whereClause.toString(), null);
		subjectsCursor.moveToFirst();
		while (subjectsCursor.isAfterLast() == false) {
			long categoryId = subjectsCursor.getLong(4);
			Category category = categoriesMap.get(categoryId);
			if (category != null) {
				Subject subject = dbMappings.toSubject(subjectsCursor);
				category.addSubject(subject);
			}
			subjectsCursor.moveToNext();
		}
		subjectsCursor.close();

		// prepare categories to be returned in the list
		List<Category> resultedCategories = new ArrayList<Category>();
		for (Category category : categoriesMap.values()) {
			resultedCategories.add(category);
		}

		Collections.sort(resultedCategories);
		return resultedCategories;
	}

}