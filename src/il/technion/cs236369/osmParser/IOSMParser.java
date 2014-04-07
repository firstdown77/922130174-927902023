package il.technion.cs236369.osmParser;

import org.json.simple.JSONArray;

public interface IOSMParser {

	JSONArray parse(String osmFile, ITagsRequired tagsRequired);
	
}
