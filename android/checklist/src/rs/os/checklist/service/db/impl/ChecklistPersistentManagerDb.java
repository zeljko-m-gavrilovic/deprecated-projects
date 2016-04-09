package rs.os.checklist.service.db.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rs.os.checklist.R;
import rs.os.checklist.model.Category;
import rs.os.checklist.model.Checklist;
import rs.os.checklist.model.ImportResultsReport;
import rs.os.checklist.model.Item;
import rs.os.checklist.model.Subject;
import rs.os.checklist.service.ChecklistPersistentManager;
import rs.os.checklist.service.ServiceRegistry;
import rs.os.checklist.service.db.DbCallback;
import rs.os.checklist.service.db.DbHelper;
import rs.os.checklist.service.db.TransactionalDbTemplate;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class ChecklistPersistentManagerDb implements ChecklistPersistentManager {

	private static int counter;
	private ChecklistSqlManager checklistSqlManager;
	private CategorySqlManager categorySqlManager;
	private TransactionalDbTemplate transactionalDbManager;
	private Context context;
	
	public ChecklistPersistentManagerDb(Context context) {
		this.context = context;
		DbHelper checkListDBHelper = DbHelper.instance(context);
		transactionalDbManager = new TransactionalDbTemplate(checkListDBHelper);
		checklistSqlManager = ServiceRegistry.getChecklistSqlManager();
		categorySqlManager = ServiceRegistry.getCategorySqlManager();
	}
	
	private static Long getNextLong() {
		return new Long(counter++);
	}
	
	/*
	 * (non-Javadoc)
	 * @see rs.os.checklist.service.ChecklistPersistentManager#createUniqueName(java.lang.String)
	 */
	public String createUniqueName(final String checklistName) {
		return transactionalDbManager.doInTransaction(new DbCallback<String>() {
			
			@Override
			public String execute(SQLiteDatabase db) {
				return createUniqueNameNoTx(checklistName, db);
			}

		});
	}

	private String createUniqueNameNoTx(final String checklistName, SQLiteDatabase db) {
		String newName = checklistName;
		while (checklistSqlManager.getChecklist(newName, db) != null) {
			newName = checklistName + getNextLong();
		}
		return newName;
	}
	
	/*
	 * (non-Javadoc)
	 * @see rs.os.checklist.service.ChecklistPersistentManager#getChecklists()
	 */
	@Override
	public List<Checklist> getChecklists() {
		return transactionalDbManager.doInTransaction(new DbCallback<List<Checklist>>() {
			
			@Override
			public List<Checklist> execute(SQLiteDatabase db) {
				return checklistSqlManager.getChecklists(db);
			}

		});
	}
	
	/*
	 * (non-Javadoc)
	 * @see rs.os.checklist.service.ChecklistPersistentManager#getChecklist(java.lang.String)
	 */
	@Override
	public Checklist getChecklist(final String checklistName) {
		return transactionalDbManager.doInTransaction(new DbCallback<Checklist>() {
			
			@Override
			public Checklist execute(SQLiteDatabase db) {
				return checklistSqlManager.getChecklist(checklistName, db);
			}

		});
	}
	
	/*
	 * (non-Javadoc)
	 * @see rs.os.checklist.service.ChecklistPersistentManager#getChecklists(rs.os.checklist.model.Subject)
	 */
	@Override
	public List<Checklist> getChecklists(final Subject subject) {
		return transactionalDbManager.doInTransaction(new DbCallback<List<Checklist>>() {
			
			@Override
			public List<Checklist> execute(SQLiteDatabase db) {
				return checklistSqlManager.getChecklists(subject.getId(), db);
			}

		});
	}

	/*
	 * (non-Javadoc)
	 * @see rs.os.checklist.service.ChecklistPersistentManager#hasChecklist(java.lang.String)
	 */
	@Override
	public boolean hasChecklist(String checklistName) {
		return getChecklist(checklistName) != null;
	}

	
	/*
	 * (non-Javadoc)
	 * @see rs.os.checklist.service.ChecklistPersistentManager#addChecklist(rs.os.checklist.model.Checklist)
	 */
	@Override
	public Long addChecklist(final Checklist checklist) {
		return transactionalDbManager.doInTransaction(new DbCallback<Long>() {
			
			@Override
			public Long execute(SQLiteDatabase db) {
				return addCheckListNoTx(checklist, db);
			}
			
		});
		
	}
	
	@Override
	public Long addChecklistFromTemplate(final Checklist checklist) {
		return transactionalDbManager.doInTransaction(new DbCallback<Long>() {
			
			@Override
			public Long execute(SQLiteDatabase db) {
				addMissingSubjects(db, new ImportResultsReport(), checklist);
				return addCheckListNoTx(checklist, db);
			}
			
		});
	}

	/*
	 * (non-Javadoc)
	 * @see rs.os.checklist.service.ChecklistPersistentManager#addChecklists(java.util.List)
	 */
	@Override
	public void addChecklists(final List<Checklist> checklists) {
		transactionalDbManager.doInTransaction(new DbCallback<Void>() {
			
			@Override
			public Void execute(SQLiteDatabase db) {
				for (Checklist checklist : checklists) {
					addCheckListNoTx(checklist, db);
				}
				return null;
			}

		});
	}

	/*
	 * (non-Javadoc)
	 * @see rs.os.checklist.service.ChecklistPersistentManager#importChecklists(java.util.List)
	 */
	@Override
	public ImportResultsReport importChecklists(final List<Checklist> checklists) {
			return transactionalDbManager.doInTransaction(new DbCallback<ImportResultsReport>() {
			
			@Override
			public ImportResultsReport execute(SQLiteDatabase db) {
				// creaate unique name for the importing checklists
				for(Checklist checklist : checklists) {
					checklist.setName(createUniqueNameNoTx(checklist.getName(), db));
				}
				
				ImportResultsReport importResults = new ImportResultsReport();
				for (Checklist checklist : checklists) {
					addMissingSubjects(db, importResults, checklist);
					Long checklistId = checklistSqlManager.insertChecklist(db, checklist);
					checklist.setId(checklistId);
					importResults.addInserted(checklist);
				}
				return importResults;
			}

		});
	}

	private void addMissingSubjects(SQLiteDatabase db,
			ImportResultsReport importResults, Checklist checklist) {
		// find missing subjects (subjects which doesn't exist in DB)
		Set<Subject> missingSubjects = new HashSet<Subject>();
		for(Item item : checklist.getItems()) {
			Subject subjectFromItem = item.getSubject();
			Subject subjectFromDb = categorySqlManager.getSubject(subjectFromItem.getName(), db);
			if(subjectFromDb == null) {
				missingSubjects.add(subjectFromItem);
			} else {
				item.setSubject(subjectFromDb);
			}
		}
		if(!missingSubjects.isEmpty()) {
			// first create category
			Category category = new Category();
			category.setName(context.getString(R.string.uncategorized_name) + checklist.getName());
			category.setDescription(context.getString(R.string.uncategorized_desc));
			Long categoryId = categorySqlManager.insertCategory(db, category);
			category.setId(categoryId);
			
			for (Subject subject : missingSubjects) {
				// then create subject (name and checklist are already set)
				subject.setCategory(category);
				Long subjectId = categorySqlManager.insertSubject(subject, db);
				subject.setId(subjectId);
				importResults.addInserted(subject);
			}
		}
	}
	
	private Long addCheckListNoTx(final Checklist checklist, SQLiteDatabase db) {
		Long checklistId = checklistSqlManager.insertChecklist(db, checklist);
		return checklistId;
	}
	
	/*
	 * (non-Javadoc)
	 * @see rs.os.checklist.service.ChecklistPersistentManager#updateChecklist(rs.os.checklist.model.Checklist)
	 */
	@Override
	public void updateChecklist(final Checklist checklist) {
		transactionalDbManager.doInTransaction(new DbCallback<Void>() {
			
			@Override
			public Void execute(SQLiteDatabase db) {
				checklistSqlManager.updateChecklist(checklist, db);
				return null;
			}

		});
		
	}

	/*
	 * (non-Javadoc)
	 * @see rs.os.checklist.service.ChecklistPersistentManager#deleteChecklist(rs.os.checklist.model.Checklist)
	 */
	@Override
	public void deleteChecklist(final Checklist checklist) {
		transactionalDbManager.doInTransaction(new DbCallback<Void>() {
			
			@Override
			public Void execute(SQLiteDatabase db) {
				checklistSqlManager.deleteChecklist(checklist, db);
				return null;
			}
		});
		
	}

	/*
	 * (non-Javadoc)
	 * @see rs.os.checklist.service.ChecklistPersistentManager#addItem(rs.os.checklist.model.Item)
	 */
	@Override
	public Long addItem(final Item item) {
		return transactionalDbManager.doInTransaction(new DbCallback<Long>() {
			
			@Override
			public Long execute(SQLiteDatabase db) {
				return checklistSqlManager.insertItem(item, db);
			}

		});

	}
	
	/*
	 * (non-Javadoc)
	 * @see rs.os.checklist.service.ChecklistPersistentManager#updateItem(rs.os.checklist.model.Item)
	 */
	@Override
	public void updateItem(final Item item) {
		transactionalDbManager.doInTransaction(new DbCallback<Void>() {
			
			@Override
			public Void execute(SQLiteDatabase db) {
				checklistSqlManager.updateItem(db, item);
				return null;
			}
		});
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see rs.os.checklist.service.ChecklistPersistentManager#updateItems(java.util.List)
	 */
	@Override
	public void updateItems(final List<Item> items) {
		transactionalDbManager.doInTransaction(new DbCallback<Void>() {
			
			@Override
			public Void execute(SQLiteDatabase db) {
				for (Item item : items) {
					checklistSqlManager.updateItem(db, item);
				}
				return null;
			}

		});
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see rs.os.checklist.service.ChecklistPersistentManager#deleteItem(rs.os.checklist.model.Item)
	 */
	@Override
	public void deleteItem(final Item item) {
		transactionalDbManager.doInTransaction(new DbCallback<Void>() {
			
			@Override
			public Void execute(SQLiteDatabase db) {
				checklistSqlManager.deleteItem(item, db);
				return null;
			}

		});
		
	}	
}