package frc.systems;

import java.util.logging.Logger;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import frc.robot.RobotDashboard;
import frc.robot.RobotMap;

public class CargoHandler {
	private static final Logger logger = Logger.getLogger(CargoHandler.class.getName());
	private static final CargoHandler instance = new CargoHandler();
	private static final MasterControls controller = MasterControls.getInstance();
	// private static final Elevator elevator = Elevator.getInstance();
	private static final SpeedController LEFT_INTAKE_MOTOR = new Talon(RobotMap.Intake.LEFT_MOTOR_CHANNEL);
	private static final SpeedController RIGHT_INTAKE_MOTOR = new Talon(RobotMap.Intake.RIGHT_MOTOR_CHANNEL);
	private static final DigitalInput ballSwitch = new DigitalInput(RobotMap.Intake.BALL_SENSOR);
	private enum IntakeState {
		OFF, IN, OUT
	}
	private IntakeState currentIntakeState = IntakeState.OFF; // start state is off
	//private Timer ejectTimer = new Timer();
	private boolean autoIntake = false;
	private boolean autoEject = false;
	//private Timer deployTimer = new Timer();
	private boolean autoDeploy = false;
	private CargoHandler() {
		// Singleton Pattern
		//logger.setLevel(RobotMap.LogLevels.intakeClass);
	}
	public static CargoHandler getInstance() {
		return instance;
	}
	public void execute() {
	//	logger.info("   Intake Up: " + isIntakeUp() + "  Down: " + isIntakeDown() + "  BoxDetected:"
		//		+ this.isCubeSensorSwitchActive());
		//if (autoEject) {
		//	if (ejectTimer.get() > RobotMap.Intake.AUTO_EJECT_SECONDS) {
				//ejectTimer.stop();
				//powerCubeIdle();
				//autoEject = false;
			//}
		//} else if (autoIntake) {
		//	if (isCubeSensorSwitchActive()) {
		//		powerCubeIdle();
		//		autoIntake = false;
		//	}
	//	} else {
		if (controller.isBallIntake()) {
				ballIntake();
			} else if (controller.isBallEject()) {
				ballEject();
			} else {
				ballIdle();
			}
		}
		
    


    

    // intake angle raise and lower
		//if (controller.isTiltDown()) {
		//	deploy();
	//	} else if (controller.isTitltUp()) {
	//		INTAKE_ANGLE_MOTOR.set(RobotMap.Intake.RAISE_INTAKE_SPEED);
	//	} else {
	//		if (autoDeploy) {
	//			if (deployTimer.get() > 2) {
	//				stopIntakeDeploy();
	///				deployTimer.stop();
	//				autoDeploy = false;
	//			}
	//		} else {
	//			stopIntakeDeploy();
	//		}
	//	}
//	}
	//public void stopIntakeDeploy() {
	//	INTAKE_ANGLE_MOTOR.stopMotor();
	//}
	//public void autoEject() {
	//	System.out.println("Eject cube");
	//	autoEject = true;
	//	powerCubeEject();
	//	ejectTimer.reset();
	//	ejectTimer.start();
//	}
	//public void autoIntake() {
	//	autoIntake = true;
	//	powerCubeIntake();
	//}
	private void ballIntake() {
		currentIntakeState = IntakeState.IN;
		if (isBallSensorSwitchActive()) {
			controller.intakeRumbleOn();
			ballIdle();
		} else {
			LEFT_INTAKE_MOTOR.set(-RobotMap.Intake.INTAKE_SPEED);// .setSpeed(RobotMap.Intake.INTAKE_SPEED);
			RIGHT_INTAKE_MOTOR.set(RobotMap.Intake.INTAKE_SPEED);// setSpeed(RobotMap.Intake.INTAKE_SPEED);
		}
	}
	private void ballEject() {
		LEFT_INTAKE_MOTOR.set(-RobotDashboard.getInstance().getIntakeEjectSpeed());// .setSpeed(RobotMap.Intake.EJECT_SPEED);
		RIGHT_INTAKE_MOTOR.set(RobotDashboard.getInstance().getIntakeEjectSpeed());// .setSpeed(RobotMap.Intake.EJECT_SPEED);
		currentIntakeState = IntakeState.OUT;
	}
	private void ballIdle() {
		if (IntakeState.OFF == currentIntakeState) {
			return;
		}
		LEFT_INTAKE_MOTOR.stopMotor();
		RIGHT_INTAKE_MOTOR.stopMotor();
		currentIntakeState = IntakeState.OFF;
		controller.intakeRumbleOff();
	}
	public boolean isIntakeRunning() {
		return IntakeState.OFF != currentIntakeState;
	}
	public boolean isBallSensorSwitchActive() {
		return !ballSwitch.get();
		// return cubeSensor.getDistanceInches(mail) < 12;
		// return cubeSensorSwitch.get();
	}
	//public void autoDeploy() {
	//	autoDeploy=true;
	//	deployTimer.reset();
	//	deployTimer.start();
	//	deploy();
	//}
//	private void deploy() {
	//	INTAKE_ANGLE_MOTOR.set(RobotMap.Intake.LOWER_INTAKE_SPEED);
	//}
	//public boolean isIntakeUp() {
	//	return INTAKE_ANGLE_MOTOR.getSensorCollection().isFwdLimitSwitchClosed();
	//}
	//public boolean isIntakeDown() {
	//	return INTAKE_ANGLE_MOTOR.getSensorCollection().isRevLimitSwitchClosed();
	//}
}
