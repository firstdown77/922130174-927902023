package hw2;

import org.json.simple.JSONArray;

public interface IJSONArray {
	
	/**
	 * 
	 * @param id ID to print
	 * @param name name to print - will usually be null.
	 * @param website website to print - will usually be null.
	 * @param wiki wikipedia entry to print - will usually be null.
	 * @param numUniqueNodes number (integer) of unique nodes belonging to way.
	 * @param uniqueCoordinates Double array of all a way's unique coordinates.
	 * @param areaCircumscribedCircle Way's circumscribed area - a double.
	 * @param users An array of users contributing to Way.
	 * @return The JSONArray which will be outputted.
	 * 
	 * Returns a complete JSON array containing all of the required
	 * parameters provided.  Note: name, website, and wiki may be null
	 * and should not be included in the returned JSON array.
	 */
	public JSONArray printJSONArray(String id, String name, String website,
			String wiki, int numUniqueNodes, String[][] uniqueCoordinates,
			double areaCircumscribedCircle, String[] users);
			
}