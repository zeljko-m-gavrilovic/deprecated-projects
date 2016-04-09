package rs.os.checklist.service;

import java.util.List;

import rs.os.checklist.model.Checklist;
import rs.os.checklist.model.ImportResultsReport;
import rs.os.checklist.model.Item;
import rs.os.checklist.model.Subject;

/**
 * Defines functionalities for contract and subject objects related to database
 * Implementation of this interface MUST BE TRANSACTIONAL
 * 
 * @author zgavrilovic
 *
 */
public interface ChecklistPersistentManager {
	
	/**
	 * 
	 * @param checklistName initial try for unique name
	 * @return unique name for this persitent object. Created from checklistName and appropriate sufix if needed
	 */
	String createUniqueName(String checklistName);
	
	/**
	 * 
	 * @return list of checklist->item->subject(without category)
	 */
	List<Checklist> getChecklists();

	/**
	 * 
	 * @param checklistName is the name of the checklist
	 * @return checklist->item->subject(without category) or null if there is no checklist with such name
	 */
	Checklist getChecklist(String checklistName);
	
	/**
	 * 
	 * @param subject
	 * @return list of checklist(without items)
	 */
	List<Checklist> getChecklists(Subject subject);

	/**
	 * 
	 * @param checklistName is the name of the checklist
	 * @return true if there is checklist with such a name or false if there is not
	 */
	boolean hasChecklist(String checklistName);

	/**
	 * It is expected that all subjects from items must exist
	 * 
	 * @param checklist to be added to DB
	 * @return id of the checklist. Checklist id will be set in parameter object checklist
	 */
	Long addChecklist(Checklist checklist);
	
	/**
	 * Note: This method was added because if user selected to create checklist from template then some subjects might note
	 * be in the database and must be added before storing checklist 
	 *  
	 * @param checklist to be added to DB
	 * @return id of the checklist. Checklist id will be set in parameter object checklist
	 */
	Long addChecklistFromTemplate(Checklist checklist);

	/**
	 * 
	 * @param checklists is the list of the checklist
	 */
	void addChecklists(List<Checklist> checklists);
	
	/**
	 * If checklist with such name already exist, suffix will be added and checklist will be stored. 
	 * If importing checklist has subjects which dont exist here, new artifitial category for this checklist will be
	 * created(and stored) and missing subjects will be added to this category(and stored).
	 * 
	 * @param cheklists to be imported
	 * @return report with info about which checklists and subjects are inserted, updated or ignored
	 */
	ImportResultsReport importChecklists(List<Checklist> cheklists);

	/**
	 * 
	 * @param checklist to be updated. Only checklist fields will be updated
	 */
	void updateChecklist(Checklist checklist);

	/**
	 * 
	 * @param checklist to be deleted
	 */
	void deleteChecklist(Checklist checklist);

	/**
	 * 
	 * @param item to be added
	 * @return id of the Item. Item id will be set in parameter object item
	 */
	Long addItem(Item item);

	/**
	 * 
	 * @param item to be updated
	 */
	void updateItem(Item item);

	/**
	 * 
	 * @param items to be updated
	 */
	void updateItems(List<Item> items);

	/**
	 * 
	 * @param item to be deleted
	 */
	void deleteItem(Item item);
}