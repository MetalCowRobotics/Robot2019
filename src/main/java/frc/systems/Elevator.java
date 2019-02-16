package frc.systems;

import java.util.logging.Logger;

import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lib14.MCR_SRX;
import frc.lib14.PIDController;
import frc.lib14.UtilityMethods;
import frc.robot.RobotDashboard;
import frc.robot.RobotMap;

public class Elevator {
	private static final Logger logger = Logger.getLogger(Elevator.class.getName());
	private static final RobotDashboard dash = RobotDashboard.getInstance();
	private static final MasterControls controller = MasterControls.getInstance();
	private static final MCR_SRX motor1 = new MCR_SRX(RobotMap.Elevator.ELEVATOR_CHANNEL1);
	private static final MCR_SRX motor2 = new MCR_SRX(RobotMap.Elevator.ELEVATOR_CHANNEL2);
	private static final SpeedControllerGroup ELEVATOR_MOTOR = new SpeedControllerGroup(motor1, motor2);
	private static final DigitalInput topLimit = new DigitalInput(RobotMap.Elevator.LIMIT_SWITCH_TOP);
	private static final DigitalInput bottomLimit = new DigitalInput(RobotMap.Elevator.LIMIT_SWITCH_BOTTOM);
	private static final Elevator instance = new Elevator();
	private boolean firstTime = true;
	private double bottomTics;
	private double topTics;
	private PIDController holdPID;
	public static boolean hatchMode = true;
	private int loggingIterations = 0;
	private int currentLevel = 1;

	public boolean getHatchMode() {
		return hatchMode;
	}

	public static void setHatchMode(boolean mode) {
		hatchMode = mode;
	}

	private Elevator() {
		// Singleton Pattern
		logger.setLevel(RobotMap.LogLevels.elevatorClass);
		motor1.configOpenloopRamp(RobotMap.Elevator.RAMP_SPEED);
		motor2.configOpenloopRamp(RobotMap.Elevator.RAMP_SPEED);
		motor1.setNeutralMode(NeutralMode.Brake);
		motor2.setNeutralMode(NeutralMode.Brake);
	}

	public static Elevator getInstance() {
		return instance;
	}

	public void execute() {
		SmartDashboard.putBoolean("DigitalSwitch", isElevatorAtTop());
		logParameters();
		if (firstTime) {
			firstTime = false;
			ELEVATOR_MOTOR.setInverted(true); // may need to be comment out on actual robot
			bottomTics = getEncoderTics();
			topTics = bottomTics + inchesToTics(RobotMap.Elevator.ELEVATOR_MAX_EXTEND);
			dash.pushElevatorTop(topTics);
			dash.pushElevatorBottom(bottomTics);
			holdPID = new PIDController(bottomTics, dash.getElevatorKP(), dash.getElevatorKI(), dash.getElevatorKD());
			// setPositionTics(bottomTics); //seeing if this helps with multiple runs
			dash.pushElevatorPIDValues();
		}
		if (controller.switchHeights()) {
			hatchMode = !hatchMode;
		}
		SmartDashboard.putBoolean("hatchmode", hatchMode);
		getElevatorTarget(); // check for level up and level down
		if (0 == controller.getElevatorThrottle()) {
			set_K_values();
			setElevatorSpeed(holdPID.calculateAdjustment(getEncoderTics()));
		} else {
			setElevatorSpeed(controller.getElevatorThrottle());
			setPositionTics(getEncoderTics());
		}
		dash.pushElevatorPID(holdPID);
		dash.pushElevatorEncoder(getEncoderTics());
		dash.pushElevatorLimits(isElevatorAtTop(), isElevatorAtBottom());
	}

	private double limitAdjustment(double adjustment) {
		return UtilityMethods.absMin(adjustment, .7);
	}

	private void setPositionTics(double tics) {
		holdPID.setSetPoint(tics);
		set_K_values();
		holdPID.reset();
	}

	private void set_K_values() {
		holdPID.set_kP(dash.getElevatorKP());
		holdPID.set_kI(dash.getElevatorKI());
		holdPID.set_kD(dash.getElevatorKD());
	}

	public void setPosition(double inches) {
		setPositionTics(inchesToTics(inches));
	}

	private double inchesToTics(double inches) {
		return (inches / RobotMap.Elevator.INCHES_PER_ROTATION) * RobotMap.Elevator.TICS_PER_ROTATION;
	}

