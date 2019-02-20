/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.util.logging.Logger;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.autonomous.ClimbToLevel2;
import frc.autonomous.ExitHabitatLevel1;
import frc.autonomous.ExitHabitatLevel2;
import frc.lib14.MCRCommand;
import frc.systems.CargoHandler;
import frc.systems.Climber;
import frc.systems.DriveTrain;
import frc.systems.Elevator;
import frc.systems.HatchHandler;
import frc.systems.MasterControls;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
// public class Robot extends IterativeRobot {
public class Robot extends TimedRobot {
  private static final Logger logger = Logger.getLogger(Robot.class.getName());

  SendableChooser autoChooser;

  private MCRCommand mission;
  private MCRCommand climbMission;// = new ClimbToLevel2();

  // Field Systems
  private DriverStation driverStation;
  private RobotDashboard dash;

  // Robot Systems
  Compressor c = new Compressor();
  DriveTrain driveTrain;
  Elevator elevator;
  HatchHandler hatchHandler;
  CargoHandler cargoHandler;
  MasterControls controllers;
  Climber climber;

  private boolean isAuto = false;

  // private boolean endGameInitiated = false;

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    logger.setLevel(RobotMap.LogLevels.robotClass);

    // Initialize Robot
    driverStation = DriverStation.getInstance();
    dash = RobotDashboard.getInstance();
    driveTrain = DriveTrain.getInstance();
    elevator = Elevator.getInstance();
    hatchHandler = HatchHandler.getInstance();
    climber = Climber.getInstance();
    cargoHandler = CargoHandler.getInstance();
    controllers = MasterControls.getInstance();
    climbMission = new ClimbToLevel2();
    // dash.initializeDashboard();

    // calibrate Gyro
    driveTrain.calibrateGyro();

    // start the camera feed
    UsbCamera camera = CameraServer.getInstance().startAutomaticCapture(0);
    camera.setResolution(640, 480);
    // start the compressor
    c.setClosedLoopControl(true);
    dash.pushAuto();

    autoChooser = new SendableChooser();
    autoChooser.addObject("ExitHabitatLevel1", new ExitHabitatLevel1());
    autoChooser.addDefault("ExitHabitatLevel2", new ExitHabitatLevel2());
    SmartDashboard.putData("Autonomous mode chooser", autoChooser);

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
    if (dash.getAuto()) {
      isAuto = true;
      mission = new ExitHabitatLevel2();  
    } 
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    commonPeriodic();
  }

  private void commonPeriodic() {
    controllers.changeMode();
    if (isAuto) {
      if (mission.isFinished()) {
        isAuto = false;
        mission = null;
      } else {
        mission.run();
      }
    } else if (controllers.autoClimb()) {
      climbMission.run();
    } else {
      driveTrain.drive();
      elevator.execute();
      hatchHandler.execute();
      climber.execute();
      cargoHandler.execute();
    }
    dash.pushEdgeSensor(climber.getSensor());
  }

  @Override
  public void teleopInit() {
    climbMission = new ClimbToLevel2();
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    commonPeriodic();
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