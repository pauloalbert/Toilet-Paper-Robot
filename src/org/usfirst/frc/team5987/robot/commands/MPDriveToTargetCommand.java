package org.usfirst.frc.team5987.robot.commands;

import org.usfirst.frc.team5987.robot.Robot;
import org.usfirst.frc.team5987.robot.RobotMap;
import org.usfirst.frc.team5987.robot.subsystems.DrivingSubsystem;

import auxiliary.MiniPID;
import auxiliary.MotionProfile;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * @author Dor Brekhman (based on Dan's work)
 * drive to target with motion profile instead of using the distance
 */
public class MPDriveToTargetCommand extends Command {
	DrivingSubsystem driveSubsystem;

	public MPDriveToTargetCommand() {
		// Use requires() here to declare subsystem dependencies
		// eg. requires(chassis);
		requires(Robot.driveSubsystem);
		driveSubsystem = Robot.driveSubsystem;
	}

	double leftkP, leftKi, leftKd, leftKf;
	double rightkP, rightKi, rightKd, rightKf;
	MiniPID leftPid, rightPid;
	MotionProfile driveMP;
	
	double leftEncoderDelta, rightEncoderDelta;
	double initLeftEncoder, initRightEncoder;
	double initDistanceFromTarget;
	double leftError, rightError;
	double leftOutput, rightOutput;

	// TODO: CHANGE!
	final double minDistanceError = 0.01;
	// TODO: CHANGE!
	final double minOutput = 0.4;
	
	
	final double DELAY = 0.005;
	
	// Called just before this Command runs the first time
	protected void initialize() {
		initLeftEncoder = driveSubsystem.getLeftEncoderDistance();
		initRightEncoder = driveSubsystem.getRightEncoderDistance();
		
		initDistanceFromTarget = SmartDashboard.getNumber("driveInitDistance", 3);
		
		// get PIDF values from smartdashboard
		leftkP = SmartDashboard.getNumber("leftDriveKp", RobotMap.leftDriveKp);
		leftKi = SmartDashboard.getNumber("leftDriveKi", RobotMap.leftDriveKi);
		leftKd = SmartDashboard.getNumber("leftDriveKd", RobotMap.leftDriveKd);
		leftKf = SmartDashboard.getNumber("leftDriveKf", RobotMap.leftDriveKf);
		
		rightkP = SmartDashboard.getNumber("rightDriveKp", RobotMap.rightDriveKp);
		rightKi = SmartDashboard.getNumber("rightDriveKi", RobotMap.rightDriveKi);
		rightKd = SmartDashboard.getNumber("rightDriveKd", RobotMap.rightDriveKd);
		rightKf = SmartDashboard.getNumber("rightDriveKf", RobotMap.rightDriveKf);
		
		// MiniPID stuff
		leftPid = new MiniPID(leftkP, leftKi, leftKd, leftKf);
		rightPid = new MiniPID(rightkP, rightKi, rightKd, rightKf);
		leftPid.setDirection(true);
		rightPid.setDirection(true);
		
		SmartDashboard.putNumber("leftEncoder", initLeftEncoder);
		SmartDashboard.putNumber("rightEncoder", initRightEncoder);
		
		// put default PIDF values in smartdashboard if not found
		SmartDashboard.putNumber("driveInitDistance", initDistanceFromTarget);
		SmartDashboard.putNumber("leftDriveKp", leftkP);
		SmartDashboard.putNumber("leftDriveKi", leftKi);
		SmartDashboard.putNumber("leftDriveKd", leftKd);
		SmartDashboard.putNumber("leftDriveKf", leftKf);
		
		SmartDashboard.putNumber("rightDriveKp", rightkP);
		SmartDashboard.putNumber("rightDriveKi", rightKi);
		SmartDashboard.putNumber("rightDriveKd", rightKd);
		SmartDashboard.putNumber("rightDriveKf", rightKf);
		
		double maxVelocity = getMaxVelocity(initDistanceFromTarget); // get max velocity according to distance
		double minVelocity = getMinVelocity();
		double accelerationDriveDistance = RobotMap.accelerationDrivePart * initDistanceFromTarget; // % of full distance
		double decelerationDriveDistance = RobotMap.decelerationDrivePart * initDistanceFromTarget; // % of full distance
		// initialize the motion profile
		driveMP = new MotionProfile(
				initDistanceFromTarget,
				maxVelocity,
				minVelocity,
				accelerationDriveDistance,
				decelerationDriveDistance
				);
//		driveMP = new MotionProfile(new double[][]{
//			{0.,    -0.04},
//			{-0.3, -0.4},
//			{-0.8, -0.4},
//			{-1.,   0.0}
//		});
		
	}
	private double getMaxVelocity(double distance){
		return Math.abs(distance*0.4);
		
//		// iterate over the velocities
//		for(int i=0; i<RobotMap.maxDriveVelocities.length; i++){
//			// check current velocity
//			double maxDistanceForVelocity = RobotMap.maxDriveVelocities[i][0];
//			// check if the distance is less than the maximum distance for that velocity
//			if(distance < maxDistanceForVelocity){
//				//return the velocity
//				return RobotMap.maxDriveVelocities[i][1];
//			}
//		}
//		// if bigger than all, return the biggest velocity
//		return RobotMap.maxDriveVelocities[RobotMap.maxDriveVelocities.length-1][1];
	}
	private double getMinVelocity(){
		return RobotMap.minDriveVelocity;
	}
	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
	
		// The distance the left motor has driven.
		leftEncoderDelta = driveSubsystem.getLeftEncoderDistance() - initLeftEncoder;

		// The distance the right motor has driven.
		rightEncoderDelta = driveSubsystem.getRightEncoderDistance() - initRightEncoder;
		
		// get the desired left speed according to the motion profile
		//TODO: change to this below
		double setpointLeftSpeed;
		if(initDistanceFromTarget > 0)
//			setpointLeftSpeed = driveMP.getV(leftEncoderDelta);
			setpointLeftSpeed = 0.15;
		else
			setpointLeftSpeed = -0.15;
		double currentLeftSpeed = driveSubsystem.getLeftEncoderSpeed();
		leftOutput = leftPid.getOutput(currentLeftSpeed, setpointLeftSpeed);
		
		double setpointRightSpeed;
		// get the desired right speed according to the motion profile
		//TODO: change to this below
		if(initDistanceFromTarget > 0)
//			setpointRightSpeed = driveMP.getV(rightEncoderDelta);
			setpointRightSpeed = 0.15;
		else
			setpointRightSpeed = -0.15;
		double currentRightSpeed = driveSubsystem.getRightEncoderSpeed();
		rightOutput = rightPid.getOutput(currentRightSpeed, setpointRightSpeed);
		
		// calculate the distance errors
		leftError = initDistanceFromTarget - leftEncoderDelta;
		double leftSpeedError = setpointLeftSpeed - currentLeftSpeed;
		rightError = initDistanceFromTarget - rightEncoderDelta;
		double rightSpeedError = setpointRightSpeed - currentRightSpeed;
		//DRIVE!
		driveSubsystem.drive(-leftOutput, -rightOutput);
		
		// show data on smartdashboard
		SmartDashboard.putNumber("Setpoint Left Speed", setpointLeftSpeed);
		SmartDashboard.putNumber("Setpoint Right Speed", setpointRightSpeed);
		SmartDashboard.putNumber("driveLeftOutput", leftOutput);
		SmartDashboard.putNumber("driveRightOutput", rightOutput);
		SmartDashboard.putNumber("driveLeftError", leftError);
		SmartDashboard.putNumber("driveRightError", rightError);
		SmartDashboard.putNumber("driveLeftSpeedError", leftSpeedError);
		SmartDashboard.putNumber("driveRightSpeedError", rightSpeedError);
		
		Timer.delay(DELAY);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		// finish when close to target
		return Math.abs(leftError) < minDistanceError &&
				Math.abs(rightError) < minDistanceError &&
				Math.abs(leftOutput) < minOutput &&
				Math.abs(rightOutput) < minOutput;
	}

	// Called once after isFinished returns true
	protected void end() {
		driveSubsystem.drive(0, 0);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
	}
}