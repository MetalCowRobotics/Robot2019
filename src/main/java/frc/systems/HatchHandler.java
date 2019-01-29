package frc.systems;

import java.util.logging.Logger;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import frc.robot.RobotDashboard;

public class HatchHandler {
    private static final MasterControls controller = MasterControls.getInstance();
    private static final Logger logger = Logger.getLogger(HatchHandler.class.getName());
    private static final HatchHandler instance = new HatchHandler();
    private DoubleSolenoid arm = new DoubleSolenoid(0, 1);
    private RobotDashboard dash = RobotDashboard.getInstance();
    private DoubleSolenoid claw = new DoubleSolenoid(2, 3);
    // private DoubleSolenoid grabber=new DoubleSolenoid(3,4);

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
    }

    public static HatchHandler getInstance() {
        return instance;
    }

    public void execute() {
        dash.pushElevatorLimits(true, false);
        if (controller.isExtended()) {
            extend();
        }
        if (controller.isRetracted()) {
            retract();
        }
        if (controller.isClawToggled()) {
            switch (clawStatus) {
            case grab:
                releaseClaw();
                break;
            case release:
                grabClaw();
                break;
            }
        }
    }

    private void retract() {
        // if (ArmStatus.extended == armStatus) {
        arm.set(DoubleSolenoid.Value.kForward);
        armStatus = ArmStatus.retracted;
        // }
        System.out.print("retract");
    }

    public void extend() {
        // if (ArmStatus.extended == armStatus) {
        arm.set(DoubleSolenoid.Value.kReverse);
        armStatus = ArmStatus.extended;
        // }
        System.out.print("extend");
    }

    // grab and release hatch handler
        //TODO: Reverse or Forward?
    public void grabClaw() {
        claw.set(DoubleSolenoid.Value.kReverse);
        clawStatus = ClawStatus.grab;
    }

    public void releaseClaw() {
        claw.set(DoubleSolenoid.Value.kForward);
        clawStatus = ClawStatus.release;
    }
}
