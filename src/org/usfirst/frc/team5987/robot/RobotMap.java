package org.usfirst.frc.team5987.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {

	public final static double distancePerPulse = 0.00133;

	public static double ConstantP = 0.045;
	public static double ConstantI = 0.001;
	public static double ConstantD = 0.2;

    public static double distanceFromCenter = 50;
    // For example to map the left and right motors, you could define the
    // following variables to use with your drivetrain subsystem.
    // public static int leftMotor = 1;
    // public static int rightMotor = 2;
  
    public static double leftDriveKp = 1.0;
    public static double leftDriveKi = 0;
    public static double leftDriveKd = 0;
    public static double rightDriveKp = 1.12;
    public static double rightDriveKi = 0;
    public static double rightDriveKd = 0;
    
    // last years robot drive ports!!!
    // TODO: change according to hanuka robot
    public static int leftFrontMotor = 6;
    public static int leftRearMotor = 7;
    public static int rightFrontMotor = 2;
    public static int rightRearMotor = 3;
	public static int leftDriveChannelA = 4;
	public static int leftDriveChannelB = 5;
	public static int rightDriveChannelA =6;
	public static int rightDriveChannelB = 7;

	public static int clawMotorPort = 0;
	
	public static int liftMotorPort = 8;
	/**
	 * how many meters the lift goes in one pulse <br>
	 * TODO: <b>CHANGE value!!</b>
	 */
	public static double liftMotorDistancePerPulse = 1./360;
	public static int liftEncoderChannelA = 7;
	public static int liftEncoderChannelB = 6;
	public static int potenMeterPort = 9;
	
	public static double liftConstantP = 0.5;
	public static double liftInitHeight = 1.3;
    // If you are using multiple modules, make sure to define both the port
    // number and the module. For example you with a rangefinder:
    // public static int rangefinderPort = 1;
    // public static int rangefinderModule = 1;
	//

	public static double leftDriveKf = 1.5;

	public static double rightDriveKf = 1.8;
	// TODO: CHANGE!!
	public static double maxDriveVelocity; // m / sec
	// TODO: CHANGE!!
	// {{tillThisDistance}, {useThisMaxVelocity}, {tillThatDistance}, {useThatMaxVelocity}} // sorted
	public static double[][] maxDriveVelocities = {{2, 0.5},{5, 2}}; // velocity in m/sec
	// TODO: CHANGE!!+
	// from 0 to 1
	public static double accelerationDrivePart = 30. / 100; // 10%
	// TODO: CHANGE!!+
	// from 0 to 1
	public static double decelerationDrivePart = 20. / 100; // 10%

	// TODO: CHANGE!!+
	public static double accelerationDriveDistance;
	// TODO: CHANGE!!+
	public static double decelerationDriveDistance;

	public static double minDriveVelocity = 0.04;
}
