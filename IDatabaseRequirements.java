package hw2;

/**
 * This database should contain 2 tables (not 1).
 * The first table will provide a node's latitude, longitude, and user.
 * The second table will provide all of a node's tags.
 * 
 * @author raphaelas
 *
 */
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
	 * The returned 2D array must be arrays each containing
	 * 2 elements: a tag's key and a tag's value. 
	 * 
	 * @param nodeID
	 * @return
	 */
	String[][] getAllNodeTags(String nodeID);

    /**
     * Saves four fields describing a "node."
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
			String nodeLongitude, String nodeUser);
	
	
	/**
	 * Saves a node's tag - a key, value pair.  Must be accessible by nodeID.
	 * 
	 * @param nodeID
	 * @param nodeKey
	 * @param nodeValue
	 * @return
	 */
	boolean saveNodeTag(String nodeID, String nodeKey, String nodeValue);

}
