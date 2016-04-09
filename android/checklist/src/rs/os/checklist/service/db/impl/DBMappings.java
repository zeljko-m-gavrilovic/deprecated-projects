package rs.os.checklist.service.db.impl;

import rs.os.checklist.model.Category;
import rs.os.checklist.model.Checklist;
import rs.os.checklist.model.Item;
import rs.os.checklist.model.Named;
import rs.os.checklist.model.Described;
import rs.os.checklist.model.Subject;
import android.content.ContentValues;
import android.database.Cursor;

public class DBMappings {

	// mapping from cursor to object
	public Category toCategory(Cursor curCategory) {
		return (Category) toNoun(new Category(), curCategory);
	}

	public Subject toSubject(Cursor curSubject) {
		return (Subject) toNamed(new Subject(), curSubject);
	}
	
	public Checklist toChecklist(Cursor curChecklist) {
		return (Checklist) toNoun(new Checklist(), curChecklist);
	}

	public Item toItem(Cursor curItem) {
		Item item = new Item();
		item.setId(curItem.getLong(0));
		boolean checked = false;
		int checkedInt = curItem.getInt(3);
		if (checkedInt == 1) {
			checked = true;
		}
		item.setChecked(checked);
		return item;
	}

	public Named toNamed(Named named, Cursor curItem) {
		named.setId(curItem.getLong(0));
		named.setName(curItem.getString(1));
		return named;
	}
	
	public Described toNoun(Described noun, Cursor curItem) {
		noun.setId(curItem.getLong(0));
		noun.setName(curItem.getString(1));
		noun.setDescription(curItem.getString(2));
		noun.setImageName(curItem.getString(3));
		return noun;
	}

	// mapping from object to cursor
	public ContentValues categoryToContentValues(Category Category) {
		return nounToContentValues(Category);
	}

	public ContentValues subjectToContentValues(Subject subject) {
		return nameToContentValues(subject);
	}

	public ContentValues checklistToContentValues(Checklist ckecklist) {
		return nounToContentValues(ckecklist);
	}

	public ContentValues itemToContentValues(Item item) {
		ContentValues itemValues = new ContentValues();
		itemValues.put(Item.PROP_CHECKLIST, item.getChecklist().getId());
		itemValues.put(Item.PROP_SUBJECT, item.getSubject().getId());
		itemValues.put(Item.PROP_CHECKED, item.isChecked());
		return itemValues;
	}
	
	public ContentValues nameToContentValues(Named named) {
		ContentValues contentValuesCategory = new ContentValues();
		contentValuesCategory.put(Named.PROP_NAME, named.getName());
		return contentValuesCategory;
	}
	
	public ContentValues nounToContentValues(Described noun) {
		ContentValues contentValuesCategory = new ContentValues();
		contentValuesCategory.put(Named.PROP_NAME, noun.getName());
		contentValuesCategory.put(Described.PROP_DESCR, noun.getDescription());
		contentValuesCategory.put(Described.PROP_IMAGE_NAME, noun.getImageName());
		return contentValuesCategory;
	}
	
}
