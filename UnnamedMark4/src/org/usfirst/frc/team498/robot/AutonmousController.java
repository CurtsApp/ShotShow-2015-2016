package org.usfirst.frc.team498.robot;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.Timer;

public class AutonmousController {
	public ADXRS450_Gyro gyro;
	public BuiltInAccelerometer acc;
	public Timer clock;
	public MaxbotixUltrasonic ultra;
	public Drive2016 drive;
	public IntakeAndShooter2016 accessories;
	public VisionManager2016 vm;
	public LifterArm2016 lifter;
	private StepManager sm = new StepManager(this);

	public int goalIndex;
	private int phase = 0;
	private ArrayList<AutoStep> autoSequence;

	// AUTONOMOUS CONSTANTS
	public double distanceToStopFromWall = 20;
	public double distanceToStopFromTower = 15;
	public double angleToTurnTowardsTower = 30;
	public double accelorometerPeak = 2.8;
	public double straightDriveSpeed = .8;

	public double adjustValue;

	AutonmousController(Drive2016 drive_a,
			IntakeAndShooter2016 intakeAndShooter, LifterArm2016 lifter_a,
			VisionManager2016 grip, Ports ports) {
		drive = drive_a;
		accessories = intakeAndShooter;
		lifter = lifter_a;
		ultra = new MaxbotixUltrasonic(ports.ultrasonicDIOInputPort,
				ports.ultrasonicDIOOutputPort);
		vm = grip;

		gyro = new ADXRS450_Gyro();
		acc = new BuiltInAccelerometer();
		clock = new Timer();
	}

	public void setup(AutoSelector key) {
		switch (key) {
		case LowBar:
			autoSequence.add(sm.lowBarInit);
			autoSequence.add(sm.putArmDown);
			autoSequence.add(sm.driveThroughLowBar);
			autoSequence.add(sm.driveUntilDistanceFromWall);
			autoSequence.add(sm.turnDegrees);
			autoSequence.add(sm.driveUntilDistanceFromTower);
			autoSequence.add(sm.checkGoalPosition);
			autoSequence.add(sm.adjustAlignment);
			autoSequence.add(sm.fire);
			autoSequence.add(sm.finished);
			break;
		}
		execute();
	}

	// The Autonomous for starting at the low bar, driving through, turning, and
	// shooting into the high goal
	public void autoInit(int startPhase) {
		phase = startPhase;

	}

	public void execute() {
		phase = phase + autoSequence.get(phase).run();
	}
}
