package frc.robot;

import java.util.logging.Logger;

import frc.lib14.PIDController;
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
		logger.setLevel(RobotMap.LogLevels.robotDashboardClass);
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

	public void pushElevatorPIDValues() {
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

	public void pushElevatorPID(PIDController pid) {
		if (null == pid)
			return;
		SmartDashboard.putNumber("PIDsetPoint", pid.getSetPoint());
		SmartDashboard.putNumber("PIDerror", pid.getError());
		//SmartDashboard.putNumber("Elevator Encoder Tics: ", Elevator.getInstance().getEncoderTics());
	}
	
	public void pushGyro(double angle) {
		SmartDashboard.putNumber("Gyro Reading", angle);
	}

	public void pushElevatorLimits(boolean upper, boolean lower){
		SmartDashboard.putBoolean("elevatorUpperLimit", upper);
		SmartDashboard.putBoolean("elevatorLowerLimit", lower);
	}

	public void pushElevatorEncoder(double elevatorEncoderTics){
		SmartDashboard.putNumber("elevatorEncoderValue", elevatorEncoderTics);
	}

	// public void pushEncoder(double encoderTics){
	// 	SmartDashboard.putNumber("encoderValue", encoderTics);

	// }

	public void pushElevatorTarget(double setPoint) {
		SmartDashboard.putNumber("elevatorSetPoint", setPoint);
	}

	public void pushElevatorCurPosition(double curPosition) {
		SmartDashboard.putNumber("elevatorEncoder", curPosition);
	}

	public double getElevatorTarget() {
		return SmartDashboard.getNumber("elevatorSetPoint", 0);
	}

	public double getIntakeEjectSpeed() {
		return SmartDashboard.getNumber("IntakeEjectSpeed", 0);
	}
	
	public void pushElevatorBottom(double bottomTics) {
		SmartDashboard.putNumber("elevatorBottomTics", bottomTics) ;
	}

	public void pushElevatorTop (double topTics) {
		SmartDashboard.putNumber("elevatorTopTics", topTics);
	}

	public void pushLeftEncoder(double leftTics){
		SmartDashboard.putNumber("driveLeftTics", leftTics);
	}

	public void pushRightEncoder(double rightTics){
		SmartDashboard.putNumber("driveRight", rightTics);
	}

	public void pushSpeed(){
		SmartDashboard.putNumber("crawlSpeed", RobotMap.Drivetrain.CRAWL_SPEED);
		SmartDashboard.putNumber("sprintSpeed", RobotMap.Drivetrain.SPRINT_SPEED);
		SmartDashboard.putNumber("normalSpeed", RobotMap.Drivetrain.NORMAL_SPEED);
	}

	//Turn commands
	public void pushTurnPID() {
		SmartDashboard.putNumber("TkP", RobotMap.TurnDegrees.kP);
		SmartDashboard.putNumber("TkI", RobotMap.TurnDegrees.kI);
		SmartDashboard.putNumber("TkD", RobotMap.TurnDegrees.kD);
	}
	public double getTurnKP() {
		return SmartDashboard.getNumber("TkP", RobotMap.Elevator.kP);
	}
	public double getTurnKI() {
		return SmartDashboard.getNumber("TkI", RobotMap.Elevator.kI);
	}
	public double getTurnKD() {
		return SmartDashboard.getNumber("TkD", RobotMap.Elevator.kD);
	}

	public void pushEdgeSensor(Boolean sensor) {
		SmartDashboard.putBoolean("EdgeSensor", sensor);
	}

	public void pushFieldMode(Boolean Mode) {
		SmartDashboard.putBoolean("Field Mode", Mode);

	}

	public void pushAuto() {
		SmartDashboard.putBoolean("Exit Level 2", true);
	}

	public boolean getAuto() {
		return SmartDashboard.getBoolean("Exit Level 2", false);
	}
}
