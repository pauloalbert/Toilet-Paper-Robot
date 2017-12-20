package org.usfirst.frc.team5987.robot.commands;

import org.usfirst.frc.team5987.robot.Robot;
import org.usfirst.frc.team5987.robot.RobotMap;
import org.usfirst.frc.team5987.robot.subsystems.LiftSubsystem;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import auxiliary.MiniPID;

/**
 *
 */
public class MoveLiftCommand extends Command {

	// All distance units are in *meters*
	// initial height of the lift, as set  by the driver in smartdashboard
	
	// the desired position you want the lift to go to
	private double desPos;
	// lets us know if we're using smartdashboard or preset numbers
	private boolean isUsingSD;
	double pos;
	
	private MiniPID pid;
	private double P;
	private double I;
	private double D;
	final double DELAY = 0.005;
	private double liftInitHeight;
	
    public MoveLiftCommand(double desiredPosition) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	// receives a position, doesn't use smartdashboard
    	requires(Robot.liftSubsystem);
    	desPos=desiredPosition;
    	isUsingSD = false;
    	Robot.liftSubsystem.resetLift();
    }
    /**
     * get the desired position from smart dashboard
     */
    public MoveLiftCommand() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	// doesn't receive a position, uses smartdashboard
    	requires(Robot.liftSubsystem);
    	isUsingSD = true;
    	Robot.liftSubsystem.resetLift();
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	P=SmartDashboard.getNumber("liftConstantP", 2);
    	SmartDashboard.putNumber("liftConstantP", P);
    	I=SmartDashboard.getNumber("liftConstantI", 0);
    	SmartDashboard.putNumber("liftConstantI", I);
    	D=SmartDashboard.getNumber("liftConstantD", 0);
    	SmartDashboard.putNumber("liftConstantD", D);
    	pid = new MiniPID(P, I, D);
    	if (isUsingSD)
        	desPos=SmartDashboard.getNumber("desired position", liftInitHeight);
    		SmartDashboard.putNumber("desired position", desPos);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	// Don't start if your'e already finished -dor 2017
    	if (!isFinished()){
        	pos = Robot.liftSubsystem.getLiftDistance();
        	SmartDashboard.putNumber("Lift Pos", pos);
        	double error = pos - desPos;
        	SmartDashboard.putNumber("Lift Error", error);
        	double out = pid.getOutput(pos, desPos);
        	out = -out;
        	SmartDashboard.putNumber("Lift out", out);
        	Robot.liftSubsystem.setLiftSpeed(out);
        	Timer.delay(DELAY);
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if (desPos >= RobotMap.liftMotorHeight)
    		return true;
    	if (desPos <= RobotMap.liftBottomHeight)
    		return true;
    	if (Math.abs(pos - desPos) <= 0.05)
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
    	end();
    }
}
