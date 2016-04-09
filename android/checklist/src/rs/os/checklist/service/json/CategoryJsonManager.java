package rs.os.checklist.service.json;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import rs.os.checklist.model.Category;
import rs.os.checklist.model.Subject;

/**
 * Parse category and subject objects to JSON format string and vice verse
 * @author zgavrilovic
 *
 */
public class CategoryJsonManager {

	private JSonMappings jSonMappings;

	public CategoryJsonManager() {
		jSonMappings = new JSonMappings();
	}
	
	public String exportCategoryList(List<Category> categories) throws JSONException {
		JSONArray jsonChecklists = new JSONArray();
		for (Category category : categories) {
			jsonChecklists.put(exportCategory(category));
		}
		return jsonChecklists.toString();
	}

	public List<Category> importCategoryList(String jsontext) throws JSONException {
		// following line of code which handle jSon string we need because without of it in some cases name and 
		// description fields (don't know why) have blank space like following " name" : "someName" which cause 
		// this functionality to throw exception.
		String processedjsoString = jsontext.replace("\" ", "\""); 
		JSONArray entries = new JSONArray(processedjsoString);
		List<Category> categories = new ArrayList<Category>();
		for (int i = 0; i < entries.length(); i++) {
			JSONObject categoryJSon = entries.getJSONObject(i);
			categories.add(importCategory(categoryJSon));
		}
		return categories;
	}

	public JSONObject exportCategory(Category category) throws JSONException {
		JSONObject jsonCategory = new JSONObject();
		jSonMappings.toJsonChecklist(category, jsonCategory);
		if(category.getSubjects().size() > 0) {
			JSONArray jsonSubjects = new JSONArray();
			for (Subject subject : category.getSubjects()) {
				JSONObject jsonSubject = new JSONObject();
				jSonMappings.toJsonItem(subject, jsonSubject);
				jsonSubjects.put(jsonSubject);
			}
			jsonCategory.put(Category.PROP_SUBJECTS, jsonSubjects);
		}
		return jsonCategory;
	}

	private Category importCategory(JSONObject categoryJSon) throws JSONException {
		Category category = jSonMappings.toCategory(categoryJSon);
		if (categoryJSon.has(Category.PROP_SUBJECTS)) {
			JSONArray subjects = categoryJSon.getJSONArray(Category.PROP_SUBJECTS);
			for (int j = 0; j < subjects.length(); j++) {
				JSONObject subjectJson = subjects.getJSONObject(j);
				Subject subject = jSonMappings.toSubject(subjectJson);
				category.addSubject(subject);
			}
		}
		return category;
	}

}