package rs.contactbook.jsf.tablemodel;

import java.util.List;

import org.richfaces.model.DataProvider;
import org.richfaces.model.ExtendedTableDataModel;

import rs.os.contactbook.core.model.contact.Contact;

public class ContactTableModel {

	private ExtendedTableDataModel<Contact> dataModel;

	public ExtendedTableDataModel<Contact> getContactsDataModel(
			final List<Contact> contacts) {
		if (dataModel == null) {
			dataModel = new ExtendedTableDataModel<Contact>(
					new DataProvider<Contact>() {
						private static final long serialVersionUID = 7745843392679977372L;

						public Contact getItemByKey(Object key) {
							for (Contact contact : contacts) {
								if (key.equals(getKey(contact))) {
									return contact;
								}
							}
							return null;
						}

						public List<Contact> getItemsByRange(int firstRow,
								int endRow) {
							return contacts.subList(firstRow, endRow);
						}

						public Object getKey(Contact item) {
							return item.getId();
						}

						public int getRowCount() {
							return contacts.size();
						}

					});
		}
		return dataModel;
	}
}
