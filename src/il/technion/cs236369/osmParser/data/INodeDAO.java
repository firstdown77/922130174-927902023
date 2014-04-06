package il.technion.cs236369.osmParser.data;

import java.util.Collection;

/**
 * This database should contain 2 tables (not 1).
 * The first table will provide a node's latitude, longitude, and user.
 * The second table will provide all of a node's tags.
 * 
 * @author raphaelas
 *
 */
public interface INodeDAO {

	/**
	 * opens the connection to the underlying storage system
	 */
	public void open();
	
	/**
	 * closes the connection to the underlying storage system
	 */
	public void close();
	
    /**
     * Returns an array with three String elements:
     * 1. nodeLatitude.  2. nodeLongitude.  3. nodeUser
     * 
     * @param nodeID A unique nodeID - a String of an integer.
     * @return a Node containing the node  information or NULL if the node does not exist.
     */
	Node getNodeData(String nodeID);
	
	/**
	 * Returns a 2D array containing all "tags" belonging to a "node".
	 * The returned 2D array must be arrays each containing
	 * 2 elements: a tag's key and a tag's value. 
	 * 
	 * @param nodeID A unique nodeID - a String of an integer.
	 * @return 2D String array containing all a node's tags.
	 */
	Collection<NodeTag> getAllNodeTags(String nodeID);

    /**
     * Saves four fields describing a "node."
     * Returns true if save successful.  Returns false if save failed.
     * Nodes with duplicate nodeIDs should not be saved (if such duplicates exist).
     *
     * @param node the node to save
     * @return True if save successful, false if failed.
     */
	boolean saveNodeData(Node node);
	
	
	/**
	 * Saves a node's tag - a key, value pair.  
	 * 
	 * @param nodeID A unique nodeID - a String of an integer.
	 * @param tag a node tag to store
	 * @return true if save successful, false if failed.
	 */
	boolean saveNodeTag(String nodeID, NodeTag tag);

}
