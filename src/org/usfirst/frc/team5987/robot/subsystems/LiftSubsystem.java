package org.usfirst.frc.team5987.robot.subsystems;

import org.usfirst.frc.team5987.robot.RobotMap;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class LiftSubsystem extends Subsystem {

	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	private Victor liftMotor;
	private Relay clawMotor;
	private Encoder liftEncoder;
	private DigitalInput limitSwitchLeft;
	private DigitalInput limitSwitchRight;

	public LiftSubsystem() {
		liftMotor = new Victor(RobotMap.liftMotorPort);
		clawMotor = new Relay(RobotMap.clawMotorPort);
		liftEncoder = new Encoder(RobotMap.leftDriveChanelA, RobotMap.leftDriveChanelB);
		liftEncoder.setDistancePerPulse(RobotMap.liftMotorDistancePerPulse);
		limitSwitchLeft = new DigitalInput(RobotMap.limitSwitchLeftPort);
		limitSwitchRight = new DigitalInput(RobotMap.limitSwitchRightPort);
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
		switch (clawMotor.get()) {
		case kOn:
			return 0;
		case kOff:
			return 0;
		case kForward:
			return 1;
		case kReverse:
			return -1;
		default:
			return 0;
		}
	}

	public void setClawSpeed(int speed) {
		switch (speed) {
		case 0:
			clawMotor.set(Value.kOff);
			break;
		case 1:
			clawMotor.set(Value.kForward);
			break;
		case -1:
			clawMotor.set(Value.kReverse);
			break;
		default:
			throw new RuntimeException("an unknown speed has occured: relay;");
		}
	}

	public double getLiftDistance() {
		return liftEncoder.getDistance();
	}

	public void resetLift() {
		liftEncoder.reset();
	}

	public boolean getLimitSwitchLeft() {
		return limitSwitchLeft.get();
	}

	public boolean getLimitSwitchRight() {
		return limitSwitchRight.get();
	}
}
