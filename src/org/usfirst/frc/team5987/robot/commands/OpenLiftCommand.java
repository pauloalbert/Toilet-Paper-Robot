package org.usfirst.frc.team5987.robot.commands;

import org.usfirst.frc.team5987.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class OpenLiftCommand extends Command {
	
	boolean open;
	double speed=0.5;

    public OpenLiftCommand(boolean b) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.liftSubsystem);
    	open=b;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.liftSubsystem.setClawSpeed(speed * (open ? 1 : -1));
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if (open && !Robot.liftSubsystem.getLimitSwitchRight())
    		return true;
    	if (!open && !Robot.liftSubsystem.getLimitSwitchLeft())
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
