package org.usfirst.frc.team5987.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	/**
	 * Distance per pulse for the driving encoders
	 * TODO: Change might be needed according to encoders
	 */
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

	public static int shootingVictor = 0;
	public static int shootingVictorDistancePerPulse = 1;
	public static int shootingVictorChannelA = 0;
	public static int shootingVictorChannelB = 0;
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
}
