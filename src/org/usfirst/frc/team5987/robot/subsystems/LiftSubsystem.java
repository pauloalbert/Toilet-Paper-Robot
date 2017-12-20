package org.usfirst.frc.team5987.robot.subsystems;

import org.usfirst.frc.team5987.robot.Robot;
import org.usfirst.frc.team5987.robot.RobotMap;
import org.usfirst.frc.team5987.robot.commands.JoystickLiftCommand;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class LiftSubsystem extends Subsystem {

	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	private Victor liftMotor;
	private Victor clawMotor;
	private Encoder liftEncoder;
	private AnalogPotentiometer potenMeter;

	public LiftSubsystem() {
		liftMotor = new Victor(RobotMap.liftMotorPort);
		clawMotor = new Victor(RobotMap.clawMotorPort);
		liftEncoder = new Encoder(RobotMap.liftEncoderChannelA, RobotMap.liftEncoderChannelB);
		liftEncoder.setDistancePerPulse(RobotMap.liftMotorDistancePerPulse);
		potenMeter = new AnalogPotentiometer(RobotMap.potenMeterPort);
	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
		setDefaultCommand(new JoystickLiftCommand());
	}

	public double getLiftSpeed() {
		return liftMotor.getSpeed();
	}

	public void setLiftSpeed(double speed) {
		double idleFactor = SmartDashboard.getNumber("Lift Idle Speed", 0.18);
		SmartDashboard.putNumber("Lift Idle Speed", idleFactor);
		speed -= idleFactor;
		// if the desired height is more than it can possibly go, it won't go
		if (getLiftDistance() + RobotMap.liftBottomHeight >= RobotMap.liftMotorHeight)
			liftMotor.set(0);
		speed = Robot.driveSubsystem.limit(-1, 1, speed);
//		speed = -speed;
		liftMotor.set(speed);
	}

	public double getClawSpeed() {
		return clawMotor.get();
	}

	public void setClawSpeed(double speed) {
		clawMotor.set(speed);
	}

	public double getLiftDistance() {
		return -liftEncoder.getDistance();
	}

	public void resetLift() {
		liftEncoder.reset();
	}

	public double getPotenMeter() {
		return potenMeter.get();
	}
}
