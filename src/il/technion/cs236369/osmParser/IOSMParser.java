package il.technion.cs236369.osmParser;

import org.json.simple.JSONArray;

/**
 * Interface provided in the assignment for the OSMParser.
 * 
 * @author raphaelas
 */
public interface IOSMParser {

	JSONArray parse(String osmFile, ITagsRequired tagsRequired);
	
}
