package org.usfirst.frc.team498.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class Shooter {
	DoubleSolenoid cat;
//cat=catapult
	//shootTheCat=shoot te catapult
	Shooter(int solenoidOut, int solenoidIn) {
		cat = new DoubleSolenoid(solenoidOut, solenoidIn);
	}
	//if the butt is pres rhwn extend the pistons if button is not pressed then reverse the pistons.
	public void shootTheCat(boolean button){
		if(button){
			cat.set(DoubleSolenoid.Value.kForward);
		}else
		{
			cat.set(DoubleSolenoid.Value.kReverse);
		}
	}
}
