package org.usfirst.frc.team498.robot;



import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class VisionManager2016 {
	NetworkTable visionTable;
	NetworkTable newTable;
	
	double[] defaultValue = new double[0];
	public double[] areas;
	public double[] heights;
	public double[] widths;
	public double[] centerXs;
	public double[] centerYs;
	
	double widthToHeightRatio = 10 / 7;
	String filename = "/home/lvuser/project.grip";	VisionManager2016() {
		
		/* Run GRIP in a new process */
		try {
        	//new ProcessBuilder("/home/lvuser/grip").inheritIO().start();
            SmartDashboard.putString("Grip", "Started");
            System.out.println("Grip Process Started"); //Print to RioLog
        } catch (Exception e) {
            SmartDashboard.putString("Grip", "Is Bad News");
            e.printStackTrace();
        }
		visionTable = NetworkTable.getTable("GRIP/myContoursReport");
		newTable = visionTable;
	}
	
	public int getBestCantidateIndex() {
		// Check alignment with camera

		// If good got to phase 6
		// If bad adjust

		// Find best contour to assume is the goal
		int bestCantidateIndex = 0;
		
		if (areas.length > 1) {

			for (int i = 1; i < widths.length; i++) {
				// Looking or a 10:7 ratio(ratio of reflective tape on
				// goal), width to height
				double previousRatio = widths[bestCantidateIndex]
						/ heights[bestCantidateIndex];
				double newRatio = widths[i] / heights[i];
				double previousError = Math.abs(widthToHeightRatio
						- previousRatio)
						/ widthToHeightRatio;
				double newError = Math.abs(widthToHeightRatio
						- newRatio)
						/ widthToHeightRatio;
				if (previousError > newError) {
					bestCantidateIndex = i;
				}
			}

		} else if (areas.length == 1) {
			bestCantidateIndex = 0;
		}	else {
			return 666;
		}
		
		return bestCantidateIndex;
	}
	public void updateTables() {
		try {
			areas = visionTable.getNumberArray("area", defaultValue);
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("area missing");

		}
		try {
			heights = visionTable.getNumberArray("height", defaultValue);
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("height missing");
		}
		try {
			centerXs = visionTable.getNumberArray("centerX", defaultValue);
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("centerX missing");
		}
		try {
			centerYs = visionTable.getNumberArray("centerY", defaultValue);
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("centerY missing");
		}
		try {
			widths = visionTable.getNumberArray("width", defaultValue);
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("widths missing");
		}

	}
}
