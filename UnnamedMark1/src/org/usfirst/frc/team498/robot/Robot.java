package org.usfirst.frc.team498.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/*
 * 
 * 
 *  ULTRASONIC TESTS
 * 
 * 
 * 
 */


public class Robot extends SampleRobot {
	Ultrasonic ultra = new Ultrasonic(0,1);
	Joystick test = new Joystick(0);
	
	

	public void print() {
		SmartDashboard.putNumber("Ultrasonic Inches", ultra.getRangeInches());
		SmartDashboard.putBoolean("Valid Range?", ultra.isRangeValid());
		SmartDashboard.putBoolean("Ultrasonic enabled?", ultra.isEnabled());
		
 }
	public void operatorControl() {

		while (isOperatorControl() && isEnabled()) {
			if (test.getRawButton(1)){
				ultra.ping();
			}
			
			print();
		}

	}

	public void disabled() {
		print();
	}
			
	
}
