package org.usfirst.frc.team498.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Talon;

public class Intake {

	Talon intakeMotor; // controlling the rollers on intake mechanism
	DoubleSolenoid intakeExtend;

	public Intake(int motorPwm,int solenoidA, int solenoidB) {
		intakeMotor = new Talon(motorPwm);
		intakeExtend = new DoubleSolenoid(solenoidA, solenoidB);
	}

	public void getBall(boolean buttonState, boolean limitState) {
		if (buttonState && !limitState) // button pressed AND limit is not
										// pressed
		{
			intakeMotor.set(1); // turn motor on
		} else {
			intakeMotor.set(0);
		}
	}
	
	public void extender(boolean buttonState) {
		//Press the botton to togggle the state of the extenderg.
		Value curVal = intakeExtend.get();
		Value newVal;

		if (buttonState) 
		{

			if (curVal.equals(DoubleSolenoid.Value.kForward))
				newVal =DoubleSolenoid.Value.kReverse;

			else if (curVal.equals(DoubleSolenoid.Value.kReverse))
				newVal = DoubleSolenoid.Value.kForward;
			
			else
				newVal = curVal;

		} 
		else
		{
			newVal = curVal;
		}

		intakeExtend.set(newVal);

	}
	// if limit and sesnor are not #triggered button make intake motor gooooo and git ball
	public void auto(Boolean buttonState, Boolean limitState, Boolean sensorState){
		if(limitState){
			intakeMotor.set(0);
		}else if(!limitState && !sensorState){
			intakeMotor.set(1);

		}
	}

}