	private void setElevatorSpeed(double speed) {
		if (isMovingUp(speed) && isElevatorAtTop()) {
			stop();
		} else if (isMovingDown(speed) && isElevatorAtBottom()) {
			stop();
		} else {
			ELEVATOR_MOTOR.set(maxSpeed(speed));
		}
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
			return Math.max(speed, -RobotMap.Elevator.DownSafeSpeed);
		} else {
			if (isMovingUp(speed)) {
				// TODO: add a variable to the robot map for max throttle
				return UtilityMethods.copySign(speed, Math.min(Math.abs(speed), .8));
			}
			return UtilityMethods.copySign(speed, Math.min(Math.abs(speed), .8));
		}
	}

	private boolean inLowerSafetyZone() {
		return getEncoderTics() < (bottomTics + RobotMap.Elevator.SafeZone);
	}

	private boolean inUpperSafetyZone() {
		return getEncoderTics() > (topTics - RobotMap.Elevator.SafeZone);

	}

	public void stop() {
		ELEVATOR_MOTOR.stopMotor();
	}

	public boolean isAtHeight(double heightInches) {
		return UtilityMethods.between(inchesToTics(heightInches), getEncoderTics() - 100, getEncoderTics() + 100);
	}

	private boolean isElevatorAtTop() {
		if (!topLimit.get()){
			currentLevel = 3;
		}
		return !topLimit.get(); // For some reason this is inverted in the hardware, correcting here in software
	}

	private boolean isElevatorAtBottom() {
		if (!bottomLimit.get()) {
			// reset bottom and top measures
			bottomTics = getEncoderTics();
			topTics = bottomTics + inchesToTics(RobotMap.Elevator.ELEVATOR_MAX_EXTEND);
			currentLevel = 1;
		}
		return !bottomLimit.get(); // for some reason this is inverted in hardware, correcting here in software
	}

	public double getEncoderTics() {
		return motor2.getSelectedSensorPosition();
	}

	private void logParameters() {
		loggingIterations++;
		if (20 < loggingIterations) {
			logger.info("Elevator throttle:" + controller.getElevatorThrottle());
			logger.info("Elevator Up limit: " + this.isElevatorAtTop() + " Elevator Down limit: "
					+ this.isElevatorAtBottom());
			logger.info("Elevator bottom tics:" + bottomTics + "   Elevator top tics:" + topTics);
			logger.info("Elevator Speed:" + ELEVATOR_MOTOR.get());
			logger.info("Elevator encoder tics:" + getEncoderTics());
			logPID();
			loggingIterations = 0;
		}
	}

	private void logPID() {
		logger.info("Elev PID set point:" + holdPID.getSetPoint());
		logger.info("Elev PID error:" + holdPID.getError());
	}

	private void getElevatorTarget() {
		// need to add a mode to switch between cargo and hatches in execute
		if (hatchMode) {
			determineLevel(RobotMap.Elevator.HATCH_LEVEL_1, RobotMap.Elevator.HATCH_LEVEL_2,
					RobotMap.Elevator.HATCH_LEVEL_3);
		} else {
			determineLevel(RobotMap.Elevator.BALL_HEIGHT_1, RobotMap.Elevator.BALL_HEIGHT_2,
					RobotMap.Elevator.BALL_HEIGHT_3);
		}
	}

	// private void determineLevel(double level1, double level2, double level3) {
	// double fudgeFactor = 50; // if the PID does not get it to height it will
	// always be lower and never go to the else
	// logger.info("current distance: " + (getEncoderTics() - bottomTics) + " <> ");
	// if (controller.upLevel()) {
	// if ((getEncoderTics() - bottomTics) < (level2 - fudgeFactor)) {
	// setPositionTics(level2 + bottomTics);
	// } else {
	// setPositionTics(level3 + bottomTics);
	// }
	// }
	// if (controller.downLevel()) {
	// if ((getEncoderTics() - bottomTics) > level2) {
	// setPositionTics(level2 + bottomTics);
	// } else {
	// setPositionTics(level1 + bottomTics);
	// }
	// }
	// }
	// handling level changes/sets level
	// level1-3 - Tic count for each level
	private void determineLevel(double level1, double level2, double level3) {
		if (controller.upLevel()) {
			if (currentLevel < 3) {
				currentLevel++;
			}
			setLevel(level1, level2, level3);
		}
		if (controller.downLevel()) {
			if (currentLevel > 1) {
				currentLevel--;
			}
			setLevel(level1, level2, level3);
		}
	}

	private void setLevel(double level1, double level2, double level3) {
		if (currentLevel == 1) {
			setPositionTics(level1 + bottomTics);
		} else if (currentLevel == 2) {
			setPositionTics(level2 + bottomTics);
		} else if (currentLevel == 3) {
			setPositionTics(level3 + bottomTics);
		}
	}
}
