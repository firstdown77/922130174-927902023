package il.technion.cs236369.osmParser;

/**
 * Interface for a class that returns the way's circumscribed area.
 * 
 * @author raphaelas
 */
public interface ICalculateCircumscribedCircleArea {
	
	/**

	 * Note: wayCoordinates have already been made unique.
	 * 
	 * @param wayCoordinates A 2D array containing all of a way's *unique* coordinates.
	 * @return The Way's circumscribed area - a double.
	 */
	double circumscribedArea(Position[] wayCoordinates);
}
