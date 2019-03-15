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
	public static boolean isHatchMode = true;
	private int loggingIterations = 0;
	private LevelManager hatchLevels = new LevelManager(RobotMap.Elevator.HATCH_LEVEL_1, RobotMap.Elevator.HATCH_LEVEL_2, RobotMap.Elevator.HATCH_LEVEL_3);
	private LevelManager cargoLevels = new LevelManager(RobotMap.Elevator.BALL_PICK_UP, RobotMap.Elevator.BALL_HEIGHT_1, RobotMap.Elevator.SHUTTLE_BALL_HEIGHT, RobotMap.Elevator.BALL_HEIGHT_3);
	private LevelManager curLevels = hatchLevels;

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
			setHatchMode(!isHatchMode);
		}
		SmartDashboard.putBoolean("hatchmode", isHatchMode);
		// check for level up and level down
		setElevatorTarget();
		if (0 == controller.getElevatorThrottle()) {
			//set_K_values();
			setElevatorSpeed(holdPID.calculateAdjustment(getEncoderTics()));
		} else {
			setElevatorSpeed(controller.getElevatorThrottle());
			setPositionTics(getEncoderTics());
		}
		dash.pushElevatorPID(holdPID);
		dash.pushElevatorEncoder(getEncoderTics());
		dash.pushElevatorLimits(isElevatorAtTop(), isElevatorAtBottom());
	}

	public boolean getHatchMode() {
		return isHatchMode;
	}

	public void setHatchMode(boolean isHatchmode) {
		if (isHatchmode != isHatchMode) {
			isHatchMode = isHatchmode;
			if (isHatchMode) {
				curLevels = hatchLevels;
			} else {
				curLevels = cargoLevels;
			}
		}
	}

	private double limitAdjustment(double adjustment) {
		return UtilityMethods.absMin(adjustment, .7);
	}

	private void setPositionTics(double tics) {
		holdPID.setSetPoint(tics);
		//set_K_values();
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
		return !topLimit.get(); // For some reason this is inverted in the hardware, correcting here in software
	}

	private boolean isElevatorAtBottom() {
		if (!bottomLimit.get()) {
			// reset bottom and top measures
			bottomTics = getEncoderTics();
			topTics = bottomTics + inchesToTics(RobotMap.Elevator.ELEVATOR_MAX_EXTEND);
			curLevels.gotoBottom();
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

	private void setElevatorTarget() {
		if (controller.upLevel()) {
			curLevels.moveUp();
			setPositionTics(curLevels.getHeightForCurLevel() + bottomTics);
		}
		if (controller.downLevel()) {
			curLevels.moveDown();
			setPositionTics(curLevels.getHeightForCurLevel() + bottomTics);
		}
	}

}
