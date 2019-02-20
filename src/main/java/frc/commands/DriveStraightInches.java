package frc.commands;

import java.util.logging.Logger;

import frc.lib14.MCRCommand;
import frc.lib14.PDController;
import frc.lib14.UtilityMethods;
import frc.robot.RobotMap;
import frc.systems.DriveTrain;

public class DriveStraightInches extends TimedCommand implements MCRCommand {
    private static final Logger logger = Logger.getLogger(DriveStraightInches.class.getName());
    private boolean firstTime = true;
    private boolean finished = false;
    private double startTics;
    private double targetTics;
    public int dir = 1;
    protected PDController driveController;
    private static final DriveTrain driveTrain = DriveTrain.getInstance();

    public DriveStraightInches(DRIVE_DIRECTION direction, double targetInches) {
        initialize(direction, targetInches);
    }

    public DriveStraightInches(double targetInches) {
        initialize(DRIVE_DIRECTION.forward, targetInches);
    }

    public DriveStraightInches(double targetInches, int timeoutSeconds) {
        initialize(DRIVE_DIRECTION.forward, targetInches);
        setTargetTime(timeoutSeconds);
    }

	private void initialize(DRIVE_DIRECTION direction, double targetInches) {
        logger.setLevel(RobotMap.LogLevels.autoDriveClass);
		switch(direction) {
            case forward:
                dir = 1;
                break;
            case backward:
            dir = -1;
            break;
        }
        setTarget(targetInches);
    }

    public enum DRIVE_DIRECTION {
        forward, backward
    }

    private void setTarget(double targetInches) {
        targetTics = (targetInches / RobotMap.DriveWithEncoder.INCHES_PER_ROTATION)
                * RobotMap.DriveWithEncoder.TICS_PER_ROTATION;
        logger.info("target for encoder drive: <<" + targetTics + ">>");
    }

    public void run() {
        if (firstTime) {
            firstTime = false;
            startTimer();
            driveTrain.resetGyro();
            driveController = new PDController(driveTrain.getAngle());
            startTics = driveTrain.getEncoderTics();
        }
        if (ticsTravelled() < targetTics) {
            driveTrain.arcadeDrive(calculateSpeed(), getCorrection() * dir);
        } else {
            end();
        }
    } 

    private void end() {
        driveTrain.stop();
        endTimer();
        finished = true;
    }

    private double ticsTravelled() {
        logger.info("Drivetrain tics travelled: " + (driveTrain.getEncoderTics() - startTics));
        return Math.abs(driveTrain.getEncoderTics() - startTics);
    }

    private double calculateSpeed() {
        if (targetTics - RobotMap.DriveWithEncoder.SLOW_DOWN_DISTANCE < ticsTravelled())
            return RobotMap.DriveWithEncoder.BOTTOM_SPEED * dir;
        return RobotMap.DriveWithEncoder.TOP_SPEED * dir;
    }

    private double getCorrection() {
        return limitCorrection(driveController.calculateAdjustment(driveTrain.getAngle()),
                RobotMap.DriveWithEncoder.MAX_ADJUSTMENT);
    }

    private double limitCorrection(double correction, double maxAdjustment) {
        if (Math.abs(correction) > Math.abs(maxAdjustment))
            return UtilityMethods.copySign(correction, maxAdjustment);
        return correction;
    }

    public boolean isFinished() {
        if (timerUp()) {
            end();
            return true;
        }
        return finished;
    }
}