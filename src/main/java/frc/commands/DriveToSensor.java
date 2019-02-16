package frc.commands;

import frc.lib14.MCRCommand;
import frc.lib14.PDController;
import frc.lib14.UtilityMethods;
import frc.robot.RobotMap;
import frc.systems.Climber;
import frc.systems.DriveTrain;
import java.util.logging.Logger;

public class DriveToSensor implements MCRCommand {
    private static final Logger logger = Logger.getLogger(DriveToSensor.class.getName());
    private DriveTrain drivetrain = DriveTrain.getInstance();
    private Climber climber = Climber.getInstance();
    private int dir = 1;
    private boolean firstTime = true;
    private boolean done = false;
    protected PDController driveController;
    private SENSOR_DIRECTION direction;

    // direction: 1 = forward, -1 = backwards
    public DriveToSensor(SENSOR_DIRECTION direction) {
        logger.setLevel(RobotMap.LogLevels.autoDriveClass);
        // this.direction = direction;
        switch (direction) {
        case forward:
            dir = 1;
            break;
        case backward:
            dir = -1;
            break;
        }
    }

    public enum SENSOR_DIRECTION {
        forward, backward
    };

    public void run() {
        if (firstTime) {
            firstTime = false;
            // drivetrain.resetGyro();
            driveController = new PDController(drivetrain.getAngle());
        }
        logger.info("Driving and edge sensor =" + ledgeSensor());
        drivetrain.arcadeDrive(RobotMap.DriveToSensor.TOP_SPEED * dir, getCorrection());
        if (ledgeSensor()) {
            drivetrain.stop();
            done = true;
        }
    }

    private boolean ledgeSensor() {
        return climber.getSensor();
    }

    @Override
    public boolean isFinished() {
        return done;
    }

    private double getCorrection() {
        // logger.info("Drivetrain angle: " + driveTrain.getAngle());
        return limitCorrection(driveController.calculateAdjustment(drivetrain.getAngle()), RobotMap.DriveWithEncoder.MAX_ADJUSTMENT);
    }

    private double limitCorrection(double correction, double maxAdjustment) {
        if (Math.abs(correction) > Math.abs(maxAdjustment))
            return UtilityMethods.copySign(correction, maxAdjustment);
        return correction;
    }
}