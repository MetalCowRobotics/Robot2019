package frc.commands;

import frc.lib14.MCRCommand;
import frc.lib14.PDController;
import frc.lib14.UtilityMethods;
import frc.robot.RobotMap;
import frc.systems.DriveTrain;
import java.util.logging.Logger;

import edu.wpi.first.wpilibj.DigitalInput;

public class DriveToSensor implements MCRCommand {
    private DriveTrain drivetrain = DriveTrain.getInstance();
    private double howClose = 0;
    private int currentState = 0;
    private final int IDLE = 0;
    private final int ACTIVE = 1;
    private final int DONE = 2;
    private DigitalInput limit = new DigitalInput(8);
    protected PDController driveController;

    public DriveToSensor(double howClose) {
        this.howClose = howClose;
    }

    public void run() {
        switch (currentState) { 
        case IDLE:
            drivetrain.resetGyro();
            driveController = new PDController(drivetrain.getAngle());
            drivetrain.arcadeDrive(RobotMap.DriveToSensor.TOP_SPEED, getCorrection());
            currentState = ACTIVE;
            break;
        case ACTIVE:
            System.out.println("Active");
            drivetrain.arcadeDrive(RobotMap.DriveToSensor.TOP_SPEED, getCorrection());
            // TODO: when is the sensor on or off
            // if (howClose > ledgeSensor()) {
            if (ledgeSensor()) {
                drivetrain.stop();
                currentState = DONE;
            }
            break;
        case DONE:
            System.out.println("Done");
            break;
        }
    }

    private boolean ledgeSensor() {
        return !limit.get();
    }

    @Override
    public boolean isFinished() {
        return DONE == currentState;
    }
    private double getCorrection() {
        // logger.info("Drivetrain angle: " + driveTrain.getAngle());
        return limitCorrection(driveController.calculateAdjustment(drivetrain.getAngle()),
                RobotMap.DriveWithEncoder.MAX_ADJUSTMENT);
    }

    private double limitCorrection(double correction, double maxAdjustment) {
        if (Math.abs(correction) > Math.abs(maxAdjustment))
            return UtilityMethods.copySign(correction, maxAdjustment);
        return correction;
    }
}