package frc.robot;

import java.util.logging.Level;

public class RobotMap {

	public final class DriverController {
		public static final int 	USB_PORT = 0;
	}

	public final class OperatorController {
		public static final int USB_PORT = 1;
	}

	public static final class Drivetrain {
		public static final double SPRINT_SPEED = 1;
		public static final double NORMAL_SPEED = 0.7;
		public static final double CRAWL_SPEED = .5;
		public static final int LEFT_MOTOR = 15;
		public static final int LEFT_MOTOR_NO_ENCODER = 4;
		public static final int RIGHT_MOTOR = 2;
		public static final int RIGHT_MOTOR_NO_ENCODER = 5;
		public static final int WHEEL_DIAMETER = 6;
		public static final double RAMP_SPEED = .6;
	}

	public final class DriveToSensor {
		public static final double TOP_SPEED =.6;
		public static final int SLOW_DOWN_DISTANCE = 18;
		public static final double BOTTOM_SPEED = .5;
		public static final double MAX_ADJUSTMENT = .4;
	}

	public final class DriveWithEncoder {
		public static final double TOP_SPEED = .5;
		public static final double BOTTOM_SPEED = .4;
		public static final double MAX_ADJUSTMENT = .4;
		public static final int TICS_PER_ROTATION = 4096; 
		public static final double INCHES_PER_ROTATION = Math.PI * RobotMap.Drivetrain.WHEEL_DIAMETER;
		public static final double SLOW_DOWN_DISTANCE = (12 / INCHES_PER_ROTATION) * TICS_PER_ROTATION;
		public static final double REVERSE_TOP_SPEED = -0.6;
		public static final double REVERSE_BOTTOM_SPEED = -0.4;
	}

	public final class TurnDegrees {
		public static final double kP = .6; 
		public static final double kI = 0; 
		public static final double kD = .05; 
		public static final double TOP_SPEED = 0;
		public static final double VARIANCE = .25;
		public static final double MAX_ADJUSTMENT = 8;
		public static final double SLOW_VARIANCE = 10;
		public static final double SLOW_ADJUSTMENT = .3;
	}

	public final class DriveStraightTime {
		public static final double TOP_SPEED = .6;
		public static final double MAX_ADJUSTMENT = .4;
		public static final double BOTTOM_SPEED = .3;
		public static final double SLOW_DOWN_TIME = 2;
	}

	public final class Intake {
		public static final double INTAKE_SPEED = 1;
		public static final double EJECT_SPEED = -1;
		// public static final double SLOW_EJECT_SPEED = 0.3;
		public static final int LEFT_MOTOR_CHANNEL = 3;// CAN
		public static final int RIGHT_MOTOR_CHANNEL = 6;// 
		//public static final double RAISE_INTAKE_SPEED = -.4;
		//public static final double LOWER_INTAKE_SPEED = .4;
		//public static final int ANGLE_MOTOR_CHANNEL = 7; // CAN
		// public static final int RANGE_FINDER = 0; // Analog Input/Output
		//public static final double AUTO_EJECT_SECONDS = 2.0;
		//public static final int BALL_SENSOR = 9; // DIO
		public static final double RAMP_SPEED = .8;
	}

