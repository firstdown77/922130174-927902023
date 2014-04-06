package il.technion.cs236369.osmParser.data;

public class Node {

	protected String user;
	protected String longitude;
	protected String latitude;
	protected String id;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Node (String id, String user, String longitude, String latitude)
	{
		this.id = id;
		this.user = user;
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	
}
