package il.technion.cs236369.osmParser.data;

public class NodeDAOFactory {
	
	public static INodeDAO createNodeDAO()
	{
		return new NodeDAO();
		//return new NodeDAOMemory();
	}

}
