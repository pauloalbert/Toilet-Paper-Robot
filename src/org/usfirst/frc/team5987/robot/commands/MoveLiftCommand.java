package org.usfirst.frc.team5987.robot.commands;

import org.usfirst.frc.team5987.robot.Robot;
import org.usfirst.frc.team5987.robot.RobotMap;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import auxiliary.MiniPID;

/**
 *
 */
public class MoveLiftCommand extends Command {

	// ALL DISTANCE NUMBERS ARE IN METERS

	/**
	 * Initial height of the lift, as set by the driver in the SmartDashboard.
	 */
	double liftInitHeight = SmartDashboard.getNumber("liftInitHeight", RobotMap.liftInitHeight);
	/**
	 * The desired position you want the lift to go to.
	 */
	private double desPos;
	/**
	 * Whether the robot uses values from the SmartDashboard or preset numbers.
	 */
	private boolean isUsingSD;
	double pos;

	private MiniPID pid;
	private double kP, kI, kD;
	final double DELAY = 0.005;

	/**
	 * Constructs the MoveLiftCommand when the values are preset.
	 * 
	 * @param desiredPosition
	 *            - The desired position you want the lift to go to.
	 */
	public MoveLiftCommand(double desiredPosition) {
		// Use requires() here to declare subsystem dependencies
		// eg. requires(chassis);
		requires(Robot.liftSubsystem);
		desPos = desiredPosition;
		isUsingSD = false;
	}

	/**
	 * Constructs the MoveLiftCommand when the values are from the
	 * SmartDashboard.
	 */
	public MoveLiftCommand() {
		// Use requires() here to declare subsystem dependencies
		// eg. requires(chassis);
		requires(Robot.liftSubsystem);
		isUsingSD = true;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		
		if (isUsingSD)
			desPos = SmartDashboard.getNumber("desired position", liftInitHeight);
		if (desPos < Robot.liftSubsystem.getLiftDistance())
		{
			kP = SmartDashboard.getNumber("downLiftConstantP", 2);
			SmartDashboard.putNumber("downLiftConstantP", kP);
			kI = SmartDashboard.getNumber("downLiftConstantI", 0);
			SmartDashboard.putNumber("downLiftConstantI", kI);
			kD = SmartDashboard.getNumber("downLiftConstantD", 0);
			SmartDashboard.putNumber("downLiftConstantD", kD);
		}
		else
		{
			kP = SmartDashboard.getNumber("upLiftConstantP", 1);
			SmartDashboard.putNumber("upLiftConstantP", kP);
			kI = SmartDashboard.getNumber("upLiftConstantI", 0);
			SmartDashboard.putNumber("upLiftConstantI", kI);
			kD = SmartDashboard.getNumber("upLiftConstantD", 0);
			SmartDashboard.putNumber("upLiftConstantD", kD);
		}
		pid = new MiniPID(kP, kI, kD);
		SmartDashboard.putNumber("desired position", desPos);
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		// Don't start if your'e already finished—Brekhman D., 2017.
		if (!isFinished()) {
			pos = liftInitHeight + Robot.liftSubsystem.getLiftDistance();
			SmartDashboard.putNumber("Lift Pos", pos);
			double error = pos - desPos;
			SmartDashboard.putNumber("Lift Error", error);
			double out = pid.getOutput(pos, desPos);
			out = -out;
			Robot.liftSubsystem.setLiftSpeed(out);
			Timer.delay(DELAY);
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return desPos >= RobotMap.liftMotorHeight || desPos <= RobotMap.liftBottomHeight;
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
