package rs.os.checklist.service.db.impl;

import java.util.List;

import rs.os.checklist.model.Category;
import rs.os.checklist.model.ImportResultsReport;
import rs.os.checklist.model.Subject;
import rs.os.checklist.service.CategoryPersistentManager;
import rs.os.checklist.service.ServiceRegistry;
import rs.os.checklist.service.db.DbCallback;
import rs.os.checklist.service.db.DbHelper;
import rs.os.checklist.service.db.TransactionalDbTemplate;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class CategoryPersitentManagerDb implements CategoryPersistentManager {
	private static int counter;
	private CategorySqlManager categoryDbQueries;
	private TransactionalDbTemplate transactionalDbManager;

	public CategoryPersitentManagerDb(Context context) {
		DbHelper checkListDBHelper = DbHelper.instance(context);
		transactionalDbManager = new TransactionalDbTemplate(checkListDBHelper);
		categoryDbQueries = ServiceRegistry.getCategorySqlManager();
	}

	private static Long getNextLong() {
		return new Long(counter++);
	}
	
	/*
	 * (non-Javadoc)
	 * @see rs.os.checklist.service.CategoryPersistentManager#createUniqueCategoryName(java.lang.String)
	 */
	public String createUniqueCategoryName(final String categoryName) {
		return transactionalDbManager.doInTransaction(new DbCallback<String>() {
			
			@Override
			public String execute(SQLiteDatabase db) {
				String newName = categoryName;
				while (categoryDbQueries.getCategory(newName, db) != null) {
					newName = categoryName + getNextLong();
				}
				return newName;
			}

		});
	}
	
	public String createUniqueSubjectName(final String subjectName) {
		return transactionalDbManager.doInTransaction(new DbCallback<String>() {
			
			@Override
			public String execute(SQLiteDatabase db) {
				String newName = subjectName;
				while (categoryDbQueries.getSubject(newName, db) != null) {
					newName = subjectName + getNextLong();
				}
				return newName;
			}

		});
	}
	
	/*
	 * (non-Javadoc)
	 * @see rs.os.checklist.service.CategoryPersistentManager#getCategories()
	 */
	@Override
	public List<Category> getCategories() {
		return transactionalDbManager.doInTransaction(new DbCallback<List<Category>>() {

			@Override
			public List<Category> execute(SQLiteDatabase db) {
				return categoryDbQueries.getCategories(db);
			}

		});
	}

	/*
	 * (non-Javadoc)
	 * @see rs.os.checklist.service.CategoryPersistentManager#getCategory(java.lang.String)
	 */
	@Override
	public Category getCategory(final String categoryName) {
		return transactionalDbManager.doInTransaction(new DbCallback<Category>() {
			
			@Override
			public Category execute(SQLiteDatabase db) {
				return categoryDbQueries.getCategory(categoryName, db);
			}

		});
	}

	/*
	 * (non-Javadoc)
	 * @see rs.os.checklist.service.CategoryPersistentManager#hasCategory(java.lang.String)
	 */
	@Override
	public boolean hasCategory(String categoryName) {
		return getCategory(categoryName) != null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see rs.os.checklist.service.CategoryPersistentManager#getSubject(java.lang.String)
	 */
	@Override
	public Subject getSubject(final String subjectName) {
		return transactionalDbManager.doInTransaction(new DbCallback<Subject>() {
			
			@Override
			public Subject execute(SQLiteDatabase db) {
				return categoryDbQueries.getSubject(subjectName, db);
			}

		});
	}

	/*
	 * (non-Javadoc)
	 * @see rs.os.checklist.service.CategoryPersistentManager#hasSubject(java.lang.String)
	 */
	@Override
	public boolean hasSubject(String subjectName) {
		return getSubject(subjectName) != null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see rs.os.checklist.service.CategoryPersistentManager#addCategory(rs.os.checklist.model.Category)
	 */
	@Override
	public Long addCategory(final Category category) {
		return transactionalDbManager.doInTransaction(new DbCallback<Long>() {
			
			@Override
			public Long execute(SQLiteDatabase db) {
				return categoryDbQueries.insertCategory(db, category);
			}
		});

	}

	/*
	 * (non-Javadoc)
	 * @see rs.os.checklist.service.CategoryPersistentManager#addCategories(java.util.List)
	 */
	@Override
	public void addCategories(final List<Category> categories) {
		transactionalDbManager.doInTransaction(new DbCallback<Void>() {
			
			@Override
			public Void execute(SQLiteDatabase db) {
				for (Category category : categories) {
					categoryDbQueries.insertCategory(db, category);
				}
				return null;
			}

		});

	}
	

	@Override
	public ImportResultsReport importCategories(final List<Category> categories) {
		return transactionalDbManager.doInTransaction(new DbCallback<ImportResultsReport>() {

			@Override
			public ImportResultsReport execute(SQLiteDatabase db) {
				ImportResultsReport importResults = new ImportResultsReport();
				for (Category category : categories) {
					Category persistedCategory = categoryDbQueries.getCategory(category.getName(), db);
					if (persistedCategory == null) {
						Long categoryId = categoryDbQueries.insertCategory(db, category);
						category.setId(categoryId);
						persistedCategory  = category;
						importResults.addInserted(category);
					}
					int addedSubjects = 0;
					for (Subject subject : category.getSubjects()) {
						Subject subjectFromDb = categoryDbQueries.getSubject(subject.getName(), db);
						if (subjectFromDb == null) {
							subject.setCategory(persistedCategory);
							categoryDbQueries.insertSubject(subject, db);
							importResults.addInserted(subject);
							addedSubjects++;
						} else {
							importResults.addIgnored(subject);
						}
					}
					if(addedSubjects > 0) {
						importResults.addUpdated(category);
					} else {
						importResults.addIgnored(category);
					}
				}
				return importResults;
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * @see rs.os.checklist.service.CategoryPersistentManager#updateCategory(rs.os.checklist.model.Category)
	 */
	@Override
	public void updateCategory(final Category category) {
		transactionalDbManager.doInTransaction(new DbCallback<Void>() {
			
			@Override
			public Void execute(SQLiteDatabase db) {
				categoryDbQueries.updateCategory(category, db);
				return null;
			}

		});

	}

	/*
	 * (non-Javadoc)
	 * @see rs.os.checklist.service.CategoryPersistentManager#deleteCategory(rs.os.checklist.model.Category)
	 */
	@Override
	public void deleteCategory(final Category category) {
		transactionalDbManager.doInTransaction(new DbCallback<Void>() {
			
			@Override
			public Void execute(SQLiteDatabase db) {
				categoryDbQueries.deleteCategory(category, db);
				return null;
			}
		});

	}

	/*
	 * (non-Javadoc)
	 * @see rs.os.checklist.service.CategoryPersistentManager#addSubject(rs.os.checklist.model.Subject)
	 */
	@Override
	public Long addSubject(final Subject subject) {
		return transactionalDbManager.doInTransaction(new DbCallback<Long>() {
			
			@Override
			public Long execute(SQLiteDatabase db) {
				return categoryDbQueries.insertSubject(subject, db);
			}

		});

	}

	/*
	 * (non-Javadoc)
	 * @see rs.os.checklist.service.CategoryPersistentManager#updateSubject(rs.os.checklist.model.Subject)
	 */
	@Override
	public void updateSubject(final Subject subject) {
		transactionalDbManager.doInTransaction(new DbCallback<Void>() {
		@Override
		public Void execute(SQLiteDatabase db) {
			categoryDbQueries.updateSubject(db, subject);
			return null;
		}
	});

	}

	/*
	 * (non-Javadoc)
	 * @see rs.os.checklist.service.CategoryPersistentManager#updateSubjects(java.util.List)
	 */
	@Override
	public void updateSubjects(final List<Subject> subjects) {
		transactionalDbManager.doInTransaction(new DbCallback<Void>() {
			
			@Override
			public Void execute(SQLiteDatabase db) {
				for (Subject subject : subjects) {
					categoryDbQueries.updateSubject(db, subject);
				}
				return null;
			}

		});

	}

	/*
	 * (non-Javadoc)
	 * @see rs.os.checklist.service.CategoryPersistentManager#deleteSubject(rs.os.checklist.model.Subject)
	 */
	@Override
	public void deleteSubject(final Subject subject) {
		transactionalDbManager.doInTransaction(new DbCallback<Void>() {
			
			@Override
			public Void execute(SQLiteDatabase db) {
				categoryDbQueries.deleteSubject(subject, db);
				return null;
			}

		});
	}

}