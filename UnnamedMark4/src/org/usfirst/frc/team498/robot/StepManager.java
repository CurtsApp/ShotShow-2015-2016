package org.usfirst.frc.team498.robot;

import edu.wpi.first.wpilibj.Timer;

public class StepManager {
	AutonmousController robot;
	// AUTO STEPS
	public AutoStep putArmDown = new AutoStep() {

		@Override
		public int run() {
			// TODO Auto-generated method stub
			robot.lifter.extend();
			return 1;
		}

	};
	AutoStep driveThroughLowBar = new AutoStep() {

		@Override
		public int run() {
			robot.drive.manualDrive(-.8, robot.gyro.getAngle());
			if (robot.acc.getZ() > robot.accelorometerPeak) {
				robot.clock.start();
			}
			if (robot.clock.get() > 1) {
				return 1;
			}
			return 0;
		}

	};
	AutoStep driveUntilDistanceFromWall = new AutoStep() {

		@Override
		public int run() {
			if (robot.ultra.getRangeInches() > robot.distanceToStopFromWall) {
				robot.drive.manualDrive(robot.straightDriveSpeed,
						robot.gyro.getAngle());
				return 0;
			} else {
				return 1;
			}
		}

	};
	AutoStep turnDegrees = new AutoStep() {

		@Override
		public int run() {
			robot.drive.manualDrive(0, .6);
			if (robot.gyro.getAngle() > robot.angleToTurnTowardsTower) {
				robot.drive.manualDrive(0, 0);
				return 1;
			}
			return 0;
		}

	};

	AutoStep driveUntilDistanceFromTower = new AutoStep() {
		@Override
		public int run() {
			if (robot.ultra.getRangeInches() > robot.distanceToStopFromTower) {
				robot.drive.manualDrive(robot.straightDriveSpeed,
						robot.gyro.getAngle());
				return 0;
			} else {
				return 1;
			}
		}

	};
	AutoStep checkGoalPosition = new AutoStep() {

		@Override
		public int run() {
			robot.goalIndex = robot.vm.getBestCantidateIndex();

			try {
				robot.adjustValue = 150 - robot.vm.centerXs[robot.goalIndex] / 150;
				if (robot.adjustValue < .4 && robot.adjustValue > -.4) {
					return 2;
				} else {
					robot.clock.reset();
					return 1;
				}
			} catch (Exception e) {
				System.out.println(e);
			}
			return 0;

		}

	};
	AutoStep adjustAlignment = new AutoStep() {

		@Override
		public int run() {
			robot.drive.manualDrive(0, robot.adjustValue);
			if (robot.clock.get() > .3) {
				robot.drive.manualDrive(0, 0);
				return -2;

			}
			return 0;
		}

	};
	AutoStep fire = new AutoStep() {

		@Override
		public int run() {
			robot.accessories.extendIntake();
			Timer.delay(.5);
			robot.accessories.overrideShoot();
			return 1;
		}

	};
	AutoStep finished = new AutoStep() {

		@Override
		public int run() {
			return 0;
		}

	};
	AutoStep lowBarInit = new AutoStep() {

		@Override
		public int run() {
			robot.gyro.reset();
			robot.clock.stop();
			robot.clock.reset();
			return 1;
		}

	};

	StepManager(AutonmousController auto) {
		robot = auto;
	}
}
