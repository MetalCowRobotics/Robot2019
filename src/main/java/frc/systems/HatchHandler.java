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
    // private DoubleSolenoid grabber=new DoubleSolenoid(3,4);

    enum ArmStatus {
        extended, retracted
    }

    private ArmStatus armStatus = ArmStatus.retracted;

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

}
