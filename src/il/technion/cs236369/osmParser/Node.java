package il.technion.cs236369.osmParser;

/**
 * A Node class containing the information about Nodes
 * relevant to the assignment.
 * 
 * @author raphaelas
 */
public class Node {

	protected String user;
	protected String longitude;
	protected String latitude;

	/**
	 * 
	 * @param latitude A Node's latitude.
	 * @param longitude A Node's longitude.
	 * @param user A Node's user.
	 */
	public Node (String latitude, String longitude, String user)
	{
		this.latitude = latitude;
		this.longitude = longitude;
		this.user = user;
	}
	
	/**
	 * User getter.
	 * @return the Node's user.
	 */
	public String getUser() {
		return user;
	}

	/**
	 * Longitude getter.
	 * @return the Node's longitude.
	 */
	public String getLongitude() {
		return longitude;
	}

	/**
	 * Latitude getter.
	 * @return the Node's latitude.
	 */
	public String getLatitude() {
		return latitude;
	}
}
