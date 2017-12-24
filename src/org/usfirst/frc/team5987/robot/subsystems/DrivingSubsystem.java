package org.usfirst.frc.team5987.robot.subsystems;

import org.usfirst.frc.team5987.robot.Robot;
import org.usfirst.frc.team5987.robot.RobotMap;
import org.usfirst.frc.team5987.robot.commands.JoystickDriveCommand;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DrivingSubsystem extends Subsystem {
	 RobotDrive robotDrive;
	
	private  Encoder leftEncoder;
	private  Encoder rightEncoder;
	
	public DrivingSubsystem(){
    	// set ports for the victors using the preassigned values of the RobotMap
    	robotDrive = new RobotDrive(RobotMap.leftFrontMotor,RobotMap.leftRearMotor,RobotMap.rightFrontMotor,RobotMap.rightRearMotor);
    	
    	leftEncoder = new Encoder(RobotMap.leftDriveChannelA, RobotMap.leftDriveChannelB);
    	rightEncoder = new Encoder(RobotMap.rightDriveChannelA, RobotMap.rightDriveChannelB);
    	
    	leftEncoder.setDistancePerPulse(RobotMap.distancePerPulse);
    	rightEncoder.setDistancePerPulse(RobotMap.distancePerPulse);
	}
    public void initDefaultCommand() {

    	setDefaultCommand(new JoystickDriveCommand());
    }
    /**
     * Normal TankDrive
     * @param leftValue leftMotor speed -1 <= speed <= 1
     * @param rightValue rightMotor speed -1 <= speed <= 1
     */
    public void drive(double leftValue, double rightValue){
    	leftValue = limit(-1,1,leftValue)*0.8;
    	rightValue = limit(-1,1,rightValue)*0.8;
    	robotDrive.tankDrive(-leftValue, -rightValue);
    	getLeftEncoderDistance();
    	getRightEncoderDistance();
    }
    public double getLeftEncoderDistance() {
    	double distance = -leftEncoder.getDistance();
    	SmartDashboard.putNumber("leftEncoder", distance);
    	return distance;
    }
    
    public double getRightEncoderDistance() {
    	double distance = -rightEncoder.getDistance();
    	SmartDashboard.putNumber("rightEncoder", distance);
    	return distance;
    }
    public double getLeftEncoderSpeed() {
    	// TODO: check if minus is needed
    	double speed = -leftEncoder.getRate();
    	SmartDashboard.putNumber("leftEncoderSpeed", speed);
    	return speed;
    }
    
    public double getRightEncoderSpeed() {
    	// TODO: check if minus is needed
    	double speed = -rightEncoder.getRate();
    	SmartDashboard.putNumber("rightEncoderSpeed", speed);
    	return speed;
    }
    public double getAngle() {
		return Robot.ahrs.getAngle();

	}
	public double limit(double minLimit, double maxLimit, double val) {
		if (val < minLimit)
			return minLimit;
		if (val > maxLimit)
			return maxLimit;
		return val;
	}

	public void resetNavX() {
		Robot.ahrs.reset();
	}
}