	public final class Elevator {
//		public static final double UP_SPEED = 0.2;// motor is reversed
//		public static final double DOWN_SPEED = -0.8;// motor is reversed
		public static final double RAMP_SPEED = .8;
		public static final int ELEVATOR_CHANNEL1 = 9;// CAN
		public static final int ELEVATOR_CHANNEL2 = 7;// CAN
		public static final int LIMIT_SWITCH_TOP = 9; // DIO 
		public static final int LIMIT_SWITCH_BOTTOM = 8; // DIO 
		public static final double ELEVATOR_WINCH_DIAMETER = 2.4;// spool diamiter
		public static final int TICS_PER_ROTATION = 4096; // need to try 360
		public static final double INCHES_PER_ROTATION = Math.PI * RobotMap.Elevator.ELEVATOR_WINCH_DIAMETER;
		//public static final double SLOW_DOWN_DISTANCE = (8 / INCHES_PER_ROTATION) * TICS_PER_ROTATION;
		public static final double SAFTEY_ZONE = (12 / RobotMap.Elevator.INCHES_PER_ROTATION) * RobotMap.Elevator.TICS_PER_ROTATION;
		public static final double SAFE_SPEED = .5;
		public static final double DownSafeSpeed = 0.2; // code set the negative
		//Elevator hold PID parameters
		public static final double HATCH_LEVEL_1 = (0 / RobotMap.Elevator.INCHES_PER_ROTATION) * RobotMap.Elevator.TICS_PER_ROTATION;
		public static final double HATCH_LEVEL_2 = (28 / RobotMap.Elevator.INCHES_PER_ROTATION) * RobotMap.Elevator.TICS_PER_ROTATION;
		public static final double HATCH_LEVEL_3 = (56 / RobotMap.Elevator.INCHES_PER_ROTATION) * RobotMap.Elevator.TICS_PER_ROTATION;
		public static final double BALL_PICK_UP = (0 / RobotMap.Elevator.INCHES_PER_ROTATION) * RobotMap.Elevator.TICS_PER_ROTATION;
		public static final double BALL_HEIGHT_1 = (22.8 / RobotMap.Elevator.INCHES_PER_ROTATION) * RobotMap.Elevator.TICS_PER_ROTATION;
		public static final double BALL_HEIGHT_2 = (50.8 / RobotMap.Elevator.INCHES_PER_ROTATION) * RobotMap.Elevator.TICS_PER_ROTATION;
		public static final double BALL_HEIGHT_3 = (78.8 / RobotMap.Elevator.INCHES_PER_ROTATION) * RobotMap.Elevator.TICS_PER_ROTATION;
		public static final	double kP = 0.003;//start values
		public static final	double kI = 0;
		public static final	double kD = 0.08;// start values
		public static final	double tolerance = 5;
		public static final	double outputMin = -.4;
		public static final	double outputMax = .6;
		public static final double SafeSpeed = 0.4;
		public static final double SafeZone = (18 / Elevator.INCHES_PER_ROTATION) * Elevator.TICS_PER_ROTATION;
		public static final double ELEVATOR_MAX_EXTEND = 80;
		public static final double SHUTTLE_BALL_HEIGHT = 40;
	}

	public final class Climber {
		public static final int FRONT_FOWARD =  4;
		public static final int FRONT_REVERSE = 5;
		public static final int REAR_FOWARD =  7; 
		public static final int REAR_REVERSE = 6;
		public static final int EDGE_SENSOR = 2;
	}
	

	public final class Hatch {
		public static final int ARM_FOWARD =  3;
		public static final int ARM_REVERSE = 2;
		public static final int GRABBER_FOWARD =  1; 
		public static final int GRABBER_REVERSE = 0;
	
	}
	public static final class Autonomous {
		public static final double leftTurn = -90;
		public static final double rightTurn = 90;
		public static final double angleTurn = 45;
		public static final double rightAngleTurn = 50;
		
	}

	public static final class LogLevels {
		public static final Level robotClass = Level.WARNING;
		public static final Level robotDashboardClass = Level.WARNING;
		public static final Level masterControlsClass = Level.WARNING;
		public static final Level driveTrainClass = Level.WARNING;
		public static final Level elevatorClass = Level.WARNING;
		public static final Level intakeClass = Level.WARNING;
		public static final Level climberClass = Level.WARNING;
		public static final Level missionClass = Level.WARNING;
		public static final Level autoDriveClass = Level.WARNING;
		public static final Level componentBuilderClass = Level.WARNING;
		public static final Level cargoHandlerClass = Level.WARNING;
		public static final Level hatchHandlerClass = Level.WARNING;
	}

	

}
