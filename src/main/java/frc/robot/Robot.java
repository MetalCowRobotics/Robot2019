/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

//import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import frc.autonomous.ClimbToLevel2;
import frc.autonomous.ExitHabitatLevel1;
import frc.autonomous.ExitHabitatLevel2;
import frc.commands.DriveBackwardsStraight;
import frc.commands.DriveStraightInches;
import frc.commands.DriveToSensor;
import frc.commands.TurnDegrees;
import frc.commands.DriveStraightInches.DRIVE_DIRECTION;
import frc.commands.DriveToSensor.SENSOR_DIRECTION;
import frc.lib14.MCRCommand;
import frc.lib14.SequentialCommands;
import frc.systems.CargoHandler;
import frc.systems.Climber;
import frc.systems.DriveTrain;
import frc.systems.Elevator;
import frc.systems.HatchHandler;
import frc.systems.MasterControls;

import java.util.logging.Logger;

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

  MCRCommand autonomousCommand;
  SendableChooser autoChooser;

  private MCRCommand mission;
  private MCRCommand climbMission ;//= new ClimbToLevel2();

  // Field Systems
  private DriverStation driverStation;
  private RobotDashboard dash;
  
  // Robot Systems
  //Compressor c = new Compressor();
  DriveTrain driveTrain;
  Elevator elevator;
  HatchHandler hatchHandler;
  CargoHandler cargoHandler;
  MasterControls controllers;
  Climber climber;

  private boolean isAuto = false;
  private boolean endGameInitiated = false;
  //private DigitalInput distanceSensor = new DigitalInput(3);
  DigitalInput test = new DigitalInput(2);

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
    // hatchHandler = HatchHandler.getInstance();
    //climber = Climber.getInstance();
    //cargoHandler = CargoHandler.getInstance();
    controllers = MasterControls.getInstance();
    // climbMission = new ClimbToLevel2();
    // dash.initializeDashboard();


    //calibrate Gyro
    //driveTrain.calibrateGyro();

    // start the camera feed
    UsbCamera camera = CameraServer.getInstance().startAutomaticCapture(0);
    camera.setResolution(640, 480);
    //start the compressor
    //c.setClosedLoopControl(true);

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
    isAuto=true;
    // mission = new ExitHabitatLevel1();
    // mission = new DriveToSensor(12);
    //DriveStraightInches driveBackwards = new DriveStraightInches(DRIVE_DIRECTION.backward, 48.00);
    //DriveStraightInches driveForwards = new DriveStraightInches(DRIVE_DIRECTION.forward, 48);
    //driveForwards = new DriveStraightInches(DRIVE_DIRECTION.forward, v);
    mission = new TurnDegrees(-180);
    
  }
  public DriveStraightInches driveForward(double targetInches) {
    return new DriveStraightInches(DRIVE_DIRECTION.forward, targetInches);
  }
  public DriveStraightInches driveBackward(double targetInches) {
    return new DriveStraightInches(DRIVE_DIRECTION.backward, targetInches);
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    commonPeriodic();
    Scheduler.getInstance().run();
  }

  private void commonPeriodic() {
    endGameInitiated = !controllers.climberControls();
    SmartDashboard.putBoolean("Lidar", test.get());
    if (isAuto) {
      if (autonomousCommand.isFinished()) {
        isAuto = false;
        autonomousCommand = null;
      } else {
        autonomousCommand.run();
      }
    } else {
      // logger.info("Teleop Periodic!");
      driveTrain.drive();
      elevator.execute();
      // hatchHandler.execute();
      // climber.execute();
      //cargoHandler.execute();
    }
    if (controllers.autoClimb()) {
      climbMission.run();
    }
  }

  @Override
  public void teleopInit() {

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