package il.technion.cs236369.osmParser.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

public class NodeDAO implements INodeDAO {

	private Connection con;
	private String url = "jdbc:mysql://localhost:3306/";
	private String dbName = "osmparser";
	private String driver = "com.mysql.jdbc.Driver";
	private String userName = "root";
	private String password = "root";
	private boolean isOpen = false;
    
	@Override
	public void open()
	{
		if (isOpen) return;
		
        try {
	        Class.forName(driver).newInstance();
	        con = DriverManager.getConnection(url+dbName,userName,password);
	        isOpen = true; 
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
	
	@Override
	public void close()
	{
		isOpen = false;
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Node getNodeData(String nodeID) {
		open();
		String SQL_QUERY= "Select * from Node where node_id="+nodeID;
		Statement stmt;
		Node n = null;
		
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL_QUERY);
			
			
			
			if(rs.next()) {
				n = new Node(nodeID, rs.getString(2), rs.getString(3), rs.getString(4));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		 
		return n;
	}

	@Override
	public Collection<NodeTag> getAllNodeTags(String nodeID) {
		String SQL_QUERY= "Select * from nodetag where node_id="+nodeID;
		Statement stmt;
		
		ArrayList<NodeTag> tags = new ArrayList<NodeTag>();
		open();
		
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL_QUERY);
						
			while(rs.next()) {
				NodeTag tag = new NodeTag(rs.getString(3), rs.getString(4));
				tags.add(tag);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return tags;
	}

	@Override
	public boolean saveNodeData(Node node) {
		String SQL_QUERY= "Select node_id from node where _id="+node.getId();
		Statement stmt;
		open();
		
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(SQL_QUERY);
						
			if(rs.next()) {
				return false; //Node exists
			}
			
			
			SQL_QUERY = "INSERT INTO node (node_id, user, longitude, latitude)" +
	                   "VALUES (?, ?, ?, ?)";
			PreparedStatement pst = con.prepareStatement(SQL_QUERY);
			pst.setInt(1, node.getIdInt());
			pst.setString(2, node.getUser());
			pst.setString(3, node.getLongitude());
			pst.setString(4, node.getLatitude());
			return (pst.executeUpdate()>0);
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean saveNodeTag(String nodeID, NodeTag tag) {
		open();
		
		try {
			String SQL_QUERY = "INSERT INTO nodetag (node_id, key, value)" +
	                   "VALUES (?, ?, ?)";
			PreparedStatement pst = con.prepareStatement(SQL_QUERY);
			pst.setInt(1, Integer.parseInt(nodeID));
			pst.setString(2, tag.getKey());
			pst.setString(3, tag.getValue());
			return (pst.executeUpdate()>0);
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

}
