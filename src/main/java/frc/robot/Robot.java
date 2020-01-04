/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.lib14.XboxControllerMetalCow;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
// public class Robot extends IterativeRobot {
public class Robot extends TimedRobot {
  private static final XboxControllerMetalCow controller = new XboxControllerMetalCow(1);
	private static SpeedController rightFrontMotor = new Spark(0);
	private static SpeedController rightBackMotor = new Spark(1); 
	private static SpeedController leftFrontMotor = new Spark(3);
	private static SpeedController leftBackMotor = new Spark(2); 
	private static final SpeedControllerGroup RIGHT_DRIVE_MOTORS = new SpeedControllerGroup(rightFrontMotor, rightBackMotor); 
	private static final SpeedControllerGroup LEFT_DRIVE_MOTORS = new SpeedControllerGroup(leftFrontMotor, leftBackMotor);
	private static final DifferentialDrive driveTrain = new DifferentialDrive(LEFT_DRIVE_MOTORS, RIGHT_DRIVE_MOTORS);

  // Field Systems
  private DriverStation driverStation = DriverStation.getInstance();

  // Robot Systems
  PowerDistributionPanel pdp;

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
   // rightBackMotor.setInverted(true);
   // leftBackMotor.setInverted(true);
    // pdp = new PowerDistributionPanel();
    DriverStation.reportWarning("ROBOT SETUP COMPLETE!  Ready to Rumble!", false);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for
   * items like diagnostics that you want ran during disabled, autonomous,
   * teleoperated and test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before LiveWindow
   * and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable chooser
   * code works with the Java SmartDashboard. If you prefer the LabVIEW Dashboard,
   * remove all of the chooser code and uncomment the getString line to get the
   * auto name from the text box below the Gyro
   *
   * <p>
   * You can add additional auto modes by adding additional comparisons to the
   * switch structure below with additional strings. If using the SendableChooser
   * make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {

  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {

  }

  @Override
  public void teleopInit() {

  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    driveTrain.arcadeDrive(controller.getLY(), controller.getLX());
    //driveTrain.curvatureDrive(controller.getLY(), controller.getLX(), false);
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
  // write a "back to the pit" self-check script here
  // something we an run that moves all the mechanisms one at a time or tests
  // sensors
  // like asks for us to press limit switches so we know they are still wired in
  // System.out.println("angle: " + driveTrain.getAngle());

}
