package org.usfirst.frc.team5987.robot.commands;

import org.usfirst.frc.team5987.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class JoystickLiftCommand extends Command {

    public JoystickLiftCommand() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.liftSubsystem);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.liftSubsystem.setLiftSpeed(Robot.oi.leftStick.getY());
    	SmartDashboard.putNumber("Lift speed", Robot.liftSubsystem.getLiftSpeed());
    	Robot.liftSubsystem.setClawSpeed(Robot.oi.rightStick.getY());
    	SmartDashboard.putNumber("Claw speed", Robot.liftSubsystem.getClawSpeed());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}