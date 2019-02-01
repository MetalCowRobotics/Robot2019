package frc.systems;

import java.util.logging.Logger;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
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
	private boolean firstTime = true;
	private double bottomTics;
	private double topTics;
	private PDController holdPID;
	private int iterations = 0;
	private boolean hatchMode = true;

	private Elevator() {
		// Singleton Pattern
		logger.setLevel(RobotMap.LogLevels.elevatorClass);
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
			holdPID = new PDController(bottomTics, dash.getElevatorKP(), dash.getElevatorKD());
			setPositionTics(bottomTics); //seeing if this helps with multiple runs
			dash.pushElevatorPID();
		}
		// check the mode button and if pressed
		// hatchMode = !hatchMode;
		getElevatorTarget(); //check for level up and level down
		if (0 == controller.getElevatorThrottle()) {
			holdPID.set_kP(dash.getElevatorKP());
			holdPID.set_kD(dash.getElevatorKD());
			setElevatorSpeed(holdPID.calculateAdjustment(getEncoderTics()));
		} else {
			setElevatorSpeed(controller.getElevatorThrottle());
			setPositionTics(getEncoderTics());
		}
		dash.pushElevatorPID(holdPID);
		dash.pushElevatorEncoder(getEncoderTics());
	}

	private void setPositionTics(double tics) {
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
			return Math.max(speed, -RobotMap.Elevator.SafeSpeed);
		} else {
			return UtilityMethods.copySign(speed, Math.min(Math.abs(speed), .7)); // xtra
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
		return UtilityMethods.between(inchesToTics(heightInches), getEncoderTics() - 100, getEncoderTics() + 100);
	}

	private boolean isElevatorAtTop() {
		return !topLimit.get(); // For some reason this is inverted in the hardware, correcting here in software
	}

	private boolean isElevatorAtBottom() {
		if (!bottomLimit.get()) {
			//reset bottom and top measures
			bottomTics = getEncoderTics();
			topTics = bottomTics + inchesToTics(RobotMap.Elevator.ELEVATOR_MAX_EXTEND);
		}
		return !bottomLimit.get(); // for some reason this is inverted in hardware, correcting here in software
	}

	public double getEncoderTics() {
		return motor1.getSelectedSensorPosition();
	}

	private void logParameters() {
		iterations++;
		if (20 < iterations) {
			logger.info("Elevator throttle:" + controller.getElevatorThrottle());
			logger.info("Elevator Up limit: " + this.isElevatorAtTop() + " Elevator Down limit: " + this.isElevatorAtBottom());
			logger.info("Elevator bottom tics:" + bottomTics + "   Elevator top tics:" + topTics);
			logger.info("Elevator Speed:" + ELEVATOR_MOTOR.get());
			logger.info("Elevator encoder tics:" + getEncoderTics());
			logPID();
			iterations = 0;
		}
	}

	private void logPID() {
		logger.info("Elev PID set point:" + holdPID.getSetPoint());
		logger.info("Elev PID error:" + holdPID.getError());
	}

	private void getElevatorTarget() {
		// need to add a mode to switch between cargo and hatches in execute
		if (hatchMode) {
			determineLevel(RobotMap.Elevator.HATCH_LEVEL_1,RobotMap.Elevator.HATCH_LEVEL_2,RobotMap.Elevator.HATCH_LEVEL_3);
		} else {
			determineLevel(RobotMap.Elevator.BALL_HEIGHT_1, RobotMap.Elevator.BALL_HEIGHT_2, RobotMap.Elevator.BALL_HEIGHT_3);
		}	
	}

	private void determineLevel(double level1, double level2, double level3) {
		double fudgeFactor = 300; // if the PID does not get it to height it will always be lower and never go to the else
		logger.info("current distance: " + (getEncoderTics() - bottomTics) + " <> ");
		if (controller.upLevel()) {
			if ((getEncoderTics() - bottomTics) < level2 - fudgeFactor) {
				setPositionTics(level2 + bottomTics);
			} else {
				setPositionTics(level3 + bottomTics);
			}
		}
		if (controller.downLevel()) {
			if ((getEncoderTics() - bottomTics) > level2 - fudgeFactor) {
				setPositionTics(level2 + bottomTics);
			} else {
				setPositionTics(level1 + bottomTics);
			}
		}
	}
}