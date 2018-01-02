package auxiliary;

/**
 * <b>Motion Profile Implementation</b> <br>
 * A class for calculating speed according to distance for motors
 * <p>
 * <b>Example 1</b> <br>
 * <code>
 *  double[][] points = {{0,0},{0.25,1},{0.75,1},{1,0}}; <br>
	MotionProfile mp = new MotionProfile(points); <br>
	mp.getV(0) // returns 0 <br>
	mp.getV(0.1) // returns 0.4 <br>
	mp.getV(1) // returns 0 <br>
	</code> <br>
 * <b>Example 2</b>: <br>
 * <code>
 *  finalDistance = 5; // [METERS] <br>
 *  maxVelocity = 2; // [METERS/SEC] <br>
 *  accelerationDistance = 1; // [METERS] <br>
 *  decelerationDistance = 1.1; // [METERS] <br>
	MotionProfile mp = MotionProfile(finalDistance, maxVelocity, accelerationDistance, decelerationDistance); <br>
	mp.getV(x)
	</code>
 */
public class MotionProfile {
	private double[][] points;
	private double[][] functions;
	private boolean isForward;

	/**
	 * @param points
	 *            2D array of points. a point represents {x, v} (x = distance, v
	 *            = velocity)<br>
	 */
	public MotionProfile(double[][] points) {
		this.points = points;
		this.functions = pointsToFunctions(points);
	}

	/**
	 * 
	 * @param finalDistance
	 *            - distance from target [m]
	 * @param maxVelocity
	 *            - the absolute value of the maximum velocity the robot can
	 *            drive [m/s]
	 * @param minVelocity
	 *            - the absolute value of the minimum velocity the robot can
	 *            drive [m/s]
	 * @param accelerationDistance
	 *            - the distance the robot drives while accelerating from 0 to
	 *            the max velocity [m]
	 * @param decelerationDistance
	 *            the distance the robot drives while accelerating from 0 to the
	 *            max velocity [m]
	 */
	public MotionProfile(double finalDistance, double maxVelocity, double minVelocity, double accelerationDistance,
			double decelerationDistance) {
		isForward = finalDistance > 0;
		if (!isForward) {
			minVelocity = -minVelocity;
			maxVelocity = -maxVelocity;
			accelerationDistance = -accelerationDistance;
			decelerationDistance = -decelerationDistance;
		}
		double[] p1 = { 0, minVelocity };
		double[] pLast = { finalDistance, 0 };
		if (Math.abs(accelerationDistance) + Math.abs(decelerationDistance) >= Math.abs(finalDistance)) { // Collision
																											// between
																											// acceleration
																											// and
																											// deceleration
			double xP2 = (accelerationDistance * finalDistance) / (decelerationDistance + accelerationDistance);
			double yP2 = xP2 * (maxVelocity / accelerationDistance);
			double[] p2 = { xP2, yP2 };
			if (isForward)
				points = new double[][] { p1, p2, pLast };
			else
				points = new double[][] { pLast, p2, p1 };
		} else {
			// No collision between acceleration and deceleration
			double[] p2 = { accelerationDistance, maxVelocity };
			double[] p3 = { finalDistance - decelerationDistance, maxVelocity };
			if (isForward) {
				points = new double[][] { p1, p2, p3, pLast };
			} else {
				points = new double[][] { pLast, p3, p2, p1 };
			}
		}
		functions = pointsToFunctions(points);
	}

	public double[][] getPoints() {
		return points;
	}

	/**
	 * 
	 * @param x
	 *            - the distance from the target
	 * @return The desired speed in that distance (x) according to the graph of
	 *         the points (from the constructor)
	 */
	public double getV(double x) {
		return getY(x, this.functions, this.points);
	}

	public double[][] getFunc() {
		return functions;
	}

	private static double[] pointsToFunction(double[] p1, double[] p2) {
		assert p1.length == 2;
		assert p2.length == 2;
		double x1 = p1[0];
		double y1 = p1[1];
		double x2 = p2[0];
		double y2 = p2[1];
		double m = (y2 - y1) / (x2 - x1);
		double n = -m * x1 + y1;
		double[] function = { m, n };
		return function;
	}

	private static double[][] pointsToFunctions(double[][] points) {
		double[][] functions = new double[points.length - 1][2];
		// from the second point to the last one
		for (int i = 1; i < points.length; i++) {
			// index for appending the function to functions[]
			functions[i - 1] = pointsToFunction(points[i - 1], points[i]);
		}
		return functions;
	}

	private static double getY(double x, double[][] functions, double[][] points) {
		// choose the last function if x is bigger then the last point's X
		double[] rightFunction = functions[functions.length - 1];
		// from the second point to the last one
		for (int i = 0; i < functions.length; i++) {
			if (x < points[i + 1][0]) {
				// index for getting the function from functions[]
				rightFunction = functions[i];
				break;
			}
		}
		double m = rightFunction[0];
		double b = rightFunction[1];
		return m * x + b;
	}
}