package frc.commands;

import frc.lib14.MCRCommand;
import frc.lib14.PDController;
import frc.lib14.UtilityMethods;
import frc.robot.RobotDashboard;
import frc.robot.RobotMap;
import frc.systems.DriveTrain;
import java.util.logging.Logger;

import edu.wpi.first.wpilibj.DigitalInput;

public class TurnDegrees implements MCRCommand {
	private double degrees;
	private double setPoint;
	private DriveTrain driveTrain = DriveTrain.getInstance();
	private RobotDashboard dashboard = RobotDashboard.getInstance();
	private PDController driveController;
	private int currentState = 0;
	private final int IDLE = 0;
    private final int ACTIVE = 1;
    private final int DONE = 2;
	public TurnDegrees(double degrees) {
		super();
		this.degrees = degrees;
	}
	public void run() {
		switch (currentState) {
		
		case IDLE:
			driveTrain.resetGyro();
			setPoint = driveTrain.getAngle() + degrees; 
//System.out.println(("TurnDegrees SetPoint:" + setPoint));
			driveController = new PDController(setPoint, dashboard.getTurnKP(), dashboard.getTurnKI()); 
			driveTrain.arcadeDrive(RobotMap.TurnDegrees.TOP_SPEED, driveController.calculateAdjustment(setPoint));
			currentState = ACTIVE;
			break;
		case ACTIVE:
			driveController.set_kP(dashboard.getTurnKP());
			driveController.set_kD(dashboard.getTurnKD()); 
			double currentAngle = driveTrain.getAngle();
			if (Math.abs(setPoint-currentAngle) < RobotMap.TurnDegrees.VARIANCE) { 
	//logger.info("======== turn on target !!! ========="); 
				driveTrain.stop();
				currentState = DONE;
			} else {
				double correction = driveController.calculateAdjustment(driveTrain.getAngle());
	//driveTrain.arcadeDrive(RobotMap.TurnDegrees.TOP_SPEED, limitCorrection(correction, RobotMap.TurnDegrees.MAX_ADJUSTMENT));
				if (UtilityMethods.between(currentAngle, setPoint - RobotMap.TurnDegrees.SLOW_VARIANCE, setPoint + RobotMap.TurnDegrees.SLOW_VARIANCE)) {
					driveTrain.arcadeDrive(RobotMap.TurnDegrees.TOP_SPEED, limitCorrection(correction, RobotMap.TurnDegrees.SLOW_ADJUSTMENT));
	//	logger.info("Turn Degrees Slow");
				} else {
					driveTrain.arcadeDrive(RobotMap.TurnDegrees.TOP_SPEED, limitCorrection(correction, RobotMap.TurnDegrees.MAX_ADJUSTMENT));
	//	logger.info("Turn Degrees Fast");
				}
	//logger.info("Angle:" + driveTrain.getAngle());	
	//	logger.info("correction:" + correction);
			}
			break;
		case DONE:
			break;
		}
	}
	protected double limitCorrection(double correction, double maxAdjustment) {
		if (Math.abs(correction) > Math.abs(maxAdjustment))
			return UtilityMethods.copySign(correction, maxAdjustment);
		return correction;
	}
	@Override
	public boolean isFinished() {
		return DONE == currentState;
	}
}