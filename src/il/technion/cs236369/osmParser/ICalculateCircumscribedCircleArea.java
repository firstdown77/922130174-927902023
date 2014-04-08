package il.technion.cs236369.osmParser;

import java.util.Collection;

public interface ICalculateCircumscribedCircleArea {
	
	/**
	 * Returns the way's circumscribed area.
	 * Note: wayCoordinates have already been made unique.
	 * 
	 * @param wayCoordinates A 2D array containing all of a way's *unique* coordinates.
	 * @return The Way's circumscribed area - a double.
	 */
	double circumscribedArea(Collection<Position> wayCoordinates);
}
