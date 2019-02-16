package frc.systems;

import java.util.logging.Logger;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import frc.robot.RobotMap;

public class Climber {
	private DigitalInput limit = new DigitalInput(RobotMap.Climber.EDGE_SENSOR);; 
    private static final Logger logger = Logger.getLogger(Climber.class.getName());
	private static final Climber instance = new Climber();
	private static final MasterControls controllers = MasterControls.getInstance();
	private DoubleSolenoid front = new DoubleSolenoid(RobotMap.Climber.FRONT_FOWARD, RobotMap.Climber.FRONT_REVERSE);
	private DoubleSolenoid back = new DoubleSolenoid(RobotMap.Climber.REAR_FOWARD, RobotMap.Climber.REAR_REVERSE);
		
	private Climber() {
		// Singleton Pattern
		logger.setLevel(RobotMap.LogLevels.climberClass);
	}

	public static Climber getInstance() {
		return instance;
	}
	
    public void execute(){
		if (controllers.lowerBack()){
			retractBackLegs();
		}
		if (controllers.lowerFront()){
			retractFrontLegs();
		}
		if (controllers.raiseBack()){
			extendBackLegs();
		}
		if (controllers.raiseFront()){
			extendFrontLegs();
		}
	}

    public void extendBackLegs() {
		back.set(DoubleSolenoid.Value.kForward);
	}
	
	public void retractBackLegs() {
		back.set(DoubleSolenoid.Value.kReverse);
	}

	public void retractFrontLegs() {
		front.set(DoubleSolenoid.Value.kReverse);
	}

	public void extendFrontLegs() {
		front.set(DoubleSolenoid.Value.kForward);
	}

	public Boolean getSensor(){
		return limit.get();
	}
}