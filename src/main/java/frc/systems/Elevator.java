package frc.systems;

import java.util.logging.Logger;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lib14.MCR_SRX;
import frc.lib14.PDController;
import frc.lib14.UtilityMethods;
import frc.robot.RobotDashboard;
import frc.robot.RobotMap;

public class Elevator {
	private static final Logger logger = Logger.getLogger(Elevator.class.getName());
	private static final RobotDashboard dash = RobotDashboard.getInstance();
	private static final MasterControls controller = MasterControls.getInstance();
	private static final MCR_SRX motor1 = new MCR_SRX(RobotMap.Elevator.ELEVATOR_CHANNEL1);
	private static final SpeedControllerGroup ELEVATOR_MOTOR = new SpeedControllerGroup(motor1);
	private static final DigitalInput topLimit = new DigitalInput(RobotMap.Elevator.LIMIT_SWITCH_TOP);
	private static final DigitalInput bottomLimit = new DigitalInput(RobotMap.Elevator.LIMIT_SWITCH_BOTTOM);
	private static final Elevator instance = new Elevator();
	boolean firstTime = true;
	double bottomTics;
	double topTics;
	PDController holdPID;

	private Elevator() {
		// Singleton Pattern
		logger.setLevel(RobotMap.LogLevels.elevatorClass);
		dash.pushHatchValues();
	}

	public static Elevator getInstance() {
		return instance;
	}

	public void execute() {
		logger.info("================== elevator iteration ==============================");
		logger.info("Elevator Up: " + this.isElevatorAtTop() + " Elevator Down: " + this.isElevatorAtBottom());
		logger.info("elevator encoder tics:" + getEncoderTics());
		if (firstTime) {
			firstTime = false;
			ELEVATOR_MOTOR.setInverted(true); //may need to be comment out
			bottomTics = getEncoderTics();
			topTics = bottomTics + inchesToTics(RobotMap.Elevator.ELEVATOR_MAX_EXTEND);
			holdPID = new PDController(bottomTics, dash.getElevatorKP(), dash.getElevatorKD());
		}
		if (0 == controller.getElevatorThrottle()) {
			// System.out.println("^^^^^^ Holding ^^^^^^");
			// if (controller.upLevel()) {
			// dash.pushElevatorTarget((dash.getElevatorTarget() +
			// RobotMap.Elevator.HATCH_LEVEL));

			// } else {
			// // holdPID.setSetPoint(dash.getElevatorTarget());
			// }
			// if (controller.downLevel()) {
			// dash.pushElevatorTarget((dash.getElevatorTarget() -
			// RobotMap.Elevator.HATCH_LEVEL));
			// } else {
			// // holdPID.setSetPoint(dash.getElevatorTarget());
			// }
			getElevatorTarget();
			holdPID.set_kP(dash.getElevatorKP());
			holdPID.set_kD(dash.getElevatorKD());
			setElevatorSpeed(holdPID.calculateAdjustment(getEncoderTics()));
			//stop(); //xtra
			//holdPID.calculateAdjustment(getEncoderTics()); //xtra
			dash.pushElevatorPID(holdPID);
		} else {
			setElevatorSpeed(controller.getElevatorThrottle());
			setPositionTics(getEncoderTics());
		}
		dash.pushElevatorPID(holdPID);
		dash.pushElevatorLimits(topLimit.get(), bottomLimit.get());
		dash.pushElevatorEncoder(getEncoderTics());
		dash.pushElevatorTarget(holdPID.getSetPoint());
	}

	public void setPositionTics(double tics) {
		holdPID.setSetPoint(tics);
		holdPID.set_kP(dash.getElevatorKP());
		holdPID.set_kD(dash.getElevatorKD());
		holdPID.reset();
	}

	public void setPosition(double inches) {
		setPositionTics(inchesToTics(inches));
	}

	private double inchesToTics(double inches) {
		return (inches / RobotMap.Elevator.INCHES_PER_ROTATION) * RobotMap.Elevator.TICS_PER_ROTATION;
	}

