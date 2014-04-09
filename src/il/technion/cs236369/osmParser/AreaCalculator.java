package il.technion.cs236369.osmParser;

import java.util.Arrays;
import java.util.HashSet;

/**
 * This class contains the method to calculate a Way's circumscribed
 * circle area.
 * 
 * @author dsainz
 *
 */
public class AreaCalculator implements ICalculateCircumscribedCircleArea {

	@Override
	public double circumscribedArea(Position[] wayCoordinates) {
		if (wayCoordinates == null) return 0;
		Position center = Position.getCenter(new HashSet<Position>(Arrays.asList(wayCoordinates)));
		double maxDist = 0;
		for (Position p : wayCoordinates)
		{
			double newDistance = center.haversineDistance(p);
			maxDist = Math.max(maxDist, newDistance);
		}
		return Position.getPI()*Math.pow(maxDist, 2);
	}

}
