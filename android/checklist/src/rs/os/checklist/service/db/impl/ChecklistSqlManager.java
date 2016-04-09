package rs.os.checklist.service.db.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rs.os.checklist.model.Checklist;
import rs.os.checklist.model.Described;
import rs.os.checklist.model.Item;
import rs.os.checklist.model.Named;
import rs.os.checklist.model.Subject;
import rs.os.checklist.service.db.DbHelper;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * This class implement functionalities related to Checklist SQL queries and it is NOT TRANSACTIONAL
 * 
 * @author zgavrilovic
 * 
 */
public class ChecklistSqlManager {
	private DBMappings dbMappings;

	public ChecklistSqlManager() {
		dbMappings = new DBMappings();
	}

	/**
	 * 
	 * @param db connection
	 * @return list of checklist->item->subject(without category) 
	 */
	public List<Checklist> getChecklists(SQLiteDatabase db) {
		Cursor checklistsCursor = db.query(DbHelper.TBL_CHECKLIST, null, null,
				null, null, null, Named.PROP_NAME + " ASC");
		return extractChecklistFull(db, checklistsCursor);
	}
	
	/**
	 * 
	 * @param subjectId is id of the subject
	 * @param db connection
	 * @return list of checklist(without items)
	 */
	public List<Checklist> getChecklists(final Long subjectId, SQLiteDatabase db) {
		List<Checklist> checklists = new ArrayList<Checklist>();
		Cursor itemsCursor = db.query(DbHelper.TBL_CHECKLIST_ITEM, null,
				Item.PROP_SUBJECT + "=?", new String[] { String.valueOf(subjectId)}, null, null, null);
		itemsCursor.moveToFirst();
		while (itemsCursor.isAfterLast() == false) {
			Long checklistId = itemsCursor.getLong(1);
			checklists.add(getChecklist(checklistId, db));
			itemsCursor.moveToNext();
		}
		itemsCursor.close();
		return checklists;
	}

	/**
	 * 
	 * @param checklistName is the name of the checklist
	 * @param db connection
	 * @return checklist->item->subject(without category) or null if there is no checklist with such name
 	 */
	public Checklist getChecklist(final String checklistName, SQLiteDatabase db) {
		Cursor checklistsCursor = db.query(DbHelper.TBL_CHECKLIST, null,
				Described.PROP_NAME + "=?", new String[] { checklistName }, null, null,
				null);
		List<Checklist> checklists = extractChecklistFull(db, checklistsCursor);
		Checklist checklist = null;
		if ((checklists != null) && (!checklists.isEmpty())) {
			checklist = checklists.get(0);
		}
		//checklistsCursor.close();
		return checklist;
	}
	
	/**
	 * 
	 * @param checklistId is the id of the checklist
	 * @param db connection
	 * @return checklist->item->subject(without category) or null if there is no checklist with such name
 	 */
	public Checklist getChecklist(final Long checklistId, SQLiteDatabase db) {
		Cursor checklistsCursor = db.query(DbHelper.TBL_CHECKLIST, null,
				Described.PROP_ID + "=?", new String[] { String.valueOf(checklistId) }, null, null,
				null);
		List<Checklist> checklists = extractChecklistFull(db, checklistsCursor);
		Checklist checklist = null;
		if ((checklists != null) && (!checklists.isEmpty())) {
			checklist = checklists.get(0);
		}
		//curChecklist.close();
		return checklist;
	}
	
	/**
	 * Insert checklist items and checklist into db. After return id of the checklist will be populated
	 * 
	 * @param db connection
	 * @param checklist to be inserted
	 * @return id of the inserted checklist
	 */
	public Long insertChecklist(SQLiteDatabase db, Checklist checklist) {
		// first insert checklist
		ContentValues checklistValues = dbMappings.checklistToContentValues(checklist);
		long checklistId = db.insertOrThrow(DbHelper.TBL_CHECKLIST, null, checklistValues);

		// then insert items for the checklist
		for (Item item : checklist.getItems()) {
			ContentValues itemValues = dbMappings.itemToContentValues(item);
			itemValues.put(Item.PROP_CHECKLIST, checklistId);
			db.insertOrThrow(DbHelper.TBL_CHECKLIST_ITEM, null, itemValues);
		}
		checklist.setId(checklistId);
		return checklistId;
	}
	
	/**
	 * Only checklist will be updated and no items will be updated 
	 * 
	 * @param checklist to be updated
	 * @param db connection
	 */
	public void updateChecklist(final Checklist checklist, SQLiteDatabase db) {
		ContentValues updateChecklist = dbMappings.checklistToContentValues(checklist);
		db.update(DbHelper.TBL_CHECKLIST, updateChecklist, Described.PROP_ID
				+ "=?", new String[] { Long.toString(checklist.getId()) });
	}

