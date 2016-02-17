package org.usfirst.frc.team498.robot;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;


public class IntakeAndShooter2016 {
	
	Joystick thisStick;
	DoubleSolenoid intakeLeft;
	DoubleSolenoid intakeRight;
	CANTalon roller;
	DoubleSolenoid shooter1;
	DoubleSolenoid shooter2;
	
	
	
	//Shooter Controllers
	public Timer clock;
	public boolean isShooterCoolingDown;
	//Roller Controllers
	private boolean isRollerRunning;
	private boolean wasButtonPressed;
	IntakeAndShooter2016(Joystick joystick, Ports ports){
	thisStick = joystick;
	clock = new Timer();
	roller = new CANTalon(ports.intakeCANID);
	intakeRight = new DoubleSolenoid(ports.rightIntakeCylinderExtendPort, ports.rightIntakeCylinderRetractPort);
	intakeLeft = new DoubleSolenoid(ports.leftIntakeCylinderExtendPort, ports.leftIntakeCylinderRetractPort);
	clock.start();
	//6 and 7 are unused slots on PCM
	shooter1 = new DoubleSolenoid(ports.shooterLeftPort, 6);
	shooter2 = new DoubleSolenoid(ports.shooterRightPort,7);
	wasButtonPressed = false;
	isRollerRunning = false;
	}
	
		public void rollerListener() {
			if(thisStick.getRawButton(1) && wasButtonPressed == false) {
				isRollerRunning = !isRollerRunning;
				wasButtonPressed = true;
			}
			if(wasButtonPressed == true && thisStick.getRawButton(1) == false) {
				wasButtonPressed = false;
			}
			
			
			if(thisStick.getRawButton(8)){
				turnRollerReverse();
			} else if(isRollerRunning == true) {
				turnRollerOn();
			}else{
				turnRollerOff();
			}
			
			
		}
		
		public void shooterListener() {
			if(thisStick.getRawButton(4)) {
				shoot();
			}
			if(thisStick.getRawButton(7)) {
				retractShooter();
			}
			if(isShooterCoolingDown) {
				if(clock.get() > 2) {
					isShooterCoolingDown = false;
					retractShooter();
				}
			}
		}
		
		private void shoot(){
			
			if(intakeLeft.get() == DoubleSolenoid.Value.kForward) {
				overrideShoot();
				isShooterCoolingDown = true;
				clock.reset();
			}  else {
				extendIntake();
							
			}
		}
		
		/*
		 * ONLY FOR USE IN AUTONMOUS 
		 */
		public void overrideShoot() {
			shooter1.set(DoubleSolenoid.Value.kForward);
			shooter2.set(DoubleSolenoid.Value.kForward);
			
		}
		public void retractShooter() {
			shooter1.set(DoubleSolenoid.Value.kReverse);
			shooter2.set(DoubleSolenoid.Value.kReverse);
		}
		public void turnRollerOn(){
			roller.set(1);
		}
		public void turnRollerOff(){
			roller.set(0);
		}
		public void turnRollerReverse(){
			roller.set(-1);
		}
		public void extendIntake(){
			intakeLeft.set(DoubleSolenoid.Value.kForward);
			intakeRight.set(DoubleSolenoid.Value.kForward);
		}
		
		public void retractIntake(){
			intakeLeft.set(DoubleSolenoid.Value.kReverse);
			intakeRight.set(DoubleSolenoid.Value.kReverse);
		}
		public void intakeListener() {
			if(thisStick.getRawButton(3)){
				retractIntake();
				
			}else if(thisStick.getRawButton(2)){
				extendIntake();
				
			
			}
		}
		
		
		
		

		
}
