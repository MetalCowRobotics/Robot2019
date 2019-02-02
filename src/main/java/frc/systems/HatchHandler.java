package frc.systems;

import java.util.logging.Logger;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import frc.robot.RobotDashboard;
import frc.robot.RobotMap;

public class HatchHandler {
    private static final MasterControls controller = MasterControls.getInstance();
    private static final Logger logger = Logger.getLogger(HatchHandler.class.getName());
    private static final HatchHandler instance = new HatchHandler();
    private DoubleSolenoid arm = new DoubleSolenoid(2, 3);
    private DoubleSolenoid grabber = new DoubleSolenoid(0,1);
    private RobotDashboard dash = RobotDashboard.getInstance();

    enum ArmStatus {
        extended, retracted
    }

    enum ClawStatus {
        grab, release
    }

    private ArmStatus armStatus = ArmStatus.retracted;
    // TODO: Grab or Release?
    private ClawStatus clawStatus = ClawStatus.release;

    private HatchHandler() {
        arm.set(DoubleSolenoid.Value.kOff);
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
            grabber.set(DoubleSolenoid.Value.kReverse);   
        }
        if (controller.release()){
            grabber.set(DoubleSolenoid.Value.kForward);
        }

    }

    private void retract() {
            arm.set(DoubleSolenoid.Value.kForward);
            armStatus = ArmStatus.retracted;
    }

    public void extend() {
            arm.set(DoubleSolenoid.Value.kReverse);
            armStatus = ArmStatus.extended;
    }

    // grab and release hatch handler
    // TODO: Reverse or Forward?
    public void grabClaw() {
        claw.set(DoubleSolenoid.Value.kReverse);
        clawStatus = ClawStatus.grab;
    }

    public void releaseClaw() {
        claw.set(DoubleSolenoid.Value.kForward);
        clawStatus = ClawStatus.release;
    }
}
