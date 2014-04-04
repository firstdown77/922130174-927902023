package hw2;

import org.json.simple.JSONArray;

public interface IJSONArray {
	
	/**
	 * 
	 * @param id
	 * @param name
	 * @param website
	 * @param wiki
	 * @param numUniqueNodes
	 * @param uniqueCoordinates
	 * @param areaCircumscribedCircle
	 * @param users
	 * @return
	 * 
	 * Returns a complete JSON array containing all of the required
	 * parameters provided.  Note: name, website, and wiki may be null
	 * and should not be included in the returned JSON array.
	 */
	public JSONArray printJSONArray(String id, String name, String website,
			String wiki, int numUniqueNodes, String[][] uniqueCoordinates,
			double areaCircumscribedCircle, String[] users);
			
}