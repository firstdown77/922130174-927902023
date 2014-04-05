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
     * @param nodeID A unique nodeID - a String of an integer.
     * @return a String array of size 3.
     */
	String[] getNodeData(String nodeID);
	
	/**
	 * Returns a 2D array containing all "tags" belonging to a "node".
	 * The returned 2D array must be arrays each containing
	 * 2 elements: a tag's key and a tag's value. 
	 * 
	 * @param nodeID A unique nodeID - a String of an integer.
	 * @return 2D String array containing all a node's tags.
	 */
	String[][] getAllNodeTags(String nodeID);

    /**
     * Saves four fields describing a "node."
     * Returns true if save successful.  Returns false if save failed.
     * Nodes with duplicate nodeIDs should not be saved (if such duplicates exist).
     * The nodeTags double array is a list of every tag's key, value pairs (so each
     * inner array of the 2D array contains just 2 elements: key, value.)
     * 
     * @param nodeID a node's ID number - this should already have been made unique.
     * @param nodeLatitude a node's latitude - a String of a double.
     * @param nodeLongitude a node's longitude - a String of a double.
     * @param nodeUser a node's user - a String.
     * @return True if save successful, false if failed.
     */
	boolean saveNodeData(String nodeID, String nodeLatitude,
			String nodeLongitude, String nodeUser);
	
	
	/**
	 * Saves a node's tag - a key, value pair.  Must be accessible by nodeID.
	 * 
	 * @param nodeID A unique nodeID - a String of an integer.
	 * @param nodeKey A node key - a String.
	 * @param nodeValue A node value - a String.
	 * @return true if save successful, false if failed.
	 */
	boolean saveNodeTag(String nodeID, String nodeKey, String nodeValue);

}
