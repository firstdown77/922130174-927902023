package il.technion.cs236369.osmParser;

public class Node {

	protected String user;
	protected String longitude;
	protected String latitude;
	protected String id;

	public Node (String id, String latitude, String longitude, String user)
	{
		this.id = id;
		this.user = user;
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
	public String getId() {
		return id;
	}
	
	public String getUser() {
		return user;
	}

	public String getLongitude() {
		return longitude;
	}

	public String getLatitude() {
		return latitude;
	}
}
