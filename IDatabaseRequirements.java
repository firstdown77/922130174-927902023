package hw2;
import java.util.Map;

public interface IDatabaseRequirements {

	Map<String, String> getTags();

    /** Returns an array with three String elements:
    * 1. nodeLatitude.  2. nodeLongitude.  3. nodeUser
    */
	String[] getNodeData(String nodeID);

    /** Saves four fields describing a "node."
    * Returns true if save successful.  Returns false if save failed.
    * Nodes with duplicate nodeIDs should not be saved (if such duplicates exist).
    */
	boolean saveNodeData(String nodeID, String nodeLatitude, String nodeLongitude, String nodeUser);

}
