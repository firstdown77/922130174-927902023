package il.technion.cs236369.osmParser.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class NodeDAOMemory implements INodeDAO {

	HashMap<String, Node> nodes;
	HashMap<String, ArrayList<NodeTag>> tags;
	
	@Override
	public void open() {
		
	}

	@Override
	public void close() {
		
	}

	@Override
	public Node getNodeData(String nodeID) {
		if (nodes.containsKey(nodeID))
		{
			return nodes.get(nodeID);
		}
		else
			return null;
	}

	@Override
	public Collection<NodeTag> getAllNodeTags(String nodeID) {
		if (tags.containsKey(nodeID))
		{
			return tags.get(nodeID);
		}
		else
			return new ArrayList<NodeTag>();
	}

	@Override
	public boolean saveNodeData(Node node) {
		if (!nodes.containsKey(node.getId()))
		{
			nodes.put(node.getId(), node);
			return true;
		}
		else
			return false;
	}

	@Override
	public boolean saveNodeTag(String nodeID, NodeTag tag) {
		if (!tags.containsKey(nodeID))
		{
			tags.put(nodeID, new ArrayList<NodeTag>());
		}
		tags.get(nodeID).add(tag);
		return true;
	}

}
