package org.usfirst.frc.team4795.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.interfaces.Accelerometer.Range;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.Gyro;

/**
 *
 */
public class IMU extends Subsystem {
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

	double startingAngle = 0.0;
	private Gyro gyro;
	private BuiltInAccelerometer accelerometer;
	private double filteredAngle = 0.0;


	public IMU(AnalogInput port, double sensitivity) {
		gyro = new Gyro(port);
		gyro.initGyro();
		gyro.setSensitivity(sensitivity);
		accelerometer = new BuiltInAccelerometer(Range.k2G);
		
		
	}
	
	public double getRotation() {
		return gyro.getRate();
	}
	
	public double getAngle() {
		return gyro.getAngle();
	}
	
	public double getPitch() {

		double y = accelerometer.getY();
		double z = accelerometer.getZ();

		return Math.atan(y / z) * 180.0 / Math.PI;
	}
	
	public void setFilteredAngle(double angle) {
		filteredAngle = angle;
	}
	
	public double getFilteredAngle() {
		return filteredAngle;
	}
	
	
	
	public void reset() {
		gyro.reset();
		gyro.initGyro();
	}
	
	public void initDefaultCommand() {
//		setDefaultCommand(new IntegrateGyro());
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
	Thread t;
	public void filter() {
		
		double timeConstant = 0.75;
		int dt = 10;
		final double alpha = (double) timeConstant / (timeConstant + (double) dt / 1000);
		SmartDashboard.putNumber("TC", timeConstant);
		SmartDashboard.putNumber("dt", dt);
		SmartDashboard.putNumber("Alpha", alpha);
    	t = new Thread() {
    		double x = 0.0,
    				y = 0.0,
    				z = 0.0;
    		double pitch = 0.0;
    		@Override
    		public void run() {
    			while(true) {
	    			
    				x = accelerometer.getX();
    				y = accelerometer.getY();
    				z = accelerometer.getZ();
    				
	    			double magnitude = x * x + y * y + z * z; 
	    			if(Math.abs(getPitch()) < 30 && Math.abs(1 - magnitude) < 0.5) {
	    				pitch = getPitch();
	    			}
	    			double angle = getFilteredAngle();
	    			angle = alpha * (angle + gyro.getAngle()) + (1 - alpha) * pitch;
	    			setFilteredAngle(angle);
	    			gyro.reset();
	    			try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    			}
    			
    		}
    		
    	};
    	
    	t.start();
	}
}

