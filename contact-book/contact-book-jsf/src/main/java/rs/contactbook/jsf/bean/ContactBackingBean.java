package rs.contactbook.jsf.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.richfaces.model.ExtendedTableDataModel;
import org.richfaces.model.selection.Selection;
import org.richfaces.model.selection.SimpleSelection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import rs.contactbook.jsf.tablemodel.ContactTableModel;
import rs.os.contactbook.core.model.address.Address;
import rs.os.contactbook.core.model.address.Email;
import rs.os.contactbook.core.model.address.Phone;
import rs.os.contactbook.core.model.address.Website;
import rs.os.contactbook.core.model.address.Address.Type;
import rs.os.contactbook.core.model.common.Celebration;
import rs.os.contactbook.core.model.contact.Contact;
import rs.os.core.manager.db.EagerDbManagerIF;

@Component
@Scope("session")
public class ContactBackingBean {

	private Selection selection;
	private Object tableState;

	private List<Contact> contacts;
	private ExtendedTableDataModel<Contact> contactsDataModel;
	private List<Contact> selectedContacts;

	private Contact contact;
	private int currentRow;
	private EagerDbManagerIF eagerDbManager;
	private boolean dirtyDataOnGui;

	public ContactBackingBean() {
	}

	@Autowired
	public ContactBackingBean(
			@Qualifier("eagerDbManager") EagerDbManagerIF genericEagerDbManager) {
		this.eagerDbManager = genericEagerDbManager;
		contacts = new ArrayList<Contact>();
		contacts = eagerDbManager.getAll(Contact.class);
		selection = new SimpleSelection();
		selectedContacts = new ArrayList<Contact>();
		contact = new Contact();
	}

	public void takeSelection() {
		selectedContacts.clear();
		Iterator<Object> iterator = getSelection().getKeys();
		while (iterator.hasNext()) {
			Object key = iterator.next();
			Contact selectedContact = getContactsDataModel()
					.getObjectByKey(key);
			selectedContacts.add(selectedContact);
			contact = eagerDbManager.getEager(selectedContact.getClass(),
					selectedContact.getId());
		}
	}

	public ExtendedTableDataModel<Contact> getContactsDataModel() {
		boolean dataModelNotInitialized = contactsDataModel == null;
		if (dataModelNotInitialized || dirtyDataOnGui) {
			contactsDataModel = new ContactTableModel()
					.getContactsDataModel(contacts);
		}
		return contactsDataModel;
	}

	@Transactional
	public void store() {
		System.out.println("contact for storing " + contact);
		// System.out.println("contact already exists in DB ? " +
		// eagerDbManager.exists(contact.getClass(), contact.getId()));
		contact = eagerDbManager.merge(contact);
		dirtyDataOnGui = true;
	}

	public void storeAddress() {
		System.out.println("store address ");
	}

	public void storePhone() {
		System.out.println("store phone");
	}

	public void storeEmail() {
		System.out.println("store emaill");
	}

	public void storeWebsite() {
		System.out.println("store website");
	}

	public void storeCelebration() {
		System.out.println("store celebration");
	}

	public void delete() {
		eagerDbManager.remove(Contact.class, contact.getId());
		dirtyDataOnGui = true;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	// table properties
	public Object getTableState() {
		return tableState;
	}

	public void setTableState(Object tableState) {
		this.tableState = tableState;
	}

	public Selection getSelection() {
		return selection;
	}

	public void setSelection(Selection selection) {
		this.selection = selection;
	}

	public List<Contact> getSelectedContacts() {
		return selectedContacts;
	}

	public int getCurrentRow() {
		return currentRow;
	}

	public void setCurrentRow(int currentRow) {
		this.currentRow = currentRow;
	}

	public void addNewAddress() {
		contact.addAddress(new Address());
		setCurrentRow(contact.getAddresses().size() - 1);
	}

	public void addNewPhone() {
		contact.addPhone(new Phone());
		setCurrentRow(contact.getPhones().size() - 1);
	}

	public void addNewEmail() {
		contact.addEmail(new Email());
		setCurrentRow(contact.getEmails().size() - 1);
	}

	public void addNewWebsite() {
		contact.addWebsite(new Website());
		setCurrentRow(contact.getWebsites().size() - 1);
	}

	public void addNewCelebration() {
		contact.addCelebration(new Celebration());
		setCurrentRow(contact.getCelebrations().size() - 1);
	}

	public Map<String, Type> getAddressTypeChoices() {
		Map<String, Type> choices = new HashMap<String, Type>();
		for (Type type : Type.values()) {
			choices.put(type.toString(), type);
		}
		return choices;
	}

}