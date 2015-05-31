	
package org.usfirst.frc.team4795.robot;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.CANJaguar.NeutralMode;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team4795.robot.subsystems.IMU;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	public static OI oi;
	
	public static IMU imu;
	
	public static Timer timer;
	private PrintWriter writer;
	private boolean open = false;
	
    Command autonomousCommand;
    AnalogInput gyroInput;
    CANJaguar left;
    CANJaguar right;
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
		oi = new OI();
        // instantiate the command used for the autonomous period
        timer = new Timer();
       
        gyroInput = new AnalogInput(0);
        imu = new IMU(gyroInput, 7/1000);
//        left = new CANJaguar(1);
//        right = new CANJaguar(2);
        
       
        

        
    }
    
    double offset;
    int counter;
    double sum;
    public void disabledInit(){
    	imu.reset();
    	counter = 0;
    	sum = 0;
    	if(open) {
    		writer.close();
    	}
    		
    }
    

	
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
		sum += imu.getPitch();
		counter++;
		
		
	}

    public void autonomousInit() {
        // schedule the autonomous command (example)
        if (autonomousCommand != null) autonomousCommand.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    public void teleopInit() {
		// This makes sure that t	he autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) autonomousCommand.cancel();
        imu.setFilteredAngle(imu.getPitch());
        imu.filter();
       

        
        
    }

    /**
     * This function is called when the disabled button is hit.
     * You can use it to reset subsystems before shutting down.
     */
    
    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        SmartDashboard.putNumber("Gyro angle", imu.getAngle());
        SmartDashboard.putNumber("Accelerometer angle", imu.getPitch());
        SmartDashboard.putNumber("Filtered Angle", imu.getFilteredAngle());	
//        writer.println(imu.getAngle() + "," + imu.getPitch() + "," + imu.getFilteredAngle());	   
    }
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
}
