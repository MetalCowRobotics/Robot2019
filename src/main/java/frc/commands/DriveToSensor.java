package frc.commands;

import frc.lib14.MCRCommand;
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

    public DriveToSensor(double howClose) {
        this.howClose = howClose;
    }

    public void run() {
        switch (currentState) {
        case IDLE:
            drivetrain.resetGyro();
            drivetrain.arcadeDrive(RobotMap.DriveToSensor.TOP_SPEED, 0);
            currentState = ACTIVE;
            break;
        case ACTIVE:
            System.out.println("Active");
            drivetrain.arcadeDrive(RobotMap.DriveToSensor.TOP_SPEED, 0);
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

}