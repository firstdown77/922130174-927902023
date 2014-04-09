package il.technion.cs236369.osmParser;

import java.util.HashMap;
import java.util.HashSet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Class to publish the required JSON Array.
 * 
 * @author dsainz
 */
public class JSONWayParser implements IJSONArray {
	ICalculateCircumscribedCircleArea areaCalc = new AreaCalculator();

	@SuppressWarnings("unchecked")
	@Override
	public JSONArray printJSONArray(HashSet<Way> waySet,
			HashMap<Way, Position[]> positionMap) {
		JSONArray array = new JSONArray();
		
		for (Way way : waySet)
		{
			JSONObject obj = new JSONObject();
			obj.put("id", way.getId());
			if (way.getName() != null)
				obj.put("name", way.getName());
			if (way.getWebsite() != null)
				obj.put("website", way.getWebsite());
			if (way.getWiki() != null)
				obj.put("wiki", way.getWiki());
			obj.put("numNodes", new Integer(way.getNumNodes()));
			Position[] coords = positionMap.get(way);
			obj.put("coordinates", getCoordinates(coords));
			obj.put("Circumscribed Circle Area", areaCalc.circumscribedArea(coords));
			obj.put("users", getUsers(way.getUsers()));
			array.add(obj);
		}
		
		return array;
	}

	/**
	 * Returns a JSONArray with the coordinates of a way
	 * @param coords coordinates of a way
	 * @return A JSONArray of coordinates.
	 */
	@SuppressWarnings("unchecked")
	private JSONArray getCoordinates(Position[] coords)
	{
		JSONArray array = new JSONArray();
		if (coords != null)
		{
			for (Position pos : coords)
			{
				JSONObject coord = new JSONObject();
				coord.put("lat", pos.getNorth()+"");
				coord.put("lon", pos.getEast()+"");
				array.add(coord);
			}
		}
		return array;
	}
	
	/**
	 * Returns a JSONArray with the users
	 * @param users the users
	 * @return A JSONArray of users.
	 */
	@SuppressWarnings("unchecked")
	private JSONArray getUsers(HashSet<String> users)
	{
		JSONArray array = new JSONArray();
		if (users != null)
		{
			for (String user : users)
			{
				array.add(user);
			}
		}
		return array;
	}

}
