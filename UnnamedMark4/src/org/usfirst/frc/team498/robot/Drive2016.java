package org.usfirst.frc.team498.robot;


import edu.wpi.first.wpilibj.RobotDrive;

public class Drive2016 {
	private FancyJoystick thisStick;
	private RobotDrive drive;
	private RampManager forwardDriveRamp;
	private boolean isGoingForward = true;
	private boolean isSpeedReduced = false;
	private boolean wasTransmitionPressed = false;
	double speedCap;

	RampManager turningDriveRamp;
	public double moveValue;
	public double turnValue;

	Drive2016(FancyJoystick joystick, Ports ports) {
		thisStick = joystick;
		drive = new RobotDrive(ports.leftDrivePWMPort, ports.rightDrivePWMPort);
		forwardDriveRamp = new RampManager(ports.forwardRampIncreaseValue);
		turningDriveRamp = new RampManager(ports.turningRampIncreaseValue);
		speedCap = ports.speedCap;
	}

	// The robot's speed slowly increases over time.
	public void rampedDriveListener() {
		// Axis 3 is RT Axis 2 is LT
		forwardDriveRamp.rampTo(thisStick.getAxis(Axis.RightTrigger) - thisStick.getAxis(Axis.LeftTrigger));
		moveValue = forwardDriveRamp.getCurrentValue();
		// Axis 0 is X Value of Left Stick
		turningDriveRamp.rampTo(-thisStick.getAxis(Axis.LeftX));
		turnValue = turningDriveRamp.getCurrentValue();
		transmitionListener();
		reverseListener();
		drive();

	}

	public void ramplessDriveListener() {
		moveValue = thisStick.getAxis(Axis.RightTrigger)
				- thisStick.getAxis(Axis.LeftTrigger);
		turnValue = -thisStick.getAxis(Axis.LeftX);
		transmitionListener();
		reverseListener();
		drive();
	}

	private void reverseListener() {
		if (thisStick.getButton(Button.LeftBumper)) {
			isGoingForward = true;
		} else if (thisStick.getButton(Button.RightBumper)) {
			isGoingForward = false;
		}
	}

	private void transmitionListener() {
		if (thisStick.getButton(Button.X) && !wasTransmitionPressed) {
			isSpeedReduced = !isSpeedReduced;
			wasTransmitionPressed = true;
		}
		if (wasTransmitionPressed && thisStick.getButton(Button.X)) {
			wasTransmitionPressed = false;
		}
	}

	private void drive() {
		double moveValue_f;
		double turnValue_f;
		if (isSpeedReduced) {
			moveValue_f = moveValue * speedCap;
			turnValue_f = turnValue * speedCap;
		} else {
			moveValue_f = moveValue;
			turnValue_f = turnValue;
		}
		if (isGoingForward) {
			drive.arcadeDrive(moveValue_f, turnValue_f);
		} else {
			drive.arcadeDrive(-moveValue_f, turnValue_f);
		}
	}

	public void manualDrive(double move, double turn) {
		drive.arcadeDrive(move, turn);
	}

}
