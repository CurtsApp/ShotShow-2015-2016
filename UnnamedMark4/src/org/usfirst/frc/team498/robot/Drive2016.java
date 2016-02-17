package org.usfirst.frc.team498.robot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;


public class Drive2016 {
	private Joystick thisStick;
	private RobotDrive drive;
	private RampManager forwardDriveRamp;
	private boolean isGoingForward = true;
	RampManager turningDriveRamp;
	double moveValue;
	double turnValue;
	
	Drive2016(Joystick joystick, Ports ports) {
	thisStick = joystick;
	drive = new RobotDrive(ports.leftDrivePWMPort, ports.rightDrivePWMPort);
	forwardDriveRamp = new RampManager(ports.forwardRampIncreaseValue);
	turningDriveRamp = new RampManager(ports.turningRampIncreaseValue);
	}
	// The robot's speed slowly increases over time.
		public void rampedDriveListener() {
			// Axis 3 is RT Axis 2 is LT
			forwardDriveRamp.rampTo(thisStick.getRawAxis(3)
					- thisStick.getRawAxis(2));
			moveValue = forwardDriveRamp.getCurrentValue();
			// Axis 0 is X Value of Left Stick
			turningDriveRamp.rampTo(-thisStick.getRawAxis(0));
			turnValue = turningDriveRamp.getCurrentValue();
			reverseListener();
			drive();
			

		}
		public void ramplessDriveListener() {
			  moveValue = thisStick.getRawAxis(3) - thisStick.getRawAxis(2);
			 turnValue = -thisStick.getRawAxis(0);
			 reverseListener();
			 drive();
		}
		private void reverseListener() {
			if(thisStick.getRawButton(5)) {
				isGoingForward = true;
			} else if(thisStick.getRawButton(6)) {
				isGoingForward = false;
			}
		}
		private void drive() {
			if(isGoingForward) {
				drive.arcadeDrive(moveValue, turnValue);
			} else {
				drive.arcadeDrive(-moveValue, turnValue);
			}
		}
		
		public void manualDrive(double move, double turn) {
			drive.arcadeDrive(move,turn);
		}

}
