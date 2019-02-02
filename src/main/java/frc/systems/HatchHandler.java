package frc.systems;

import java.util.logging.Logger;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import frc.robot.RobotDashboard;

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

}
