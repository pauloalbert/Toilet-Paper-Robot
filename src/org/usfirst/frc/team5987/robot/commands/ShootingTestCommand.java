package org.usfirst.frc.team5987.robot.commands;

import org.usfirst.frc.team5987.robot.Robot;

import auxiliary.MiniPID;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class ShootingTestCommand extends Command {

    public ShootingTestCommand() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.shootingSubsystem);
    }
    
    double desSpeed;
    private MiniPID pid;
	private double P;
	private double I;
	private double D;
	private double F;
	final double DELAY = 0.005;

    // Called just before this Command runs the first time
    protected void initialize() {
    	SmartDashboard.putNumber("Desired speed", 0.25);
    	desSpeed = SmartDashboard.getNumber("Desired speed", 0.25);
    	P=SmartDashboard.getNumber("shootConstantP", 2);
    	SmartDashboard.putNumber("shootConstantP", P);
    	I=SmartDashboard.getNumber("shootConstantI", 0);
    	SmartDashboard.putNumber("shootConstantI", I);
    	D=SmartDashboard.getNumber("shootConstantD", 0);
    	SmartDashboard.putNumber("shootConstantD", D);
    	F=SmartDashboard.getNumber("F", 0.5);
    	pid = new MiniPID(P, I, D, F);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double curSpeed = Robot.shootingSubsystem.getSpeedLeft();
    	SmartDashboard.putNumber("Current speed", curSpeed);
    	double error = curSpeed - desSpeed;
    	SmartDashboard.putNumber("Error", error);
    	double out = pid.getOutput(curSpeed, desSpeed);
    	Robot.shootingSubsystem.setFront(out);
    	Timer.delay(DELAY);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if (Math.abs(Robot.shootingSubsystem.getSpeedLeft()-SmartDashboard.getNumber("Desired speed", 0.25)) <= 0.05)
    		return true;
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.shootingSubsystem.setFront(0);;
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
