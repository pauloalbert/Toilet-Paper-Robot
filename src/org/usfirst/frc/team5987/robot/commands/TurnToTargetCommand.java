package org.usfirst.frc.team5987.robot.commands;

import org.usfirst.frc.team5987.robot.Robot;
import org.usfirst.frc.team5987.robot.RobotMap;
import org.usfirst.frc.team5987.robot.subsystems.DrivingSubsystem;

import auxiliary.MiniPID;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * 
 */
public class TurnToTargetCommand extends Command {
	DrivingSubsystem driveSubsystem;
	MiniPID pid;
	double lastTime;
	double output;
	// these values you get from the raspberry pi.
	//double prevCameraTargetAngle = 0;
	double cameraTargetAngle = 0;
	double cameraTargetDistance = 0;

	// These are determined at the start of the command
	double ConstantP;
	double ConstantI;
	double ConstantD;
	
	double DELAY = 0.005;

	// the angle from the center of the robot to the target
	double robotTargetAngle;
	
	//error: the angle the robot needs to turn
	double error, prevError;
	private double rotateMinOut;
	//pid changing values

	public TurnToTargetCommand() {
		driveSubsystem = Robot.driveSubsystem;
		requires(driveSubsystem);

	}

	// Called just before this Command runs the first time
	protected void initialize() {
		Robot.driveSubsystem.resetNavX();
		rotateMinOut = RobotMap.rotateMinOut;
		//Getting the constants from smartdashboard.
		pid = new MiniPID(0,0,0);
		updatePID();
		//getting the CAMERA VALUES from the raspberry pi
		//prevCameraTargetAngle = SmartDashboard.getNumber("Target Angle: ", 30);
		
		cameraTargetAngle = SmartDashboard.getNumber("targetAngle", 180);
		SmartDashboard.putNumber("targetAngle", cameraTargetAngle);
		
		//Angle from the robot base
		robotTargetAngle = cameraTargetAngle;
		
		
	}
	public void updatePID(){
		ConstantP = SmartDashboard.getNumber("kpRotation", RobotMap.rotateConstantP);
		ConstantI = SmartDashboard.getNumber("kiRotation", RobotMap.rotateConstantI);
		ConstantD = SmartDashboard.getNumber("kdRotation", RobotMap.rotateConstantD);
		pid.setPID(ConstantP, ConstantI, ConstantD);
	}
	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		//Check if the camera Values changed, and fix the target.
		/*cameraTargetAngle = SmartDashboard.getNumber("Target Angle: ", 30);
		if(cameraTargetAngle != prevCameraTargetAngle){
			robotTargetAngle = cameraToCenterAngle(cameraTargetAngle, cameraTargetDistance)
					+ Robot.driveSubsystem.getAngle();
		}*/

		updatePID();
		error = robotTargetAngle - Robot.driveSubsystem.getAngle();
		SmartDashboard.putNumber("error", error);
		
		 output = pid.getOutput(Robot.driveSubsystem.getAngle(), robotTargetAngle);
		 output = -output;
		 output = driveSubsystem.limit(-0.7, 0.7, output);
		 if(output>0 && output<rotateMinOut)
			 output = rotateMinOut;
		 if(output<0 && output>-rotateMinOut)
			 output = -rotateMinOut;
		SmartDashboard.putNumber("rotationPOutput", pid.getP());
		SmartDashboard.putNumber("rotationIOutput", pid.getI());
		SmartDashboard.putNumber("rotationDOutput", pid.getD());
		SmartDashboard.putNumber("rotationPIDOutput", output);
		Robot.driveSubsystem.drive(output, -output);
		Timer.delay(DELAY);
		SmartDashboard.putNumber("errorRotation", error);
		//prevCameraTargetAngle = cameraTargetAngle;
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
//		if(ConstantD!=0)
//			return (Math.abs(error) < 1.0 && pid.getD()/ConstantD<0.4);
		return Math.abs(error) < 1.0 &&
				Math.abs(driveSubsystem.getLeftEncoderSpeed()) < 0.1;
		//(Math.abs(error) < 1 && Math.abs(output)<0.16)
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.driveSubsystem.drive(0, 0);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}

//Find more accurate delay
//Document