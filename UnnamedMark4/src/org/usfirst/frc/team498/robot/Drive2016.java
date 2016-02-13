package org.usfirst.frc.team498.robot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;


public class Drive2016 {
	private Joystick thisStick;
	private RobotDrive drive;
	private RampManager forwardDriveRamp;
	RampManager turningDriveRamp;
	double moveValue;
	double turnValue;
	
	Drive2016(Joystick joystick, int leftDrivePWM, int rigthDrivePWM, double forwardRampIncreaseValue, double turningRampIncreaseValue) {
	thisStick = joystick;
	drive = new RobotDrive(leftDrivePWM, rigthDrivePWM);
	forwardDriveRamp = new RampManager(forwardRampIncreaseValue);
	turningDriveRamp = new RampManager(turningRampIncreaseValue);
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
			drive();
			

		}
		public void ramplessDriveListener() {
			  moveValue = thisStick.getRawAxis(3) - thisStick.getRawAxis(2);
			 turnValue = -thisStick.getRawAxis(0);
			 drive();
		}
		private void drive() {
			drive.arcadeDrive(moveValue, turnValue);
		}
		
		public void manualDrive(double move, double turn) {
			drive.arcadeDrive(move,turn);
		}

}
