package frc.commands;

import frc.lib14.MCRCommand;
import frc.lib14.PDController;
import frc.lib14.UtilityMethods;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.systems.Climber;
import frc.systems.DriveTrain;
import java.util.logging.Logger;

import edu.wpi.first.wpilibj.DigitalInput;

public class DriveToSensor implements MCRCommand {
    private DriveTrain drivetrain = DriveTrain.getInstance();
    private Climber climber = Climber.getInstance();
    private int dir = 1;
    private boolean firstTime = true;
    private boolean done = false;
    protected PDController driveController;
    private SENSOR_DIRECTION direction;

    // direction: 1 = forward, -1 = backwards
    public DriveToSensor(SENSOR_DIRECTION direction) {
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
        drivetrain.arcadeDrive(RobotMap.DriveToSensor.TOP_SPEED * dir, getCorrection() * dir);
        if (ledgeSensor()) {
            drivetrain.stop();
            done = true;
        }

    }

    private boolean ledgeSensor() {
        return !climber.getSensor();
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