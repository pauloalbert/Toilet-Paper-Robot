package org.usfirst.frc.team5987.robot.subsystems;

import org.usfirst.frc.team5987.robot.RobotMap;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class ShootingSubsystem extends Subsystem {
    Victor shootingMotorRear;
    Victor shootingMotorMiddle;
    Victor shootingMotorFront;
    Encoder shootingEncoder;
    public ShootingSubsystem() {
    	shootingMotorRear = new Victor(RobotMap.shootingVictor);
    	shootingMotorMiddle = new Victor(RobotMap.shootingVictor);
    	shootingMotorFront = new Victor(RobotMap.shootingVictor);
    	shootingEncoder = new Encoder(RobotMap.shootingVictorChannelA,RobotMap.shootingVictorChannelB);
    	shootingEncoder.setDistancePerPulse(RobotMap.shootingVictorDistancePerPulse);
    }
    public void setRear(double speed){
    	shootingMotorRear.set(speed);
    }
    
    public void setMiddle(double speed){
    	shootingMotorMiddle.set(speed);
    }
    
    public void setFront(double speed){
    	shootingMotorFront.set(speed);
    }
    
    public double getPosition(){
    	return shootingEncoder.getDistance();
    }
    public double getSpeed(){
    	return shootingEncoder.getRate();
    }

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		
	}
}

