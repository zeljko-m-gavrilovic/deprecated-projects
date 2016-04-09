package rs.os.checklist.service.json;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import rs.os.checklist.model.Category;
import rs.os.checklist.model.Checklist;
import rs.os.checklist.model.Item;
import rs.os.checklist.model.Subject;

/**
 * Parse checklist and item objects to JSON format string and vice verse
 * @author zgavrilovic
 *
 */
public class ChecklistJsonManager {

	private JSonMappings jSonMappings;

	public ChecklistJsonManager() {
		jSonMappings = new JSonMappings();
	}
	
	public String exportChecklists(List<Checklist> checklists) throws JSONException {
		JSONArray jsonChecklists = new JSONArray();
		for (Checklist checklist : checklists) {
			jsonChecklists.put(exportChecklist(checklist));
		}
		return jsonChecklists.toString();
	}

	public List<Checklist> importChecklists(String jsontext) throws JSONException {
		// following line of code which handle jSon string we need because without of it in some cases name and 
		// description fields (don't know why) have blank space like following " name" : "someName" which cause 
		// this functionality to throw exception.
		String processedjsoString = jsontext.replace("\" ", "\""); 
		JSONArray entries = new JSONArray(processedjsoString);
		List<Checklist> checklists = new ArrayList<Checklist>();
		for (int i = 0; i < entries.length(); i++) {
			JSONObject checklistJSon = entries.getJSONObject(i);
			checklists.add(importChecklist(checklistJSon));
		}
		return checklists;
	}

	public JSONObject exportChecklist(Checklist checklist) throws JSONException {
		JSONObject jsonChecklist = new JSONObject();
		jSonMappings.toJsonChecklist(checklist, jsonChecklist);
		if(checklist.getItems().size() > 0) {
			JSONArray jsonSubjects = new JSONArray();
			for (Item item : checklist.getItems()) {
				JSONObject jsonSubject = new JSONObject();
				jSonMappings.toJsonItem(item.getSubject(), jsonSubject);
				jsonSubjects.put(jsonSubject);
			}
			jsonChecklist.put(Category.PROP_SUBJECTS, jsonSubjects);
		}
		return jsonChecklist;
	}

	private Checklist importChecklist(JSONObject checklistJSon) throws JSONException {
		Checklist checklist = jSonMappings.toChecklist(checklistJSon);
		if (checklistJSon.has(Category.PROP_SUBJECTS)) {
			JSONArray subjects = checklistJSon.getJSONArray(Category.PROP_SUBJECTS);
			for (int j = 0; j < subjects.length(); j++) {
				JSONObject subjectJson = subjects.getJSONObject(j);
				Subject subject = jSonMappings.toSubject(subjectJson);
				Item item = new Item();
				item.setChecklist(checklist);	
				item.setSubject(subject);
				checklist.addItem(item);
			}
		}
		return checklist;
	}

}