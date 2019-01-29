package frc.systems;

import java.util.logging.Logger;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lib14.UtilityMethods;
import frc.lib14.XboxControllerMetalCow;
import frc.robot.RobotMap;

public class MasterControls {
	private static final Logger logger = Logger.getLogger(MasterControls.class.getName());
	private static final double throttleVariance = .14;
	private static final MasterControls instance = new MasterControls();

	private static final XboxControllerMetalCow driver = new XboxControllerMetalCow(RobotMap.DriverController.USB_PORT);
	private static final XboxControllerMetalCow operator = new XboxControllerMetalCow(
			RobotMap.OperatorController.USB_PORT);

	private MasterControls() {
		// Intentionally Blank for Singleton
		logger.setLevel(RobotMap.LogLevels.masterControlsClass);
	}

	public static MasterControls getInstance() {
		return instance;
	}

	public boolean isSprintToggle() {
		return driver.getXButton();
	}

	public boolean isCrawlToggle() {
		return driver.getAButton();
	}

	public boolean invertDrive() {
		return driver.getYButtonPressed();
	}

	public double getElevatorThrottle() {
		return UtilityMethods.deadZoneCalculation(operator.getRY());
		//return (Math.abs(operator.getRY()) > throttleVariance) ? operator.getRY() : 0;
	}

	public void intakeRumbleOn() {
		operator.rumbleLeft(0.5);
	}

	public void intakeRumbleOff() {
		operator.rumbleLeft(0);
	}

	public double forwardSpeed() {
		return driver.getRT();
	}

	public double reverseSpeed() {
		return driver.getLT();
	}

	public double direction() {
		return driver.getLX();
	}

	public boolean isExtended() {
		if (UtilityMethods.between(operator.getPOV(), 0, 89)) {
			return true;
		}
		if (UtilityMethods.between(operator.getPOV(), 271, 360)) {
			return true;
		}
		return false;
	}

	public boolean isRetracted() {
		if (UtilityMethods.between(operator.getPOV(), 91, 269)) {
			return true;
		}
		return false;
	}
	public boolean isClawToggled() {
		return operator.getBButton();
	}

	public boolean isBallIntake() {
		return operator.getRB();
	}

	public boolean isBallEject() {
		return operator.getLB();
	}
}
