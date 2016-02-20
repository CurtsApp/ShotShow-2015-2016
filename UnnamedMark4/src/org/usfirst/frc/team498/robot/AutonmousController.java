package org.usfirst.frc.team498.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;

public class AutonmousController {
	public ADXRS450_Gyro gyro;
	public BuiltInAccelerometer acc;
	private Timer clock;
	private MaxbotixUltrasonic ultra;
	private Drive2016 drive;
	private IntakeAndShooter2016 accessories;
	public VisionManager2016 vm;
	private LifterArm2016 lifter;
	
	private int goalIndex;
	private int phase = 0;


	// AUTONOMOUS CONSTANTS
	private double distanceToStopFromWall = 20;
	private double distanceToStopFromTower = 15;
	private double angleToTurnTowardsTower = 30;
	private double accelorometerPeak = 2.8;

	private double adjustValue;
	
	AutonmousController(Drive2016 drive_a, IntakeAndShooter2016 intakeAndShooter, LifterArm2016 lifter_a, VisionManager2016 grip,Ports ports) {
		drive = drive_a;
		accessories = intakeAndShooter;
		lifter = lifter_a;
		ultra = new MaxbotixUltrasonic(ports.ultrasonicDIOInputPort, ports.ultrasonicDIOOutputPort);
		vm = grip;
		
		gyro = new ADXRS450_Gyro();
		acc = new BuiltInAccelerometer();
		clock = new Timer();
	}
	
	
	public void run(AutoSelector key) {
		switch(key) {
		case LowBar:
			break;
		}
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
				if(clock.get() > .8){
					lifter.setLifter(0);
					clock.stop();
					clock.reset();
					phase++;
				}
				
				break;
			case 0:
					// Drive through low bar
					
					drive.manualDrive(-.8, 0 /*gyro.getAngle()*/);
					if (acc.getZ() > accelorometerPeak) {
						clock.start();
					}
					if (clock.get() > 1) {
						phase=40;
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
					accessories.extendIntake();
					Timer.delay(.5);
					accessories.overrideShoot();
					break;
				case 40:
					break;

				}
		}
}
