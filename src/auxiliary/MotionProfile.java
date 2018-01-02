package auxiliary;
/**
 * <b>Motion Profile Implementation</b> <br>
 * A class for calculating speed according to distance for motors <br>
 * <br>
 * <b>Example 1</b>: <br>
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
	 *            2D array of points. a point represents {x, v} (x=distance,
	 *            v=velocity)<br>
	 */
	public MotionProfile(double[][] points) {
		this.points = points;
		this.functions = pointsToFunctions(points);
	}

	/**
	 * 
	 * @param finalDistance
	 *            distance from target [METERS]
	 * @param maxVelocity
	 *            ABSOLUTE(>0) the maximum velocity the robot can drive
	 *            [METERS/SEC]
	 * @param minVelocity
	 *            ABSOLUTE(>0) the minimum velocity the robot can drive
	 *            [METERS/SEC]
	 * @param accelerationDistance
	 *            the distance the robot drives from velocity 0 to max velocity
	 *            [METERS]
	 * @param decelerationDistance
	 *            the distance the robot drives from max velocity to 0 [METRES]
	 */
	public MotionProfile(double finalDistance, double maxVelocity, double minVelocity, double accelerationDistance,
			double decelerationDistance) {
		double Vmax = maxVelocity;
		double Xt = finalDistance;
		double Xa = accelerationDistance;
		double Xd = decelerationDistance;
		isForward = finalDistance > 0;
		if (!isForward) {
			minVelocity = -minVelocity;
			maxVelocity = -maxVelocity;
			Xa = -Xa;
			Xd = -Xd;
		}
		double[] p1 = { 0, minVelocity };
		double[] pLast = { Xt, 0 };
		if (Math.abs(Xa) + Math.abs(Xd) >= Math.abs(Xt)) { // collision between
															// acceleration and
															// deceleration
			double Xp2 = (Xa * Xt) / (Xd + Xa);
			double Yp2 = Xp2 * (maxVelocity / Xa);
			double[] p2 = { Xp2, Yp2 };
			if (isForward)
				this.points = new double[][] { p1, p2, pLast };
			else
				this.points = new double[][] { pLast, p2, p1 };
		} else { // no collision between acceleration and deceleration
			double[] p2 = { Xa, maxVelocity };
			double[] p3 = { Xt - Xd, maxVelocity };
			this.points = new double[][] { p1, p2, p3, pLast };
			this.points = new double[][] { pLast, p3, p2, p1 };
		}
		this.functions = pointsToFunctions(points);
	}

	public double[][] getPoints() {
		return points;
	}

	/**
	 * 
	 * @param x
	 *            the distance from the target
	 * @return the desired speed in that distance (x) according to the graph of
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
			int functionsI = i - 1;
			functions[functionsI] = pointsToFunction(points[i - 1], points[i]);
		}
		return functions;
	}

	private static double getY(double x, double[][] functions, double[][] points) {
		// choose the last function if x is bigger then the last point's X
		double[] rightFunction = functions[functions.length - 1];
		// from the second point to the last one
		for (int i = 0; i < functions.length; i++) {
			if (x < points[i+1][0]) {
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