package org.usfirst.frc.team498.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;

public class AutonmousController {
	private ADXRS450_Gyro gyro;
	private BuiltInAccelerometer acc;
	private Timer clock;
	private Ultrasonic ultra;
	private Drive2016 drive;
	private IntakeAndShooter2016 accesories;
	private VisionManager2016 vm;
	private LifterArm2016 lifter;
	
	private int goalIndex;
	private int phase = 0;


	// AUTONOMOUS CONSTANTS
	private double distanceToStopFromWall = 20;
	private double distanceToStopFromTower = 15;
	private double angleToTurnTowardsTower = 30;

	private double adjustValue;
	
	AutonmousController(Drive2016 drive_a, IntakeAndShooter2016 intakeAndShooter, LifterArm2016 lifter_a, VisionManager2016 grip,int ultrasonicInputPort, int ultrasonicOutputPort) {
		drive = drive_a;
		accesories = intakeAndShooter;
		lifter = lifter_a;
		ultra = new Ultrasonic(ultrasonicInputPort, ultrasonicOutputPort);
		vm = grip;
	}
	
	//The Autonomous for starting at the low bar, driving through, turning, and shooting into the high goal
		public void autoInit(int startPhase) {
			phase = startPhase;
			gyro.reset();
			clock.start();
			clock.reset();
		}
		public void autoLowBar() {
			
			switch (phase) {
			case -1:
				// Move Arm Down
				lifter.setLifter(.6);
				if(clock.get() > 2){
					lifter.setLifter(0);
					clock.stop();
					clock.reset();
					phase++;
				}
				
				break;
			case 0:
					// Drive through low bar
					
					drive.manualDrive(.8, gyro.getAngle());
					if (acc.getZ() > 1.2) {
						clock.start();
					}
					if (clock.get() > 1) {
						phase++;
					}
					break;
				case 1:
					// After through low bar, drive until XXX Distance from the wall
					if (ultra.getRangeInches() > distanceToStopFromWall) {
						drive.manualDrive(.7, 0);
					}
					break;
				case 2:
					// Turn XXX Degrees to face tower
					drive.manualDrive(0, .6);
					if (gyro.getAngle() > angleToTurnTowardsTower) {
						drive.manualDrive(0, 0);
						phase++;
					}
					break;
				case 3:
					// Drive closer to tower until XXX Distance
					drive.manualDrive(distanceToStopFromTower, 0);
					break;
				case 4:
					 goalIndex = vm.getBestCantidateIndex();

					try {
						adjustValue = 150 - vm.centerXs[goalIndex] / 150;
						if (adjustValue < .4 && adjustValue > -.4) {
							phase = 6;
						} else {
							clock.reset();
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
					drive.manualDrive(0, adjustValue);
					if (clock.get() > .3) {
						drive.manualDrive(0, 0);
						phase = 4;
						break;
					}
					break;
				case 6:
					accesories.extendIntake();
					Timer.delay(.5);
					accesories.overrideShoot();
					

				}
		}
}
