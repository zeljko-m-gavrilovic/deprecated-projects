package rs.os.checklist.service.json;

import org.json.JSONException;
import org.json.JSONObject;

import rs.os.checklist.model.Category;
import rs.os.checklist.model.Checklist;
import rs.os.checklist.model.Item;
import rs.os.checklist.model.Named;
import rs.os.checklist.model.Described;
import rs.os.checklist.model.Subject;

public class JSonMappings {

	public void toJsonChecklist(Described item, JSONObject jsonItem) throws JSONException {
		jsonItem.put(Described.PROP_NAME, item.getName());
		jsonItem.put(Described.PROP_DESCR, item.getDescription());
		jsonItem.put(Described.PROP_IMAGE_NAME, item.getImageName());
	}
	
	public void toJsonItem(Named named, JSONObject jsonItem) throws JSONException {
		jsonItem.put(Described.PROP_NAME, named.getName());
	}
	
	public void toNoun(JSONObject itemJson, Described item) throws JSONException {
		item.setName(itemJson.getString(Described.PROP_NAME));
		if (itemJson.has(Described.PROP_DESCR)) {
			item.setDescription(itemJson.getString(Described.PROP_DESCR));
		}
		if (itemJson.has(Described.PROP_IMAGE_NAME)) {
			String imageName = itemJson.getString(Described.PROP_IMAGE_NAME);
			if(!imageName.startsWith("content:")) {
				item.setImageName(imageName);
			}
		}
	}
	
	public void toNamed(JSONObject itemJson, Named item) throws JSONException {
		item.setName(itemJson.getString(Described.PROP_NAME));
	}

	public Category toCategory(JSONObject categoryJson) throws JSONException {
		Category category = new Category();
		toNoun(categoryJson, category);
		return category;
	}
	
	public Subject toSubject(JSONObject subjectJson) throws JSONException {
		Subject subject = new Subject();
		toNamed(subjectJson, subject);
		return subject;
	}
	
	public Checklist toChecklist(JSONObject checklistJson) throws JSONException {
		Checklist checklist = new Checklist();
		toNoun(checklistJson, checklist);
		return checklist;
	}

	public Item toItem(JSONObject itemJson) throws JSONException {
		Item item = new Item();
		toNamed(itemJson, item);
		return item;
	}

}