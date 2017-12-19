package org.usfirst.frc.team5987.robot.commands;

import org.usfirst.frc.team5987.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class OpenGripCommand extends Command {
	
	private boolean open;
    public OpenGripCommand(boolean isOpen) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.liftSubsystem);
    	open=isOpen;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	SmartDashboard.putBoolean("Limit Switch Right", Robot.liftSubsystem.getLimitSwitchRight());
    	SmartDashboard.putBoolean("Limit Switch Left", Robot.liftSubsystem.getLimitSwitchLeft());
    	Robot.liftSubsystem.setClawSpeed(open ? 1 : -1);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if (open && Robot.liftSubsystem.getLimitSwitchRight())
    		return true;
    	if (!open && Robot.liftSubsystem.getLimitSwitchLeft())
    		return true;
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.liftSubsystem.setClawSpeed(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
