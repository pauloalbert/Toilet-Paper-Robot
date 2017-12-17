package org.usfirst.frc.team5987.robot.subsystems;

import org.usfirst.frc.team5987.robot.RobotMap;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class ShootingSubsystem extends Subsystem {
    Victor shootingMotor;
    Encoder shootingEncoder;
    public ShootingSubsystem() {
    	shootingMotor = new Victor(RobotMap.shootingVictor);
    	shootingEncoder = new Encoder(RobotMap.shootingVictorChannelA,RobotMap.shootingVictorChannelB);
    	shootingEncoder.setDistancePerPulse(RobotMap.distancePerPulse);
    }
    public void set(double speed){
    	shootingMotor.set(speed);
    }
    public double getPosition(){
    	return shootingEncoder.getDistance();
    }

	@Override
	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		
	}
}

