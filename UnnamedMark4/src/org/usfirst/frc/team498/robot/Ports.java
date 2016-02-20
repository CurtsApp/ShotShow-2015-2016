package org.usfirst.frc.team498.robot;


public class Ports {

	int leftDrivePWMPort = 1;
	int rightDrivePWMPort = 0;
	double forwardRampIncreaseValue = 1.3;
	double turningRampIncreaseValue = 1.3;
	double speedCap = .8;
			
	//Accessories
	int leftIntakeCylinderExtendPort = 1;
	int leftIntakeCylinderRetractPort = 0;
	int rightIntakeCylinderExtendPort = 4;
	int rightIntakeCylinderRetractPort = 5; 
	int shooterLeftPort = 2;

	//Lifter
	int lifterCylinderExtendPort = 3;
	int lifterCylinderRetractPort = 7;
	int intakeCANID = 1;

	//Auto
	int ultrasonicDIOInputPort = 2;
	int ultrasonicDIOOutputPort = 3;
	
}
