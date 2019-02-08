package frc.systems;

import java.util.logging.Logger;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SpeedController;
import frc.lib14.MCR_SRX;
import frc.robot.RobotMap;

public class CargoHandler {
	private static final Logger logger = Logger.getLogger(CargoHandler.class.getName());
	private static final CargoHandler instance = new CargoHandler();
	private static final MasterControls controller = MasterControls.getInstance();
	// both motors will operate off a single motor controller with one being reverse wired
	private static final SpeedController INTAKE_MOTORS = new MCR_SRX(RobotMap.Intake.LEFT_MOTOR_CHANNEL);
	private static final DigitalInput ballSwitch = new DigitalInput(RobotMap.Intake.BALL_SENSOR);
	private enum IntakeState {
		OFF, IN, OUT
	}
	private IntakeState currentIntakeState = IntakeState.OFF; // start state is off

	private CargoHandler() {
		// Singleton Pattern
		logger.setLevel(RobotMap.LogLevels.cargoHandlerClass);
	}

	public static CargoHandler getInstance() {
		return instance;
	}

	public void execute() {
		if (controller.isBallIntake()) {
			ballIntake();
			Elevator.setHatchMode(false);
		} else if (controller.isBallEject()) {
			ballEject();
		} else {
			ballIdle();
		}
	}

	private void ballIntake() {
		currentIntakeState = IntakeState.IN;
		if (isBallSensorSwitchActive()) {
			controller.intakeRumbleOn();
			ballIdle();
		} else {
			INTAKE_MOTORS.set(RobotMap.Intake.INTAKE_SPEED);
		}
	}

	private void ballEject() {
		INTAKE_MOTORS.set(RobotMap.Intake.EJECT_SPEED);
		currentIntakeState = IntakeState.OUT;
	}

	private void ballIdle() {
		INTAKE_MOTORS.stopMotor();
		currentIntakeState = IntakeState.OFF;
		controller.intakeRumbleOff();
	}

	public boolean isIntakeRunning() {
		return IntakeState.OFF != currentIntakeState;
	}

	public boolean isBallSensorSwitchActive() {
		return ballSwitch.get();
	}
}
