package il.technion.cs236369.osmParser;

import java.util.HashMap;
import java.util.HashSet;

import org.json.simple.JSONArray;

public interface IJSONArray {
	
	/**
	 * 
	 * @param waySet
	 * @param positionMap
	 * @return 	Returns a complete JSON array containing all of the required
	 * parameters provided.  
	 * 
	 * Note: name, website, and wiki may be null
	 * and should not be included in the returned JSON array.
	 */
	public JSONArray printJSONArray(
			HashSet<Way> waySet, HashMap<Way, Position[]> positionMap);
}