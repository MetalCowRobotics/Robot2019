package frc.robot;

import java.util.logging.Logger;

import frc.lib14.PDController;
import frc.systems.DriveTrain;
import frc.systems.Elevator;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class RobotDashboard {
	private static final Logger logger = Logger.getLogger(RobotDashboard.class.getName());
	private static frc.robot.RobotDashboard ourInstance = new frc.robot.RobotDashboard();
	private DriverStation driverStation;
	private PowerDistributionPanel pdp;

	public static frc.robot.RobotDashboard getInstance() {
		return ourInstance;
	}

	private RobotDashboard() {
		logger.setLevel(RobotMap.LogLevels.hamburgerDashboardClass);
	}

	public void initializeDashboard() {
		// driverStation = edu.wpi.first.wpilibj.DriverStation.getInstance();
		// // boolean isFMSAttached = driverStation.isFMSAttached();
		// if (!isFMSAttached) {
		// 	pdp = new PowerDistributionPanel();
		// 	pdp.resetTotalEnergy();
		// }
		// pushElevatorPID();
	}

	public void pushElevatorPID() {
		SmartDashboard.putNumber("EkP", RobotMap.Elevator.kP);
		SmartDashboard.putNumber("EkI", RobotMap.Elevator.kI);
		SmartDashboard.putNumber("EkD", RobotMap.Elevator.kD);
		// SmartDashboard.putNumber("Etolerance", RobotMap.Elevator.tolerance);
		// SmartDashboard.putNumber("Emin output", RobotMap.Elevator.outputMin);
		// SmartDashboard.putNumber("Emax output", RobotMap.Elevator.outputMax);
	}

	public double getElevatorKP() {
		return SmartDashboard.getNumber("EkP", RobotMap.Elevator.kP);
	}

	public double getElevatorKI() {
		return SmartDashboard.getNumber("EkI", RobotMap.Elevator.kI);
	}

	public double getElevatorKD() {
		return SmartDashboard.getNumber("EkD", RobotMap.Elevator.kD);
	}

	public double getElevatorTolerance() {
		return SmartDashboard.getNumber("Etolerance", RobotMap.Elevator.tolerance);
	}

	public double getElevatorOutputMin() {
		return SmartDashboard.getNumber("Emin output", RobotMap.Elevator.outputMin);
	}

	public double getElevatorOutputMax() {
		return SmartDashboard.getNumber("Emax output", RobotMap.Elevator.outputMax);
	}

	public void pushElevatorPID(PDController pid) {
		if (null == pid)
			return;
		SmartDashboard.putNumber("PIDsetPoint", pid.getSetPoint());
		SmartDashboard.putNumber("PIDerror", pid.getError());
		SmartDashboard.putNumber("Elevator Encoder Tics: ", Elevator.getInstance().getEncoderTics());
	}
	
	public void pushGyro() {
		SmartDashboard.putNumber("Gyro Reading", DriveTrain.getInstance().getAngle());
	}

	public void pushElevatorLimits(boolean upper, boolean lower){
		SmartDashboard.putBoolean("elevatorUpperLimit", upper);
		SmartDashboard.putBoolean("elevatorLowerLimit", lower);
	}

	public void pushElevatorEncoder(double elevatorEncoderTics){
		SmartDashboard.putNumber("elevatorEncoderValue", elevatorEncoderTics);
	}

	public void pushEncoder(double encoderTics){
		SmartDashboard.putNumber("encoderValue", encoderTics);

	}

	public void pushElevatorTarget(double setPoint) {
		SmartDashboard.putNumber("elevatorSetPoint", setPoint);
	}

	public double getElevatorTarget() {
		return SmartDashboard.getNumber("elevatorSetPoint", 0);
	}

	public double getIntakeEjectSpeed() {
		return SmartDashboard.getNumber("IntakeEjectSpeed", 0);
	}
	
	public void pushElevatorBottom(double bottomTics) {
		SmartDashboard.putNumber("elevatorBottom", bottomTics) ;
	}

}
