package org.usfirst.frc.team5987.robot.commands;

import org.usfirst.frc.team5987.robot.Robot;
import org.usfirst.frc.team5987.robot.RobotMap;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class MoveLiftCommand extends Command {

	// All distance units are in *meters*
	double liftMotorHeight = 1.6;
	double liftBottomHeight = 0;
	double liftInitHeight = SmartDashboard.getNumber("liftInitHeight", RobotMap.liftInitHeight);
	double desPos;
	double pos;
	double P;
	final double DELAY = 0.005;
	
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
    	P=SmartDashboard.getNumber("liftConstantP", 2);
    	SmartDashboard.putNumber("liftConstantP", P);
    	if (desPos == -1)
        	desPos=SmartDashboard.getNumber("desired position", liftInitHeight);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	pos = liftInitHeight + Robot.liftSubsystem.getLiftDistance();
    	SmartDashboard.putNumber("Lift Pos", pos);
    	double error = desPos - pos;
    	SmartDashboard.putNumber("Lift Error", error);
    	double POut = error * P;
    	double out = POut;
    	Robot.liftSubsystem.setLiftSpeed(out);
    	Timer.delay(DELAY);
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
    	Robot.liftSubsystem.setLiftSpeed(0);

    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
