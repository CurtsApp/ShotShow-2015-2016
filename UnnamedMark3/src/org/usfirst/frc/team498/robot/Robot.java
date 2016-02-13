package org.usfirst.frc.team498.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
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
	NetworkTable visionTable = NetworkTable.getTable("GRIP/myContoursReport");

	RobotDrive drive = new RobotDrive(1, 0);
	Joystick thisStick = new Joystick(0);

	// DigitalInput limit = new DigitalInput(1);
	Talon armMotor = new Talon(3);
	DigitalInput upLimit = new DigitalInput(2);
	DigitalInput downLimit = new DigitalInput(4);
	RampManager forwardDriveRamp = new RampManager(30);
	RampManager turningDriveRamp = new RampManager(30);
	double[] defaultValue = new double[0];
	double[] areas;
	double[] heights;
	double[] widths;
	double[] centerXs;
	double[] centerYs;
	Relay light = new Relay(0);
	String log = "";
	ADXRS450_Gyro gyro = new ADXRS450_Gyro();
	BuiltInAccelerometer acc = new BuiltInAccelerometer();
	Timer autoClock = new Timer();
	Ultrasonic ultra = new Ultrasonic(0, 1);
	DoubleSolenoid newIntake = new DoubleSolenoid(5, 6);
	DoubleSolenoid shooter = new DoubleSolenoid(2, 3);
	CANTalon roller = new CANTalon(1);
	CANTalon lifter = new CANTalon(2);
	DigitalInput outerBounds = new DigitalInput(0);
	DigitalInput innerBounds = new DigitalInput(1);

	double lifterValue;
	int goalIndex;
	int phase = 0;
	double moveValue;
	double turnValue;
	double rollerValue;

	// AUTONOMOUS CONSTANTS
	double distanceToStopFromWall = 20;
	double distanceToStopFromTower = 15;
	double angleToTurnTowardsTower = 30;
	double widthToHeightRatio = 10 / 7;
	double adjustValue;

	public void robotInit() {
		// Turn on the green ring light for camera vision tracking
		light.set(Relay.Value.kForward);
	}

	// Select which autonomous to run
	public void autonmous() {
		autoLowBar();
	}

	public void operatorControl() {

		while (isOperatorControl() && isEnabled()) {
			
			// Send stats to the driver
			print();
			// Drive the robot via controller
			drive();
			// Listen for intake related commands
			intake();
			// Listen for actionArm related commands
			lifter();

		}

	}

	// Handles intake related inputs
	private void intake() {
		if (thisStick.getRawButton(3)) {
			newIntake.set(DoubleSolenoid.Value.kForward);
		} else if (thisStick.getRawButton(4)) {
			newIntake.set(DoubleSolenoid.Value.kReverse);
		}
		if (thisStick.getRawButton(1)) {
			rollerValue = 1;
		} else if (thisStick.getRawButton(2)) {
			rollerValue = 0;
		} else if (thisStick.getRawButton(7)) {
			rollerValue = -1;
		}

	}

	// The robot's speed slowly increases over time.
	private void drive() {
		// Axis 3 is RT Axis 2 is LT
		forwardDriveRamp.rampTo(thisStick.getRawAxis(3)
				- thisStick.getRawAxis(2));
		moveValue = forwardDriveRamp.getCurrentValue();
		// Axis 0 is X Value of Left Stick
		turningDriveRamp.rampTo(-thisStick.getRawAxis(0));
		turnValue = turningDriveRamp.getCurrentValue();

		/*
		 * Uncomment for rampless driving
		 * 
		 * moveValue = thisStick.getRawAxis(3) - thisStick.getRawAxis(2);
		 * turnValue = -thisStick.getRawAxis(0);
		 */

		drive.arcadeDrive(moveValue, turnValue);
	}

	// Update contour data from network tables
	public void updateTables() {
		try {
			areas = visionTable.getNumberArray("area", defaultValue);
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("area missing");

		}
		try {
			heights = visionTable.getNumberArray("height", defaultValue);
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("height missing");
		}
		try {
			centerXs = visionTable.getNumberArray("centerX", defaultValue);
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("centerX missing");
		}
		try {
			centerYs = visionTable.getNumberArray("centerY", defaultValue);
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("centerY missing");
		}
		try {
			widths = visionTable.getNumberArray("width", defaultValue);
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("widths missing");
		}

	}

	// Sends information to the driver
	private void print() {
		SmartDashboard.putNumber("Y Axis", thisStick.getRawAxis(2));
		SmartDashboard.putNumber("Right Trigger", thisStick.getRawAxis(3));
		SmartDashboard.putNumber("Left Trigger", thisStick.getRawAxis(4));
		SmartDashboard.putNumber("move value", moveValue);
		SmartDashboard.putNumber("turn value", turnValue);
	}

	// Handle action arm related inputs and outputs
	private void lifter() {

		if (innerBounds.get()) {
			if (thisStick.getRawAxis(5) < 0) {
				lifter.set(0);
			} else {
				lifter.set(thisStick.getRawAxis(5));
			}

		} else if (outerBounds.get()) {
			if (thisStick.getRawAxis(5) > 0) {
				lifter.set(0);
			} else {
				lifter.set(thisStick.getRawAxis(5));
			}

		} else {
			lifter.set(thisStick.getRawAxis(5));
		}
	}

	//The Autonomous for starting at the low bar, driving through, turning, and shooting into the high goal
	public void autoLowBar() {
		phase = 0;
		gyro.reset();
		autoClock.stop();
		autoClock.reset();
		while (isAutonomous() && isEnabled()) {
			switch (phase) {
			case 0:
				// Drive through low bar
				drive.arcadeDrive(.8, gyro.getAngle());
				if (acc.getZ() > 1.2) {
					autoClock.start();
				}
				if (autoClock.get() > 1) {
					phase++;
				}
				break;
			case 1:
				// After through low bar, drive until XXX Distance from the wall
				if (ultra.getRangeInches() > distanceToStopFromTower) {
					drive.arcadeDrive(.7, 0);
				}
				break;
			case 2:
				// Turn XXX Degrees to face tower
				drive.arcadeDrive(0, .6);
				if (gyro.getAngle() > 1) {
					drive.arcadeDrive(0, 0);
					phase++;
				}
				break;
			case 3:
				// Drive closer to tower until XXX Distance
				drive.arcadeDrive(distanceToStopFromTower, 0);
				break;
			case 4:
				// Check alignment with camera

				// If good got to phase 6
				// If bad adjust

				// Find best contour to assume is the goal
				int bestCantidateIndex = 0;

				if (areas.length > 1) {

					for (int i = 1; i < widths.length; i++) {
						// Looking or a 10:7 ratio(ratio of reflective tape on
						// goal), width to height
						double previousRatio = widths[bestCantidateIndex]
								/ heights[bestCantidateIndex];
						double newRatio = widths[i] / heights[i];
						double previousError = Math.abs(widthToHeightRatio
								- previousRatio)
								/ widthToHeightRatio;
						double newError = Math.abs(widthToHeightRatio
								- newRatio)
								/ widthToHeightRatio;
						if (previousError > newError) {
							bestCantidateIndex = i;
						}
					}

				} else if (areas.length == 1) {
					bestCantidateIndex = 0;
				} else {
					System.out.println("NO CONTOURS FOUND");
				}

				try {
					adjustValue = 150 - centerXs[bestCantidateIndex] / 150;
					if (adjustValue < .4 && adjustValue > -.4) {
						phase = 6;
					} else {
						autoClock.reset();
						phase++;
					}
					break;
				} catch (Exception e) {
					System.out.println(e);
				}

				break;
			case 5:
				// adjust angle
				// go to case 4
				drive.arcadeDrive(0, adjustValue);
				if (autoClock.get() > .3) {
					drive.arcadeDrive(0, 0);
					phase = 4;
					break;
				}
				break;
			case 6:
				// newIntake.set(DoubleSolenoid.Value.kReverse);
				Timer.delay(.5);
				// shooter.shootTheCat(true);
				// shoot

			}
		}
	}

}
