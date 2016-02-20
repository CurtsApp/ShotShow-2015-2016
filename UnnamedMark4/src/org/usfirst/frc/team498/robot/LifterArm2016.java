package org.usfirst.frc.team498.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;


public class LifterArm2016 {
	// Handle action arm related inputs and outputs
	FancyJoystick thisStick;
	DoubleSolenoid lifter;

	
	LifterArm2016(FancyJoystick joystick,Ports ports) {
		thisStick = joystick;
		lifter = new DoubleSolenoid(ports.lifterCylinderExtendPort,ports.lifterCylinderRetractPort);

	}
	public void baseListener() {

			if(thisStick.getAxis(Axis.RightY) > .5) {
				setLifter(true);
			} else if(thisStick.getAxis(Axis.RightY) <.5) {
				setLifter(false);
			}
		}
	public void extend() {
		lifter.set(DoubleSolenoid.Value.kForward);
	}
	public void retract() {
		lifter.set(DoubleSolenoid.Value.kReverse);
	}
	public void setLifter(boolean isForward){
		if(isForward) {
			extend();
		} else {
			retract();
		}
	}
	
	private DoubleSolenoid.Value getLifterState() {
		return lifter.get();
	}
	
	
}
