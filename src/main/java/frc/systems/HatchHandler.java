package frc.systems;

import java.util.logging.Logger;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import frc.robot.RobotMap;
import frc.robot.RobotMap.Hatch;

public class HatchHandler {
    private static final MasterControls controller = MasterControls.getInstance();
    private static final Logger logger = Logger.getLogger(HatchHandler.class.getName());
    private static final Elevator elevator = Elevator.getInstance();
    private static final HatchHandler instance = new HatchHandler();
    private DoubleSolenoid arm = new DoubleSolenoid(Hatch.ARM_FOWARD, Hatch.ARM_REVERSE);
    private DoubleSolenoid grabber = new DoubleSolenoid(Hatch.GRABBER_FOWARD, Hatch.GRABBER_REVERSE);

    enum ArmStatus {
        extended, retracted
    }

    enum ClawStatus {
        grab, release
    }

    private ArmStatus armStatus = ArmStatus.retracted;

    private ClawStatus clawStatus = ClawStatus.grab;

    private HatchHandler() {
        grab();
        logger.setLevel(RobotMap.LogLevels.hatchHandlerClass);
    }

    public static HatchHandler getInstance() {
        return instance;
    }

    public void execute() {
        if (controller.isExtended()) {
            extend();
        }
        if (controller.isRetracted()) {
            retract();
        }
        if (controller.grab()) {
            if (ClawStatus.release == clawStatus) {
                grab();
            } else {
                release();
            }
        }
    }

    private void release() {
        grabber.set(DoubleSolenoid.Value.kReverse);
        clawStatus = ClawStatus.release;
    }

    private void grab() {
        grabber.set(DoubleSolenoid.Value.kForward);
        clawStatus = ClawStatus.grab;
        elevator.setHatchMode(true);
    }

    public void retract() {
        if (ArmStatus.extended == armStatus) {
            arm.set(DoubleSolenoid.Value.kForward);
            armStatus = ArmStatus.retracted;
        }
    }

    public void extend() {
        arm.set(DoubleSolenoid.Value.kReverse);
        armStatus = ArmStatus.extended;
    }

    // grab and release hatch handler
    public void grabClaw() {
        grabber.set(DoubleSolenoid.Value.kReverse);
        clawStatus = ClawStatus.grab;
    }

    public void releaseClaw() {
        grabber.set(DoubleSolenoid.Value.kForward);
        clawStatus = ClawStatus.release;
    }
}
