package org.usfirst.frc.team5987.robot.subsystems;

import org.usfirst.frc.team5987.robot.RobotMap;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class TransportingSubsystem extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	private Victor TransportMotor;
	private Victor StorageMotor;
	

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    	TransportMotor=new Victor(RobotMap.TransportMotorChannel);
    	StorageMotor=new Victor(RobotMap.StorageMotorChannel);
    	
    }
    public double getTransportSpeed() {
    	return TransportMotor.getSpeed();
    }
    
    public double getStorageSpeed() {
    	return StorageMotor.getSpeed();
    }
    
    public void setStorageSpeed(double speed) {
    	if(speed > 1) {
    		speed=1;
    	}
    	else if(speed<-1) {
    		speed=-1;
    	}
    	StorageMotor.set(speed);
    }
    
    public void setTransportSpeed(double speed) {
    	TransportMotor.set(speed);
    }
}