	/**
	 * Deletes checklist items and checklist 
	 * 
	 * @param checklist to be deleted(together with items)
	 * @param db connection
	 */
	public void deleteChecklist(final Checklist checklist, SQLiteDatabase db) {
		// because we can not relay on sqllite 'on delete' cascade restriction
		for (Item item : checklist.getItems()) {
			db.delete(DbHelper.TBL_CHECKLIST_ITEM, Described.PROP_ID
					+ "=?", new String[] { Long.toString(item.getId()) });
		}
		db.delete(DbHelper.TBL_CHECKLIST, Described.PROP_ID + "=?",
				new String[] { Long.toString(checklist.getId()) });
	}

	/**
	 * Insert item into db. After return id of the item will be populated
	 * 
	 * @param item to be inserted
	 * @param db connection
	 * @return id of the inserted item.
	 */
	public Long insertItem(final Item item, SQLiteDatabase db) {
		ContentValues itemValues = dbMappings.itemToContentValues(item);
		long itemId = db.insertOrThrow(DbHelper.TBL_CHECKLIST_ITEM, null, itemValues);
		item.setId(itemId);
		return itemId;
	}

	/**
	 * 
	 * @param db connection
	 * @param item to be updated. No related checklist or subject rows will be updated 
	 */
	public void updateItem(SQLiteDatabase db, Item item) {
		ContentValues itemValues = dbMappings.itemToContentValues(item);
		db.update(DbHelper.TBL_CHECKLIST_ITEM, itemValues,
				Described.PROP_ID + "=?",
				new String[] { Long.toString(item.getId()) });
	}

	/**
	 * 
	 * @param item to be deleted
	 * @param db connection
	 */
	public void deleteItem(final Item item, SQLiteDatabase db) {
		db.delete(DbHelper.TBL_CHECKLIST_ITEM, Described.PROP_ID + "=?",
				new String[] { Long.toString(item.getId()) });
	}

	/**
	 * 
	 * @param db connection
	 * @param checklistsCursor is already populated cursor with Checklist rows
	 * @return list of checklist->item->subject(without category) 
	 */
	private List<Checklist> extractChecklistFull(SQLiteDatabase db, Cursor checklistsCursor) {
		// take all subjects from db
		Cursor subjectsCursor = db.query(DbHelper.TBL_SUBJECT, null,
				null, null, null, null, null);
		Map<Long, Subject> subjects = new HashMap<Long, Subject>();
		subjectsCursor.moveToFirst();
		while (subjectsCursor.isAfterLast() == false) {
			Subject subject = dbMappings.toSubject(subjectsCursor);
			subjects.put(subject.getId(), subject);
			subjectsCursor.moveToNext();
		}
		subjectsCursor.close();

		// take the checklists from DB
		Map<Long, Checklist> checklists = new HashMap<Long, Checklist>();
		checklistsCursor.moveToFirst();
		while (checklistsCursor.isAfterLast() == false) {
			Checklist checklist = dbMappings.toChecklist(checklistsCursor);
			checklists.put(checklist.getId(), checklist);
			checklistsCursor.moveToNext();
		}
		checklistsCursor.close();

		// prepare id's of checklists in order to query items
		StringBuilder whereClause = new StringBuilder("SELECT * FROM ");
		whereClause.append(DbHelper.TBL_CHECKLIST_ITEM);
		if(!checklists.keySet().isEmpty()) {
			whereClause.append(" WHERE ");
			whereClause.append(Item.PROP_CHECKLIST);
			whereClause.append(" IN (");
			for (Long checklistId : checklists.keySet()) {
				whereClause.append(checklistId);
				whereClause.append(",");
			}
			whereClause.deleteCharAt(whereClause.length() - 1);
			whereClause.append(");");
		}

		Cursor itemsCursor = db.rawQuery(whereClause.toString(), null);
		itemsCursor.moveToFirst();
		while (itemsCursor.isAfterLast() == false) {
			Item item = dbMappings.toItem(itemsCursor);
			Checklist checklist = checklists.get(itemsCursor.getLong(1));
			if (checklist != null) {
				checklist.addItem(item);
			}
			Subject subject = subjects.get(itemsCursor.getLong(2));
			if (subject != null) {
				item.setSubject(subject);
			}
			itemsCursor.moveToNext();
		}
		itemsCursor.close();

		// prepare checklists to be returned in the list
		List<Checklist> checklistsList = new ArrayList<Checklist>();
		for (Checklist checklist : checklists.values()) {
			checklistsList.add(checklist);
		}

		return checklistsList;
	}

}