package org.usfirst.frc.team498.robot;


import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
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
	//Drive
	Ports ports = new Ports();
	FancyJoystick thisStick = new FancyJoystick(0);
	Drive2016 drive = new Drive2016(thisStick, ports);
	IntakeAndShooter2016 accessories = new IntakeAndShooter2016(thisStick, ports);
	LifterArm2016 lifter = new LifterArm2016(thisStick, ports);
	VisionManager2016 vm = new VisionManager2016();
	AutonmousController autoController = new AutonmousController(drive, accessories, lifter, vm, ports);
	PowerDistributionPanel pdp = new PowerDistributionPanel();
	
	
	SendableChooser sc = new SendableChooser();
	
	public void robotInit() {
		sc.addDefault("The default", AutoSelector.LowBar);
		sc.addObject("Better", AutoSelector.LowBar);
	}

	// Select which autonomous to run
	public void autonomous() {
		autoController.autoInit(-1);
		while(isAutonomous() && isEnabled()) {
			if((AutoSelector)sc.getSelected() == AutoSelector.LowBar) {
				autoController.autoLowBar();
			} else {
				System.out.println("Selector did not match any know pattern");
			}
			print();
			
		}
	}
	
	public void operatorControl() {
		accessories.retractShooter();
		accessories.retractIntake();
		drive.moveValue = 0;
		drive.turnValue = 0;
	
		while (isOperatorControl() && isEnabled()) {
			
			// Send stats to the driver
			print();
			// Drive the robot via controller
			drive.rampedDriveListener();
			// Listen for intake related commands
			accessories.intakeListener();
			// Listen for actionArm related commands
			accessories.rollerListener();
			// Listen for shooter related commands
			accessories.shooterListener();
			// Listen for lifter related commands
			lifter.baseListener();

		}

	}

	
	
	public void disabled() {
		while(isDisabled()) {
			print();
		} 
		
	}

	// Sends information to the driver
	private void print() {

		SmartDashboard.putNumber("2", pdp.getCurrent(2));

		SmartDashboard.putNumber("12", pdp.getCurrent(12));
		SmartDashboard.putNumber("13", pdp.getCurrent(13));
		SmartDashboard.putNumber("14", pdp.getCurrent(14));
		
		SmartDashboard.putBoolean("InnerBounds", lifter.innerBounds.get());
		SmartDashboard.putBoolean("isCoolingDown", accessories.isShooterCoolingDown);
		SmartDashboard.putNumber("Gyro angle", autoController.gyro.getAngle());
		SmartDashboard.putNumber("Accel X", autoController.acc.getX());
		SmartDashboard.putNumber("Accel Y", autoController.acc.getY());
		SmartDashboard.putNumber("Accel Z", autoController.acc.getZ());
		SmartDashboard.putNumber("Cooldown Clock", accessories.clock.get());
		SmartDashboard.putBoolean("InnerBound", lifter.innerBounds.get());
		SmartDashboard.putBoolean("Outer Bounds", lifter.outerBounds.get());
		//SmartDashboard.putData("Auto Selector", sc);
		
		autoController.vm.updateTables();
		
		try{
			SmartDashboard.putNumber("Area Length", autoController.vm.areas.length);
		} catch(Exception e) {
			System.out.println(e);
			System.out.println("areas is missing!!");
		}
		
	}

	

	
		
	

}
