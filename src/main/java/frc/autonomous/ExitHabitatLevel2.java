package frc.autonomous;

import frc.commands.DeployRearLegs;
import frc.commands.DriveBackwardsStraight;
import frc.commands.RetractRearLegs;
import frc.lib14.MCRCommand;
import frc.lib14.SequentialCommands;

public class ExitHabitatLevel2 implements MCRCommand {
    MCRCommand mission;
    boolean firstTime = true;
    public ExitHabitatLevel2() {
        mission = new SequentialCommands(
            new DeployRearLegs(1),
            new DriveBackwardsStraight(24,3),
            new RetractRearLegs(),
            new DriveBackwardsStraight(48,3)
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