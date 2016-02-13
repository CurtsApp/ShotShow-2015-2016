package org.usfirst.frc.team498.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;



/*
 * 
 * 
 *  The revised version of Unnamed Mark 2
 * 
 * 
 * 
 */
public class Robot extends SampleRobot {
	//Drive
	int leftDrivePWMPort = 1;
	int rightDrivePWMPort = 0;
	//Accessories
	int leftIntakeCylinderExtendPort = 4;
	int leftIntakeCylinderRetractPort = 5;
	int rightIntakeCylinderExtendPort = 0;
	int rightIntakeCylinderRetractPort = 3; 
	int shooterLeftPort = 1;
	int shooterRightPort = 2;
	//Lifter
	int lifterCANID = 0;
	int intakeCANID = 1;
	int innerBoundDIOPort = 0;
	//Auto
	int ultrasonicDIOInputPort = 1;
	int ultrasonicDIOOutputPort = 2;
	
	Joystick thisStick = new Joystick(0);
	Drive2016 drive = new Drive2016(thisStick, leftDrivePWMPort, rightDrivePWMPort, 30, 30);
	IntakeAndShooter2016 accesories = new IntakeAndShooter2016(thisStick, rightIntakeCylinderExtendPort, rightIntakeCylinderRetractPort, leftIntakeCylinderExtendPort, leftIntakeCylinderRetractPort, intakeCANID, shooterLeftPort, shooterRightPort);
	LifterArm2016 lifter = new LifterArm2016(thisStick, lifterCANID, innerBoundDIOPort);
	VisionManager2016 vm = new VisionManager2016();
	AutonmousController autoController = new AutonmousController(drive, accesories, lifter, vm, ultrasonicDIOInputPort, ultrasonicDIOOutputPort);
	
	public void robotInit() {
		
	}

	// Select which autonomous to run
	public void autonmous() {
		autoController.autoInit(-1);
		while(isAutonomous() && isEnabled()) {
			autoController.autoLowBar();
		}
	}

	public void operatorControl() {
		accesories.retractShooter();
		accesories.retractIntake();
		while (isOperatorControl() && isEnabled()) {
			
			// Send stats to the driver
			print();
			// Drive the robot via controller
			drive.rampedDriveListener();
			// Listen for intake related commands
			accesories.intakeListener();
			// Listen for actionArm related commands
			accesories.rollerListener();
			// Listen for shooter related commands
			accesories.shooterListener();
			// Listen for lifter related commands
			lifter.baseListener();

		}

	}

	
	
	

	// Sends information to the driver
	private void print() {
		SmartDashboard.putBoolean("InnerBounds", lifter.innerBounds.get());
	}

	

	
		
	

}
