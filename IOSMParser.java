package hw2;

import org.json.simple.JSONArray;

public interface IOSMParser {

	JSONArray parse(String osmFile, ITagsRequired tagsRequired);
	
}
