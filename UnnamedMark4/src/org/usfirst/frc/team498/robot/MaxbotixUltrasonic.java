package org.usfirst.frc.team498.robot;


import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.SensorBase;

public class MaxbotixUltrasonic extends SensorBase {
	DigitalOutput activator;
	AnalogInput sensorOutput;
	private double CM_TO_IN_CONVERSION = 2.54;
	private double mV_To_MM_CONVERSION = 0.977; //~0.977 mV per 1 mm  Accurate Value is Vcc/5120 per mm (Vcc is the power supply 5V)
	double min_voltage = 293; //Measured in mV
	double max_voltage = 4.885 * 1000; //Scaled to mV 
	double voltage_range = max_voltage - min_voltage;
	
	MaxbotixUltrasonic(int input_a, int output_a) {
		
		activator = new DigitalOutput(output_a);
		sensorOutput= new AnalogInput(input_a);
		activator.set(true);
	}
	
	
	public void stopReading() {
		activator.set(false);
	}
	public void startReading() {
		activator.set(true);
	}
	public double getRangeMM() {
		double range = sensorOutput.getVoltage();
		if(range < min_voltage) {
			return -1; //A number for debugging purposes
		}
		
		//Convert to mV
		range = range * 1000;
		//Convert to mm
		range = range * mV_To_MM_CONVERSION;
		return range;
	}
	public double getRangeInches() {
		//Get range in MM 
		double range = getRangeMM();
		//Convert to CM
		range = range / 1000;
		//Convert to IN
		range = range * CM_TO_IN_CONVERSION;
		return range;
	}
}

