package il.technion.cs236369.osmParser;

import java.util.HashMap;
import java.util.HashSet;

import org.json.simple.JSONArray;

/**
 * Interface for creating the JSONArray that is returned.
 * 
 * @author raphaelas
 */
public interface IJSONArray {
	
	/**
	 * Note: name, website, and wiki may be null
	 * and should not be included in the returned JSON array.
	 * 
	 * @param waySet
	 * @param positionMap
	 * @return 	Returns a complete JSON array containing all of the required
	 * parameters provided.
	 */
	public JSONArray printJSONArray(
			HashSet<Way> waySet, HashMap<Way, Position[]> positionMap);
}