	public void setElevatorSpeed(double speed) {
		if (isMovingUp(speed) && isElevatorAtTop()) {
			stop();
		} else if (isMovingDown(speed) && isElevatorAtBottom()) {
			stop();
		} else {
			ELEVATOR_MOTOR.set(maxSpeed(speed));
		}
		SmartDashboard.putNumber("setSpeed", maxSpeed(speed));
	}

	private boolean isMovingUp(double speed) {
		return speed > 0;
	}

	private boolean isMovingDown(double speed) {
		return speed < 0;
	}

	private double maxSpeed(double speed) {
		if (isMovingUp(speed) && inUpperSafetyZone()) {
			return Math.min(speed, RobotMap.Elevator.SafeSpeed);
		} else if (isMovingDown(speed) && inLowerSafetyZone()) {
			return Math.max(speed, -RobotMap.Elevator.SafeSpeed);
		} else {
			return UtilityMethods.copySign(speed, Math.min(Math.abs(speed), .7)); //xtra
		}
	}

	private boolean inLowerSafetyZone() {
		return getEncoderTics() > (topTics - RobotMap.Elevator.SafeZone);
	}

	private boolean inUpperSafetyZone() {
		return getEncoderTics() < (bottomTics + RobotMap.Elevator.SafeZone);
	}

	public void stop() {
		ELEVATOR_MOTOR.stopMotor();
	}

	public boolean isAtHeight(double heightInches) {
		return UtilityMethods.between(inchesToTics(heightInches), getEncoderTics() - 10, getEncoderTics() + 10);
	}

	private boolean isElevatorAtTop() {
		return !topLimit.get(); // For some reason this is inverted in the hardware, correcting here in software
	}

	private boolean isElevatorAtBottom() {
		if (!bottomLimit.get()) {
			bottomTics = getEncoderTics();
			topTics = bottomTics + inchesToTics(RobotMap.Elevator.ELEVATOR_MAX_EXTEND);
		}
		dash.pushElevatorBottom(bottomTics);
		return !bottomLimit.get(); // for some reason this is inverted in hardware, correcting here in software
	}

	public double getEncoderTics() {
		return motor1.getSelectedSensorPosition();
	}

	private void logParameters() {
		logger.info("Elevator throttle:" + controller.getElevatorThrottle());
		logger.info(
				"Elevator Up limit: " + this.isElevatorAtTop() + " Elevator Down limit: " + this.isElevatorAtBottom());
		logger.info("Speed:" + ELEVATOR_MOTOR.get());
		logger.info("Elevator encoder tics:" + getEncoderTics());
		logger.info("Elevator bottom tics:" + bottomTics + "   Elevator top tics:" + topTics);
	}

	private void logPID() {
		logger.info("PID error:" + holdPID.getSetPoint());
		logger.info("PID error:" + holdPID.getError());
	}

	private void getElevatorTarget() {
		logger.info("current distance: " + (getEncoderTics() - bottomTics) + " <> ");
		if (controller.upLevel()) {
			if ((getEncoderTics() - bottomTics) < RobotMap.Elevator.HATCH_LEVEL_2) {
				setPositionTics(RobotMap.Elevator.HATCH_LEVEL_2 + bottomTics);
				// dash.pushElevatorTarget(RobotMap.Elevator.HATCH_LEVEL_2 + bottomTics);
			} else {
				setPositionTics(RobotMap.Elevator.HATCH_LEVEL_3 + bottomTics);
				// dash.pushElevatorTarget(RobotMap.Elevator.HATCH_LEVEL_3 + bottomTics);
			}
		}
		if (controller.downLevel()) {
			if ((getEncoderTics() - bottomTics) > RobotMap.Elevator.HATCH_LEVEL_2) {
				setPositionTics(RobotMap.Elevator.HATCH_LEVEL_2 + bottomTics);
				// dash.pushElevatorTarget(RobotMap.Elevator.HATCH_LEVEL_1 + bottomTics);
			} else {
				setPositionTics(RobotMap.Elevator.HATCH_LEVEL_1 + bottomTics);
				// dash.pushElevatorTarget(RobotMap.Elevator.HATCH_LEVEL_2 + bottomTics);
			}
		}
	}
}