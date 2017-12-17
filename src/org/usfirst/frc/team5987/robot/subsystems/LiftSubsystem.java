package org.usfirst.frc.team5987.robot.subsystems;

import org.usfirst.frc.team5987.robot.RobotMap;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class LiftSubsystem extends Subsystem {
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

	Victor liftMotor;
	Victor clawMotor;
	Encoder liftEncoder;
	DigitalInput limitSwitchLeft;
	DigitalInput limitSwitchRight;
	
	public LiftSubsystem() {
		liftMotor = new Victor(RobotMap.liftMotorPort);
		clawMotor = new Victor(RobotMap.clawMotorPort);
		liftEncoder = new Encoder(RobotMap.leftDriveChanelA, RobotMap.leftDriveChanelB);
		limitSwitchLeft = new DigitalInput(RobotMap.limitSwitchLeftPort);
		limitSwitchRight = new DigitalInput(RobotMap.limitSwitchRightPort);
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public double getLiftSpeed() {
    	return liftMotor.getSpeed();
    }
    
    public void setLiftSpeed(double speed) {
    	liftMotor.set(speed);
    }
    
    public double getClawSpeed() {
    	return clawMotor.getSpeed();
    }
    
    public void setClawSpeed(double speed) {
    	clawMotor.set(speed);
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

