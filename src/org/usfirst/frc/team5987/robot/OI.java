package org.usfirst.frc.team5987.robot;


import org.usfirst.frc.team5987.robot.commands.MPDriveToTargetCommand;
import org.usfirst.frc.team5987.robot.commands.MoveLiftCommand;
import org.usfirst.frc.team5987.robot.commands.OpenGripCommand;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {

    //// CREATING BUTTONS
    // One type of button is a joystick button which is any button on a joystick.
    // You create one by telling it which joystick it's on and which button
    // number it is.
    // Joystick stick = new Joystick(port);
    // Button button = new JoystickButton(stick, buttonNumber);
    public Joystick leftStick = new Joystick(0);
    public Joystick rightStick = new Joystick(1);
    public Joystick xbox = new Joystick(2);
    Button LB = new JoystickButton(xbox, 5);
    Button RB = new JoystickButton(xbox, 6);
    Button xboxA = new JoystickButton(xbox, 1);
    Button xboxB = new JoystickButton(xbox, 2);
    Button leftCenterBtn = new JoystickButton(leftStick, 3);
    Button rightCenterBtn = new JoystickButton(rightStick, 3);
     
    // There are a few additional built in buttons you can use. Additionally,
    // by subclassing Button you can create custom triggers and bind those to
    // commands the same as any other Button.
    
    //// TRIGGERING COMMANDS WITH BUTTONS
    // Once you have a button, it's trivial to bind it to a button in one of
    // three ways:
    
    // Start the command when the button is pressed and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenPressed(new ExampleCommand());
    
    // Run the command while the button is being held down and interrupt it once
    // the button is released.
    // button.whileHeld(new ExampleCommand());
    
    // Start the command when the button is released  and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenReleased(new ExampleCommand());
    
	//// CREATING1 BUTTONS
	// One type of button is a joystick button which is any button on a
	//// joystick.
	// You create one by telling it which joystick it's on and which button
	// number it is.
	// Joystick stick = new Joystick(port);
	// Button button = new JoystickButton(stick, buttonNumber);

	public OI() {
		leftCenterBtn.whenPressed(new OpenGripCommand(true));
		rightCenterBtn.whenPressed(new OpenGripCommand(false));
		xboxA.whenPressed(new MoveLiftCommand());
		xboxB.whenPressed(new MPDriveToTargetCommand());
	}
}