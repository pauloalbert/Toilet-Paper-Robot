package org.usfirst.frc.team5987.robot.commands;

import org.usfirst.frc.team5987.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class MoveLiftCommand extends Command {

	// All distance units are in *meters*
	double liftMotorHeight = 1.5;
	double liftBottomHeight = 1.0;
	double liftIniHeight = SmartDashboard.getNumber("liftIniHeight", liftBottomHeight);
	double desPos;
	double pos;
	double P;
	Timer timer = new Timer();
	double delay = 0.1;
	
    public MoveLiftCommand(double p) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.liftSubsystem);
    	desPos=p;
    }
    
    public MoveLiftCommand() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.liftSubsystem);
    	desPos = -1;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	P=SmartDashboard.getNumber("liftConstantP", 0.5);
    	if (desPos == -1)
        	desPos=SmartDashboard.getNumber("desired position", liftIniHeight);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	pos=liftIniHeight+Robot.liftSubsystem.getLiftDistance();
    	double error = (desPos-pos) * P;
    	Robot.liftSubsystem.setLiftSpeed(error);
    	timer.delay(delay);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if (desPos >= liftMotorHeight)
    		return true;
    	if (desPos <= liftBottomHeight)
    		return true;
    	if (Math.abs(pos - desPos) <= 0.01)
    		return true;
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
