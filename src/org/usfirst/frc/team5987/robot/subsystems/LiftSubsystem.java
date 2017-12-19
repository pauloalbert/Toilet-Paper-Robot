package org.usfirst.frc.team5987.robot.subsystems;

import org.usfirst.frc.team5987.robot.RobotMap;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;

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
	}

	public double getLiftSpeed() {
		return liftMotor.getSpeed();
	}

	public void setLiftSpeed(double speed) {
		liftMotor.set(speed);
	}

	public double getClawSpeed() {
		return clawMotor.get();
	}

	public void setClawSpeed(int speed) {
		clawMotor.set(speed);
	}

	public double getLiftDistance() {
		return liftEncoder.getDistance();
	}

	public void resetLift() {
		liftEncoder.reset();
	}

	public double getPotenMeter() {
		return potenMeter.get();
	}
}
