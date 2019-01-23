package frc.systems;

import java.util.logging.Logger;

public class Climber {
    private static final Logger logger = Logger.getLogger(Elevator.class.getName());
	private static final Climber instance = new Climber();

	private Climber() {
		// Singleton Pattern
		//logger.setLevel(RobotMap.LogLevels.elevatorClass);
	}

	public static Climber getInstance() {
		return instance;
    }
    
    public void extendBackLegs() {
    }

	public void retractBackLegs() {
	}

	public void retractFrontLegs() {
	}

	public void extendFrontLegs() {
	}
}