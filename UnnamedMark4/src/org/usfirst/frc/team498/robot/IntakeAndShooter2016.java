package org.usfirst.frc.team498.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;

public class IntakeAndShooter2016 {

	FancyJoystick thisStick;
	DoubleSolenoid intakeLeft;
	DoubleSolenoid intakeRight;
	CANTalon roller;
	DoubleSolenoid shooter1;
	DoubleSolenoid shooter2;

	// Shooter Controllers
	public Timer clock;
	public boolean isShooterCoolingDown;
	// Roller Controllers
	private boolean isRollerRunning;
	private boolean wasRollerPressed = false;
	private boolean wasShootPressed = false;
	private boolean wasIntakePressed = false;

	IntakeAndShooter2016(FancyJoystick joystick, Ports ports) {
		thisStick = joystick;
		clock = new Timer();
		roller = new CANTalon(ports.intakeCANID);
		intakeRight = new DoubleSolenoid(ports.rightIntakeCylinderExtendPort,
				ports.rightIntakeCylinderRetractPort);
		intakeLeft = new DoubleSolenoid(ports.leftIntakeCylinderExtendPort,
				ports.leftIntakeCylinderRetractPort);
		clock.start();
		// 6 and 7 are unused slots on PCM
		shooter1 = new DoubleSolenoid(ports.shooterLeftPort, 6);
		shooter2 = new DoubleSolenoid(ports.shooterRightPort, 7);
		wasRollerPressed = false;
		isRollerRunning = false;
	}

	public void rollerListener() {
		if (thisStick.getButton(Button.A) && wasRollerPressed == false) {
			isRollerRunning = !isRollerRunning;
			wasRollerPressed = true;
		}
		if (wasRollerPressed == true && thisStick.getButton(Button.A) == false) {
			wasRollerPressed = false;
		}

		if (thisStick.getButton(Button.START)) {
			turnRollerReverse();
		} else if (isRollerRunning == true) {
			turnRollerOn();
		} else {
			turnRollerOff();
		}

	}

	public void shooterListener() {
		if (thisStick.getButton(Button.X)) {
			shoot();
			wasShootPressed = true;
		}

		if (wasShootPressed && !thisStick.getButton(Button.Y)) {
			wasShootPressed = false;
		}
		if (thisStick.getButton(Button.SELECT)) {
			retractShooter();
		}

		if (isShooterCoolingDown) {
			if (clock.get() > 1) {
				isShooterCoolingDown = false;
				retractShooter();
			}
		}

	}

	private void shoot() {

		if (intakeLeft.get() == DoubleSolenoid.Value.kForward
				&& !wasShootPressed) {
			overrideShoot();
			isShooterCoolingDown = true;
			clock.reset();
		} else {
			extendIntake();

		}
	}

	/*
	 * ONLY FOR USE IN AUTONMOUS
	 */
	public void overrideShoot() {
		shooter1.set(DoubleSolenoid.Value.kForward);
		shooter2.set(DoubleSolenoid.Value.kForward);

	}

	public void retractShooter() {
		shooter1.set(DoubleSolenoid.Value.kReverse);
		shooter2.set(DoubleSolenoid.Value.kReverse);
	}

	public void turnRollerOn() {
		roller.set(1);
	}

	public void turnRollerOff() {
		roller.set(0);
	}

	public void turnRollerReverse() {
		roller.set(-1);
	}

	public void extendIntake() {
		intakeLeft.set(DoubleSolenoid.Value.kForward);
		intakeRight.set(DoubleSolenoid.Value.kForward);
	}

	public void retractIntake() {
		intakeLeft.set(DoubleSolenoid.Value.kReverse);
		intakeRight.set(DoubleSolenoid.Value.kReverse);
	}

	public void intakeListener() {
		if (thisStick.getButton(Button.B) && wasIntakePressed == false) {
			if (intakeLeft.get() == DoubleSolenoid.Value.kForward) {
				retractIntake();
			} else {
				extendIntake();
			}
			wasIntakePressed = true;
		}
		if (wasIntakePressed == true && thisStick.getButton(Button.B) == false) {
			wasIntakePressed = false;
		}

	}

}
