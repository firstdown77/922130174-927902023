package hw2;

public interface IDatabaseRequirements {

    /**
     * Returns an array with three String elements:
     * 1. nodeLatitude.  2. nodeLongitude.  3. nodeUser
     * 
     * @param nodeID
     * @return
     */
	String[] getNodeData(String nodeID);
	
	/**
	 * Returns a 2D array containing all "tags" belonging to a "node".
	 * The returned 2D array must contain 2D arrays each containing
	 * 2 elements: a tag's key and a tag's value. 
	 * 
	 * @param nodeID
	 * @return
	 */
	String[][] getNodeTagsData(String nodeID);

    /**
     * Saves five fields describing a "node."
     * Returns true if save successful.  Returns false if save failed.
     * Nodes with duplicate nodeIDs should not be saved (if such duplicates exist).
     * The nodeTags double array is a list of every tag's key, value pairs (so each
     * inner array of the 2D array contains just 2 elements: key, value.)
     * 
     * @param nodeID
     * @param nodeLatitude
     * @param nodeLongitude
     * @param nodeUser
     * @param nodeTags
     * @return
     */
	boolean saveNodeData(String nodeID, String nodeLatitude,
			String nodeLongitude, String nodeUser, String[][] nodeTags);

}
