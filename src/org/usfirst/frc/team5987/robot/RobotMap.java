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
  
    public static double leftDriveKp = 0.93;
    public static double leftDriveKi = 0.007;
    public static double leftDriveKd = 20;
    public static double rightDriveKp = 0.95;
    public static double rightDriveKi = 0.007;
    public static double rightDriveKd = 20.0;

    public static int leftFrontMotor = 0;
    public static int leftRearMotor = 1;
    public static int rightFrontMotor = 2;
    public static int rightRearMotor = 3;
	public static int leftDriveChanelA = 0;
	public static int leftDriveChanelB = 1;
	public static int rightDriveChanelA = 2;
	public static int rightDriveChanelB = 3;

	public static int clawMotorPort = 0;

	
	public static int liftMotorPort = 8;
	/**
	 * how many meters the lift goes in one pulse <br>
	 * TODO: <b>CHANGE value!!</b>
	 */
	public static double liftMotorDistancePerPulse = 1./360;
	public static int liftEncoderChannelA = 7;
	public static int liftEncoderChannelB = 6;
	public static int limitSwitchLeftPort = 9;
	public static int limitSwitchRightPort = 8;
	
	public static double liftConstantP = 0.5;
	public static double liftInitHeight = 1.3;
    // If you are using multiple modules, make sure to define both the port
    // number and the module. For example you with a rangefinder:
    // public static int rangefinderPort = 1;
    // public static int rangefinderModule = 1;
	//
}
