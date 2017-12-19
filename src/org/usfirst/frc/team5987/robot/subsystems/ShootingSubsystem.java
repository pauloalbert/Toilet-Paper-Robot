package org.usfirst.frc.team5987.robot.subsystems;

import org.usfirst.frc.team5987.robot.RobotMap;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class ShootingSubsystem extends Subsystem {
	
    Victor shootingMotorFrontLeft;
    Victor shootingMotorFrontRight;
    Encoder shootingEncoderLeft;
    Encoder shootingEncoderRight;
    
    public ShootingSubsystem() {
    	shootingMotorFrontLeft = new Victor(RobotMap.shootingVictorLeft);
    	shootingMotorFrontRight = new Victor(RobotMap.shootingVictorRight);
    	shootingEncoderLeft = new Encoder(RobotMap.shootingVictorChannelA,RobotMap.shootingVictorChannelB);
    	shootingEncoderLeft.setDistancePerPulse(RobotMap.shootingVictorDistancePerPulse);
    }
    
    public void setFront(double speed){
    	shootingMotorFrontLeft.set(speed);
    	shootingMotorFrontRight.set(-speed);
    }
    
    public double getPositionLeft(){
    	return shootingEncoderLeft.getDistance();
    }
    
    public double getPositionRight(){
    	return shootingEncoderRight.getDistance();
    }
    
    public double getSpeedLeft(){
    	return shootingEncoderLeft.getRate();
    }
    
    public double getSpeedRight(){
    	return shootingEncoderRight.getRate();
    }

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		
	}
}

