package frc.autonomous;

import frc.commands.CommandPause;
import frc.commands.DeployFrontLegs;
import frc.commands.DeployRearLegs;
import frc.commands.DriveStraightInches;
import frc.commands.DriveToSensor;
import frc.commands.RetractFrontLegs;
import frc.commands.RetractRearLegs;
import frc.lib14.MCRCommand;
import frc.lib14.SequentialCommands;

public class ClimbToLevel2 implements MCRCommand {
    MCRCommand mission;

    public ClimbToLevel2() {
        mission = new SequentialCommands(    
            new DeployFrontLegs(1),
            new CommandPause(.75),
            new DriveToSensor(DriveToSensor.SENSOR_DIRECTION.forward),
            new RetractFrontLegs(1), 
            new DeployRearLegs(1),
            new DriveStraightInches(24, 3), 
            new RetractRearLegs()
        );
    }

    @Override
    public void run() {
        mission.run();
    }

    @Override
    public boolean isFinished() {
        return mission.isFinished();
    }
}