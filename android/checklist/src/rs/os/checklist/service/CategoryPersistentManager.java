package rs.os.checklist.service;

import java.util.List;

import rs.os.checklist.model.Category;
import rs.os.checklist.model.ImportResultsReport;
import rs.os.checklist.model.Subject;

/**
 * Defines functionalities for contract and subject objects related to database
 * Implementation of this interface MUST BE TRANSACTIONAL
 * 
 * @author zgavrilovic
 *
 */
public interface CategoryPersistentManager {
	
	/**
	 * 
	 * @param categoryName initial try for unique name
	 * @return unique name for this persitent object. Created from category and appropriate sufix if needed
	 */
	String createUniqueCategoryName(String categoryName);
	
	/**
	 * 
	 * @param subjectName initial try for unique name
	 * @return unique name for this persitent object. Created from subject and appropriate sufix if needed
	 */
	String createUniqueSubjectName(String subjectName);
	
	/**
	 * 
	 * @return list of category->subject(without category)
	 */
	List<Category> getCategories();

	/**
	 * 
	 * @param categoryName is the name of the category
	 * @return category->subject(without category)
	 */
	Category getCategory(String categoryName);

	/**
	 * 
	 * @param categoryName is the name of the category
	 * @return true if there is category with such name or false if ther is not
	 */
	boolean hasCategory(String categoryName);
	
	/**
	 * 
	 * @param category to be added to DB
	 * @return id of the category. Category id will be also set in parameter object category 
	 */
	Long addCategory(Category category);

	/**
	 * 
	 * @param categories is the list of the categories to be added to DB 
	 */
	void addCategories(List<Category> categories);
	
	/**
	 * If category with such name doesn exist it will be created and stored. Subjects will be then added to this category
	 * (and stored).
	 * If importing category has subjects which doesn't exist, missing subjects will be added to this category(and stored).
	 * If importing category has subjects which already exist then such subject will be just ignored
	 * 
	 * @param categories to be imported
	 * @return report with info about which categories and subjects are inserted, updated or ignored
	 */
	ImportResultsReport importCategories(List<Category> categories);

	/**
	 * 
	 * @param category to be updated. Only category properties will be updated. 
	 * Nothing with subjects will be touched
	 */
	void updateCategory(Category category);

	/**
	 * 
	 * @param category to be deleted
	 */
	void deleteCategory(Category category);

	/**
	 * 
	 * @param subjectName is the name of the subject
	 * @return subject with such name
	 */
	Subject getSubject(String subjectName);
	
	/**
	 * 
	 * @param subjectName is the name of the subject
	 * @return true if such subject exist or false if it doesn't exist
	 */
	boolean hasSubject(String subjectName);
	
	/**
	 * 
	 * @param subject to be added to DB. Suject id will be also set in parameter object subject 
	 * @return id of the subject. 
	 */
	Long addSubject(Subject subject);

	/**
	 * 
	 * @param subject to be updated.
	 */
	void updateSubject(Subject subject);

	/**
	 * 
	 * @param subject is list of subjects to be updated
	 */
	void updateSubjects(List<Subject> subject);

	/**
	 * 
	 * @param subject to be deleted
	 */
	void deleteSubject(Subject subject);
}