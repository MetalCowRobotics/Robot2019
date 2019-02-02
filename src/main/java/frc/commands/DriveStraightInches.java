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
    private DriveTrain driveTrain = DriveTrain.getInstance();
    private DRIVE_DIRECTION direction;
    public DriveStraightInches(DRIVE_DIRECTION direction, double targetInches) {
        this.direction = direction;
        setTarget(targetInches);
    }
    public enum DRIVE_DIRECTION {
        forward, backward
    }

    public void DriveStraightInches(double targetInches) {
        setTarget(targetInches);
    }

    public DriveStraightInches(double targetInches, int timeoutSeconds) {
        setTarget(targetInches);
        setTargetTime(timeoutSeconds);
    }

    private void setTarget(double targetInches) {
        targetTics = (targetInches / RobotMap.DriveWithEncoder.INCHES_PER_ROTATION)
                * RobotMap.DriveWithEncoder.TICS_PER_ROTATION;
        logger.info("target encoder drive:" + targetTics);
    }

    public void run() {
        // case public void run() {
        switch(direction) {
            case forward:
                dir = 1;
                break;
            case backward:
                dir = -1;
                break;
        }
                //case end
        if (firstTime) {
            firstTime = false;
            startTimer();
            driveTrain.resetGyro();
            driveController = new PDController(driveTrain.getAngle());
            startTics = driveTrain.getEncoderTics();
        }
        if (ticsTravelled() < targetTics) {
            driveTrain.arcadeDrive(calculateSpeed(), getCorrection());
        } else {
            end();
        }
        // logger.warning("targetTics: " + targetTics + " <<|>> ticsTravelled: " +
        // ticsTravelled() + " <<|>> correction: " + getCorrection());
        logger.warning("angle: " + driveTrain.getAngle() + " <<|>> correction: " + getCorrection());
    } 

    private void end() {
        driveTrain.stop();
        endTimer();
        finished = true;
    }

    private double ticsTravelled() {
        // logger.info("Drivetrain current encoder tics: " +
        // (driveTrain.getEncoderTics() - startTics));
        return Math.abs(driveTrain.getEncoderTics() - startTics);
    }

    private double calculateSpeed() {
        if (targetTics - RobotMap.DriveWithEncoder.SLOW_DOWN_DISTANCE < ticsTravelled())
            return RobotMap.DriveWithEncoder.BOTTOM_SPEED * dir;
        return RobotMap.DriveWithEncoder.TOP_SPEED * dir;
    }

    private double getCorrection() {
        // logger.info("Drivetrain angle: " + driveTrain.getAngle());
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