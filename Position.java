package hw2;

import java.util.Set;

public class Position {
	private static final double EARTH_MEAN_RADIUS = 6371.009; // KMs

	public final double north;
	public final double east;

	/**
	 * @param north
	 *            latitude
	 * @param east
	 *            longitude
	 */
	public Position(double north, double east) {
		this.north = north;
		this.east = east;
	}

	public double getEast() {
		return east;
	}

	public double getNorth() {
		return north;
	}

	/**
	 * Calculate the Haversine distance between two positions
	 * 
	 * @return distance in KMs
	 */
	public double haversineDistance(Position that) {
		// convert to radians
		double eRad = Math.toRadians(this.east), nRad = Math
				.toRadians(this.north);
		double thatERad = Math.toRadians(that.east), thatNRad = Math
				.toRadians(that.north);

		double dEast = thatERad - eRad, dNorth = thatNRad - nRad;

		double a = Math.pow((Math.sin(dEast / 2)), 2) + Math.cos(eRad)
				* Math.cos(thatERad) * Math.pow(Math.sin(dNorth / 2), 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		return EARTH_MEAN_RADIUS * c;
	}
	
	/**
	 * Calculate the Center of mass in a set of positions
	 * 
	 * @return a position that is center of mass
	 */
	public static Position getCenter(Set<Position> ps) {
		double nMid = 0, eMid = 0;
		for (Position p : ps) {
			nMid += p.north;
			eMid += p.east;
		}
		return new Position(nMid / ps.size(), eMid / ps.size());
	}
	
	/**
	 * 
	 * @return PI
	 */
	public static double getPI() {
		return Math.PI;
	}

	@Override
	public boolean equals(Object that) {
		return ((that != null) && (that instanceof Position)) ? this
				.equals((Position) that) : false;
	}

	public boolean equals(Position that) {
		return ((this.north == that.north) && (this.east == that.east));
	}

	@Override
	public int hashCode() {
		return (int) (this.north + this.east);
	}

	@Override
	public String toString() {
		return String.valueOf(north) + "," + String.valueOf(east);
	}
	
}
