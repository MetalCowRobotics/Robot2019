package frc.systems;

import java.util.logging.Logger;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lib14.MCR_SRX;
import frc.robot.RobotMap;

public class DriveTrain {
	//rightFrontMotor has the Encoder
	private static MCR_SRX rightFrontMotor = new MCR_SRX(RobotMap.Drivetrain.RIGHT_MOTOR_CHANNEL1);
	private static MCR_SRX leftFrontMotor = new MCR_SRX(RobotMap.Drivetrain.LEFT_MOTOR_CHANNEL1);
	private static final SpeedControllerGroup RIGHT_DRIVE_MOTORS = new SpeedControllerGroup(rightFrontMotor);
	private static final SpeedControllerGroup LEFT_DRIVE_MOTORS = new SpeedControllerGroup(leftFrontMotor);
	//TODO: add a proper device number to back motors
	/** 
	private static MCR_SRX rightBackMotor = new MCR_SRX(Drivetrain.RIGHT_MOTOR_CHANNEL2);
	private static MCR_SRX leftBackMotor = new MCR_SRX(Drivetrain.LEFT_MOTOR_CHANNEL2);
	private static final SpeedControllerGroup RIGHT_DRIVE_MOTORS = new SpeedControllerGroup(rightFrontMotor, rightBackMotor);
	private static final SpeedControllerGroup LEFT_DRIVE_MOTORS = new SpeedControllerGroup(leftFrontMotor, leftBackMotor);
	*/
	private static final ADXRS450_Gyro GYRO = new ADXRS450_Gyro();
	private static final Logger logger = Logger.getLogger(DriveTrain.class.getName());
	private static final DriveTrain instance = new DriveTrain();
	MasterControls controller = MasterControls.getInstance();
	private static final DifferentialDrive drive = new DifferentialDrive(LEFT_DRIVE_MOTORS, RIGHT_DRIVE_MOTORS);

	private int inverted = 1;

	// Singleton
	protected DriveTrain() {
		//rightMotor.configOpenloopRamp(.8);
		//leftMotor.configOpenloopRamp(.8);
		// logger.setLevel(RobotMap.LogLevels.driveTrainClass);
	}

	public static DriveTrain getInstance() {
		return instance;
	}

	public void drive() {
		if (controller.invertDrive()) {
			invert();
		}
		double speed = (controller.forwardSpeed() - controller.reverseSpeed()) * inverted * getThrottle();
		drive.arcadeDrive(speed, controller.direction());
		SmartDashboard.putNumber("getSelectedSensorPosition", rightFrontMotor.getSelectedSensorPosition());
	}

	/**
	 * Used in Autonomous
	 * 
	 * @param speed
	 * @param angle
	 */
	public void arcadeDrive(double speed, double angle) {
		// if only used in autonomous may not need the throttle
		drive.arcadeDrive(speed, angle);
	}

	public void stop() {
		drive.stopMotor();
	}

	public void calibrateGyro() {
		GYRO.calibrate();
	}

	public void resetGyro() {
		DriverStation.reportWarning("Gyro Before Reset: " + GYRO.getAngle(), false);
		GYRO.reset();
		DriverStation.reportWarning("Gryo After Reset: " + GYRO.getAngle(), false);
	}

	public double getAngle() {
		return GYRO.getAngle();
	}

	/**
	 * Determine the top speed threshold: CRAWL - Lowest speed threshold Normal -
	 * Normal driving conditions SPRINT - Highest speed threshold
	 * 
	 * @link org.usfirst.frc.team4213.robot.RobotMap
	 */
	private double getThrottle() {
		if (controller.isCrawlToggle()) {
			return RobotMap.Drivetrain.CRAWL_SPEED;
		} else if (controller.isSprintToggle()) {
			return RobotMap.Drivetrain.SPRINT_SPEED;
		} else {
			return RobotMap.Drivetrain.NORMAL_SPEED;
		}
	}

	private void invert() {
		inverted = inverted * -1;
	}

	private double getLeftEncoderTics() {
		// return leftEncoder.getDistance();
		return 0;
	}

	private double getRightEncoderTics() {
		return rightFrontMotor.getSelectedSensorPosition();
	}

	public void printRightEncoder() {
		System.out.println(getRightEncoderTics() + " RightEncoder");
	}

	public void printLeftEncoder() {
		System.out.println(getLeftEncoderTics() + " LeftEncoder");

	}

	public double encoderDifference() {
		return (getRightEncoderTics() - getLeftEncoderTics());
	}

	public double getEncoderTics() {
		// return (getRightEncoderTics() + getLeftEncoderTics()) / 2;
		return getRightEncoderTics();
	}

	
}
