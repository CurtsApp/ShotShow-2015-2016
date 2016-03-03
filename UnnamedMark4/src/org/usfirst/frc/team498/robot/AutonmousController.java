package org.usfirst.frc.team498.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.Timer;


public class AutonmousController {
	public BuiltInAccelerometer acc;
	private Timer clock;

	private Drive2016 drive;
	private IntakeAndShooter2016 accessories;
	private LifterArm2016 lifter;


	private int phase = 0;



	AutonmousController(Drive2016 drive_a,
			IntakeAndShooter2016 intakeAndShooter, LifterArm2016 lifter_a,
			 Ports ports) {
		drive = drive_a;
		accessories = intakeAndShooter;
		lifter = lifter_a;
		
		clock = new Timer();
	}

	public void run(AutoSelector key) {
		switch (key) {
		case LowBar:
			break;
		}
	}

	// The Autonomous for starting at the low bar, driving through, turning, and
	// shooting into the high goal
	public void autoInit(int startPhase) {
		lifter.retract();
		accessories.extendIntake();
		phase = startPhase;
		clock.stop();
		clock.reset();
	}

	public void autoLowBar() {

		switch (phase) {
		case -1:
			// Move Arm Down
			lifter.extend();
			clock.start();
			phase++;

			break;
		case 0:
			// Drive through low bar

			drive.manualDrive(-.8, 0);
			if (clock.get() > 6) {
				phase = 40;
				drive.manualDrive(0, 0);
			}
			break;
		
		case 40:
			break;

		}
	}
}
