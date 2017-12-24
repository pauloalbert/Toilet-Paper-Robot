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
	private double P, I, D;
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
		P = SmartDashboard.getNumber("liftConstantP", 2);
		SmartDashboard.putNumber("liftConstantP", P);
		I = SmartDashboard.getNumber("liftConstantI", 0);
		SmartDashboard.putNumber("liftConstantI", I);
		D = SmartDashboard.getNumber("liftConstantD", 0);
		SmartDashboard.putNumber("liftConstantD", D);
		pid = new MiniPID(P, I, D);
		if (isUsingSD)
			desPos = SmartDashboard.getNumber("desired position", liftInitHeight);
		SmartDashboard.putNumber("desired position", desPos);
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		// Don't start if your'e already finished -dor 2017
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
	}
}
